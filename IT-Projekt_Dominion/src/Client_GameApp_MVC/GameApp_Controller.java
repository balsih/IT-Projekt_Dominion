package Client_GameApp_MVC;

import Abstract_MVC.Controller;
import MainClasses.Dominion_Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author Adrian
 * @version 1.0
 * @created 31-Okt-2017 17:04:38
 */
public class GameApp_Controller extends Controller<GameApp_Model, GameApp_View>{

	public GameApp_Controller(GameApp_Model model, GameApp_View view){
		super(model, view);
	}

}//end GameApp_Controller