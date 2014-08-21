package net.dtkanov.blocks.circuit.high_level.derived;

import net.dtkanov.blocks.circuit.DeMux;
import net.dtkanov.blocks.circuit.LookUp;
import net.dtkanov.blocks.circuit.MultiNOT;
import net.dtkanov.blocks.circuit.Mux;
import net.dtkanov.blocks.circuit.high_level.MultiMux;
import net.dtkanov.blocks.circuit.high_level.Register;
import net.dtkanov.blocks.logic.ANDNode;
import net.dtkanov.blocks.logic.ConstantNode;
import net.dtkanov.blocks.logic.NOPNode;
import net.dtkanov.blocks.logic.NOTNode;
import net.dtkanov.blocks.logic.Node;
import net.dtkanov.blocks.logic.derived.ORNode;
import net.dtkanov.blocks.logic.derived.XORNode;
/** Implements control unit. */
public class ControlUnit extends Node {
	/** Bitness of CPU. */
	public static int BITNESS = 8;
	/** Carry-flag index. */
	public static int C_FLAG = 0;
	/** Flag is not used. */
	public static int F1_FLAG = 1;
	/** Parity-flag index. */
	public static int P_FLAG = 2;
	/** Flag is not used. */
	public static int F3_FLAG = 3;
	/** Aux.carry-flag index. */
	public static int H_FLAG = 4;
	/** Interrupt-flag index. */
	public static int I_FLAG = 5;
	/** Zero-flag index. */
	public static int Z_FLAG = 6;
	/** Sign-flag index. */
	public static int S_FLAG = 7;
	/** Accumulator */
	private Register A;
	/** Flags */
	private Register F;
	private Register B;
	private Register C;
	private Register D;
	private Register E;
	private Register H;
	private Register L;
	/** Stack Pointer */
	private Register SP;
	/** Program Counter */
	private Register PC;
	/** Operation selection. */
	private Node opNOPs[];
	/** Data-byte 1 */
	private Node inNOPs_A[];
	/** Data-byte 2 */
	private Node inNOPs_B[];
	/** Clock input */
	private Node clock;
	/** Selector for ALU operand 1 */
	private Node ALU_in_mux_A[];
	/** Selector for output to registers. */
	private Node out_demux[];
	/** ALU */
	private ALU alu;
	/** Memory */
	private LookUp mem_alu_in;
	/** Lookup table for ALU control */
	private LookUp alu_ctrl;
	/** Storage */
	private byte storage[];
	/** Fake zero */
	private ConstantNode zero;
	/** Checks if it is rotations instruction. */
	private ANDNode comb_rot_ctrl[];
	/** Controller for PC register. */
	private PCRegController pc_ctrl;
	/** ALU disabler. */
	private NOPNode is_alu_on;
	/** True if jump condition is not satisfied. */
	private XORNode not_jump_cond;
	/** True if jump is not requested. */
	private ORNode is_not_jump;
	
	public ControlUnit() {
		super(null);
		zero = new ConstantNode(false);
		storage = new byte[1<<(BITNESS*2)];
		initInputs();
		initElements();
		zero.propagate();
	}
	
	public void loadToStorage(int offset, byte[] data) {
		if (offset < 0)
			throw new IllegalArgumentException("Memory offset can't be negative.");
		for (int i = 0; i < data.length; i++) {
			if (offset + i >= storage.length)
				break;
			storage[offset+i] = data[i];
		}
	}
	
	public void loadToStorage(byte[] data) {
		loadToStorage(0, data);
	}
	
	public void loadToStorage(int offset, byte data) {
		byte temp[] = new byte[1];
		temp[0] = data;
		loadToStorage(offset, temp);
	}
	
