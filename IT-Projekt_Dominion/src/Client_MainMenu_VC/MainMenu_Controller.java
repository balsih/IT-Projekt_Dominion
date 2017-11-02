package Client_MainMenu_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import MainClasses.Dominion_Main;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:05:00
 */
public class MainMenu_Controller extends Controller<GameApp_Model, MainMenu_View> {

	/**
	 * 
	 * @param main
	 * @param model
	 * @param view
	 */
	public MainMenu_Controller(Dominion_Main main, GameApp_Model model, MainMenu_View view){
		super(model, view);
		System.out.println("z Bäre");
	}
}//end MainMenu_Controller