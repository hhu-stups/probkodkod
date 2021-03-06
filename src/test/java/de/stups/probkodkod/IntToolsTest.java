/**
 * 
 */
package de.stups.probkodkod;

import static de.stups.probkodkod.tools.IntTools.bitwidth;
import static de.stups.probkodkod.tools.IntTools.countOneBits;
import static de.stups.probkodkod.tools.IntTools.pow2;
import junit.framework.Assert;

import org.junit.Test;

/**
 * @author plagge
 * 
 */
public class IntToolsTest {
	@Test
	public void testCountOneBit() {
		Assert.assertEquals(0, countOneBits(0));
		Assert.assertEquals(1, countOneBits(1));
		Assert.assertEquals(1, countOneBits(2));
		Assert.assertEquals(1, countOneBits(4));
		Assert.assertEquals(1, countOneBits(8));
		Assert.assertEquals(2, countOneBits(5));
		Assert.assertEquals(3, countOneBits(7));
		Assert.assertEquals(8, countOneBits(255));
	}

	@Test
	public void testBitwidth() {
		Assert.assertEquals(0, bitwidth(0));
		Assert.assertEquals(1, bitwidth(1));
		Assert.assertEquals(2, bitwidth(2));
		Assert.assertEquals(2, bitwidth(3));
		Assert.assertEquals(3, bitwidth(4));
		Assert.assertEquals(5, bitwidth(31));
		Assert.assertEquals(6, bitwidth(32));
	}

	public void testPow2() {
		Assert.assertEquals(1, pow2(0));
		Assert.assertEquals(2, pow2(1));
		Assert.assertEquals(4, pow2(2));
		Assert.assertEquals(8, pow2(3));
		Assert.assertEquals(16, pow2(4));
	}
}
