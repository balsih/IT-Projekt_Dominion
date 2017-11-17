package Client_Services;

import java.util.Locale;

import Client_Services.Gallery.Gallery;
import MainClasses.Dominion_Main;

/**
 * @author Rene
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

	private ServiceLocator() {
		//
	}

	public Class<?> getAPP_CLASS(){
		return APP_CLASS;
	}

	public String getAPP_NAME(){
		return APP_NAME;
	}

	public Configuration getConfiguration(){
		return configuration;
	}

	public Gallery getGallery(){
		return gallery;
	}

	public Locale[] getLocales(){
		return locales;
	}

	public static ServiceLocator getServiceLocator(){
		if(serviceLocator == null)
			serviceLocator = new ServiceLocator();
		return serviceLocator;
	}

	public Translator getTranslator(){
		if(translator == null)
			translator = new Translator(APP_NAME);
		return translator;
	}

	/**
	 * 
	 * @param configuration
	 */
	public void setConfiguration(Configuration configuration){
		this.configuration = configuration;
	}

	/**
	 * 
	 * @param gallery
	 */
	public void setGallery(Gallery gallery){
		this.gallery = gallery;
	}

	/**
	 * 
	 * @param translator
	 */
	public void setTranslator(Translator translator){
		this.translator = translator;
	}
}//end ServiceLocator