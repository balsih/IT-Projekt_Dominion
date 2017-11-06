package Client_Splash_MVC;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import Abstract_MVC.Model;
import Client_Services.ServiceLocator;
import javafx.concurrent.Task;

/**
 * @author René  --> grösstenteil Copy Past von Brad
 * @version 1.0
 * @created 31-Okt-2017 17:06:12
 */
public class Splash_Model extends Model {

	private ServiceLocator sl = ServiceLocator.getServiceLocator();


	public Splash_Model(){
		super();
	}
	
	
	final Task<Void> initializer = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            // First, take some time, update progress
            Integer i = 0;
            for (; i < 1000000000; i++) {
                if ((i % 1000000) == 0)
                    this.updateProgress(i, 1000000000);
            }

            // Create the service locator to hold our resources
            sl = ServiceLocator.getServiceLocator();

            // Initialize the resources in the service locator
           // sl.setLogger(configureLogging());

            // ... more resources would go here...

            return null;
        }
    };
	
    private Logger configureLogging() {
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
    }
    
    
    
	
	

	public void initialize(){

	}
}//end Splash_Model