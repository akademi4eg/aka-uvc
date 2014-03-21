package net.dtkanov.blocks.core;

import net.dtkanov.blocks.logic.*;
import net.dtkanov.blocks.logic.derived.*;

public class AppCore {
	public static void main(String[] args) {
		Node in1 = new ConstantNode(true);
		Node in2 = new ConstantNode(false);
		
		Node out = new XORNode().connectSrc(in1, 0, 0)
								.connectSrc(in2, 0, 1);
		in1.propagate();
		in2.propagate();
		System.out.println(out.out(0));
		in1.propagate();
		in2.propagate();
		System.out.println(out.out(0));
	}
}
