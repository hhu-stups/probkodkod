/**
 * (c) 2009 Lehrstuhl fuer Softwaretechnik und Programmiersprachen, Heinrich
 * Heine Universitaet Duesseldorf This software is licenced under EPL 1.0
 * (http://www.eclipse.org/org/documents/epl-v10.html)
 * */

/**
 * 
 */
package de.stups.probkodkod.prolog;

/**
 * Represents a Prolog variable.
 * 
 * @author plagge
 */
public final class VariablePrologTerm extends PrologTerm {
	private final String name;

	public VariablePrologTerm(final String name) {
		super();
		if (name == null)
			throw new IllegalArgumentException(
					"Name of variable must not be null");
		this.name = name;
	}

	@Override
	public boolean isVariable() {
		return true;
	}

	public String getName() {
		return name;
	}

	@Override
	public void toTermOutput(final IPrologTermOutput pto) {
		pto.printVariable(name);
	}

	@Override
	public boolean equals(final Object other) {
		boolean isEqual;
		if (this == other) {
			isEqual = true;
		} else if (other != null && other instanceof VariablePrologTerm) {
			isEqual = name.equals(((VariablePrologTerm) other).name);
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	@Override
	public int hashCode() {
		return name.hashCode() * 5 + 18;
	}
}
