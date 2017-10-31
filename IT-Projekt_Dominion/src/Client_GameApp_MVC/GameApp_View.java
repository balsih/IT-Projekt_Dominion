package Client_GameApp_MVC;

import Abstract_MVC.View;
import Client_Services.ServiceLocator;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Adrian
 * @version 1.0
 * @created 31-Okt-2017 17:04:43
 */
public class GameApp_View extends View<GameApp_Model> {

	private ServiceLocator sl = ServiceLocator.getServiceLocator();

	/**
	 * 
	 * @param model
	 */
	public GameApp_View(Stage stage, GameApp_Model model){
		super(stage, model);
	}

	@Override
	protected Scene create_GUI(){
		return null;
	}
}//end GameApp_View