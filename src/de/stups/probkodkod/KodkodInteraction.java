package de.stups.probkodkod;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import de.prob.prolog.output.IPrologTermOutput;
import de.prob.prolog.output.PrologTermOutput;
import de.stups.probkodkod.parser.lexer.Lexer;
import de.stups.probkodkod.parser.lexer.LexerException;
import de.stups.probkodkod.parser.node.Start;
import de.stups.probkodkod.parser.parser.Parser;
import de.stups.probkodkod.parser.parser.ParserException;
import de.stups.probkodkod.tools.LogReader;
import de.stups.probkodkod.tools.LogWriter;
import de.stups.probkodkod.tools.MergeWriter;

/**
 * Main class for the Kodkod wrapper.
 * 
 * Commands are read and executed until the session (see {@link KodkodSession})
 * ends. The commands are directly executed from the Analysis of the syntax tree
 * 
 * @author plagge
 * 
 * @see KodkodAnalysis
 */
public class KodkodInteraction {
	private static Logger logger = Logger.getLogger("de.stups.probkodkod");

	private KodkodSession session;

	public void interaction(final Reader in, final PrintWriter out)
			throws IOException, ParserException, LexerException {
		logger.info("Starting kodkod session");

		final IPrologTermOutput pto = new PrologTermOutput(out, false);
		session = new KodkodSession();
		final KodkodAnalysis analysis = new KodkodAnalysis(session, pto);
		final Lexer lexer = new EOFLexer(in);
		final Parser parser = new Parser(lexer);

		try {
			while (!session.isStopped()) {
				final Start tree = parser.parse();
				tree.apply(analysis);
				pto.flush();
			}
		} catch (EOFLexer.AbortException e) {
			logger.info("EOF reached");
		}

		logger.info("Kodkod session finished");
	}

	public KodkodSession getSession() {
		return session;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		try {
			Handler filehandler = new FileHandler("probkodkod.log");
			filehandler.setFormatter(new SimpleFormatter());
			logger.setUseParentHandlers(false);
			logger.addHandler(filehandler);
			logger.setLevel(Level.SEVERE);

			final FileWriter fw = createDumpFile();
			final Reader in;
			final PrintWriter out;

			if (fw == null) {
				in = new InputStreamReader(System.in);
				out = new PrintWriter(System.out);
			} else {
				MergeWriter merge = new MergeWriter(fw);
				in = new LogReader(new InputStreamReader(System.in),
						merge.createWriter("  ProB:"));
				out = new PrintWriter(new LogWriter(new OutputStreamWriter(
						System.out), merge.createWriter("Kodkod:")));
			}

			KodkodInteraction interaction = new KodkodInteraction();
			interaction.interaction(in, out);

			if (fw != null) {
				fw.close();
			}
		} catch (Exception e) {
			logger.severe(e.toString());
			e.printStackTrace();
		}
	}

	protected static FileWriter createDumpFile() throws IOException {
		return null;
		// return new FileWriter("dump.txt");
	}
}
