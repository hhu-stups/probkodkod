package de.stups.probkodkod.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.prob.prolog.term.PrologTerm;

public interface Result {

	public final static class SetResult implements Result {
		private final Set<PrologTerm> terms;

		public SetResult(final Collection<PrologTerm> terms) {
			super();
			this.terms = new HashSet<PrologTerm>(terms);
		}

		@Override
		public int hashCode() {
			return 31 + terms.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			final SetResult other = (SetResult) obj;
			if (terms.size() != other.terms.size())
				return false;
			final boolean verdict = terms.equals(other.terms);
			return verdict;
		}

		@Override
		public String toString() {
			return terms.toString();
		}
	}

	public final static class SingletonResult implements Result {
		private final PrologTerm result;

		public SingletonResult(final PrologTerm result) {
			super();
			this.result = result;
		}

		@Override
		public int hashCode() {
			return 11 + this.result.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			final boolean verdict = result.equals(((SingletonResult) obj).result);
			return verdict;
		}

		@Override
		public String toString() {
			return result.toString();
		}
	}
}
