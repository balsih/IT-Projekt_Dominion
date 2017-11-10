package Client_Splash_MVC;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import Abstract_MVC.Model;
import Client_Services.Configuration;
import Client_Services.Gallery;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.concurrent.Task;

/**
 * @author René  --> grösstenteil Copy Past von Brad
 * @version 1.0
 * @created 31-Okt-2017 17:06:12
 */
public class Splash_Model extends Model {

	private ServiceLocator sl;


	public Splash_Model(){
		super();
	}
	
	
	final Task<Void> initializer = new Task<Void>() {
        
		@Override
        protected Void call() throws Exception {
			this.updateProgress(1, 5); // step 1 of total 5 
			
			Thread.sleep(1000); // Wartezeit zur Kontrolle
			
        	sl = ServiceLocator.getServiceLocator();
        	this.updateProgress(2, 5); // step 2 of total 5
        	
        	Thread.sleep(1000); // Wartezeit zur Kontrolle
        	
        	sl.setConfiguration(new Configuration());
        	this.updateProgress(3, 5); // step 3 of total 5 
        	
        	Thread.sleep(1000); // Wartezeit zur Kontrolle
        	
        	String language = sl.getConfiguration().getOption("Language");
        	sl.setTranslator(new Translator(language));
        	this.updateProgress(4, 5); // step 4 of total 5
        	
        	Thread.sleep(1000); // Wartezeit zur Kontrolle
        	
        	sl.setGallery(new Gallery(language));
        	this.updateProgress(5, 5); // step 5 of total 5
        	
        	Thread.sleep(1000); // Wartezeit zur Kontrolle

//            // Initialize the resources in the service locator
//           // sl.setLogger(configureLogging());
//
//            // ... more resources would go here...

            return null;
        }
    };
	
    /*private Logger configureLogging() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.FINEST);

        // By default there is one handler: the console
        Handler[] defaultHandlers = Logger.getLogger("").getHandlers();
        defaultHandlers[0].setLevel(Level.INFO);

        // Add our logger
        Logger ourLogger = Logger.getLogger(sl.getAPP_NAME());
        ourLogger.setLevel(Level.FINEST);
        
        // Add a file handler, putting the rotating files in the tmp directory
        try {
            Handler logHandler = new FileHandler("%t/"
                    + sl.getAPP_NAME() + "_%u" + "_%g" + ".log",
                    1000000, 9);
            logHandler.setLevel(Level.FINEST);
            ourLogger.addHandler(logHandler);
        } catch (Exception e) { // If we are unable to create log files
            throw new RuntimeException("Unable to initialize log files: "
                    + e.toString());
        }

        return ourLogger;
    }*/
    
    
    
	
	

	public void initialize(){
		new Thread(initializer).start();
	}
}//end Splash_Model