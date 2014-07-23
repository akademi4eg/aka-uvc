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
		
		assertTrue(cu.getRegBValue(0)==true);
		assertTrue(cu.getRegBValue(1)==true);
		assertTrue(cu.getRegBValue(2)==false);
		assertTrue(cu.getRegBValue(3)==true);
		assertTrue(cu.getRegBValue(4)==false);
		assertTrue(cu.getRegBValue(5)==false);
		assertTrue(cu.getRegBValue(6)==true);
		assertTrue(cu.getRegBValue(7)==false);
	}

}
