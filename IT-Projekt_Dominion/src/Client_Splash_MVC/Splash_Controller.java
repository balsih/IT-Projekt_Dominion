package Client_Splash_MVC;

import Abstract_MVC.Controller;
import MainClasses.Dominion_Main;

/**
 * @author Renate
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
	}
}//end Splash_Controller