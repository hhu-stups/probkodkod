package de.stups.probkodkod.tools;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

public class LogReader extends Reader {
	private final Reader in;
	private final Writer out;
	
	public LogReader(Reader reader, Writer writer) {
		super();
		this.in = reader;
		this.out = writer;
	}

	public void close() throws IOException {
		in.close();
	}

	public void mark(int readAheadLimit) throws IOException {
		in.mark(readAheadLimit);
	}

	public boolean markSupported() {
		return in.markSupported();
	}

	public int read() throws IOException {
		int c = in.read();
		out.write(c);
		out.flush();
		return c;
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		int c = in.read(cbuf, off, len);
		out.write(cbuf, off, len);
		out.flush();
		return c;
	}

	public int read(char[] cbuf) throws IOException {
		int c = in.read(cbuf);
		out.write(cbuf);
		out.flush();
		return c;
	}

	public int read(CharBuffer target) throws IOException {
		int c = in.read(target);
		out.write(target.toString());
		out.flush();
		return c;
	}

	public boolean ready() throws IOException {
		return in.ready();
	}

	public void reset() throws IOException {
		in.reset();
	}

	public long skip(long n) throws IOException {
		return in.skip(n);
	}

	public String toString() {
		return in.toString();
	}

	
}
