package net.dtkanov.blocks.tests;

import static org.junit.Assert.*;

import net.dtkanov.blocks.circuit.high_level.Complementer;
import net.dtkanov.blocks.circuit.high_level.Incrementer;
import net.dtkanov.blocks.circuit.high_level.MultiAdder;
import net.dtkanov.blocks.circuit.high_level.MultiMux;
import net.dtkanov.blocks.circuit.high_level.Register;
import net.dtkanov.blocks.logic.ConstantNode;
import net.dtkanov.blocks.logic.Node;

import org.junit.Test;

public class HighLevelCircuitsTest {

	@Test
	public void RegisterTest() {
		int num_bits = 4;
		Node reg = new Register(num_bits);
		ConstantNode in[] = new ConstantNode[num_bits+1];
		for (int i = 0; i <= num_bits; i++) {
			in[i] = new ConstantNode(true);
			in[i].connectDst(0, reg, i);
		}
		
		for (int i = 0; i <= num_bits; i++) {
			in[i].propagate();
		}
		for (int i = 0; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}

		in[num_bits].setValue(false);
		for (int i = 0; i <= num_bits; i++) {
			in[i].propagate(true);
		}
		for (int i = 0; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in[0].setValue(false);
		for (int i = 0; i <= num_bits; i++) {
			in[i].propagate(true);
		}
		assertTrue(reg.out(0));
		assertTrue(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in[1].setValue(false);
		in[num_bits].setValue(true);
		for (int i = 0; i <= num_bits; i++) {
			in[i].propagate(true);
		}
		assertFalse(reg.out(0));
		assertFalse(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in[0].setValue(true);
		for (int i = 0; i <= num_bits; i++) {
			in[i].propagate(true);
		}
		assertTrue(reg.out(0));
		assertFalse(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
	}

	@Test
	public void MultiAdderTest() {
		int num_bits = 4;
		Node reg1 = new Register(num_bits);
		Node reg2 = new Register(num_bits);
		ConstantNode C = new ConstantNode(false);
		Node add = new MultiAdder(num_bits);
		for (int i = 0; i < num_bits; i++) {
			add.connectSrc(reg1, i, i);
			add.connectSrc(reg2, i, i+num_bits);
		}
		add.connectSrc(C, 0, 2*num_bits);
		
		// 10 == 1010b
		reg1.in(0, false)
			.in(1, true)
			.in(2, false)
			.in(3, true)
			.in(4, true);
		// 7 == 0111b
		reg2.in(0, true)
			.in(1, true)
			.in(2, true)
			.in(3, false)
			.in(4, true);
		reg1.propagate();
		reg2.propagate();
		C.propagate();
		// 17 == (1)0001b
		assertTrue(add.out(0)==true);
		assertTrue(add.out(1)==false);
		assertTrue(add.out(2)==false);
		assertTrue(add.out(3)==false);
		assertTrue(add.out(4)==true);
		
		C.setValue(true);
		// 2 == 0010b
		reg1.in(0, false)
			.in(1, true)
			.in(2, false)
			.in(3, false)
			.in(4, true);
		// 7 == 0111b
		reg2.in(0, true)
			.in(1, true)
			.in(2, true)
			.in(3, false)
			.in(4, true);
		reg1.propagate();
		reg2.propagate();
		C.propagate();
		// 10 == (0)1010b
		assertTrue(add.out(0)==false);
		assertTrue(add.out(1)==true);
		assertTrue(add.out(2)==false);
		assertTrue(add.out(3)==true);
		assertTrue(add.out(4)==false);
	}
	
	@Test
	public void IncrementerTest() {
		int num_bits = 4;
		Node reg = new Register(num_bits);
		Node add = new Incrementer(num_bits);
		for (int i = 0; i < num_bits; i++) {
			add.connectSrc(reg, i, i);
		}
		
		// 10 == 1010b
		reg.in(0, false)
		   .in(1, true)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		reg.propagate();
		// 11 == (0)1011b
		assertTrue(add.out(0)==true);
		assertTrue(add.out(1)==true);
		assertTrue(add.out(2)==false);
		assertTrue(add.out(3)==true);
		assertTrue(add.out(4)==false);
		
		// 3 == 0011b
		reg.in(0, true)
		   .in(1, true)
		   .in(2, false)
		   .in(3, false)
		   .in(4, true);
		reg.propagate();
		// 4 == (0)0100b
		assertTrue(add.out(0)==false);
		assertTrue(add.out(1)==false);
		assertTrue(add.out(2)==true);
		assertTrue(add.out(3)==false);
		assertTrue(add.out(4)==false);
		
		// 15 == 1111b
		reg.in(0, true)
		   .in(1, true)
		   .in(2, true)
		   .in(3, true)
		   .in(4, true);
		reg.propagate();
		// 16 == (1)0000b
		assertTrue(add.out(0)==false);
		assertTrue(add.out(1)==false);
		assertTrue(add.out(2)==false);
		assertTrue(add.out(3)==false);
		assertTrue(add.out(4)==true);
	}
	
	@Test
	public void ComplementerTest() {
		int num_bits = 4;
		Node reg = new Register(num_bits);
		Node add = new Complementer(num_bits);
		for (int i = 0; i < num_bits; i++) {
			add.connectSrc(reg, i, i);
		}
		
		// -6 == 1010b
		reg.in(0, false)
		   .in(1, true)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		reg.propagate();
		// 6 == 0110b
		assertTrue(add.out(0)==false);
		assertTrue(add.out(1)==true);
		assertTrue(add.out(2)==true);
		assertTrue(add.out(3)==false);
		
		// 3 == 0011b
		reg.in(0, true)
		   .in(1, true)
		   .in(2, false)
		   .in(3, false)
		   .in(4, true);
		reg.propagate();
		// -3 == 1101b
		assertTrue(add.out(0)==true);
		assertTrue(add.out(1)==false);
		assertTrue(add.out(2)==true);
		assertTrue(add.out(3)==true);
	}
	
	@Test
	public void MultiMuxTest() {
		int num_bits = 4;
		Node in1 = new Register(num_bits);
		Node in2 = new Register(num_bits);
		ConstantNode control = new ConstantNode(true);
		Node mux = new MultiMux(num_bits);
		for (int i = 0; i < num_bits; i++) {
			mux.connectSrc(in1, i, i);
			mux.connectSrc(in2, i, i+num_bits);
		}
		mux.connectSrc(control, 0, 2*num_bits);
		
		// 3 == 0011b
		in1.in(0, true)
		   .in(1, true)
		   .in(2, false)
		   .in(3, false)
		   .in(4, true);
		// -6 == 1010b
		in2.in(0, false)
		   .in(1, true)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		
		in1.propagate(true);
		in2.propagate(true);
		control.propagate(true);
		// 3 == 0011b
		assertTrue(mux.out(0)==true);
		assertTrue(mux.out(1)==true);
		assertTrue(mux.out(2)==false);
		assertTrue(mux.out(3)==false);
		
		in1.propagate(true);
		in2.propagate(true);
		control.propagate(true);
		// 3 == 0011b
		assertTrue(mux.out(0)==true);
		assertTrue(mux.out(1)==true);
		assertTrue(mux.out(2)==false);
		assertTrue(mux.out(3)==false);
		
		in1.propagate(true);
		in2.propagate(true);
		control.setValue(false).propagate(true);
		// -6 == 1010b
		assertTrue(mux.out(0)==false);
		assertTrue(mux.out(1)==true);
		assertTrue(mux.out(2)==false);
		assertTrue(mux.out(3)==true);
		
		in1.propagate(true);
		in2.in(0, false)
		   .in(1, true)
		   .in(2, false)
		   .in(3, false)
		   .in(4, true).propagate(true);
		control.propagate(true);
		// 2 == 0010b
		assertTrue(mux.out(0)==false);
		assertTrue(mux.out(1)==true);
		assertTrue(mux.out(2)==false);
		assertTrue(mux.out(3)==false);
	}
}