	public boolean getRegAValue(int index) {
		return A.out(index);
	}
	public boolean getRegBValue(int index) {
		return B.out(index);
	}
	public boolean getRegCValue(int index) {
		return C.out(index);
	}
	public boolean getRegDValue(int index) {
		return D.out(index);
	}
	public boolean getRegEValue(int index) {
		return E.out(index);
	}
	public boolean getRegHValue(int index) {
		return H.out(index);
	}
	public boolean getRegLValue(int index) {
		return L.out(index);
	}
	public boolean getFlag(int index) {
		return F.out(index);
	}
	public boolean getRegSPValue(int index) {
		return SP.out(index);
	}
	public boolean getRegPCValue(int index) {
		return PC.out(index);
	}
	
	private void initInputs() {
		inNOPs_A = new NOPNode[BITNESS];
		inNOPs_B = new NOPNode[BITNESS];
		for (int i = 0; i < BITNESS; i++) {
			inNOPs_A[i] = new NOPNode();
			inNOPs_B[i] = new NOPNode();
		}
		opNOPs = new NOPNode[BITNESS];
		for (int i = 0; i < BITNESS; i++) {
			opNOPs[i] = new NOPNode();
		}
		clock = new NOPNode();
	}
	
	private void initElements() {
		A = new Register(BITNESS);
		F = new Register(BITNESS);
		B = new Register(BITNESS);
		C = new Register(BITNESS);
		D = new Register(BITNESS);
		E = new Register(BITNESS);
		H = new Register(BITNESS);
		L = new Register(BITNESS);
		SP = new Register(2*BITNESS);
		PC = new Register(2*BITNESS);
		///////////////////////////////////////////////////////////////////////
		initJumps();
		///////////////////////////////////////////////////////////////////////
		alu = new ALU(BITNESS);// 8-bit ALU
		// control byte
		// TODO replace lookup with clever logic
		// table format: xRPPAAAA, R - is ALU on?, PP - PC controller mode, AAAA - ALU mode
		byte[] alu_lookup = new byte[1<<BITNESS];
		for (int i = 0; i < 1<<6; i++) {
			alu_lookup[(1<<6) + i] = 0b1010111;// MOV => OP1
			alu_lookup[i] = 0b1100111;// MVI => OP1
		}
		for (int i = 0; i < 1<<3; i++) {
			alu_lookup[0b10000000 + i] = 0b1010001;// ADD => ADD
			alu_lookup[0b10001000 + i] = 0b1010001;// ADC => ADD
			
			alu_lookup[0b10010000 + i] = 0b1011001;// SUB => SUB
			alu_lookup[0b10011000 + i] = 0b1011001;// SBB => SUB
			
			alu_lookup[0b00000100 + (i<<3)] = 0b1010101;// INR => INC
			alu_lookup[0b00000101 + (i<<3)] = 0b1011101;// DER => DEC
			
			alu_lookup[0b10100000 + i] = 0b1010000;// ANA => AND
			alu_lookup[0b10110000 + i] = 0b1011000;// ORA => OR
			alu_lookup[0b10101000 + i] = 0b1010100;// XRA => XOR
			
			alu_lookup[0b11000010 + (i<<3)] = 0b0000111;// Jccc
		}
		alu_lookup[0b00000000] = 0b0010111;// NOP
		alu_lookup[0b11000011] = 0b0000111;// JMP
		alu_lookup[0b11000110] = 0b1100001;// ADI => ADD
		alu_lookup[0b11001110] = 0b1100001;// ACI => ADD
		alu_lookup[0b11010110] = 0b1101001;// SUI => SUB
		alu_lookup[0b11011110] = 0b1101001;// SBI => SUB
		alu_lookup[0b11100110] = 0b1100000;// ANI => AND
		alu_lookup[0b11110110] = 0b1101000;// ORI => OR
		alu_lookup[0b11101110] = 0b1100100;// XRI => XOR
		alu_lookup[0b00000111] = 0b1010110;// RLC => ROL
		alu_lookup[0b00001111] = 0b1011110;// RRC => ROR
		alu_ctrl = new LookUp(BITNESS, alu_lookup);
		mem_alu_in = new LookUp(BITNESS*2, storage);// 16-bit addressing
		///////////////////////////////////////////////////////////////////////
		pc_ctrl = new PCRegController(PC);
		for (int i = 0; i < BITNESS; i++) {
			pc_ctrl.connectSrc(inNOPs_A[i], 0, i);
			pc_ctrl.connectSrc(inNOPs_B[i], 0, i + BITNESS);
		}
		// lowest bit of opcode = 0 => conditional jump
		is_not_jump = new ORNode();
		is_not_jump.connectSrc(alu_ctrl, 4, 0);
		is_not_jump.connectSrc(alu_ctrl, 5, 1);
		NOTNode neg_is_cond = new NOTNode();
		neg_is_cond.connectSrc(opNOPs[0], 0, 0);// true when conditional jump
		ANDNode skip_jump = new ANDNode();
		skip_jump.connectSrc(neg_is_cond, 0, 0);
		skip_jump.connectSrc(not_jump_cond, 0, 1);
		Mux jumper = new Mux();
		jumper.connectSrc(is_not_jump, 0, 2);
		jumper.connectSrc(alu_ctrl, 4, 0);
		jumper.connectSrc(skip_jump, 0, 1);
		pc_ctrl.connectSrc(jumper, 0, BITNESS*2);
		jumper = new Mux();
		jumper.connectSrc(is_not_jump, 0, 2);
		jumper.connectSrc(alu_ctrl, 5, 0);
		jumper.connectSrc(skip_jump, 0, 1);
		pc_ctrl.connectSrc(jumper, 0, BITNESS*2 + 1);
		pc_ctrl.connectSrc(clock, 0, BITNESS*2 + 2);
		///////////////////////////////////////////////////////////////////////
		is_alu_on = new NOPNode();
		is_alu_on.connectSrc(alu_ctrl, 6, 0);
		///////////////////////////////////////////////////////////////////////
		
		initOutputToRegisters();
		initInputFromRegisters();
	}
	
