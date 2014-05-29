package net.dtkanov.blocks.circuit.high_level.derived;

import net.dtkanov.blocks.circuit.Mux;
import net.dtkanov.blocks.logic.NOPNode;
import net.dtkanov.blocks.logic.Node;
/** Implements 2 operands arithmetic-logic unit. */
public class ALU extends Node {
	/** Number of bits representing command selection. */
	public static final int NUM_CMD_BITS = 4; 
	
	protected int bitness;
	protected Node inNOPs_A[];
	protected Node inNOPs_B[];
	protected Node opNOPs[];
	protected Node outMUXs[];
	
	public ALU(int num_bits) {
		super(null);
		bitness = num_bits;
		inNOPs_A = new NOPNode[num_bits];
		inNOPs_B = new NOPNode[num_bits];
		for (int i = 0; i < num_bits; i++) {
			inNOPs_A[i] = new NOPNode();
			inNOPs_B[i] = new NOPNode();
		}
		opNOPs = new NOPNode[NUM_CMD_BITS];
		for (int i = 0; i < NUM_CMD_BITS; i++) {
			opNOPs[i] = new NOPNode();
		}
		outMUXs = new Mux[(1<<NUM_CMD_BITS) - 1];
		outMUXs[0] = new Mux();
		for (int i = 1; i < outMUXs.length; i++) {
			outMUXs[i] = new Mux();
			// heap-like organization of indexes
			outMUXs[i/2].connectSrc(outMUXs[i], 0, i%2);
		}
		// TODO connect muxs
	}

	@Override
	public Node in(int index, boolean value) {
		if (index < bitness) {
			inNOPs_A[index].in(0, value);
		} else if (index < 2*bitness) {
			inNOPs_A[index-bitness].in(0, value);
		} else {
			opNOPs[index - 2*bitness].in(0, value);
		}
		return this;
	}

	@Override
	public boolean out(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReady() {
		for (int i = 0; i < bitness; i++) {
			if (!inNOPs_A[i].isReady() || !inNOPs_B[i].isReady())
				return false;
		}
		for (int i = 0; i < NUM_CMD_BITS; i++) {
			if (!opNOPs[i].isReady())
				return false;
		}
		return true;
	}

	@Override
	public void reset() {
		for (int i = 0; i < bitness; i++) {
			inNOPs_A[i].reset();
			inNOPs_B[i].reset();
		}
		for (int i = 0; i < NUM_CMD_BITS; i++) {
			opNOPs[i].reset();
		}
	}

	@Override
	public void propagate(boolean force) {
		if (!force && !isReady())
			return;
		for (int i = 0; i < bitness; i++) {
			inNOPs_A[i].propagate();
			inNOPs_B[i].propagate();
		}
		for (int i = 0; i < NUM_CMD_BITS; i++) {
			opNOPs[i].propagate();
		}
		// TODO finish implementation
		super.propagate(true);
	}
}
