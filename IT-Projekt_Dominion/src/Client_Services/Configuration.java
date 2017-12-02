package Client_Services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 17:03:20
 */
public class Configuration {

	private Properties defaultOptions;
	private Properties localOptions;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	// private Logger logger = sl.getLogger();

	public Configuration() {

		defaultOptions = new Properties();
		String defaultFilename = sl.getAPP_NAME() + "_defaults.cfg";
		InputStream inStream = sl.getAPP_CLASS().getResourceAsStream(defaultFilename);
		try {
			defaultOptions.load(inStream);
			// logger.config("Default configuration file found");
		} catch (Exception e) {
			// logger.warning("No default configuration file found: " +
			// defaultFilename);
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (Exception ignore) {
			}
		}
		localOptions = new Properties(defaultOptions);
		
		
		// tries to load local language options 
		try {
			inStream = new FileInputStream(sl.getAPP_NAME() + "_local.cfg");
			localOptions.load(inStream);
		} catch (Exception e) { // from loading the properties
			//logger.warning("Error reading local options file: " + e.toString());
		} finally {
			try {
				inStream.close();
			} catch (Exception ignore) {
			}
		}
		
		

		
		
	}

	/**
	 * 
	 * @param name
	 */
	public String getOption(String name) {
		return localOptions.getProperty(name);
	}

	public void save() {

	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setLocalOption(String name, String value) {
		localOptions.setProperty(name, value);
	}
}// end Configuration