	private void initJumps() {
		Mux flag_sel[] = new Mux[3];
		flag_sel[0] = new Mux();
		flag_sel[0].connectSrc(F, S_FLAG, 0);
		flag_sel[0].connectSrc(F, C_FLAG, 1);
		flag_sel[0].connectSrc(opNOPs[5], 0, 2);
		flag_sel[1] = new Mux();
		flag_sel[1].connectSrc(F, P_FLAG, 0);
		flag_sel[1].connectSrc(F, Z_FLAG, 1);
		flag_sel[1].connectSrc(opNOPs[5], 0, 2);
		flag_sel[2] = new Mux();
		flag_sel[2].connectSrc(flag_sel[0], 0, 0);
		flag_sel[2].connectSrc(flag_sel[1], 0, 1);
		flag_sel[2].connectSrc(opNOPs[4], 0, 2);
		not_jump_cond = new XORNode();
		not_jump_cond.connectSrc(flag_sel[2], 0, 0);
		not_jump_cond.connectSrc(opNOPs[3], 0, 1);
	}
	
	private void initInputFromRegisters() {
		final int REG_SEL_CNT = 3;
		// Control byte format: xxDDDSSS or CCCCCSSS.
		// Also 00DDD10X means inc/dec, so DDD==SSS.
		/* Briefly: this code checks if opNOPs pattern
		 * is not 00XXX10X (not_incdec_comb).
		 * not_incdec_comb==0 means usage of DDD as source.
		 * not_incdec_comb==1 means usage of SSS as source. */
		NOTNode incdec_3rd_bit_inv = new NOTNode();
		incdec_3rd_bit_inv.connectSrc(opNOPs[2], 0, 0);
		ORNode not_incdec_pre1 = new ORNode();
		ORNode not_incdec_pre2 = new ORNode();
		ORNode not_incdec_comb = new ORNode();
		not_incdec_pre1.connectSrc(opNOPs[1], 0, 0);
		not_incdec_pre1.connectSrc(incdec_3rd_bit_inv, 0, 1);
		not_incdec_pre2.connectSrc(opNOPs[BITNESS-2], 0, 0);
		not_incdec_pre2.connectSrc(opNOPs[BITNESS-1], 0, 1);
		not_incdec_comb.connectSrc(not_incdec_pre1, 0, 0);
		not_incdec_comb.connectSrc(not_incdec_pre2, 0, 1);
		MultiMux incdec_sel = new MultiMux(REG_SEL_CNT);
		for (int i = 0; i < 2*REG_SEL_CNT; i++) {
			incdec_sel.connectSrc(opNOPs[i], 0, i);
		}
		incdec_sel.connectSrc(not_incdec_comb, 0, 2*REG_SEL_CNT);
		
		ALU_in_mux_A = new MultiMux[(1<<REG_SEL_CNT) - 1];
		ALU_in_mux_A[0] = new MultiMux(BITNESS);
		ALU_in_mux_A[0].connectSrc(incdec_sel, 0, 2*BITNESS);
		for (int i = 1; i < ALU_in_mux_A.length; i++) {
			ALU_in_mux_A[i] = new MultiMux(BITNESS);
			// heap-like organization of indexes
			for (int j = 0; j < BITNESS; j++) {
				ALU_in_mux_A[(i-1)/2].connectSrc(ALU_in_mux_A[i], j, j + ((i+1)%2)*BITNESS);
			}
			// Adding small magic number to prevent rounding errors.
			int level = (int)Math.floor(Math.log(i+1)/Math.log(2) + 1e-10);
			ALU_in_mux_A[i].connectSrc(incdec_sel, level, 2*BITNESS);
		}
		
		// connecting ALU inputs
		// For rotations we should pass 1 as second op for ALU.
		// In other cases it should be A register.
		NOTNode not_rot = new NOTNode();
		not_rot.connectSrc(comb_rot_ctrl[4], 0, 0);
		for (int j = 1; j < BITNESS; j++) {
			ANDNode temp_con = new ANDNode();
			temp_con.connectSrc(not_rot, 0, 0);
			temp_con.connectSrc(A, j, 1);
			temp_con.connectDst(0, alu, j+BITNESS);
		}
		ORNode temp_con = new ORNode();
		temp_con.connectSrc(comb_rot_ctrl[4], 0, 0);
		temp_con.connectSrc(A, 0, 1);
		temp_con.connectDst(0, alu, BITNESS);
		for (int j = 0; j < BITNESS; j++) {
			ALU_in_mux_A[0].connectDst(j, alu, j);
		}
		for (int j = 0; j < BITNESS; j++) {
			alu_ctrl.connectSrc(opNOPs[j], 0, j);
		}
		for (int j = 0; j < ALU.NUM_CMD_BITS; j++) {
			alu_ctrl.connectDst(j, alu, j+2*BITNESS);
		}
		
		// init memory H:L indexing
		for (int j = 0; j < BITNESS; j++) {
			H.connectDst(j, mem_alu_in, j);
			L.connectDst(j, mem_alu_in, j+BITNESS);
		}
		
		// connecting registers
		for (int j = 0; j < BITNESS; j++) {
			// 000
			B.connectDst(j, ALU_in_mux_A[ALU_in_mux_A.length-1], j+BITNESS);
			// 100
			H.connectDst(j, ALU_in_mux_A[ALU_in_mux_A.length-1], j);
			// 010
			D.connectDst(j, ALU_in_mux_A[ALU_in_mux_A.length-2], j+BITNESS);
			// 110
			inNOPs_A[j].connectDst(0, ALU_in_mux_A[ALU_in_mux_A.length-2], j);
			// 001
			C.connectDst(j, ALU_in_mux_A[ALU_in_mux_A.length-3], j+BITNESS);
			// 101
			L.connectDst(j, ALU_in_mux_A[ALU_in_mux_A.length-3], j);
			// 011
			E.connectDst(j, ALU_in_mux_A[ALU_in_mux_A.length-4], j+BITNESS);
			// 111
			A.connectDst(j, ALU_in_mux_A[ALU_in_mux_A.length-4], j);
		}
	}
	
