package Client_Services;

import java.util.Properties;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:03:20
 */
public class Configuration {

	private Properties defaultOptions;
	private Properties localOptions;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();


	public Configuration(){

	}

	/**
	 * 
	 * @param name
	 */
	public String getOption(String name){
		return "";
	}

	public void save(){

	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setLocalOption(String name, String value){

	}
}//end Configuration