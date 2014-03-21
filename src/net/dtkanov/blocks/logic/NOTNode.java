package net.dtkanov.blocks.logic;

public class NOTNode extends Node {
	protected boolean state = false;
	protected boolean data = false;
	
	public NOTNode() {
		this(null);
	}
	
	public NOTNode(Wire out) {
		super(out);
	}
	
	@Override
	public void reset() {
		state = false;
	}
	
	@Override
	public Node in(int index, boolean value) {
		propagate();
		if (index == 0) {
			data = value;
			state = true;
		}
		return this;
	}

	@Override
	public boolean out(int index) {
		return !data;
	}

	@Override
	public boolean isReady() {
		return state;
	}

}
