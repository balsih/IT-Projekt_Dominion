package Client_Login_VC;

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
 * @created 31-Okt-2017 17:04:53
 */
public class Login_View extends View<GameApp_Model> {

	//private ServiceLocator sl = ServiceLocator.getServiceLocator();
	private ServiceLocator sl;
	

	/**
	 * 
	 * @param model
	 */
	public Login_View(Stage stage, GameApp_Model model){
		super(stage, model);
		//stage.initStyle(StageStyle.TRANSPARENT);
		
	}

	@Override
	protected Scene create_GUI(){
		
		//sl.setTranslator(new Translator("en"));
		sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();
		
		// layouts
		GridPane root = new GridPane();
		root.setId("root");
		VBox centerBox = new VBox();
		centerBox.setId("centerBox");
		VBox ipBox = new VBox();
		ipBox.setId("ipBox");
		VBox nameBox = new VBox();
		nameBox.setId("nameBox");
		VBox passwordBox = new VBox();
		passwordBox.setId("passwordBox");
		
		VBox languageBox = new VBox();
		languageBox.setId("languageBox");
		
		// labels and text fields
		Label login = new Label(t.getString("login.login"));
		login.setId("login");
		
		Label ipLabel = new Label(t.getString("login.ipLabel"));
		ipLabel.setId("ipLabel");
		TextField ipText = new TextField();
		ipText.setId("ipText");
		Button connect = new Button(t.getString("login.connect"));
		connect.setId("connect");	
		HBox ipAndConnect = new HBox(ipText, connect);
		ipAndConnect.setId("ipAndConnect");
		ipBox.getChildren().addAll(ipLabel, ipAndConnect);
		
		Label name = new Label(t.getString("login.name"));
		name.setId("name");
		TextField nameText = new TextField();
		nameText.setId("nameText");
		nameBox.getChildren().addAll(name, nameText);
		
		Label password = new Label(t.getString("login.password"));
		password.setId("password");
		TextField passwordText = new TextField();
		passwordText.setId("passwordText");
		passwordBox.getChildren().addAll(password, passwordText);
		
		// buttons
		Button createNewPlayerBtn = new Button(t.getString("login.createNewPlayerBtn"));
		createNewPlayerBtn.setId("createNewPlayerBtn");
		Button quitBtn = new Button(t.getString("login.quitBtn"));
		quitBtn.setId("quitBtn");
		Button loginBtn = new Button(t.getString("login.loginBtn"));
		loginBtn.setId("loginBtn");
		
		HBox buttonBox = new HBox(createNewPlayerBtn, quitBtn, loginBtn);
		buttonBox.setId("buttonBox");
		
		
		// layout and size configurations 
		root.setPrefSize(1280,720);
		root.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(login, ipBox, nameBox, passwordBox, buttonBox);
		// centerBox.getChildren().addAll(createNewPlayer, nameBox, passwordBox, buttonBox); // -> ohne Sprachauswahl 
		root.getChildren().add(centerBox);
		
		
		// https://panjutorials.de/tutorials/javafx-8-gui/lektionen/audio-player-in-javafx-2/?cache-flush=1510439948.4916 
		//hier legen wir die Resource an, welche unbedingt im entsprechenden Ordner sein muss
		final URL resource = getClass().getResource("willy.mp3");
		// wir legen das Mediaobjekt and und weisen unsere Resource zu
		final Media media = new Media(resource.toString());
		// wir legen den Mediaplayer an und weisen ihm das Media Objekt zu
		final MediaPlayer mediaPlayer = new MediaPlayer(media);
		
		mediaPlayer.play();
		//mediaPlayer.stop();
		
		
		Scene scene = new Scene(root);	
		scene.getStylesheets().add(getClass().getResource("CreatePlayer.css").toExternalForm());
		this.stage.setScene(scene);
		//stage.setFullScreen(true); // set Full Screen
		
		return scene;
	}
	
	//public void start() {
	
}//end Login_View