package de.stups.probkodkod.test;

import java.util.Arrays;

public class Tuple {
	private final int[] values;

	public Tuple(int[] values) {
		this.values = values;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tuple) {
			Tuple other = (Tuple) obj;
			return Arrays.equals(values, other.values);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(values);
	}

	@Override
	public String toString() {
		return "t(" + Arrays.toString(values) + ")";
	}
	
	
}