	private void initOutputToRegisters() {
		// It seems like we can take in account last opNOPs bit
		//   to check if there is a specific destination.
		ORNode dst_ctrl[] = new ORNode[3];
		for (int i = 0; i < dst_ctrl.length; i++) {
			dst_ctrl[i] = new ORNode();
			dst_ctrl[i].connectSrc(opNOPs[BITNESS-1], 0, 0)
					   .connectSrc(opNOPs[3+i], 0, 1);
		}
		// Also 000XX111 pattern (rotations) should set A as destanation.
		ORNode dst_ctrl_rot[] = new ORNode[3];
		MultiNOT rev_high_3bit = new MultiNOT(3);
		comb_rot_ctrl = new ANDNode[5];
		for (int i = 0; i < 3; i++) {
			rev_high_3bit.connectSrc(opNOPs[BITNESS-1-i], 0, i);
			comb_rot_ctrl[i] = new ANDNode();
			comb_rot_ctrl[i].connectSrc(opNOPs[i], 0, 0);
			comb_rot_ctrl[i].connectSrc(rev_high_3bit, i, 1);
		}
		comb_rot_ctrl[3] = new ANDNode();
		comb_rot_ctrl[3].connectSrc(comb_rot_ctrl[0], 0, 0);
		comb_rot_ctrl[3].connectSrc(comb_rot_ctrl[1], 0, 1);
		comb_rot_ctrl[4] = new ANDNode();
		comb_rot_ctrl[4].connectSrc(comb_rot_ctrl[2], 0, 0);
		comb_rot_ctrl[4].connectSrc(comb_rot_ctrl[3], 0, 1);
		for (int i = 0; i < dst_ctrl_rot.length; i++) {
			dst_ctrl_rot[i] = new ORNode();
			dst_ctrl_rot[i].connectSrc(dst_ctrl[i], 0, 0)
			   			   .connectSrc(comb_rot_ctrl[4], 0, 1);
		}
		ANDNode comb_alu = new ANDNode();
		comb_alu.connectSrc(is_alu_on, 0, 0);
		comb_alu.connectSrc(clock, 0, 1);
		out_demux = new DeMux[(1<<3) - 1];
		out_demux[0] = new DeMux();
		out_demux[0].connectSrc(comb_alu, 0, 0);
		out_demux[0].connectSrc(dst_ctrl_rot[0], 0, 1);
		for (int i = 1; i < out_demux.length; i++) {
			out_demux[i] = new DeMux();
			// heap-like organization of indexes
			out_demux[(i-1)/2].connectDst((i+1)%2, out_demux[i], 0);
			// Adding small magic number to prevent rounding errors.
			int level = (int)Math.floor(Math.log(i+1)/Math.log(2) + 1e-10);
			out_demux[i].connectSrc(dst_ctrl_rot[level], 0, 1);
		}
		
		// connecting registers
		// 000
		B.connectSrc(out_demux[out_demux.length-1], 1, BITNESS);
		// 100
		H.connectSrc(out_demux[out_demux.length-1], 0, BITNESS);
		// 010
		D.connectSrc(out_demux[out_demux.length-2], 1, BITNESS);
		// 110
		// TODO add memory here
		//D.connectSrc(out_demux[out_demux.length-2], 0, BITNESS);
		// 001
		C.connectSrc(out_demux[out_demux.length-3], 1, BITNESS);
		// 101
		L.connectSrc(out_demux[out_demux.length-3], 0, BITNESS);
		// 011
		E.connectSrc(out_demux[out_demux.length-4], 1, BITNESS);
		// 111
		A.connectSrc(out_demux[out_demux.length-4], 0, BITNESS);
		// flags
		F.connectSrc(comb_alu, 0, BITNESS);
		
		// connect ALU output
		for (int i = 0; i < BITNESS; i++) {
			A.connectSrc(alu, i, i);
			B.connectSrc(alu, i, i);
			C.connectSrc(alu, i, i);
			D.connectSrc(alu, i, i);
			E.connectSrc(alu, i, i);
			H.connectSrc(alu, i, i);
			L.connectSrc(alu, i, i);
			// for initialization
			A.connectSrc(zero, 0, i);
			B.connectSrc(zero, 0, i);
			C.connectSrc(zero, 0, i);
			D.connectSrc(zero, 0, i);
			E.connectSrc(zero, 0, i);
			H.connectSrc(zero, 0, i);
			L.connectSrc(zero, 0, i);
			F.connectSrc(zero, 0, i);
			PC.connectSrc(zero, 0, i);
			PC.connectSrc(zero, 0, i + BITNESS);
		}
		// TODO prevent flags change for mov, inc, dec etc.
		F.connectSrc(alu, BITNESS+ALU.C_FLAG_SHIFT, C_FLAG);
		F.connectSrc(alu, BITNESS+ALU.Z_FLAG_SHIFT, Z_FLAG);
		F.connectSrc(alu, BITNESS+ALU.S_FLAG_SHIFT, S_FLAG);
		F.connectSrc(alu, BITNESS+ALU.P_FLAG_SHIFT, P_FLAG);
		// TODO implement A-flag (H-flag)
		F.connectSrc(clock, 0, H_FLAG);
		F.connectSrc(clock, 0, I_FLAG);
		F.connectSrc(clock, 0, F3_FLAG);
		F.connectSrc(clock, 0, F1_FLAG);
		
		// for initialization
		A.connectSrc(zero, 0, BITNESS);
		B.connectSrc(zero, 0, BITNESS);
		C.connectSrc(zero, 0, BITNESS);
		D.connectSrc(zero, 0, BITNESS);
		E.connectSrc(zero, 0, BITNESS);
		H.connectSrc(zero, 0, BITNESS);
		L.connectSrc(zero, 0, BITNESS);
		F.connectSrc(zero, 0, BITNESS);
		PC.connectSrc(zero, 0, 2*BITNESS);
	}
	
