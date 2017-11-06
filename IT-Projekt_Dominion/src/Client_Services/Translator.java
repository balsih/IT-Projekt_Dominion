package Client_Services;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 17:06:30
 */
public class Translator {

	protected Locale currentLocale;
	private ResourceBundle resourceBundle;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();

	public Translator(){

	}

	/**
	 * 
	 * @param localeString
	 */
	public Translator(String localeString){

	}

	public Locale getCurrentLocale(){
		return null;
	}

	/**
	 * 
	 * @param key
	 */
	public String getString(String key){
		return "";
	}
}//end Translator