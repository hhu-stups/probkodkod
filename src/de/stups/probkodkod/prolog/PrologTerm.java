/**
 * (c) 2009 Lehrstuhl fuer Softwaretechnik und Programmiersprachen, Heinrich
 * Heine Universitaet Duesseldorf This software is licenced under EPL 1.0
 * (http://www.eclipse.org/org/documents/epl-v10.html)
 * */

/**
 * 
 */
package de.stups.probkodkod.prolog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the abstract base class for Prolog terms
 * 
 * @author plagge
 */
public abstract class PrologTerm {
	public boolean isTerm() {
		return false;
	}

	public boolean isAtom() {
		return false;
	}

	public boolean isList() {
		return false;
	}

	public boolean isNumber() {
		return false;
	}

	public boolean isVariable() {
		return false;
	}

	public boolean hasFunctor(final String functor, final int arity) {
		return false;
	}

	public abstract void toTermOutput(IPrologTermOutput pto);

	@Override
	public String toString() {
		StringWriter sWriter = new StringWriter();
		PrologTermOutput pto = new PrologTermOutput(new PrintWriter(sWriter),
				false);
		toTermOutput(pto);
		return sWriter.toString();
	}

	public static String strip(final PrologTerm term) {
		String string = term.toString();
		// Strip if the string starts and ends with a quote
		if ((string.charAt(0) != '\'')
				|| (string.charAt(string.length() - 1) != '\''))
			return string;
		return string.substring(1, string.length() - 1);
	}

	public static String atomicString(final PrologTerm term) {
		if (term.isAtom())
			return ((CompoundPrologTerm) term).getFunctor();
		else
			throw new IllegalArgumentException(
					"Expected an atomic prolog term, but was "
							+ term.toString());
	}

	public static List<String> strip(final ListPrologTerm list) {
		List<String> ret = new ArrayList<String>();
		for (PrologTerm term : list) {
			ret.add(strip(term));
		}
		return ret;
	}
}
