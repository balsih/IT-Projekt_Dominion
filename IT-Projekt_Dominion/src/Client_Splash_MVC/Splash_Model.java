package Client_Splash_MVC;

import Abstract_MVC.Model;
import Client_Services.Configuration;
import Client_Services.Gallery;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.concurrent.Task;

/**
 * Model class for Splash screen. 
 * 
 * @author Brad Richards, Copyright 2015, FHNW
 * , adapted by Rene Schwab
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
			
        	sl = ServiceLocator.getServiceLocator();
        	this.updateProgress(2, 5); // step 2 of total 5
        	
        	sl.setConfiguration(new Configuration());
        	this.updateProgress(3, 5); // step 3 of total 5 
        	
        	// loads the language from the local.cfg file
        	String language = sl.getConfiguration().getOption("Language");
        	sl.setTranslator(new Translator(language));
        	this.updateProgress(4, 5); // step 4 of total 5

        	sl.setGallery(new Gallery(language));
        	this.updateProgress(5, 5); // step 5 of total 5

            return null;
        }
    };
	

	public void initialize(){
		new Thread(initializer).start();
	}
}//end Splash_Model