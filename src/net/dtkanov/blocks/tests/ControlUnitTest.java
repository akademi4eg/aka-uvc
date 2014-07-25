package net.dtkanov.blocks.tests;

import static org.junit.Assert.*;

import net.dtkanov.blocks.circuit.high_level.derived.ControlUnit;
import net.dtkanov.blocks.logic.ConstantNode;

import org.junit.Before;
import org.junit.Test;

public class ControlUnitTest {
	private ControlUnit cu;
	private ConstantNode in_op[];
	private ConstantNode in_data1[];
	private ConstantNode in_data2[];
	private ConstantNode clock;
	
	@Before
	public void setUp() throws Exception {
		cu = new ControlUnit();
		in_op = new ConstantNode[ControlUnit.BITNESS];
		in_data1 = new ConstantNode[ControlUnit.BITNESS];
		in_data2 = new ConstantNode[ControlUnit.BITNESS];
		for (int i = 0; i < ControlUnit.BITNESS; i++) {
			in_op[i] = new ConstantNode(false);
			in_op[i].connectDst(0, cu, i);
			in_data1[i] = new ConstantNode(false);
			in_data1[i].connectDst(0, cu, i+ControlUnit.BITNESS);
			in_data2[i] = new ConstantNode(false);
			in_data2[i].connectDst(0, cu, i+2*ControlUnit.BITNESS);
		}
		clock = new ConstantNode(true);
		clock.connectDst(0, cu, 3*ControlUnit.BITNESS);
	}

	@Test
	public void MOVTest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}
		
		// MVI B, 01001011b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(false).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(false).propagate();
		
		in_data1[0].setValue(true).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(true).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(false).propagate();
		in_data1[6].setValue(true).propagate();
		in_data1[7].setValue(false).propagate();
		
		in_data2[0].setValue(false).propagate();
		in_data2[1].setValue(false).propagate();
		in_data2[2].setValue(false).propagate();
		in_data2[3].setValue(false).propagate();
		in_data2[4].setValue(false).propagate();
		in_data2[5].setValue(false).propagate();
		in_data2[6].setValue(false).propagate();
		in_data2[7].setValue(false).propagate();
		
		clock.setValue(true).propagate();
		
		assertTrue(cu.getRegBValue(0)==true);
		assertTrue(cu.getRegBValue(1)==true);
		assertTrue(cu.getRegBValue(2)==false);
		assertTrue(cu.getRegBValue(3)==true);
		assertTrue(cu.getRegBValue(4)==false);
		assertTrue(cu.getRegBValue(5)==false);
		assertTrue(cu.getRegBValue(6)==true);
		assertTrue(cu.getRegBValue(7)==false);
		
		// MVI H, 11111111b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(false).propagate();
		in_op[5].setValue(true).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(false).propagate();
		
		in_data1[0].setValue(true).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(true).propagate();
		in_data1[3].setValue(true).propagate();
		in_data1[4].setValue(true).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(true).propagate();
		in_data1[7].setValue(true).propagate();
		
		in_data2[0].setValue(false).propagate();
		in_data2[1].setValue(false).propagate();
		in_data2[2].setValue(false).propagate();
		in_data2[3].setValue(false).propagate();
		in_data2[4].setValue(false).propagate();
		in_data2[5].setValue(false).propagate();
		in_data2[6].setValue(false).propagate();
		in_data2[7].setValue(false).propagate();
		
		clock.setValue(true).propagate();
		
		assertTrue(cu.getRegHValue(0)==true);
		assertTrue(cu.getRegHValue(1)==true);
		assertTrue(cu.getRegHValue(2)==true);
		assertTrue(cu.getRegHValue(3)==true);
		assertTrue(cu.getRegHValue(4)==true);
		assertTrue(cu.getRegHValue(5)==true);
		assertTrue(cu.getRegHValue(6)==true);
		assertTrue(cu.getRegHValue(7)==true);
	}
	
	protected void printRegisters() {
		System.out.print("[A:");
		for (int i = 0; i < 8; i++)
			System.out.print(cu.getRegAValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[B:");
		for (int i = 0; i < 8; i++)
			System.out.print(cu.getRegBValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[C:");
		for (int i = 0; i < 8; i++)
			System.out.print(cu.getRegCValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[D:");
		for (int i = 0; i < 8; i++)
			System.out.print(cu.getRegDValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[E:");
		for (int i = 0; i < 8; i++)
			System.out.print(cu.getRegEValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[H:");
		for (int i = 0; i < 8; i++)
			System.out.print(cu.getRegHValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[L:");
		for (int i = 0; i < 8; i++)
			System.out.print(cu.getRegLValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[F:");
		for (int i = 0; i < 8; i++)
			System.out.print(cu.getFlag(i)?"1":"0");
		System.out.print("]");
		System.out.println();
	}

}
