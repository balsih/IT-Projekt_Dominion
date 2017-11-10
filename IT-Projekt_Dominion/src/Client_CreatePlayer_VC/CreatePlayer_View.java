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
	
	protected BorderPane root;
	protected Button save;
	protected TextField name_text;
	protected TextField password_text;
	protected Label createNewPlayer; 
	protected Label name;
	protected Label password;
	protected VBox vBox;
	
	/**
	 * 
	 * @param model
	 */
	public CreatePlayer_View(Stage stage, GameApp_Model model){
		super(stage, model);
	}

	@Override
	protected Scene create_GUI(){
		root = new BorderPane();
		save = new Button();
		name_text = new TextField();
		password_text = new TextField();
		createNewPlayer = new Label();
		name = new Label();
		password = new Label();
		vBox = new VBox();
		
		root.setPrefSize(250,300);
	
		// dummy mässig, muss über Translator gesetzt werden
		createNewPlayer.setText("Neuer Spieler erstellen");
		name.setText("Name:");
		password.setText("Passwort:");
		
		save.setText("Speichern");
		
		root.setBottom(save);
		root.setCenter(vBox);
		root.setTop(createNewPlayer);
		vBox.getChildren().addAll(name, name_text, password, password_text);
		//vBox.getChildren().addAll(createNewPlayer, name, name_text, password, password_text);
		
		// Fill borderpane
		
		Scene scene = new Scene(root);
		this.stage.setScene(scene);
		this.root.setBottom(save);
		return scene;
	}
	
	
	public void start() {
		this.stage.show();
	}
}//end CreatePlayer_View