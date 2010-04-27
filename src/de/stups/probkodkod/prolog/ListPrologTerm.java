/**
 * (c) 2009 Lehrstuhl fuer Softwaretechnik und Programmiersprachen, Heinrich
 * Heine Universitaet Duesseldorf This software is licenced under EPL 1.0
 * (http://www.eclipse.org/org/documents/epl-v10.html)
 * */

/**
 * 
 */
package de.stups.probkodkod.prolog;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents a Prolog list.
 * 
 * @author plagge
 */
public final class ListPrologTerm extends PrologTerm implements
		List<PrologTerm> {
	public static final ListPrologTerm EMPTY_LIST = new ListPrologTerm(
			new PrologTerm[0]);

	private final PrologTerm[] elements;

	public ListPrologTerm(final PrologTerm[] elements) {
		super();
		if (elements == null)
			throw new IllegalStateException(
					"elements of Prolog list must not be null");
		this.elements = elements;
	}

	@Override
	public boolean isList() {
		return true;
	}

	public int size() {
		return elements.length;
	}

	public PrologTerm get(final int index) {
		return elements[index];
	}

	@Override
	public void toTermOutput(final IPrologTermOutput pto) {
		pto.openList();
		for (PrologTerm term : elements) {
			term.toTermOutput(pto);
		}
		pto.closeList();
	}

	@Override
	public boolean equals(final Object other) {
		boolean isEqual;
		if (this == other) {
			isEqual = true;
		} else if (other != null && other instanceof ListPrologTerm) {
			isEqual = Arrays
					.equals(elements, ((ListPrologTerm) other).elements);
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(elements) * 13 + 8;
	}

	public Iterator<PrologTerm> iterator() {
		return Arrays.asList(elements).iterator();
	}

	public boolean add(final PrologTerm o) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(final Collection<? extends PrologTerm> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(final Object o) {
		return Arrays.asList(elements).contains(o);
	}

	public boolean containsAll(final Collection<?> c) {
		return Arrays.asList(elements).containsAll(c);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean remove(final Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		return elements.clone();
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < elements.length) {
			a = (T[]) java.lang.reflect.Array.newInstance(a.getClass()
					.getComponentType(), elements.length);
		}
		System.arraycopy(elements, 0, a, 0, elements.length);
		if (a.length > elements.length) {
			a[elements.length] = null;
		}
		return a;
	}

	public void add(final int arg0, final PrologTerm arg1) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(final int arg0,
			final Collection<? extends PrologTerm> arg1) {
		throw new UnsupportedOperationException();
	}

	public int indexOf(final Object object) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].equals(object))
				return i;
		}
		return -1;
	}

	public int lastIndexOf(final Object object) {
		for (int i = elements.length - 1; i >= 0; i++) {
			if (elements[i].equals(object))
				return i;
		}
		return -1;
	}

	public ListIterator<PrologTerm> listIterator() {
		return Arrays.asList(elements).listIterator();
	}

	public ListIterator<PrologTerm> listIterator(final int index) {
		return Arrays.asList(elements).listIterator(index);
	}

	public PrologTerm remove(final int arg0) {
		throw new UnsupportedOperationException();
	}

	public PrologTerm set(final int arg0, final PrologTerm arg1) {
		throw new UnsupportedOperationException();
	}

	public List<PrologTerm> subList(int start, int end) {
		end = Math.min(elements.length, end);
		start = Math.max(0, start);
		final PrologTerm[] sublist;
		if (start >= end) {
			sublist = new PrologTerm[0];
		} else {
			final int length = end - start;
			sublist = new PrologTerm[length];
			System.arraycopy(elements, start, sublist, 0, length);
		}
		return new ListPrologTerm(sublist);
	}

}
