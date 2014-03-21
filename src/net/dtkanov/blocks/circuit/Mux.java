package net.dtkanov.blocks.circuit;

import net.dtkanov.blocks.logic.ANDNode;
import net.dtkanov.blocks.logic.NOPNode;
import net.dtkanov.blocks.logic.NOTNode;
import net.dtkanov.blocks.logic.Node;
import net.dtkanov.blocks.logic.Wire;
import net.dtkanov.blocks.logic.derived.ORNode;

public class Mux extends Node {
	private Node inNOP[];
	private Node csNOP;
	private Node csNOT;
	private Node outOR = new ORNode();
	
	public Mux() {
		this(null);
	}
	
	public Mux(Wire out) {
		super(out);
		Node and_nodes[] = new ANDNode[2];
		and_nodes[0] = new ANDNode().connectDst(0, outOR, 0);
		and_nodes[0].connectSrc(inNOP[0], 0, 0)
					.connectSrc(csNOP, 0, 1);
		and_nodes[1] = new ANDNode().connectDst(0, outOR, 1);
		and_nodes[1].connectSrc(inNOP[1], 0, 0)
					.connectSrc(csNOT, 0, 1);
	}
	
	@Override
	public Node in(int index, boolean value) {
		if (index == 2) {
			csNOP.in(0, value);
			csNOT.in(0, value);
		} else {
			inNOP[index].in(0, value);
		}
		return this;
	}

	@Override
	public boolean out(int index) {
		return outOR.out(index);
	}

	@Override
	public boolean isReady() {
		return inNOP[0].isReady() && inNOP[1].isReady()
				&& csNOP.isReady() && csNOT.isReady();
	}
	
	@Override
	public void propagate(boolean force) {
		if (!force && !isReady())
			return;
		inNOP[0].propagate();
		inNOP[1].propagate();
		csNOP.propagate();
		csNOT.propagate();
		super.propagate(true);
	}

	@Override
	public void reset() {
		if (inNOP == null) {
			inNOP = new NOPNode[2];
			inNOP[0] = new NOPNode();
			inNOP[1] = new NOPNode();
		}
		if (csNOP == null)
			csNOP = new NOPNode();
		if (csNOT == null)
			csNOT = new NOTNode();
		inNOP[0].reset();
		inNOP[1].reset();
		csNOP.reset();
		csNOT.reset();
	}

}
