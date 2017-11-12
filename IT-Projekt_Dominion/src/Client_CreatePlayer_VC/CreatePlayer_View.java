package Client_CreatePlayer_VC;

import java.net.URL;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
		stage.initStyle(StageStyle.TRANSPARENT);
	}

	@Override
	protected Scene create_GUI(){
		
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		//sl.setTranslator(new Translator("de"));
		Translator t = sl.getTranslator();
		
		GridPane root = new GridPane();
		
		
		//VBox root = new VBox();
		
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
		
//		ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("Deutsch", "Englisch"));
//		cb.setTooltip(new Tooltip("Wähle eine Sprache"));
//		cb.setPrefSize(20, 30);

		
		// https://panjutorials.de/tutorials/javafx-8-gui/lektionen/audio-player-in-javafx-2/?cache-flush=1510439948.4916 
		// hier legen wir die Resource an, welche unbedingt im
		// entsprechenden Ordner sein muss
		final URL resource = getClass().getResource("sound.mp3");
		// wir legen das Mediaobjekt and und weisen unsere Resource zu
		final Media media = new Media(resource.toString());
		// wir legen den Mediaplayer an und weisen ihm das Media Objekt zu
		final MediaPlayer mediaPlayer = new MediaPlayer(media);
		
		mediaPlayer.play();
		//mediaPlayer.stop();
		
		root.add(createNewPlayer, 0, 0, 4, 2);
		root.add(name, 0, 2, 4, 2);
		root.add(name_text, 0, 4, 4, 2);
		root.add(password, 0, 6, 4, 2);
		root.add(password_text, 0, 8, 4, 2);
		root.add(save, 0, 10, 4, 2);
		
		
		root.setPrefSize(1280,720); 		
		
		//save.setAlignment(Pos.BOTTOM_RIGHT);
		//root.setVgrow(name, Priority.ALWAYS);
		
		root.setAlignment(Pos.CENTER);
		
		//root.getChildren().addAll(name, name_text, password, password_text, save);
		

		
		Scene scene = new Scene(root);		
		scene.getStylesheets().addAll(this.getClass().getResource("CreatePlayer.css").toExternalForm());
		this.stage.setScene(scene);
		
		return scene;
	}
	
	
	public void start() {
		this.stage.show();
	}
}//end CreatePlayer_View