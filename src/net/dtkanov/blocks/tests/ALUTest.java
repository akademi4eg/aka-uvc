package net.dtkanov.blocks.tests;

import static org.junit.Assert.*;

import net.dtkanov.blocks.circuit.high_level.Register;
import net.dtkanov.blocks.circuit.high_level.derived.ALU;
import net.dtkanov.blocks.logic.ConstantNode;

import org.junit.Before;
import org.junit.Test;

public class ALUTest {
	private ALU alu;
	private int num_bits;
	private Register op1;
	private Register op2;
	private ConstantNode ctrl[];

	@Before
	public void setUp() throws Exception {
		num_bits = 4;
		alu = new ALU(num_bits);
		op1 = new Register(num_bits);
		op2 = new Register(num_bits);
		ctrl = new ConstantNode[ALU.NUM_CMD_BITS];
		for (int i = 0; i < num_bits; i++) {
			op1.connectDst(i, alu, i);
			op2.connectDst(i, alu, i+num_bits);
		}
		for (int i = 0; i < ctrl.length; i++) {
			ctrl[i] = new ConstantNode(true);
			ctrl[i].connectDst(0, alu, i + 2*num_bits);
		}
	}

	@Test
	public void ANDTest() {
		ctrl[0].setValue(false);
		ctrl[1].setValue(false);
		ctrl[2].setValue(false);
		ctrl[3].setValue(false);
		
		op1.in(0, true)
		   .in(1, false)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		op2.in(0, true)
		   .in(1, false)
		   .in(2, true)
		   .in(3, false)
		   .in(4, true);
		
		op1.propagate();
		op2.propagate();
		for (int i = 0; i < ctrl.length; i++)
			ctrl[i].propagate();
		
		assertTrue(alu.out(0)==true);
		assertTrue(alu.out(1)==false);
		assertTrue(alu.out(2)==false);
		assertTrue(alu.out(3)==false);
		
		op1.in(0, false)
		   .in(1, true)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		op2.in(0, true)
		   .in(1, false)
		   .in(2, false)
		   .in(3, false)
		   .in(4, true);
		
		op1.propagate();
		op2.propagate();
		for (int i = 0; i < ctrl.length; i++)
			ctrl[i].propagate();
		
		assertTrue(alu.out(0)==false);
		assertTrue(alu.out(1)==false);
		assertTrue(alu.out(2)==false);
		assertTrue(alu.out(3)==false);
	}
	
	@Test
	public void ORTest() {
		ctrl[0].setValue(false);
		ctrl[1].setValue(false);
		ctrl[2].setValue(false);
		ctrl[3].setValue(true);
		
		op1.in(0, true)
		   .in(1, false)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		op2.in(0, true)
		   .in(1, false)
		   .in(2, true)
		   .in(3, false)
		   .in(4, true);
		
		op1.propagate();
		op2.propagate();
		for (int i = 0; i < ctrl.length; i++)
			ctrl[i].propagate();
		
		assertTrue(alu.out(0)==true);
		assertTrue(alu.out(1)==false);
		assertTrue(alu.out(2)==true);
		assertTrue(alu.out(3)==true);
		
		op1.in(0, false)
		   .in(1, true)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		op2.in(0, true)
		   .in(1, false)
		   .in(2, false)
		   .in(3, false)
		   .in(4, true);
		
		op1.propagate();
		op2.propagate();
		for (int i = 0; i < ctrl.length; i++)
			ctrl[i].propagate();
		
		assertTrue(alu.out(0)==true);
		assertTrue(alu.out(1)==true);
		assertTrue(alu.out(2)==false);
		assertTrue(alu.out(3)==true);
	}

	@Test
	public void XORTest() {
		ctrl[0].setValue(false);
		ctrl[1].setValue(false);
		ctrl[2].setValue(true);
		ctrl[3].setValue(false);
		
		op1.in(0, true)
		   .in(1, false)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		op2.in(0, true)
		   .in(1, false)
		   .in(2, true)
		   .in(3, false)
		   .in(4, true);
		
		op1.propagate();
		op2.propagate();
		for (int i = 0; i < ctrl.length; i++)
			ctrl[i].propagate();
		
		assertTrue(alu.out(0)==false);
		assertTrue(alu.out(1)==false);
		assertTrue(alu.out(2)==true);
		assertTrue(alu.out(3)==true);
		
		op1.in(0, false)
		   .in(1, true)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		op2.in(0, true)
		   .in(1, false)
		   .in(2, false)
		   .in(3, false)
		   .in(4, true);
		
		op1.propagate();
		op2.propagate();
		for (int i = 0; i < ctrl.length; i++)
			ctrl[i].propagate();
		
		assertTrue(alu.out(0)==true);
		assertTrue(alu.out(1)==true);
		assertTrue(alu.out(2)==false);
		assertTrue(alu.out(3)==true);
	}
	
	@Test
	public void NOTTest() {
		ctrl[0].setValue(false);
		ctrl[1].setValue(false);
		ctrl[2].setValue(true);
		ctrl[3].setValue(true);
		
		op1.in(0, true)
		   .in(1, false)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		op2.in(0, true)
		   .in(1, false)
		   .in(2, true)
		   .in(3, false)
		   .in(4, true);
		
		op1.propagate();
		op2.propagate();
		for (int i = 0; i < ctrl.length; i++)
			ctrl[i].propagate();
		
		assertTrue(alu.out(0)==false);
		assertTrue(alu.out(1)==true);
		assertTrue(alu.out(2)==true);
		assertTrue(alu.out(3)==false);
		
		op1.in(0, false)
		   .in(1, true)
		   .in(2, false)
		   .in(3, true)
		   .in(4, true);
		op2.in(0, true)
		   .in(1, false)
		   .in(2, false)
		   .in(3, false)
		   .in(4, true);
		
		op1.propagate();
		op2.propagate();
		for (int i = 0; i < ctrl.length; i++)
			ctrl[i].propagate();
		
		assertTrue(alu.out(0)==true);
		assertTrue(alu.out(1)==false);
		assertTrue(alu.out(2)==true);
		assertTrue(alu.out(3)==false);
	}
}
