package Client_MainMenu_VC;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Adrian
 * @version 1.0
 * @created 31-Okt-2017 17:05:02
 */
public class MainMenu_View extends View<GameApp_Model> {

	private ServiceLocator sl = ServiceLocator.getServiceLocator();

	/**
	 * 
	 * @param model
	 */
	public MainMenu_View(Stage stage, GameApp_Model model){
		super(stage, model);
	}

	@Override
	protected Scene create_GUI(){
		return null;
	}
}//end MainMenu_View