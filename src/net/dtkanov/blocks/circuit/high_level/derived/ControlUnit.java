package net.dtkanov.blocks.circuit.high_level.derived;

import net.dtkanov.blocks.circuit.DeMux;
import net.dtkanov.blocks.circuit.LookUp;
import net.dtkanov.blocks.circuit.high_level.MultiMux;
import net.dtkanov.blocks.circuit.high_level.Register;
import net.dtkanov.blocks.logic.ConstantNode;
import net.dtkanov.blocks.logic.NOPNode;
import net.dtkanov.blocks.logic.Node;
/** Implements control unit. */
public class ControlUnit extends Node {
	/** Bitness of CPU. */
	public static int BITNESS = 8;
	/** Carry-flag index. */
	public static int C_FLAG = 0;
	/** Parity-flag index. */
	public static int P_FLAG = 2;
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
		alu = new ALU(BITNESS);// 8-bit ALU
		// control byte
		byte[] alu_lookup = new byte[1<<8];
		for (int i = 0; i < 1<<6; i++) {
			alu_lookup[(1<<6) + i] = 0b0111;// MOV => OP1
			alu_lookup[i] = 0b0111;// MVI => OP1
		}
		for (int i = 0; i < 1<<3; i++) {
			alu_lookup[0b10000000 + i] = 0b0001;// ADD => ADD
			alu_lookup[0b10001000 + i] = 0b0001;// ADC => ADD
			
			alu_lookup[0b10010000 + i] = 0b1001;// SUB => SUB
			alu_lookup[0b10011000 + i] = 0b1001;// SBB => SUB
			
			alu_lookup[0b00000100 + (i<<3)] = 0b0101;// INR => INC
			alu_lookup[0b00000101 + (i<<3)] = 0b1101;// DER => DEC
			
			alu_lookup[0b10100000 + i] = 0b0000;// ANA => AND
			alu_lookup[0b10110000 + i] = 0b1000;// ORA => OR
			alu_lookup[0b10101000 + i] = 0b0100;// XRA => XOR
		}
		alu_lookup[0b11000110] = 0b0001;// ADI => ADD
		alu_lookup[0b11001110] = 0b0001;// ACI => ADD
		alu_lookup[0b11010110] = 0b1001;// SUI => SUB
		alu_lookup[0b11011110] = 0b1001;// SBI => SUB
		alu_lookup[0b11100110] = 0b0000;// ANI => AND
		alu_lookup[0b11110110] = 0b1000;// ORI => OR
		alu_lookup[0b11101110] = 0b0100;// XRI => XOR
		alu_lookup[0b00000111] = 0b0110;// RLC => ROL
		alu_lookup[0b00001111] = 0b1110;// RRC => ROR
		alu_ctrl = new LookUp(5, alu_lookup);
		mem_alu_in = new LookUp(BITNESS*2, storage);// 16-bit addressing
		///////////////////////////////////////////////////////////////////////
		initInputFromRegisters();
		initOutputToRegisters();
	}
	
	private void initInputFromRegisters() {
		// control byte format: xxDDDSSS or CCCCCSSS
		final int REG_SEL_CNT = 3;
		ALU_in_mux_A = new MultiMux[(1<<REG_SEL_CNT) - 1];
		ALU_in_mux_A[0] = new MultiMux(BITNESS);
		ALU_in_mux_A[0].connectSrc(opNOPs[0], 0, 2*BITNESS);
		for (int i = 1; i < ALU_in_mux_A.length; i++) {
			ALU_in_mux_A[i] = new MultiMux(BITNESS);
			// heap-like organization of indexes
			for (int j = 0; j < BITNESS; j++) {
				ALU_in_mux_A[(i-1)/2].connectSrc(ALU_in_mux_A[i], j, j + ((i+1)%2)*BITNESS);
			}
			// Adding small magic number to prevent rounding errors.
			int level = (int)Math.floor(Math.log(i+1)/Math.log(2) + 1e-10);
			ALU_in_mux_A[i].connectSrc(opNOPs[level], 0, 2*BITNESS);
		}
		
		// connecting ALU inputs
		for (int j = 0; j < BITNESS; j++) {
			ALU_in_mux_A[0].connectDst(j, alu, j);
			A.connectDst(j, alu, j+BITNESS);
		}
		for (int j = 0; j < 5; j++) {
			alu_ctrl.connectSrc(opNOPs[3+j], 0, j);
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
		final int REG_SEL_CNT = 3;
		out_demux = new DeMux[(1<<REG_SEL_CNT) - 1];
		out_demux[0] = new DeMux();
		out_demux[0].connectSrc(opNOPs[3], 0, 1);
		for (int i = 1; i < out_demux.length; i++) {
			out_demux[i] = new DeMux();
			// heap-like organization of indexes
			out_demux[(i-1)/2].connectDst((i+1)%2, out_demux[i], 0);
			// Adding small magic number to prevent rounding errors.
			int level = (int)Math.floor(Math.log(i+1)/Math.log(2) + 1e-10);
			out_demux[i].connectSrc(opNOPs[level+3], 0, 1);
		}
		
		// connecting registers
		// 000
		B.connectSrc(out_demux[out_demux.length-1], 0, BITNESS);
		// 100
		H.connectSrc(out_demux[out_demux.length-1], 0, BITNESS);
		// 010
		D.connectSrc(out_demux[out_demux.length-2], 0, BITNESS);
		// 110
		// TODO add memory here
		D.connectSrc(out_demux[out_demux.length-2], 0, BITNESS);
		// 001
		C.connectSrc(out_demux[out_demux.length-3], 0, BITNESS);
		// 101
		L.connectSrc(out_demux[out_demux.length-3], 0, BITNESS);
		// 011
		E.connectSrc(out_demux[out_demux.length-4], 0, BITNESS);
		// 111
		A.connectSrc(out_demux[out_demux.length-4], 0, BITNESS);
		
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
		}
		
		// for initialization
		A.connectSrc(clock, 0, BITNESS);
		B.connectSrc(clock, 0, BITNESS);
		C.connectSrc(clock, 0, BITNESS);
		D.connectSrc(clock, 0, BITNESS);
		E.connectSrc(clock, 0, BITNESS);
		H.connectSrc(clock, 0, BITNESS);
		L.connectSrc(clock, 0, BITNESS);
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
