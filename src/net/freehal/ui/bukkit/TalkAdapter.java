package net.freehal.ui.bukkit;

import java.util.Collections;

import net.freehal.core.lang.Language;
import net.freehal.core.lang.german.GermanLanguage;
import net.freehal.core.util.SystemUtils;
import net.freehal.ui.common.DataInitializer;

public class TalkAdapter {

	private boolean initialized = false;

	public TalkAdapter() {
		new Thread(new LazyInitializer(this)).start();
	}

	public String answer(String input) {
		while (!initialized) {
			SystemUtils.sleep(500);
		}
		// process the given input text
		final String output = DataInitializer.processInput(input);
		return output;
	}

	public static class LazyInitializer implements Runnable {

		private TalkAdapter talkAdapter;

		public LazyInitializer(TalkAdapter talkAdapter) {
			this.talkAdapter = talkAdapter;
		}

		@Override
		public void run() {
			DataInitializer.initializeFilesystem();

			// default base directory
			final String base = "plugins/test/";
			// default log file
			final String logfile = base + "/stdout.txt";
			// show logs in terminal? it's default on unix systems...
			final boolean showLogTerminal = false;

			DataInitializer.initializeLogging(base, logfile, showLogTerminal);
			DataInitializer.initializeStorage(base);

			// default language
			Language language = new GermanLanguage();
			DataInitializer.initializeLanguage(language);

			// initialize data
			DataInitializer.initializeLanguageSpecificData(Collections.<String> emptySet());

			talkAdapter.initialized = true;
		}
	}
}
