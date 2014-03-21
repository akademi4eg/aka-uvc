package net.dtkanov.blocks.tests;

import static org.junit.Assert.*;

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
			in[i].propagate();
		}
		for (int i = 0; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in[0].setValue(false);
		for (int i = 0; i <= num_bits; i++) {
			in[i].propagate();
		}
		assertTrue(reg.out(0));
		assertTrue(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in[1].setValue(false);
		in[num_bits].setValue(true);
		for (int i = 0; i <= num_bits; i++) {
			in[i].propagate();
		}
		assertFalse(reg.out(0));
		assertFalse(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
		
		in[0].setValue(true);
		for (int i = 0; i <= num_bits; i++) {
			in[i].propagate();
		}
		assertTrue(reg.out(0));
		assertFalse(reg.out(1));
		for (int i = 2; i < num_bits; i++) {
			assertTrue(reg.out(i));
		}
	}

}
