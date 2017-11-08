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
			this.updateProgress(1, 5); // step one of total 4 
			
			//Thread.sleep(500);
			
        	sl = ServiceLocator.getServiceLocator();
        	this.updateProgress(2, 5); // step two of total 4 
        	
        	sl.setConfiguration(new Configuration());
        	this.updateProgress(3, 5); // step two of total 4 
        	
        	String language = sl.getConfiguration().getOption("Language");
        	sl.setTranslator(new Translator(language));
        	this.updateProgress(4, 5); // step three of total 4
        	
        	sl.setGallery(new Gallery(language));
        	this.updateProgress(5, 5); // step four of total 4
        	

//            // First, take some time, update progress
//            Integer i = 0;
//            for (; i < 1000000000; i++) {
//                if ((i % 1000000) == 0)
//                    this.updateProgress(i, 1000000000);
//            }
//
//            // Create the service locator to hold our resources
//            sl = ServiceLocator.getServiceLocator();
//
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