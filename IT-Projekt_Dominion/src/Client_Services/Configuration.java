package Client_Services;

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
	//private Logger logger = sl.getLogger();


	public Configuration(){

		
		defaultOptions = new Properties();
		String defaultFilename = sl.getAPP_NAME() + "_defaults.cfg";
		InputStream inStream = sl.getAPP_CLASS().getResourceAsStream(defaultFilename);
		try {
		defaultOptions.load(inStream);
		logger.config("Default configuration file found");
		} catch (Exception e) {
		logger.warning("No default configuration file found: " + defaultFilename);
		} finally {
		try {
		inStream.close();
		} catch (Exception ignore) {
		}
		}
		
>>>>>>> branch 'master' of https://github.com/Eagleman1997/IT-Projekt_Dominion.git
		localOptions = new Properties(defaultOptions);
	}

	/**
	 * 
	 * @param name
	 */
	public String getOption(String name){
		return localOptions.getProperty(name);
	}

	public void save(){

	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setLocalOption(String name, String value){
		localOptions.setProperty(name, value);
	}
}//end Configuration