package Client_Splash_MVC;

import Abstract_MVC.Controller;
import MainClasses.Dominion_Main;
import javafx.concurrent.Worker;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 17:06:09
 */
public class Splash_Controller extends Controller<Splash_Model, Splash_View> {

	/**
	 * 
	 * @param main
	 * @param model
	 * @param view
	 */
	public Splash_Controller(Dominion_Main main, Splash_Model model, Splash_View view){
		super(model, view);
		
		view.progress.progressProperty().bind(model.initializer.progressProperty()); // Connects progress task with progress bar
		
	
		model.initializer.stateProperty().addListener(     (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED)
                    	main.startLogin(); // starts login after initializer is finish 
                });		
	}
}//end Splash_Controller