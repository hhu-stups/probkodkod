package de.stups.probkodkod.test;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import de.stups.probkodkod.prolog.CompoundPrologTerm;
import de.stups.probkodkod.prolog.IntegerPrologTerm;
import de.stups.probkodkod.prolog.ListPrologTerm;
import de.stups.probkodkod.prolog.PrologTerm;
import de.stups.probkodkod.prolog.StructuredPrologOutput;
import de.stups.probkodkod.prolog.VariablePrologTerm;

public class StructuredPrologOutputTest extends TestCase {

	private StructuredPrologOutput pto;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		pto = new StructuredPrologOutput();
	}

	public void testAtom() {
		pto.printAtom("bla").fullstop();
		checkUniqueResult(new CompoundPrologTerm("bla"));
	}

	public void testInteger() {
		pto.printNumber(511).fullstop();
		checkUniqueResult(new IntegerPrologTerm(511));
	}

	public void testEmptyList() {
		pto.emptyList().fullstop();
		checkUniqueResult(ListPrologTerm.EMPTY_LIST);
	}

	public void testVariable() {
		pto.printVariable("vvv").fullstop();
		checkUniqueResult(new VariablePrologTerm("vvv"));
	}

	public void testSimpleCompound() {
		pto.openTerm("tt");
		pto.printAtom("bla");
		pto.printNumber(42);
		pto.closeTerm().fullstop();
		final PrologTerm expected = new CompoundPrologTerm("tt",
				new CompoundPrologTerm("bla"), new IntegerPrologTerm(42));
		checkUniqueResult(expected);
	}

	public void testNestedCompound() {
		pto.openTerm("first");
		pto.printAtom("bla");
		pto.openTerm("second");
		pto.printVariable("vv");
		pto.openTerm("third").printNumber(77).closeTerm();
		pto.closeTerm();
		pto.printNumber(42);
		pto.closeTerm().fullstop();
		final PrologTerm third = new CompoundPrologTerm("third",
				new IntegerPrologTerm(77));
		final PrologTerm second = new CompoundPrologTerm("second",
				new VariablePrologTerm("vv"), third);
		final PrologTerm expected = new CompoundPrologTerm("first",
				new CompoundPrologTerm("bla"), second,
				new IntegerPrologTerm(42));
		checkUniqueResult(expected);
	}

	public void testList() {
		pto.openTerm("first");
		pto.printAtom("bla");
		pto.openList();
		pto.openTerm("elem").printNumber(1).closeTerm();
		pto.openTerm("elem").printNumber(2).closeTerm();
		pto.closeList();
		pto.printNumber(42);
		pto.closeTerm().fullstop();
		final PrologTerm elem1 = new CompoundPrologTerm("elem",
				new IntegerPrologTerm(1));
		final PrologTerm elem2 = new CompoundPrologTerm("elem",
				new IntegerPrologTerm(2));
		final PrologTerm list = new ListPrologTerm(new PrologTerm[] { elem1,
				elem2 });
		final PrologTerm expected = new CompoundPrologTerm("first",
				new CompoundPrologTerm("bla"), list, new IntegerPrologTerm(42));
		checkUniqueResult(expected);
	}

	public void testEmptyAtom() {
		pto.openTerm("empty");
		pto.closeTerm().fullstop();
		checkUniqueResult(new CompoundPrologTerm("empty"));
	}

	public void testTermInProgress() {
		pto.openTerm("term");
		assertEquals(true, pto.isSentenceStarted());
		pto.closeTerm();
		assertEquals(true, pto.isSentenceStarted());
		pto.fullstop();
		checkUniqueResult(new CompoundPrologTerm("term"));
	}

	public void testMultipleSentences() {
		pto.printAtom("first").fullstop();
		pto.printAtom("second").fullstop();
		assertFalse(pto.isSentenceStarted());
		Collection<PrologTerm> terms = pto.getSentences();
		assertEquals(2, terms.size());
		final Iterator<PrologTerm> iterator = terms.iterator();
		final PrologTerm actFirst = iterator.next();
		final PrologTerm actSecond = iterator.next();
		assertEquals(new CompoundPrologTerm("first"), actFirst);
		assertEquals(new CompoundPrologTerm("second"), actSecond);
	}

	private void checkUniqueResult(final PrologTerm expected) {
		assertFalse(pto.isSentenceStarted());
		Collection<PrologTerm> terms = pto.getSentences();
		assertEquals(1, terms.size());
		final PrologTerm actual = terms.iterator().next();
		assertEquals(expected, actual);
	}
}
