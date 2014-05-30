package net.dtkanov.blocks.circuit.high_level.derived;

import net.dtkanov.blocks.circuit.MultiAND;
import net.dtkanov.blocks.circuit.MultiOR;
import net.dtkanov.blocks.circuit.high_level.MultiMux;
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
	// operations
	protected Node opAND;
	protected Node opOR;
	
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
		outMUXs = new MultiMux[(1<<NUM_CMD_BITS) - 1];
		outMUXs[0] = new MultiMux(bitness);
		outMUXs[0].connectSrc(opNOPs[0], 0, 2*bitness);
		for (int i = 1; i < outMUXs.length; i++) {
			outMUXs[i] = new MultiMux(bitness);
			// heap-like organization of indexes
			for (int j = 0; j < bitness; j++) {
				outMUXs[i/2].connectSrc(outMUXs[i], j, j + (i%2)*bitness);
			}
			// Adding small magic number to prevent rounding errors.
			int level = (int)Math.floor(Math.log(i+1)/Math.log(2) + 1e-10);
			outMUXs[i].connectSrc(opNOPs[level], 0, 2*bitness);
		}
		initOperations();
	}
	
	protected void initOperations() {
		opOR = new MultiOR(bitness);
		for (int j = 0; j < bitness; j++) {
			opOR.connectSrc(inNOPs_A[j], 0, j);
			opOR.connectSrc(inNOPs_B[j], 0, j+bitness);
			// 0000
			opOR.connectDst(j, outMUXs[outMUXs.length/2], j);
		}
		opAND = new MultiAND(bitness);
		for (int j = 0; j < bitness; j++) {
			opAND.connectSrc(inNOPs_A[j], 0, j);
			opAND.connectSrc(inNOPs_B[j], 0, j+bitness);
			// 0001
			opAND.connectDst(j, outMUXs[outMUXs.length/2], j+bitness);
		}
		// TODO connect operations, remove this stub
		for (int i = 1; i < 1<<(NUM_CMD_BITS-1); i++) {
			for (int j = 0; j < bitness; j++) {
				outMUXs[i+outMUXs.length/2].connectSrc(inNOPs_A[i], 0, j);
				outMUXs[i+outMUXs.length/2].connectSrc(inNOPs_B[i], 0, j + bitness);
			}
		}
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
		return outMUXs[0].out(index);
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
		if (inNOPs_A != null) {
			for (int i = 0; i < bitness; i++) {
				inNOPs_A[i].reset();
				inNOPs_B[i].reset();
			}
		}
		if (opNOPs != null) {
			for (int i = 0; i < NUM_CMD_BITS; i++) {
				opNOPs[i].reset();
			}
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
		super.propagate(true);
	}
}
