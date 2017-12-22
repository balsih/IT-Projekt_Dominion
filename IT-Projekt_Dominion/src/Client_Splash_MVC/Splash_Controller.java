package Client_Splash_MVC;

import Abstract_MVC.Controller;
import MainClasses.Dominion_Main;
import javafx.concurrent.Worker;

/**
 * Controller class for Splash screen
 * Handling of action events for this GUI 
 * 
 * @author Rene Schwab
 */
public class Splash_Controller extends Controller<Splash_Model, Splash_View> {


	public Splash_Controller(Dominion_Main main, Splash_Model model, Splash_View view){
		super(model, view);
		
		view.progress.progressProperty().bind(model.initializer.progressProperty()); // connects progress task with progress bar
		
	
		model.initializer.stateProperty().addListener(     (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED)
                    	main.startLogin(); // starts login after initializer is finish 
                });		
	}
}//end Splash_Controller