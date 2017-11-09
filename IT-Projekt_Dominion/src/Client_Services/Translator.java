package Client_Services;

import java.util.Locale;
import java.util.MissingResourceException;
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

//	public Translator(){  wird Konstruktor ohne Parameter benötigt??
//
//	}

	/**
	 * 
	 * @param localeString
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
        
        
        // logger.info("Loaded resources for " + locale.getLanguage());

	}

	public Locale getCurrentLocale(){
		return currentLocale;
	}

	/**
	 * 
	 * @param key
	 */
	public String getString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			// logger.warning("Missing string: " + key);
			return "--";
		}
	}
}//end Translator