package net.dtkanov.blocks.logic;

public class ANDNode extends Node {
	protected boolean state[];
	protected boolean data[] = new boolean[2];
	
	public ANDNode() {
		this(null);
	}
	
	public ANDNode(Wire out) {
		super(out);
	}
	
	public void reset() {
		if (state == null)
			state = new boolean[2];
		state[0] = false;
		state[1] = false;
	}

	@Override
	public Node in(int index, boolean value) {
		propagate();
		if (index == 0 || index == 1) {
			data[index] = value;
			state[index] = true;
		}
		return this;
	}

	@Override
	public boolean out(int index) {
		return data[0] && data[1];
	}

	@Override
	public boolean isReady() {
		return state[0] && state[1];
	}
}
