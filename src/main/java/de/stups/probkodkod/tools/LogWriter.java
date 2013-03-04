package de.stups.probkodkod.tools;

import java.io.IOException;
import java.io.Writer;

public class LogWriter extends Writer {
	private final Writer out1,out2;

	public LogWriter(Writer writer1, Writer writer2) {
		super();
		this.out1 = writer1;
		this.out2 = writer2;
	}

	public Writer append(char c) throws IOException {
		out1.append(c);
		out2.append(c);
		return this;
	}

	public Writer append(CharSequence csq, int start, int end)
			throws IOException {
		out1.append(csq, start, end);
		out2.append(csq, start, end);
		return this;
	}

	public Writer append(CharSequence csq) throws IOException {
		out1.append(csq);
		out2.append(csq);
		return this;
	}

	public void close() throws IOException {
		out1.close();
		out2.close();
	}

	public void flush() throws IOException {
		out1.flush();
		out2.flush();
	}

	public void write(char[] cbuf, int off, int len) throws IOException {
		out1.write(cbuf, off, len);
		out2.write(cbuf, off, len);
	}

	public void write(char[] cbuf) throws IOException {
		out1.write(cbuf);
		out2.write(cbuf);
	}

	public void write(int c) throws IOException {
		out1.write(c);
		out2.write(c);
	}

	public void write(String str, int off, int len) throws IOException {
		out1.write(str, off, len);
		out2.write(str, off, len);
	}

	public void write(String str) throws IOException {
		out1.write(str);
		out2.write(str);
	}
}
