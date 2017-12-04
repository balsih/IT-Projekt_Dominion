package Client_Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		localOptions = new Properties(defaultOptions);
		
		
		// tries to load local language options 
		String localFileName = sl.getAPP_NAME() + "_local.cfg";
		InputStream localInStream = sl.getAPP_CLASS().getResourceAsStream(localFileName);
		try {
			localOptions.load(localInStream);
		} catch (Exception e) { // from loading the properties
			//logger.warning("Error reading local options file: " + e.toString());
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (Exception e) {
				e.printStackTrace();
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

	// saves the language at teh local.cfg file. Method is called always when mainMenu view gets closed
	public void save() {
		FileOutputStream propFile = null;
		try {
			File localeFile = new File(sl.getAPP_CLASS().getResource(sl.getAPP_NAME() + "_local.cfg").toURI());
			propFile = new FileOutputStream(localeFile);
			localOptions.store(propFile, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (propFile != null) {
				try {
					propFile.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
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