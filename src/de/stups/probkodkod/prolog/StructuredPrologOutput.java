/**
 * 
 */
package de.stups.probkodkod.prolog;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

/**
 * @author plagge
 * 
 */
public class StructuredPrologOutput implements IPrologTermOutput {
	private final Collection<PrologTerm> sentences = new ArrayList<PrologTerm>();

	private final Stack<PrologTerm> termStack = new Stack<PrologTerm>();

	private final Stack<Integer> numArgStack = new Stack<Integer>();
	private final Stack<String> functorStack = new Stack<String>();

	private String currentFunctor = null;
	private int currentArguments = 0;

	@Override
	public IPrologTermOutput closeList() {
		PrologTerm[] elements = getArguments();
		popFromStack();
		addArgument(new ListPrologTerm(elements));
		return this;
	}

	@Override
	public IPrologTermOutput closeTerm() {
		PrologTerm[] elements = getArguments();
		final String functor = currentFunctor;
		popFromStack();
		addArgument(new CompoundPrologTerm(functor, elements));
		return this;
	}

	@Override
	public IPrologTermOutput emptyList() {
		addArgument(ListPrologTerm.EMPTY_LIST);
		return this;
	}

	@Override
	public IPrologTermOutput flush() {
		return this;
	}

	@Override
	public IPrologTermOutput fullstop() {
		if (termStack.size() != 1)
			throw new IllegalArgumentException("term stacksize is not 1");
		if (currentArguments != 1)
			throw new IllegalArgumentException("number of arguments is not 1");
		sentences.add(termStack.pop());
		currentArguments = 0;
		return this;
	}

	@Override
	public IPrologTermOutput openList() {
		pushOnStack();
		currentFunctor = null;
		currentArguments = 0;
		return this;
	}

	@Override
	public IPrologTermOutput openTerm(final String functor) {
		pushOnStack();
		currentFunctor = functor;
		currentArguments = 0;
		return this;
	}

	@Override
	public IPrologTermOutput openTerm(final String functor,
			final boolean ignoreIndention) {
		return openTerm(functor);
	}

	@Override
	public IPrologTermOutput printAtom(final String content) {
		addArgument(new CompoundPrologTerm(content));
		return this;
	}

	@Override
	public IPrologTermOutput printAtomOrNumber(final String content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IPrologTermOutput printNumber(final long number) {
		addArgument(new IntegerPrologTerm(number));
		return this;
	}

	@Override
	public IPrologTermOutput printNumber(final BigInteger number) {
		addArgument(new IntegerPrologTerm(number));
		return this;
	}

	@Override
	public IPrologTermOutput printString(final String content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IPrologTermOutput printVariable(final String var) {
		addArgument(new VariablePrologTerm(var));
		return this;
	}

	public Collection<PrologTerm> getSentences() {
		return new ArrayList<PrologTerm>(sentences);
	}

	public void clearSentences() {
		sentences.clear();
	}

	public boolean isSentenceStarted() {
		return !functorStack.isEmpty() || !termStack.isEmpty();
	}

	private PrologTerm[] getArguments() {
		PrologTerm[] args = new PrologTerm[currentArguments];
		for (int i = currentArguments - 1; i >= 0; i--) {
			args[i] = termStack.pop();
		}
		currentArguments = 0;
		return args;
	}

	private void addArgument(final PrologTerm term) {
		termStack.push(term);
		currentArguments++;
	}

	private void pushOnStack() {
		numArgStack.push(currentArguments);
		functorStack.push(currentFunctor);
		currentArguments = 0;
		currentFunctor = null;
	}

	private void popFromStack() {
		currentArguments = numArgStack.pop();
		currentFunctor = functorStack.pop();
	}
}
