package Client_Services;

import java.util.Properties;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 17:03:20
 */
public class Configuration {

	private Properties defaultOptions;
	private Properties localOptions;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();


	public Configuration(){
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