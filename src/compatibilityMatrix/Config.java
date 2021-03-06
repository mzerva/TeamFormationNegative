package compatibilityMatrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configuration Class
 * 
 * @author ksemer
 *
 */
public class Config {

	public static String DATA_PATH;
	public static String OUTPUT_PATH;
	public static boolean RUN_HEURISTIC;
	public static int MAX_PATH_LENGTH;
	private static final Logger _log = Logger.getLogger(Config.class.getName());

	public static void loadConfig() {
		final String SETTINGS_FILE = "./config/settings.properties";

		try {
			Properties Settings = new Properties();
			InputStream is = new FileInputStream(new File(SETTINGS_FILE));
			Settings.load(is);
			is.close();

			// ============================================================
			DATA_PATH = Settings.getProperty("DataPath", "");
			OUTPUT_PATH = Settings.getProperty("OutputPath", "");
			RUN_HEURISTIC = Boolean.parseBoolean(Settings.getProperty("RunHeuristic", "false"));
			MAX_PATH_LENGTH = Integer.parseInt(Settings.getProperty("MaxPathLength", "-1"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, "Failed to Load " + SETTINGS_FILE + " File.", e);
		}
	}
}