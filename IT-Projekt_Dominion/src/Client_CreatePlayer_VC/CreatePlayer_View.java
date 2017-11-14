package Client_CreatePlayer_VC;

import java.net.URL;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
		//stage.initStyle(StageStyle.TRANSPARENT);
	}

	@Override
	protected Scene create_GUI(){
		
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		//sl.setTranslator(new Translator("en"));
		Translator t = sl.getTranslator();
		
		// layouts
		GridPane root = new GridPane();
		root.setId("root");
		VBox vBox = new VBox();
		vBox.setId("vBox");
		
		// labels
		Label createNewPlayer = new Label(t.getString("cnp.createNewPlayer"));
		createNewPlayer.setId("createNewPlayer");
		//createNewPlayer.getStyleClass().add("createNewPlayer");  --> alternative Zuweisung zu CSS 
		Label name = new Label(t.getString("cnp.name"));
		name.setId("name");
		Label password = new Label(t.getString("cnp.password"));
		password.setId("password");
		
		// buttons
		Button save = new Button(t.getString("cnp.save"));
		save.setId("save");
		Button back = new Button(t.getString("cnp.back"));
		save.setId("back");
		HBox buttonBox = new HBox(save, back);
		buttonBox.setId("buttonBox");
		
		// textfields
		TextField name_text = new TextField();
		name_text.setId("name_text");
		TextField password_text = new TextField();
		password_text.setId("password_text");
		
		// language selection with ComboBox
		ObservableList<String> language = FXCollections.observableArrayList(t.getString("program.german"), t.getString("program.english"));
		final ComboBox cb = new ComboBox(language);
		cb.setTooltip(new Tooltip(t.getString("program.languageTip")));
		cb.setPrefSize(300.0, 15.0);
		
		
		// https://panjutorials.de/tutorials/javafx-8-gui/lektionen/audio-player-in-javafx-2/?cache-flush=1510439948.4916 
		// hier legen wir die Resource an, welche unbedingt im entsprechenden Ordner sein muss
		final URL resource = getClass().getResource("sound.mp3");
		// wir legen das Mediaobjekt and und weisen unsere Resource zu
		final Media media = new Media(resource.toString());
		// wir legen den Mediaplayer an und weisen ihm das Media Objekt zu
		final MediaPlayer mediaPlayer = new MediaPlayer(media);
		
		mediaPlayer.play();
		//mediaPlayer.stop();
		
		
		root.setPrefSize(1280,720);
		//vBox.setPrefSize(300,200);
		
		//save.setAlignment(Pos.BOTTOM_RIGHT);
		//root.setVgrow(name, Priority.ALWAYS);
		
		root.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(createNewPlayer, name, name_text, password, password_text, cb, buttonBox);
		root.getChildren().add(vBox);
		
		
		Scene scene = new Scene(root);		
		//scene.getStylesheets().addAll(this.getClass().getResource("CreatePlayer.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("CreatePlayer.css").toExternalForm());
		this.stage.setScene(scene);
		
		return scene;
	}
	
	
	public void start() {
		this.stage.show();
	}
}//end CreatePlayer_View