package net.dtkanov.blocks.logic;

public abstract class Node {
	protected Wire out_node;
	
	protected Node(Wire out) {
		out_node = out;
		reset();
	}
	
	public Wire getWire() {
		return out_node;
	}
	
	public Node connectDst(int src_port, Node dst, int dst_port) {
		if (out_node == null)
			out_node = new Wire(this);
		out_node.addConnection(src_port, dst, dst_port);
		return this;
	}
	
	public Node connectSrc(Node src, int src_port, int dst_port) {
		src.connectDst(src_port, this, dst_port);
		return this;
	}
	
	public abstract Node in(int index, boolean value);
	public abstract boolean out(int index);
	public abstract boolean isReady();
	public abstract void reset();
	
	public void propagate() {
		propagate(false);
	}
	
	public void propagate(boolean force) {
		if (!force && !isReady())
			return;
		if (out_node != null)
			out_node.propagate();
		reset();
	}
	
	public String toString() {
		return "["+(isReady()?"+":"-")+hashCode()+":"+getClass().getSimpleName()+"]";
	}
	
	public String toStringDeep() {
		if (out_node != null)
			return toString()+System.getProperty("line.separator")+getWire().toStringDeep();
		else
			return toString();
	}
}