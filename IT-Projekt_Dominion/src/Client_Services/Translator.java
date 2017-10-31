

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:06:30
 */
public class Translator {

	protected Locale currentLocale;
	private ResourceBundle resourceBundle;
	private ServiceLocator sl;
	public ServiceLocator m_ServiceLocator;

	public Translator(){

	}

	public void finalize() throws Throwable {

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