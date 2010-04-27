package de.stups.probkodkod.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.SortedMap;
import java.util.TreeMap;

import de.stups.probkodkod.prolog.PrologTerm;

public class ResultSetBuilder {
	private final Collection<SortedMap<String, Result>> entries = new HashSet<SortedMap<String, Result>>();
	private SortedMap<String, Result> currentValues = new TreeMap<String, Result>();

	public ResultSetBuilder set(final String id, final PrologTerm... values) {
		final Result asSet = new Result.SetResult(Arrays.asList(values));
		return add(id, asSet);
	}

	public ResultSetBuilder single(final String id, final PrologTerm value) {
		return add(id, new Result.SingletonResult(value));
	}

	public ResultSetBuilder add(final String id, final Result values) {
		if (currentValues.containsKey(id))
			throw new IllegalArgumentException("Value for " + id
					+ " defined twice!");
		currentValues.put(id, values);
		return this;
	}

	public void store() {
		entries.add(currentValues);
		currentValues = new TreeMap<String, Result>();
	}

	public Collection<SortedMap<String, Result>> toCollection() {
		if (!currentValues.isEmpty())
			throw new IllegalArgumentException("current values are not empty");
		return entries;
	}
}
