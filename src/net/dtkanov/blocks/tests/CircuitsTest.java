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
	
	@Test
	public void MultiNOTTest() {
		int num_bits = 4;
		Node reg = new MultiNOT(num_bits);
		ConstantNode in[] = new ConstantNode[num_bits];
		for (int i = 0; i < num_bits; i++) {
			in[i] = new ConstantNode(true);
			in[i].connectDst(0, reg, i);
		}
		
		for (int i = 0; i < num_bits; i++) {
			in[i].propagate();
		}
		for (int i = 0; i < num_bits; i++) {
			assertFalse(reg.out(i));
		}
		
		in[0].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in[i].propagate();
		}
		assertTrue(reg.out(0));
		for (int i = 1; i < num_bits; i++) {
			assertFalse(reg.out(i));
		}
		
		in[1].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in[i].propagate();
		}
		assertTrue(reg.out(0));
		assertTrue(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertFalse(reg.out(i));
		}
		
		in[0].setValue(true);
		for (int i = 0; i < num_bits; i++) {
			in[i].propagate();
		}
		assertFalse(reg.out(0));
		assertTrue(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertFalse(reg.out(i));
		}
	}
	
	@Test
	public void MultiANDTest() {
		int num_bits = 4;
		Node reg = new MultiAND(num_bits);
		ConstantNode in1[] = new ConstantNode[num_bits];
		ConstantNode in2[] = new ConstantNode[num_bits];
		for (int i = 0; i < num_bits; i++) {
			in1[i] = new ConstantNode(true);
			in1[i].connectDst(0, reg, i);
			in2[i] = new ConstantNode(true);
			in2[i].connectDst(1, reg, i+num_bits);
		}
		
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		for (int i = 0; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in1[0].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertFalse(reg.out(0));
		for (int i = 1; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in2[1].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertFalse(reg.out(0));
		assertFalse(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in1[0].setValue(true);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertTrue(reg.out(0));
		assertFalse(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
	}
	
	@Test
	public void MultiORTest() {
		int num_bits = 4;
		Node reg = new MultiOR(num_bits);
		ConstantNode in1[] = new ConstantNode[num_bits];
		ConstantNode in2[] = new ConstantNode[num_bits];
		for (int i = 0; i < num_bits; i++) {
			in1[i] = new ConstantNode(true);
			in1[i].connectDst(0, reg, i);
			in2[i] = new ConstantNode(true);
			in2[i].connectDst(1, reg, i+num_bits);
		}
		
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		for (int i = 0; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in1[0].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		for (int i = 0; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in2[0].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertFalse(reg.out(0));
		for (int i = 1; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in2[1].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertFalse(reg.out(0));
		assertTrue(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in1[1].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertFalse(reg.out(0));
		assertFalse(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
	}
	
	@Test
	public void MultiXORTest() {
		int num_bits = 4;
		Node reg = new MultiXOR(num_bits);
		ConstantNode in1[] = new ConstantNode[num_bits];
		ConstantNode in2[] = new ConstantNode[num_bits];
		for (int i = 0; i < num_bits; i++) {
			in1[i] = new ConstantNode(true);
			in1[i].connectDst(0, reg, i);
			in2[i] = new ConstantNode(true);
			in2[i].connectDst(1, reg, i+num_bits);
		}
		
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		for (int i = 0; i < num_bits; i++) {
			assertFalse(reg.out(i));
		}
		
		in1[0].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertTrue(reg.out(0));
		for (int i = 1; i < num_bits; i++) {
			assertFalse(reg.out(i));
		}
		
		in2[0].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertFalse(reg.out(0));
		for (int i = 1; i < num_bits; i++) {
			assertFalse(reg.out(i));
		}
		
		in2[1].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertFalse(reg.out(0));
		assertTrue(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertFalse(reg.out(i));
		}
		
		in1[1].setValue(false);
		for (int i = 0; i < num_bits; i++) {
			in1[i].propagate();
			in2[i].propagate();
		}
		assertFalse(reg.out(0));
		assertFalse(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertFalse(reg.out(i));
		}
	}

}
