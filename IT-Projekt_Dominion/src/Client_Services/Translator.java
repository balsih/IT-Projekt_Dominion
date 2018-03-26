package Client_Services;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *  Class stores and handle the multilingual function of the application. 
 * 
 * @author Brad Richards, Copyright 2015, FHNW
 * , adapted by Rene Schwab
 * 
 */
public class Translator {

	protected Locale currentLocale;
	private ResourceBundle resourceBundle;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();


	/**
	 * Sets up a new Translator object. 
	 * 
	 * @author Rene Schwab
	 * 
	 * @param localeString
	 * , language locale as String (de, en, ...) 
	 * 
	 */
	public Translator(String localeString){
		// Can we find the language in our supported locales?
        // If not, use VM default locale
        Locale locale = Locale.getDefault();
        if (localeString != null) {
            Locale[] availableLocales = sl.getLocales();
            for (int i = 0; i < availableLocales.length; i++) {
                String tmpLang = availableLocales[i].getLanguage();
                if (localeString.substring(0, tmpLang.length()).equals(tmpLang)) {
                    locale = availableLocales[i];
                    break;
                }
            }
        }
        
        // Load the resource strings
        resourceBundle = ResourceBundle.getBundle(sl.getAPP_CLASS().getName(), locale);
        Locale.setDefault(locale); // Change VM default (for dialogs, etc.)
        currentLocale = locale;
	}

	
	public Locale getCurrentLocale(){
		return currentLocale;
	}

	/**
	 * Gives back the corresponding String for the given key (in the selected language of the Translator) 
	 * 
	 * @author Rene Schwab
	 * 
	 * @param key
	 * , that corresponds with the key of the property file 
	 * 
	 * @return String
	 * , value of the corresponding key 
	 */
	public String getString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			// gives "--" back if no resource (key) was found 
			return "--";
		}
	}
	
}//end Translator