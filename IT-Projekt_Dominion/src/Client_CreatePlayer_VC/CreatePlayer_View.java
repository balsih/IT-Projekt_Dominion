package Client_CreatePlayer_VC;

import java.net.URL;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


/**
 * @author Rene
 * @version 1.0
 * @created 31-Okt-2017 17:03:48
 */
public class CreatePlayer_View extends View<GameApp_Model> {

	private ServiceLocator sl; 
	
	// controls -> accessed by controller 
	protected Label createNewPlayerLbl;
		
	protected Label nameLbl;
	protected TextField nameText;
		
	protected Label passwordLbl;
	protected TextField passwordText;
	
	protected Label languageSelectLbl;
	protected ComboBox languageSelectComboBox;
		
	protected Button saveBtn;
	protected Button backBtn;
	

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
		
		sl = ServiceLocator.getServiceLocator();
		//sl.setTranslator(new Translator("en"));
		Translator t = sl.getTranslator();
		
		// layouts
		GridPane root = new GridPane();
		root.setId("root");
		VBox centerBox = new VBox();
		centerBox.setId("centerBox");
		VBox nameBox = new VBox();
		nameBox.setId("nameBox");
		VBox passwordBox = new VBox();
		passwordBox.setId("passwordBox");
		
		VBox languageBox = new VBox();
		languageBox.setId("languageBox");
		
		
		// labels and text fields
		createNewPlayerLbl = new Label(t.getString("cnp.createNewPlayerLbl"));
		createNewPlayerLbl.setId("createNewPlayerLbl");
		
		nameLbl = new Label(t.getString("cnp.nameLbl"));
		nameLbl.setId("nameLbl");
		nameText = new TextField();
		nameText.setId("nameText");
		nameBox.getChildren().addAll(nameLbl, nameText);
		
		passwordLbl = new Label(t.getString("cnp.passwordLbl"));
		passwordLbl.setId("passwordLbl");
		passwordText = new TextField();
		passwordText.setId("passwordText");
		passwordBox.getChildren().addAll(passwordLbl, passwordText);
		
		// language selection with ComboBox
		languageSelectLbl = new Label(t.getString("cnp.languageSelectLbl"));
		languageSelectLbl.setId("languageSelectLbl");

		ObservableList<String> language = FXCollections.observableArrayList(t.getString("program.german"), t.getString("program.english"));
		languageSelectComboBox = new ComboBox(language);
		languageSelectComboBox.setTooltip(new Tooltip(t.getString("program.languageTip")));
		languageSelectComboBox.setPrefSize(280.0, 30.0);
		languageBox.getChildren().addAll(languageSelectLbl, languageSelectComboBox);
		
		// buttons
		saveBtn = new Button(t.getString("cnp.saveBtn"));
		saveBtn.setId("saveBtn");
		backBtn = new Button(t.getString("cnp.backBtn"));
		backBtn.setId("backBtn");	
		HBox buttonBox = new HBox(saveBtn, backBtn);
		buttonBox.setId("buttonBox");
		
		// VBox for layout and spacing 
		VBox namePasswordLanguageBox = new VBox();
		namePasswordLanguageBox.setId("namePasswordLanguageBox");
		namePasswordLanguageBox.getChildren().addAll(nameBox, passwordBox, languageBox);
		
		// layout and size configurations 
		root.setPrefSize(1280,720);
		root.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(createNewPlayerLbl, namePasswordLanguageBox, buttonBox);
		// centerBox.getChildren().addAll(createNewPlayer, nameBox, passwordBox, buttonBox); // -> ohne Sprachauswahl 
		root.getChildren().add(centerBox);
		
		
//		// https://panjutorials.de/tutorials/javafx-8-gui/lektionen/audio-player-in-javafx-2/?cache-flush=1510439948.4916 
//		// hier legen wir die Resource an, welche unbedingt im entsprechenden Ordner sein muss
//		final URL resource = getClass().getResource("sound.mp3");
//		// wir legen das Mediaobjekt and und weisen unsere Resource zu
//		final Media media = new Media(resource.toString());
//		// wir legen den Mediaplayer an und weisen ihm das Media Objekt zu
//		final MediaPlayer mediaPlayer = new MediaPlayer(media);
//		
//		mediaPlayer.play();
//		//mediaPlayer.stop();
		
		
		Scene scene = new Scene(root);	
		scene.getStylesheets().add(getClass().getResource("CreatePlayer.css").toExternalForm());
		this.stage.setScene(scene);
		//stage.setFullScreen(true); // set Full Screen
		
		return scene;
	}
	
	
	public void start() {
		this.stage.show();
	}
}//end CreatePlayer_View