package Client_MainMenu_VC;

import java.net.URL;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import Server_GameLogic.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * @author Rene
 * @version 1.0
 * @created 31-Okt-2017 17:05:02
 */
public class MainMenu_View extends View<GameApp_Model> {

	private ServiceLocator sl;

	// controls -> accessed by controller
	protected Label playerLbl;
	protected Label mainMenuLbl;
	
	protected Label selectModeLbl;
	protected Button singlePlayerBtn;
	protected Button multiPlayerBtn;

	protected Label highscoreLbl;
	protected Label number1;
	protected Label number2;
	protected Label number3;
	protected Label number4;
	protected Label number5;

	protected Button startGameBtn;
	protected Button quitBtn;

	/**
	 * 
	 * @param model
	 */
	public MainMenu_View(Stage stage, GameApp_Model model) {
		super(stage, model);
	}
	
	@Override
	protected Scene create_GUI() {

		// sl.setTranslator(new Translator("en"));
		sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();

		// layouts
		GridPane root = new GridPane();
		root.setId("root");
		VBox centerBox = new VBox();
		centerBox.setId("centerBox");
		VBox gameModeBox = new VBox();
		gameModeBox.setId("gameModeBox");
		VBox highscoreBox = new VBox();
		highscoreBox.setId("highscoreBox");

		// labels and text fields
		
		// shows the name of the actual player
		//playerLbl = new Label(t.getString("menu.playerLbl"));
		//playerLbl = new Label(model.getClientName());
		playerLbl = new Label("Spieler: Bodo Grütter");
		playerLbl.setId("playerLbl");
		
		mainMenuLbl = new Label(t.getString("menu.mainMenuLbl"));
		mainMenuLbl.setId("mainMenuLbl");
		
		selectModeLbl = new Label(t.getString("menu.selectModeLbl"));
		selectModeLbl.setId("selectModeLbl");
		singlePlayerBtn = new Button(t.getString("menu.singlePlayerBtn"));
		singlePlayerBtn.setId("singlePlayerBtn");
		multiPlayerBtn = new Button(t.getString("menu.multiPlayerBtn"));
		multiPlayerBtn.setId("multiPlayerBtn");
		HBox singleAndMultiplayerBox = new HBox(singlePlayerBtn, multiPlayerBtn);
		singleAndMultiplayerBox.setId("singleAndMultiplayerBox");
		gameModeBox.getChildren().addAll(selectModeLbl, singleAndMultiplayerBox);
		
		highscoreLbl = new Label(t.getString("menu.highscoreLbl"));
		highscoreLbl.setId("highscoreLbl");
		number1 = new Label("1. Chuck Norris");
		number2 = new Label("2. Bodo Grütter");
		number3 = new Label("3. dini Mueter");
		
		// model.sendHighScoreRequest() -> gibt 5x String und int zurück = muss verwendet werden um die Labels abzufüllen
		
		
		
		
		// nameText.setPrefSize(220.0, 30.0);
		highscoreBox.getChildren().addAll(highscoreLbl, number1, number2, number3);
		
		startGameBtn = new Button(t.getString("menu.startGameBtn"));
		startGameBtn.setId("startGameBtn");
		quitBtn = new Button(t.getString("menu.quitBtn"));
		quitBtn.setId("quitBtn");
		HBox startAndQuitBox = new HBox(startGameBtn, quitBtn);
		startAndQuitBox.setId("startAndQuitBox");
		

		// VBox for layout and spacing
		VBox gameModeAndHigscoreBox = new VBox();
		gameModeAndHigscoreBox.setId("gameModeAndHigscoreBox");
		gameModeAndHigscoreBox.getChildren().addAll(gameModeBox, highscoreBox);

		// layout and size configurations
		root.setPrefSize(1280, 720);
		root.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(playerLbl, mainMenuLbl, gameModeAndHigscoreBox, startAndQuitBox );
		// centerBox.getChildren().addAll(createNewPlayer, nameBox, passwordBox,
		// buttonBox); // -> ohne Sprachauswahl
		root.getChildren().add(centerBox);

		// https://panjutorials.de/tutorials/javafx-8-gui/lektionen/audio-player-in-javafx-2/?cache-flush=1510439948.4916
		// hier legen wir die Resource an, welche unbedingt im entsprechenden
		// Ordner sein muss
		//final URL resource = getClass().getResource("Medieval_Camelot.mp3");
		// wir legen das Mediaobjekt and und weisen unsere Resource zu
		//final Media media = new Media(resource.toString());
		// wir legen den Mediaplayer an und weisen ihm das Media Objekt zu
		//final MediaPlayer mediaPlayer = new MediaPlayer(media);

	//	mediaPlayer.play();
		// mediaPlayer.stop();

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("MainMenu.css").toExternalForm());
		this.stage.setScene(scene);
		// stage.setFullScreen(true); // set Full Screen

		return scene;
	}
}// end MainMenu_View