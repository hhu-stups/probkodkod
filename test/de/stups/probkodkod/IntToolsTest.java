/**
 * 
 */
package de.stups.probkodkod;

import static de.stups.probkodkod.tools.IntTools.countOneBits;
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
}
