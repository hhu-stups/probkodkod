package de.stups.probkodkod;

import java.io.PrintWriter;
import java.io.StringWriter;

import de.stups.probkodkod.tools.MergeWriter;

import junit.framework.TestCase;

public class MergeWriterTest extends TestCase {
	public void testMerge() {
		StringWriter dest = new StringWriter();
		MergeWriter merge = new MergeWriter(dest);
		PrintWriter p1 = new PrintWriter(merge.createWriter("a:"));
		PrintWriter p2 = new PrintWriter(merge.createWriter("b:"));

		p1.println("Nummer1");
		p2.println("Nummer2");
		p2.println("bla");
		p1.println("blubb");
		p1.close();
		p2.close();

		assertEquals(" a:Nummer1\n b:Nummer2\n b:bla\n a:blubb\n", dest.toString());
	}

	public void testInterupted() {
		StringWriter dest = new StringWriter();
		MergeWriter merge = new MergeWriter(dest);
		PrintWriter p1 = new PrintWriter(merge.createWriter("a:"));
		PrintWriter p2 = new PrintWriter(merge.createWriter("b:"));

		p1.print("Gel");
		p2.println("Unterbrechung");
		p1.println("aber");
		p1.close();
		p2.close();

		assertEquals(" a:Gel[+]\n b:Unterbrechung\n+a:aber\n", dest.toString());
	}
}
