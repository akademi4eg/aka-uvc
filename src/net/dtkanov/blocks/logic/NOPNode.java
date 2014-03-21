package net.dtkanov.blocks.logic;

public class NOPNode extends NOTNode {
	@Override
	public boolean out(int index) {
		return data;
	}
}
