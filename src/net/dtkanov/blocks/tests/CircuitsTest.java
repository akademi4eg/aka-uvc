package net.dtkanov.blocks.tests;

import static org.junit.Assert.*;

import net.dtkanov.blocks.circuit.*;
import net.dtkanov.blocks.logic.*;

import org.junit.Test;

public class CircuitsTest {

	@Test
	public void SRLatchTest() {
		ConstantNode in1 = new ConstantNode(true);
		ConstantNode in2 = new ConstantNode(false);
		Node out = new SRLatch().connectSrc(in1, 0, 0)
								.connectSrc(in2, 0, 1);
		
		in1.propagate();
		in2.propagate();
		assertTrue(out.out(0)!=out.out(1));
		assertFalse(out.out(0));
		
		in1.propagate();
		in2.propagate();
		assertTrue(out.out(0)!=out.out(1));
		assertFalse(out.out(0));
		
		boolean state = out.out(0);
		in1.setValue(false).propagate();
		in2.propagate();
		assertTrue(out.out(0)!=out.out(1));
		assertTrue(out.out(0)==state);
		
		in1.propagate();
		in2.propagate();
		assertTrue(out.out(0)!=out.out(1));
		assertTrue(out.out(0)==state);
		
		in1.propagate();
		in2.setValue(true).propagate();
		assertTrue(out.out(0)!=out.out(1));
		assertTrue(out.out(0));
		
		in1.propagate();
		in2.propagate();
		assertTrue(out.out(0)!=out.out(1));
		assertTrue(out.out(0));
		
		state = out.out(0);
		in1.propagate();
		in2.setValue(false).propagate();
		assertTrue(out.out(0)!=out.out(1));
		assertTrue(out.out(0)==state);
	}
	
	@Test
	public void GatedDLatchTest() {
		ConstantNode in = new ConstantNode(true);
		ConstantNode timer = new ConstantNode(true);
		Node out = new GatedDLatch().connectSrc(in, 0, 0)
									.connectSrc(timer, 0, 1);
		in.propagate();
		timer.propagate();
		assertTrue(out.out(0));
		
		in.propagate();
		timer.setValue(false).propagate();
		assertTrue(out.out(0));
		
		in.setValue(false).propagate();
		timer.setValue(true).propagate();
		assertFalse(out.out(0));
		
		in.propagate();
		timer.setValue(false).propagate();
		assertFalse(out.out(0));
		
		in.setValue(true).propagate();
		timer.setValue(true).propagate();
		assertTrue(out.out(0));
		
		in.propagate();
		timer.setValue(false).propagate();
		assertTrue(out.out(0));
		
		in.propagate();
		timer.propagate();
		assertTrue(out.out(0));
	}
	
	@Test
	public void MuxTest() {
		ConstantNode in1 = new ConstantNode(true);
		ConstantNode in2 = new ConstantNode(false);
		ConstantNode control = new ConstantNode(true);
		Node mux = new Mux().connectSrc(in1, 0, 0)
							.connectSrc(in2, 0, 1)
							.connectSrc(control, 0, 2);
		
		in1.propagate();
		in2.propagate();
		control.propagate();
		assertTrue(mux.out(0));
		
		in1.propagate();
		in2.propagate();
		control.propagate();
		assertTrue(mux.out(0));
		
		in1.propagate();
		in2.propagate();
		control.setValue(false).propagate();
		assertFalse(mux.out(0));
		
		in1.propagate();
		in2.setValue(true).propagate();
		control.propagate();
		assertTrue(mux.out(0));
	}

}
