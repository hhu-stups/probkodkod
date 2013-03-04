/**
 * 
 */
package de.stups.probkodkod;

import org.junit.Assert;
import org.junit.Test;

import de.stups.probkodkod.Problem.CalculatedIntegerAtoms;

/**
 * Testing the pinche integer universe.
 * 
 * @author plagge
 */
public class ProblemTest {

	@Test
	public void testNaturalCalculationWithAtoms() {
		final Problem problem = new Problem("test");
		final IntegerIntervall iRange = new IntegerIntervall(0, 6);
		final IntegerIntervall pRange = new IntegerIntervall(0, 15);
		problem.registerType("dummy", 5);
		problem.registerIntegerTypes("pow", "atoms", iRange, pRange);
		final CalculatedIntegerAtoms calc = problem.getCalculatedIntegerAtoms();
		Assert.assertEquals("correct bitwidht", Integer.valueOf(5),
				calc.getBitwidth());
		Assert.assertEquals("integers should start at atom 5", 5,
				calc.getNumberOffset());
		final int[] expectedInts = new int[] { 0, 3, 5, 6, 1, 2, 4, 8, -16 };
		Assert.assertArrayEquals(expectedInts, calc.getNumbers());
	}

	@Test
	public void testNaturalCalculationWithoutAtoms() {
		final Problem problem = new Problem("test");
		final IntegerIntervall pRange = new IntegerIntervall(0, 15);
		problem.registerType("dummy", 5);
		problem.registerIntegerTypes("pow", null, null, pRange);
		final CalculatedIntegerAtoms calc = problem.getCalculatedIntegerAtoms();
		Assert.assertEquals("correct bitwidht", Integer.valueOf(5),
				calc.getBitwidth());
		Assert.assertEquals("integers should start at atom 5", 5,
				calc.getNumberOffset());
		final int[] expectedInts = new int[] { 1, 2, 4, 8, -16 };
		Assert.assertArrayEquals(expectedInts, calc.getNumbers());
	}

	@Test
	public void testIntegerCalculationWithAtoms() {
		final Problem problem = new Problem("test");
		final IntegerIntervall iRange = new IntegerIntervall(-3, 3);
		final IntegerIntervall pRange = new IntegerIntervall(-6, 6);
		problem.registerType("dummy", 5);
		problem.registerIntegerTypes("pow", "atoms", iRange, pRange);
		final CalculatedIntegerAtoms calc = problem.getCalculatedIntegerAtoms();
		Assert.assertEquals("correct bitwidht", Integer.valueOf(4),
				calc.getBitwidth());
		Assert.assertEquals("integers should start at atom 5", 5,
				calc.getNumberOffset());
		final int[] expectedInts = new int[] { -3, -2, -1, 0, 3, 1, 2, 4, -8 };
		Assert.assertArrayEquals(expectedInts, calc.getNumbers());
	}

	@Test
	public void testIntegerCalculationWithoutAtoms() {
		final Problem problem = new Problem("test");
		final IntegerIntervall pRange = new IntegerIntervall(-6, 6);
		problem.registerType("dummy", 5);
		problem.registerIntegerTypes("pow", null, null, pRange);
		final CalculatedIntegerAtoms calc = problem.getCalculatedIntegerAtoms();
		Assert.assertEquals("correct bitwidht", Integer.valueOf(4),
				calc.getBitwidth());
		Assert.assertEquals("integers should start at atom 5", 5,
				calc.getNumberOffset());
		final int[] expectedInts = new int[] { 1, 2, 4, -8 };
		Assert.assertArrayEquals(expectedInts, calc.getNumbers());
	}

	@Test
	public void testIntegerCalculationSameBounds() {
		final Problem problem = new Problem("test");
		final IntegerIntervall iRange = new IntegerIntervall(-4, 3);
		final IntegerIntervall pRange = new IntegerIntervall(-4, 3);
		problem.registerType("dummy", 5);
		problem.registerIntegerTypes("pow", "atoms", iRange, pRange);
		final CalculatedIntegerAtoms calc = problem.getCalculatedIntegerAtoms();
		Assert.assertEquals("correct bitwidht", Integer.valueOf(3),
				calc.getBitwidth());
		Assert.assertEquals("integers should start at atom 5", 5,
				calc.getNumberOffset());
		final int[] expectedInts = new int[] { -3, -2, -1, 0, 3, -4, 1, 2 };
		Assert.assertArrayEquals(expectedInts, calc.getNumbers());
	}
}
