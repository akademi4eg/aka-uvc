package net.dtkanov.blocks.circuit.high_level.derived;

import net.dtkanov.blocks.circuit.DeMux;
import net.dtkanov.blocks.circuit.Memory;
import net.dtkanov.blocks.circuit.high_level.MultiMux;
import net.dtkanov.blocks.circuit.high_level.Register;
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
	/** Register-assistant. */
	private Register W;
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
	/** Selector for ALU operand 1 */
	private Node ALU_in_mux_A[];
	/** Selector for ALU operand 2 */
	private Node ALU_in_mux_B[];
	/** Selector for output to registers. */
	private Node out_demux[];
	/** ALU */
	private ALU alu;
	/** Memory */
	private Memory mem;
	
	public ControlUnit() {
		super(null);
		initInputs();
		initElements();
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
		alu = new ALU(BITNESS*2);
		mem = new Memory(BITNESS*2);
		initOutputToRegisters();
	}
	
	private void initOutputToRegisters() {
		final int REG_SEL_CNT = 4;
		out_demux = new DeMux[(1<<REG_SEL_CNT) - 1];
		out_demux[0] = new DeMux();
		//out_demux[0].connectSrc(REG_CTRL[0], 0, 1);
		for (int i = 1; i < out_demux.length; i++) {
			out_demux[i] = new DeMux();
			// heap-like organization of indexes
			out_demux[(i-1)/2].connectDst((i+1)%2, out_demux[i], 0);
			// Adding small magic number to prevent rounding errors.
			//int level = (int)Math.floor(Math.log(i+1)/Math.log(2) + 1e-10);
			//out_demux[i].connectSrc(REG_CTRL[level], 0, 1);
		}
	}
	
	/** Input: Opcode byte followed by two data bytes. */
	@Override
	public Node in(int index, boolean value) {
		if (index < BITNESS)
			opNOPs[index].in(0, value);
		else if (index < 2*BITNESS)
			inNOPs_A[index].in(0, value);
		else
			inNOPs_B[index].in(0, value);
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
		return true;
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
		super.propagate(true);
	}

}
