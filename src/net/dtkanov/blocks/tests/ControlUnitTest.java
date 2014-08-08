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
	public void MVITest() {
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
	
	@Test
	public void MOVTest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}

		// MVI C, 10101010b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(false).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(false).propagate();
		
		in_data1[0].setValue(false).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(true).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(false).propagate();
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
		
		assertTrue(cu.getRegCValue(0)==false);
		assertTrue(cu.getRegCValue(1)==true);
		assertTrue(cu.getRegCValue(2)==false);
		assertTrue(cu.getRegCValue(3)==true);
		assertTrue(cu.getRegCValue(4)==false);
		assertTrue(cu.getRegCValue(5)==true);
		assertTrue(cu.getRegCValue(6)==false);
		assertTrue(cu.getRegCValue(7)==true);

		// MVI E, C
		in_op[0].setValue(true).propagate();
		in_op[1].setValue(false).propagate();
		in_op[2].setValue(false).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(true).propagate();
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

		assertTrue(cu.getRegEValue(0)==false);
		assertTrue(cu.getRegEValue(1)==true);
		assertTrue(cu.getRegEValue(2)==false);
		assertTrue(cu.getRegEValue(3)==true);
		assertTrue(cu.getRegEValue(4)==false);
		assertTrue(cu.getRegEValue(5)==true);
		assertTrue(cu.getRegEValue(6)==false);
		assertTrue(cu.getRegEValue(7)==true);
	}
	
	@Test
	public void ADITest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}
		
		// MVI A, 11001011b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(false).propagate();
		
		in_data1[0].setValue(true).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(true).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(false).propagate();
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
		
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==true);
		
		// ADI 00100111b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(false).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(true).propagate();
		in_op[7].setValue(true).propagate();
		
		in_data1[0].setValue(true).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(true).propagate();
		in_data1[3].setValue(false).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(false).propagate();
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

		// 11001011b + 00100111b = 11110010b
		assertTrue(cu.getRegAValue(0)==false);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==false);
		assertTrue(cu.getRegAValue(4)==true);
		assertTrue(cu.getRegAValue(5)==true);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==true);
	}
	
	@Test
	public void ADDTest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}
		
		// MVI A, 11001011b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(false).propagate();
		
		in_data1[0].setValue(true).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(true).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(false).propagate();
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
		
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==true);
		
		// MVI D, 00101010b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(false).propagate();
		
		in_data1[0].setValue(false).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(true).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(false).propagate();
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

		assertTrue(cu.getRegDValue(0)==false);
		assertTrue(cu.getRegDValue(1)==true);
		assertTrue(cu.getRegDValue(2)==false);
		assertTrue(cu.getRegDValue(3)==true);
		assertTrue(cu.getRegDValue(4)==false);
		assertTrue(cu.getRegDValue(5)==true);
		assertTrue(cu.getRegDValue(6)==false);
		assertTrue(cu.getRegDValue(7)==false);
		
		// ADD D
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(false).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(false).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(true).propagate();
		
		in_data1[0].setValue(false).propagate();
		in_data1[1].setValue(false).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(false).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(false).propagate();
		in_data1[6].setValue(false).propagate();
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

		// 11001011b + 00101010b = 11110101b
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==false);
		assertTrue(cu.getRegAValue(2)==true);
		assertTrue(cu.getRegAValue(3)==false);
		assertTrue(cu.getRegAValue(4)==true);
		assertTrue(cu.getRegAValue(5)==true);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==true);
	}
	
	@Test
	public void SUITest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}
		
		// MVI A, 01001011b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
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
		
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==false);
		
		// SUI 00100111b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(true).propagate();
		in_op[7].setValue(true).propagate();
		
		in_data1[0].setValue(true).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(true).propagate();
		in_data1[3].setValue(false).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(false).propagate();
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

		// 01001011b - 00100111b = 00100100b
		assertTrue(cu.getRegAValue(0)==false);
		assertTrue(cu.getRegAValue(1)==false);
		assertTrue(cu.getRegAValue(2)==true);
		assertTrue(cu.getRegAValue(3)==false);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==true);
		assertTrue(cu.getRegAValue(6)==false);
		assertTrue(cu.getRegAValue(7)==false);
	}
	
	@Test
	public void SUBTest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}
		
		// MVI A, 01001011b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
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
		
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==false);
		
		// MVI D, 00101010b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(false).propagate();
		
		in_data1[0].setValue(false).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(true).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(false).propagate();
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

		assertTrue(cu.getRegDValue(0)==false);
		assertTrue(cu.getRegDValue(1)==true);
		assertTrue(cu.getRegDValue(2)==false);
		assertTrue(cu.getRegDValue(3)==true);
		assertTrue(cu.getRegDValue(4)==false);
		assertTrue(cu.getRegDValue(5)==true);
		assertTrue(cu.getRegDValue(6)==false);
		assertTrue(cu.getRegDValue(7)==false);
		
		// SUB D
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(false).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(true).propagate();
		
		in_data1[0].setValue(false).propagate();
		in_data1[1].setValue(false).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(false).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(false).propagate();
		in_data1[6].setValue(false).propagate();
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

		// 01001011b - 00101010b = 00100001b
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==false);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==false);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==true);
		assertTrue(cu.getRegAValue(6)==false);
		assertTrue(cu.getRegAValue(7)==false);
	}
	
	@Test
	public void ANITest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}
		
		// MVI A, 01001011b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
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
		
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==false);
		
		// ANI 00100111b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(false).propagate();
		in_op[5].setValue(true).propagate();
		in_op[6].setValue(true).propagate();
		in_op[7].setValue(true).propagate();
		
		in_data1[0].setValue(true).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(true).propagate();
		in_data1[3].setValue(false).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(false).propagate();
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

		// 01001011b & 00100111b = 00000011b
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==false);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==false);
		assertTrue(cu.getRegAValue(7)==false);
	}
	
	@Test
	public void ANATest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}
		
		// MVI A, 01001011b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
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
		
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==false);
		
		// MVI C, 00101010b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(false).propagate();
		in_op[5].setValue(false).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(false).propagate();
		
		in_data1[0].setValue(false).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(true).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(false).propagate();
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

		assertTrue(cu.getRegCValue(0)==false);
		assertTrue(cu.getRegCValue(1)==true);
		assertTrue(cu.getRegCValue(2)==false);
		assertTrue(cu.getRegCValue(3)==true);
		assertTrue(cu.getRegCValue(4)==false);
		assertTrue(cu.getRegCValue(5)==true);
		assertTrue(cu.getRegCValue(6)==false);
		assertTrue(cu.getRegCValue(7)==false);
		
		// ANA C
		in_op[0].setValue(true).propagate();
		in_op[1].setValue(false).propagate();
		in_op[2].setValue(false).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(false).propagate();
		in_op[5].setValue(true).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(true).propagate();
		
		in_data1[0].setValue(false).propagate();
		in_data1[1].setValue(false).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(false).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(false).propagate();
		in_data1[6].setValue(false).propagate();
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

		// 01001011b & 00101010b = 00001010b
		assertTrue(cu.getRegAValue(0)==false);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==false);
		assertTrue(cu.getRegAValue(7)==false);
	}
	
	@Test
	public void ORITest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}
		
		// MVI A, 01001011b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
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
		
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==false);
		
		// ORI 00100111b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
		in_op[6].setValue(true).propagate();
		in_op[7].setValue(true).propagate();
		
		in_data1[0].setValue(true).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(true).propagate();
		in_data1[3].setValue(false).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(false).propagate();
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

		// 01001011b | 00100111b = 01101111b
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==true);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==true);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==false);
	}
	
	@Test
	public void ORATest() {
		for (int i = 0; i < 2*ControlUnit.BITNESS; i++) {
			assertTrue(cu.getRegPCValue(i)==false);
		}
		
		// MVI A, 01001011b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
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
		
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==false);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==false);
		
		// MVI L, 00101010b
		in_op[0].setValue(false).propagate();
		in_op[1].setValue(true).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(true).propagate();
		in_op[4].setValue(false).propagate();
		in_op[5].setValue(true).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(false).propagate();
		
		in_data1[0].setValue(false).propagate();
		in_data1[1].setValue(true).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(true).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(true).propagate();
		in_data1[6].setValue(false).propagate();
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

		assertTrue(cu.getRegLValue(0)==false);
		assertTrue(cu.getRegLValue(1)==true);
		assertTrue(cu.getRegLValue(2)==false);
		assertTrue(cu.getRegLValue(3)==true);
		assertTrue(cu.getRegLValue(4)==false);
		assertTrue(cu.getRegLValue(5)==true);
		assertTrue(cu.getRegLValue(6)==false);
		assertTrue(cu.getRegLValue(7)==false);
		
		// ORA L
		in_op[0].setValue(true).propagate();
		in_op[1].setValue(false).propagate();
		in_op[2].setValue(true).propagate();
		in_op[3].setValue(false).propagate();
		in_op[4].setValue(true).propagate();
		in_op[5].setValue(true).propagate();
		in_op[6].setValue(false).propagate();
		in_op[7].setValue(true).propagate();
		
		in_data1[0].setValue(false).propagate();
		in_data1[1].setValue(false).propagate();
		in_data1[2].setValue(false).propagate();
		in_data1[3].setValue(false).propagate();
		in_data1[4].setValue(false).propagate();
		in_data1[5].setValue(false).propagate();
		in_data1[6].setValue(false).propagate();
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

		// 01001011b | 00101010b = 01101011b
		assertTrue(cu.getRegAValue(0)==true);
		assertTrue(cu.getRegAValue(1)==true);
		assertTrue(cu.getRegAValue(2)==false);
		assertTrue(cu.getRegAValue(3)==true);
		assertTrue(cu.getRegAValue(4)==false);
		assertTrue(cu.getRegAValue(5)==true);
		assertTrue(cu.getRegAValue(6)==true);
		assertTrue(cu.getRegAValue(7)==false);
	}
	
	protected void printRegisters() {
		System.out.print("[A:");
		for (int i = 7; i >= 0; i--)
			System.out.print(cu.getRegAValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[B:");
		for (int i = 7; i >= 0; i--)
			System.out.print(cu.getRegBValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[C:");
		for (int i = 7; i >= 0; i--)
			System.out.print(cu.getRegCValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[D:");
		for (int i = 7; i >= 0; i--)
			System.out.print(cu.getRegDValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[E:");
		for (int i = 7; i >= 0; i--)
			System.out.print(cu.getRegEValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[H:");
		for (int i = 7; i >= 0; i--)
			System.out.print(cu.getRegHValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[L:");
		for (int i = 7; i >= 0; i--)
			System.out.print(cu.getRegLValue(i)?"1":"0");
		System.out.print("]");
		System.out.print("[F:");
		for (int i = 7; i >= 0; i--)
			System.out.print(cu.getFlag(i)?"1":"0");
		System.out.print("]");
		System.out.println();
	}

}
