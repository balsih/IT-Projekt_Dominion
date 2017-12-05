package Client_MainMenu_VC;

import java.net.URL;
import java.util.Locale;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import Server_GameLogic.Game;
import Server_GameLogic.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
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
	protected ComboBox<String> languageSelectComboBox;
	protected Label mainMenuLbl;
	
	protected Label selectModeLbl;
	protected Button singlePlayerBtn;
	protected Button multiPlayerBtn;
	
	protected Label highscoreLbl;
	protected Label highscoreListLbl;
	
	protected Button startGameBtn;
	protected Button quitBtn;
	
	protected Alert startGameAlert;
	
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
	
		//playerLbl = new Label("Spieler: Bodo Grütter"); // zum testen da noch kein Playername vorhanden
		playerLbl = new Label(model.getClientName());
		playerLbl.setId("playerLbl");
		
		// language selection with ComboBox
		ObservableList<String> lang = FXCollections.observableArrayList();
		int currentIndex = 0;
		for (int i = 0; i < sl.getLocales().length; i++) {
			Locale locale = sl.getLocales()[i];
			lang.add(locale.getLanguage());
			// Find current language index
			if (locale.equals(sl.getTranslator().getCurrentLocale())) {
				currentIndex = i;
			} 
		} 
		languageSelectComboBox = new ComboBox<String>(lang);
		languageSelectComboBox.setValue(lang.get(currentIndex)); 
		languageSelectComboBox.setId("languageSelectComboBox");
		
		//languageSelectComboBox.setTooltip(new Tooltip(t.getString("program.languageTip")));
		//languageSelectComboBox.setPrefSize(280.0, 30.0);
		
		HBox playerAndLanguageBox = new HBox(playerLbl, languageSelectComboBox);
		playerAndLanguageBox.setId("playerAndLanguageBox");
		
		
		mainMenuLbl = new Label(t.getString("menu.mainMenuLbl"));
		mainMenuLbl.setId("mainMenuLbl");
		
		selectModeLbl = new Label(/*t.getString("menu.selectModeLbl")*/);
		selectModeLbl.setId("selectModeLbl");
		singlePlayerBtn = new Button(/*t.getString("menu.singlePlayerBtn")*/);
		singlePlayerBtn.setId("singlePlayerBtn");
		multiPlayerBtn = new Button(/*t.getString("menu.multiPlayerBtn")*/);
		multiPlayerBtn.setId("multiPlayerBtn");
		HBox singleAndMultiplayerBox = new HBox(singlePlayerBtn, multiPlayerBtn);
		singleAndMultiplayerBox.setId("singleAndMultiplayerBox");
		gameModeBox.getChildren().addAll(selectModeLbl, singleAndMultiplayerBox);
		
		// warning message, if start game (single- or multiplayer) fails
		startGameAlert = new Alert(AlertType.WARNING);
		//startGameAlert.setTitle(t.getString("menu.startGameAlert"));
		//startGameAlert.setHeaderText(t.getString("NoConnection"));
		// loginAlert.setContentText("do this or that");
		
		
		highscoreLbl = new Label(/*t.getString("menu.highscoreLbl")*/);
		highscoreLbl.setId("highscoreLbl");
		
		highscoreListLbl = new Label(model.sendHighScoreRequest()); // sets the top five as a five line String
		
//		// test code :
//		String s = "Rene";
//		int i = 18;
//		String ss = "Rene Schwab";
//		int ii = 69;
//		String f = ""+s+ ":\t"+i+ "\n"+ss+":\t"+ii+ "\n"+"slkfajlfkdasj"+ "\n"+"slkfajlfkdasj"+ "\n"+"slkfajlfkdasj";
//		// end of testcode
//		highscoreListLbl = new Label(f);
		
		highscoreListLbl.setId("highscoreListLbl");
		highscoreListLbl.setPrefSize(280, 160);

		
		
		// nameText.setPrefSize(220.0, 30.0);
		highscoreBox.getChildren().addAll(highscoreLbl, highscoreListLbl );
		
		//startGameBtn = new Button(t.getString("menu.startGameBtn"));
		
		//startGameBtn.setId("startGameBtn");
		quitBtn = new Button(/*t.getString("menu.quitBtn")*/);
		quitBtn.setId("quitBtn");
		HBox startAndQuitBox = new HBox(/*startGameBtn, */quitBtn);
		startAndQuitBox.setId("startAndQuitBox");
		

		// VBox for layout and spacing
		VBox gameModeAndHigscoreBox = new VBox();
		gameModeAndHigscoreBox.setId("gameModeAndHigscoreBox");
		gameModeAndHigscoreBox.getChildren().addAll(gameModeBox, highscoreBox);

		// layout and size configurations
		root.setPrefSize(1280, 720);
		root.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(playerAndLanguageBox, mainMenuLbl, gameModeAndHigscoreBox, startAndQuitBox );
		// centerBox.getChildren().addAll(createNewPlayer, nameBox, passwordBox,
		// buttonBox); // -> ohne Sprachauswahl
		root.getChildren().add(centerBox);


		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("MainMenu.css").toExternalForm());
		this.stage.setScene(scene);
		// stage.setFullScreen(true); // set Full Screen
		updateTexts();
		return scene;
	}
	
	public void updateTexts() {
		//startGameBtn
		Translator t = sl.getTranslator();
		quitBtn.setText(t.getString("menu.quitBtn"));
		highscoreLbl.setText(t.getString("menu.highscoreLbl"));
		startGameAlert.setTitle(t.getString("menu.startGameAlert"));
		startGameAlert.setHeaderText(t.getString("NoConnection"));
		multiPlayerBtn.setText(t.getString("menu.multiPlayerBtn"));
		singlePlayerBtn.setText(t.getString("menu.singlePlayerBtn"));
		selectModeLbl.setText(t.getString("menu.selectModeLbl"));
		mainMenuLbl.setText(t.getString("menu.mainMenuLbl"));
	}
	
}// end MainMenu_View