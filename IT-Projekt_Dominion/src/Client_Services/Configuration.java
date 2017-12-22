package Client_Services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *  Class methods load the default and if available the local Properties. 
 * 
 * @author Brad Richards, Copyright 2015, FHNW
 * , adapted by Rene Schwab
 * 
 */
public class Configuration {

	private Properties defaultOptions;
	private Properties localOptions;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();

	
	public Configuration() {

		defaultOptions = new Properties();
		String defaultFilename = sl.getAPP_NAME() + "_defaults.cfg";
		InputStream inStream = sl.getAPP_CLASS().getResourceAsStream(defaultFilename);
		try {
			defaultOptions.load(inStream); // Default configuration file found
		} catch (Exception e) { // No default configuration file found
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
		} catch (Exception e) { // Error reading local options file
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	public String getOption(String name) {
		return localOptions.getProperty(name);
	}
	
	
	
	/**
	 * Saves the language to the local.cfg file. Method is called always when mainMenu view gets closed
	 * 
	 * @author Rene Schwab
	 * 
	 */

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
	 * Sets the local options. Method gets called in MainMenu when user changes language
	 * in the languageSelectComboBox. The value of "Language" in the local.cfg gets adapted. 
	 * 
	 * @author Rene Schwab
	 * 
	 * @param name
	 * , the key in the local.cfg file (Language)
	 * @param value
	 * , corresponds with the language locale (de, en, ...)
	 * 
	 */
	public void setLocalOption(String name, String value) {
		localOptions.setProperty(name, value);
	}
	
}// end Configuration