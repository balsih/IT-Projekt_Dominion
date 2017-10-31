package Client_Services;

import java.util.Locale;

import MainClasses.Dominion_Main;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:05:16
 */
public class ServiceLocator {

	private final Class<?> APP_CLASS = Dominion_Main.class;
	private final String APP_NAME = Dominion_Main.class.getSimpleName();
	private Configuration configuration;
	private Gallery gallery;
	private final Locale[] locales = new Locale[] {new Locale("en"), new Locale("de")};
	private static ServiceLocator serviceLocator;
	private Translator translator;



	private ServiceLocator(){

	}

	public Class<?> getAPP_CLASS(){
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
		if(serviceLocator == null)
			return new ServiceLocator();
		return serviceLocator;
	}

	public Translator getTranslator(){
		return null;
	}

	/**
	 * 
	 * @param configuration
	 */
	public void setConfiguration(Configuration configuration){

	}

	/**
	 * 
	 * @param gallery
	 */
	public void setGallery(Gallery gallery){

	}

	/**
	 * 
	 * @param translator
	 */
	public void setTranslator(Translator translator){

	}
}//end ServiceLocator