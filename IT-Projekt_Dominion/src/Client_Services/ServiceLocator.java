

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:05:16
 */
public class ServiceLocator {

	private final Class APP_CLASS;
	private final String APP_NAME;
	private Configuration configuration;
	private Gallery gallery;
	private final Locale[] locales;
	private static ServiceLocator serviceLocator;
	private Translator translator;



	public void finalize() throws Throwable {

	}
	private ServiceLocator(){

	}

	public Class getAPP_CLASS(){
		return null;
	}

	public String getAPP_NAME(){
		return "";
	}

	public Configuration getConfiguration(){
		return null;
	}

	public Gallery getGallery(){
		return null;
	}

	public Locale[] getLocales(){
		return null;
	}

	public static ServiceLocator getServiceLocator(){
		return null;
	}

	public Translator getTranslator(){
		return null;
	}

	/**
	 * 
	 * @param configuration
	 */
	public setConfiguration(Configuration configuration){

	}

	/**
	 * 
	 * @param gallery
	 */
	public setGallery(Gallery gallery){

	}

	/**
	 * 
	 * @param translator
	 */
	public setTranslator(Translator translator){

	}
}//end ServiceLocator