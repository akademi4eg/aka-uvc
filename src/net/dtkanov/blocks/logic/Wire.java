package net.dtkanov.blocks.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Wire {
	private Node in_node;
	private Map<AddrPair, Integer> out_node;
	
	public Wire(Node src) {
		Map<AddrPair, Integer> data = new HashMap<AddrPair, Integer>();
		in_node = src;
		out_node = data;
	}
	
	public Wire(Node in, Map<AddrPair, Integer> out) {
		in_node = in;
		out_node = out;
	}
	
	public void addConnection(int src_port, Node dst, int dst_port) {
		out_node.put(new AddrPair(dst, dst_port), src_port);
	}
	
	public Wire(Node src, int src_port, Node dst, int dst_port) {
		Map<AddrPair, Integer> data = new HashMap<AddrPair, Integer>();
		data.put(new AddrPair(dst, dst_port), src_port);
		in_node = src;
		out_node = data;
	}
	
	public void propagate() {
		for (Entry<AddrPair, Integer> data : out_node.entrySet()) {
			int port = data.getValue();
			AddrPair addr = data.getKey();
			addr.node.in(addr.port, in_node.out(port));
			addr.node.propagate();
		}
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Entry<AddrPair, Integer> data : out_node.entrySet()) {
			int port = data.getValue();
			AddrPair addr = data.getKey();
			str.append("("+port+") -> " + addr.node + "("+addr.port+")"+System.getProperty("line.separator"));
		}
		return str.toString();
	}
	
	public String toStringDeep() {
		StringBuilder str = new StringBuilder(toString());
		for (AddrPair addr : out_node.keySet())
			str.append(addr.node.toStringDeep()+System.getProperty("line.separator"));
		return str.toString();
	}
}
