/**
 * 
 */
package org.soton.docdb;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.soton.docdb.diff.FileScanner;

/**
 * @author Felipe
 */
public class DocDB {
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(DocDB.class.getName());

	public static final String LOGGER_FILE = "logging.properties";

	protected String activeDirectory = "./";

	public DocDB(String[] args) {
		logger.config("Parsing command line arguments.");
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-d") && args.length > i) {
				activeDirectory = args[++i];
			}
		}
	}

	public void start() {
		logger.info("Starting DocDB.");
		try {
			FileScanner scanner = new FileScanner(new File(activeDirectory));
			scanner.scanFiles();
			scanner.findDuplicates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Stopping DocDB.");
	}

	public static void setupLogger() {
		try {
			if (new File(LOGGER_FILE).exists()) {

				LogManager.getLogManager().readConfiguration(
						new FileInputStream(new File(LOGGER_FILE)));

			} else {
				LogManager.getLogManager().readConfiguration(
						DocDB.class.getResourceAsStream("/" + LOGGER_FILE));
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println("Error setting up logger:" + e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setupLogger();
		DocDB docDB = new DocDB(args);
		docDB.start();

	}

}
