package Client_CreatePlayer_VC;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 17:03:48
 */
public class CreatePlayer_View extends View<GameApp_Model> {

	//private ServiceLocator sl = ServiceLocator.getServiceLocator(); // muss dieser als Instanzvariable vorhanden sein bzw warum? 
	

	/**
	 * 
	 * @param model
	 */
	public CreatePlayer_View(Stage stage, GameApp_Model model){
		super(stage, model);
	}

	@Override
	protected Scene create_GUI(){
		
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		//sl.setTranslator(new Translator("de"));
		Translator t = sl.getTranslator();
		
		BorderPane pane = new BorderPane();
		
		
		VBox root = new VBox();
		root.setId("root");
		Button save = new Button(t.getString("cnp.saveButton"));
		save.setId("save");
		TextField name_text = new TextField();
		name_text.setId("name_text");
		TextField password_text = new TextField();
		password_text.setId("password_text");
		Label createNewPlayer = new Label(t.getString("cnp.createNewPlayer"));
		createNewPlayer.setId("createNewPlayer");
		Label name = new Label(t.getString("cnp.name"));
		name.setId("name");
		Label password = new Label(t.getString("cnp.password"));
		password.setId("password");
		
		root.setPrefSize(250,300);
		

		save.setAlignment(Pos.BOTTOM_RIGHT);
		root.setVgrow(pane, Priority.ALWAYS);

	
		root.getChildren().addAll(name, name_text, password, password_text, save);
		
		
		Scene scene = new Scene(root);
		this.stage.setScene(scene);

		return scene;
	}
	
	
	public void start() {
		this.stage.show();
	}
}//end CreatePlayer_View