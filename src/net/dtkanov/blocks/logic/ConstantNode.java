package net.dtkanov.blocks.logic;

public class ConstantNode extends Node {
	private boolean value;
	
	public ConstantNode(boolean const_val) {
		this(const_val, null);
	}
	
	public ConstantNode(boolean const_val, Wire out) {
		super(out);
		value = const_val;
	}
	
	public ConstantNode setValue(boolean val) {
		value = val;
		return this;
	}
	
	@Override
	public Node in(int index, boolean value) {
		propagate();
		return this;
	}

	@Override
	public boolean out(int index) {
		return value;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void reset() {
	}
}
