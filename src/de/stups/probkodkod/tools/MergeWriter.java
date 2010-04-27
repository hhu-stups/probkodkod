package de.stups.probkodkod.tools;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MergeWriter {
	private final Writer out;
	private boolean bol = true;
	private int counter = 0;
	private int last;
	private Set<Integer> opened = new HashSet<Integer>();
	private Map<Integer, Writer> writers = new HashMap<Integer, Writer>();

	public MergeWriter(Writer out) {
		this.out = out;
	}

	public synchronized Writer createWriter(String mark) {
		int id = counter;
		counter++;
		Writer writer = new WDelegate(id, mark);
		writers.put(id, writer);
		opened.add(id);
		return writer;
	}

	private class WDelegate extends Writer {
		private final int id;
		private boolean local_bol = true;
		private String mark;

		public WDelegate(int id, String mark) {
			this.id = id;
			this.mark = mark;
		}

		@Override
		public void flush() throws IOException {
			out.flush();
		}

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			synchronized (MergeWriter.this) {
				if (!bol && last != id) {
					out.write("[+]\n");
					bol = true;
				}
				int first_in_line = 0;
				for (int i = 0; i < len; i++) {
					if (bol) {
						out.write(local_bol ? ' ' : '+');
						out.write(mark);
					}
					if (cbuf[i] == '\n') {
						out.write(cbuf, off + first_in_line, i - first_in_line
								+ 1);
						first_in_line = i + 1;
						bol = true;
						local_bol = true;
					} else {
						bol = false;
						local_bol = false;
					}
				}
				if (first_in_line < len) {
					out.write(cbuf, off + first_in_line, len - first_in_line);
				}
				last = id;
			}
		}

		@Override
		public void close() throws IOException {
			synchronized (MergeWriter.this) {
				opened.remove(id);
				if (opened.isEmpty()) {
					out.close();
				}
			}
		}
	}
}
