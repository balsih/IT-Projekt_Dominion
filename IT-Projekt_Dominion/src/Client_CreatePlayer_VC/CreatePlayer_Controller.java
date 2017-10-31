package Client_CreatePlayer_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import MainClasses.Dominion_Main;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:03:44
 */
public class CreatePlayer_Controller extends Controller<GameApp_Model, CreatePlayer_View> {
	private Dominion_Main main;

	/**
	 * 
	 * @param main
	 * @param model
	 * @param view
	 */
	public CreatePlayer_Controller(Dominion_Main main, GameApp_Model model, CreatePlayer_View view){
		super(model, view);
	}
}//end CreatePlayer_Controller