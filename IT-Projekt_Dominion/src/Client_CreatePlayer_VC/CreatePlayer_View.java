package Client_CreatePlayer_VC;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 17:03:48
 */
public class CreatePlayer_View extends View<GameApp_Model> {

	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	
	protected BorderPane root;
	protected GridPane grid;
	protected Button button;
	protected TextField text;

	/**
	 * 
	 * @param model
	 */
	public CreatePlayer_View(Stage stage, GameApp_Model model){
		super(stage, model);
		
		Scene scene = new Scene(root);
		this.stage.setScene(scene);
		this.root.setBottom(text);
		
	}

	@Override
	protected Scene create_GUI(){
		return null;
	}
	
	
	public void start() {
		this.stage.show();
	}
}//end CreatePlayer_View