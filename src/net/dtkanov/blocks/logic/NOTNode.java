package net.dtkanov.blocks.logic;
/** Class describing NOT operation. */
public class NOTNode extends Node {
	/** Is input received? */
	protected boolean state = false;
	/** Received input. */
	protected boolean data = false;
	/** Constructs NOT node without and connections. */
	public NOTNode() {
		super(null);
	}
	
	@Override
	public void reset() {
		state = false;
	}
	
	@Override
	public Node in(int index, boolean value) {
		propagate();
		data = value;
		state = true;
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
