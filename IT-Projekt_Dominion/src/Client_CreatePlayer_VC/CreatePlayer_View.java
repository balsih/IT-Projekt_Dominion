package Client_CreatePlayer_VC;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 17:03:48
 */
public class CreatePlayer_View extends View<GameApp_Model> {

	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	
	

	/**
	 * 
	 * @param model
	 */
	public CreatePlayer_View(Stage stage, GameApp_Model model){
		super(stage, model);
	}

	@Override
	protected Scene create_GUI(){
		
		VBox root = new VBox();
		root.setId("root");
		Button save = new Button();
		save.setId("save");
		TextField name_text = new TextField();
		name_text.setId("name_text");
		TextField password_text = new TextField();
		password_text.setId("password_text");
		Label createNewPlayer = new Label();
		createNewPlayer.setId("createNewPlayer");
		Label name = new Label();
		name.setId("name");
		Label password = new Label();
		password.setId("password");
		
		root.setPrefSize(250,300);
	
		// dummy mässig, muss über Translator gesetzt werden
		createNewPlayer.setText("Neuer Spieler erstellen");
		name.setText("Name:");
		password.setText("Passwort:");
		
		save.setText("Speichern");
		
		root.getChildren().addAll(name, name_text, password, password_text);
		//vBox.getChildren().addAll(createNewPlayer, name, name_text, password, password_text);
		
		// Fill borderpane
		
		Scene scene = new Scene(root);
		this.stage.setScene(scene);
		//this.root.setBottom(save);
		return scene;
	}
	
	
	public void start() {
		this.stage.show();
	}
}//end CreatePlayer_View