	/** Input: Opcode byte followed by two data bytes. */
	@Override
	public Node in(int index, boolean value) {
		if (index < BITNESS)
			opNOPs[index].in(0, value);
		else if (index < 2*BITNESS)
			inNOPs_A[index-BITNESS].in(0, value);
		else if (index < 3*BITNESS)
			inNOPs_B[index-2*BITNESS].in(0, value);
		else
			clock.in(0, value);
		return this;
	}

	/** Returns address of next instruction for execution. */
	@Override
	public boolean out(int index) {
		return PC.out(index);
	}

	@Override
	public boolean isReady() {
		for (Node n : opNOPs)
			if (!n.isReady())
				return false;
		for (Node n : inNOPs_A)
			if (!n.isReady())
				return false;
		for (Node n : inNOPs_B)
			if (!n.isReady())
				return false;
		return clock.isReady();
	}

	@Override
	public void reset() {
		if (inNOPs_A != null) {
			for (int i = 0; i < BITNESS; i++) {
				inNOPs_A[i].reset();
				inNOPs_B[i].reset();
			}
		}
		if (opNOPs != null) {
			for (int i = 0; i < BITNESS; i++) {
				opNOPs[i].reset();
			}
		}
		if (clock != null)
			clock.reset();
	}
	
	@Override
	public void propagate(boolean force) {
		if (!force && !isReady())
			return;
		for (int i = 0; i < BITNESS; i++) {
			inNOPs_A[i].propagate();
			inNOPs_B[i].propagate();
		}
		for (int i = 0; i < BITNESS; i++) {
			opNOPs[i].propagate();
		}
		clock.propagate();
		super.propagate(true);
	}

}
