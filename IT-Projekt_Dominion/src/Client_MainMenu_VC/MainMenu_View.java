package Client_MainMenu_VC;

import java.net.URL;
import java.util.Locale;

import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.Highscore;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	protected Label logoLbl;
	protected Label keepTypingLbl;
	
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
	
	protected TableView<Highscore> table;
	
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
		
		VBox structureBox = new VBox();
		structureBox.setId("structureBox");
		

		// labels and text fields
		logoLbl = new Label();
		logoLbl.setId("logoLbl");
		logoLbl.setPrefSize(410.0, 150.0);
		logoLbl.setMinSize(380.0, 0.0);
		logoLbl.setAlignment(Pos.CENTER);
	
		Image image = new Image(getClass().getResourceAsStream("Logo.png"));
		ImageView imageView = new ImageView(image);
		logoLbl.setGraphic(imageView);

		imageView.setScaleX(1.1);
		imageView.setScaleY(1.1);

		// Keep Typing Label
		keepTypingLbl = new Label();
		keepTypingLbl.setId("keepTypingLbl");
		keepTypingLbl.setPrefSize(40.0, 20.0);
		keepTypingLbl.setMinSize(40.0, 20.0);
		//keepTypingLbl.setAlignment(Pos.CENTER_RIGHT);
		keepTypingLbl.setAlignment(Pos.CENTER_LEFT);
	
		Image image2 = new Image(getClass().getResourceAsStream("KeepTyping.png"));
		ImageView imageView2 = new ImageView(image2);
		keepTypingLbl.setGraphic(imageView2);
		
		Label space = new Label();
		HBox typingBox = new HBox();
		typingBox.setId("typingBox");
		typingBox.getChildren().addAll(space, keepTypingLbl);

//		imageView2.setScaleX(1.1);
//		imageView2.setScaleY(1.1);
		
		
		
		// shows the name of the actual player
		playerLbl = new Label(t.getString("menu.player")+" "+model.getClientName());
		playerLbl.setId("playerLbl");
		playerLbl.setPrefSize(295, 15);
		playerLbl.setAlignment(Pos.CENTER.BOTTOM_LEFT);
		
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
		
		languageSelectComboBox.setTooltip(new Tooltip(t.getString("program.languageTip")));
		//languageSelectComboBox.setPrefSize(20.0, 20.0);
		
		HBox playerAndLanguageBox = new HBox(playerLbl, languageSelectComboBox);
		
		//playerAndLanguageBox.setPrefSize(360, 40);
		playerAndLanguageBox.setId("playerAndLanguageBox");
		playerAndLanguageBox.setAlignment(Pos.CENTER.CENTER_LEFT);
		
		
		
		mainMenuLbl = new Label(t.getString("menu.mainMenuLbl"));
		mainMenuLbl.setPrefSize(300, 50);
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
		startGameAlert.setTitle(t.getString("menu.startGameAlert"));
		//startGameAlert.setHeaderText(t.getString("NoConnection"));
		// loginAlert.setContentText("do this or that");
		
		
		highscoreLbl = new Label(t.getString("menu.highscoreLbl"));
		highscoreLbl.setId("highscoreLbl");
		
//		highscoreListLbl = new Label(model.sendHighScoreRequest()); // sets the top five as a five line String
//		highscoreListLbl.setId("highscoreListLbl");
//		highscoreListLbl.setPrefSize(360, 130);
		
		
		TableColumn<Highscore, String> nameColumn = new TableColumn<Highscore, String>("Player");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setPrefWidth(220);
		
		TableColumn<Highscore, String> scoreColumn = new TableColumn<Highscore, String>("Score");
		scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
		scoreColumn.setPrefWidth(30);
		
		TableColumn<Highscore, String> movesColumn = new TableColumn<Highscore, String>("Moves");
		movesColumn.setCellValueFactory(new PropertyValueFactory<>("moves"));
		movesColumn.setPrefWidth(30);
		
		table = new TableView<Highscore>();
		table.setId("table");
		//table.setItems(model.sendHighScoreRequest());
		table.getColumns().addAll(nameColumn, scoreColumn, movesColumn);
		table.setPrefSize(360, 250);
		
		
		
		// nameText.setPrefSize(220.0, 30.0);
		//highscoreBox.getChildren().addAll(highscoreLbl, highscoreListLbl );
		highscoreBox.getChildren().addAll(highscoreLbl, table);
		
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
		
		structureBox.getChildren().addAll(logoLbl, typingBox, centerBox);
		
		root.getChildren().add(structureBox);
		
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("MainMenu.css").toExternalForm());
		this.stage.setScene(scene);
		stage.setFullScreen(true); // set Full Screen
		stage.setFullScreenExitHint(""); // set full screen message -> shows nothing
		updateTexts(); // switch text language if chanced over dropdown menu
		
		startGameAlert.initOwner(stage); // focus stays on full screen when alert message appears
		
		return scene;
	}
	
	public void updateTexts() {
		//startGameBtn
		Translator t = sl.getTranslator();
		languageSelectComboBox.setTooltip(new Tooltip(t.getString("program.languageTip")));
		mainMenuLbl.setText(t.getString("menu.mainMenuLbl"));
		playerLbl.setText(t.getString("menu.player")+" "+model.getClientName());
		selectModeLbl.setText(t.getString("menu.selectModeLbl"));
		singlePlayerBtn.setText(t.getString("menu.singlePlayerBtn"));
		multiPlayerBtn.setText(t.getString("menu.multiPlayerBtn"));
		highscoreLbl.setText(t.getString("menu.highscoreLbl"));
		quitBtn.setText(t.getString("menu.quitBtn"));
		startGameAlert.setTitle(t.getString("menu.startGameAlert"));
	}
	
}// end MainMenu_View