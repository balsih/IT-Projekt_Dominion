package Server_MVC;

import Abstract_MVC.View;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:09:20
 */
public class Server_View extends View<Server_Model> {

	private TextArea txtLog;

	/**
	 * 
	 * @param stage
	 * @param model
	 * @param txtLog
	 */
	public Server_View(Stage stage, Server_Model model, TextArea txtLog){
		super(stage, model);
	}


	@Override
	protected Scene create_GUI() {
		return null;
	}
}//end Server_View