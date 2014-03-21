package net.dtkanov.blocks.logic;

public class AddrPair {
	public Node node;
	public int port;
	
	public AddrPair(Node dst_node, int dst_port) {
		node = dst_node;
		port = dst_port;
	}
}