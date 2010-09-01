package de.stups.probkodkod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Permutations implements Iterable<int[]> {
	private final int[] orig;

	public Permutations(final int[] orig) {
		this.orig = orig;
	}

	@Override
	public Iterator<int[]> iterator() {
		return new PermutationIterator(orig);
	}

	private static final class PermutationIterator implements Iterator<int[]> {
		private final int[] orig;
		private final int length;
		private final int[] current;
		private int pos;

		public PermutationIterator(final int[] orig) {
			this.orig = orig;
			this.length = orig.length;
			this.current = new int[length];
			for (int i = 0; i < length; i++) {
				current[i] = 0;
			}
			this.pos = length - 1;
		}

		@Override
		public boolean hasNext() {
			return pos >= 0;
		}

		@Override
		public int[] next() {
			int[] perm = nextRawPermutation();
			return permute(perm);
		}

		private int[] permute(final int[] perm) {
			int[] result = new int[length];
			List<Integer> list = new LinkedList<Integer>();
			for (final int i : orig) {
				list.add(i);
			}
			int pos = 0;
			for (final int p : perm) {
				result[pos] = list.remove(p);
				pos++;
			}
			return result;
		}

		private int[] nextRawPermutation() {
			int[] result = new int[length];
			System.arraycopy(current, 0, result, 0, length);
			step();
			return result;
		}

		private void step() {
			if (pos >= 0) {
				final int maxAtPos = length - 1 - pos;
				if (current[pos] < maxAtPos) {
					current[pos]++;
					pos = length - 1;
				} else {
					current[pos] = 0;
					pos--;
					step();
				}
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove on permutations");
		}
	}
}