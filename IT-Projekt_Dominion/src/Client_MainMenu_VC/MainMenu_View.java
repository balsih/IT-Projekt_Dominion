package Client_MainMenu_VC;

import java.util.Locale;
import Abstract_MVC.View;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.Highscore;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * View class for MainMenu screen 
 * Defines controls and elements of this GUI, aligns and styles them.
 * 
 * @author Rene Schwab
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
	protected TableView<Highscore> table;
	protected TableColumn<Highscore, String> nameColumn, pointColumn, roundColumn;
	
	protected Button quitBtn;
	protected Button logoutBtn;
	
	protected Alert startGameAlert;
	

	public MainMenu_View(Stage stage, GameApp_Model model) {
		super(stage, model);
	}
	
	@Override
	protected Scene create_GUI() {
		
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
		

		// header with Dominion and Keep typing logo
		Label logoLbl = new Label();
		logoLbl.setId("logoLbl");
		logoLbl.setPrefSize(410.0, 150.0);
		logoLbl.setMinSize(380.0, 0.0);
		logoLbl.setAlignment(Pos.CENTER);

		Image image = new Image(getClass().getResourceAsStream("Logo.png"));
		ImageView imageView = new ImageView(image);
		logoLbl.setGraphic(imageView);
		imageView.setScaleX(1.1);
		imageView.setScaleY(1.1);

		Label keepTypingLbl = new Label();
		keepTypingLbl.setId("keepTypingLbl");
		keepTypingLbl.setPrefSize(40.0, 20.0);
		keepTypingLbl.setMinSize(40.0, 20.0);
		keepTypingLbl.setAlignment(Pos.CENTER_LEFT);

		Image image2 = new Image(getClass().getResourceAsStream("KeepTyping.png"));
		ImageView imageView2 = new ImageView(image2);
		keepTypingLbl.setGraphic(imageView2);

		Label space = new Label();
		HBox typingBox = new HBox();
		typingBox.setId("typingBox");
		typingBox.getChildren().addAll(space, keepTypingLbl);

		
		// Shows the name of the actual player
		playerLbl = new Label(t.getString("menu.player")+" "+model.getClientName());
		playerLbl.setId("playerLbl");
		playerLbl.setPrefSize(295, 15);
		playerLbl.setAlignment(Pos.BOTTOM_LEFT);
		
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
		// Sets the current locale language (de, en, ...) as value of the ComboBox 
		languageSelectComboBox = new ComboBox<String>(lang);
		languageSelectComboBox.setValue(lang.get(currentIndex)); 
		languageSelectComboBox.setId("languageSelectComboBox");
		
		languageSelectComboBox.setTooltip(new Tooltip(t.getString("program.languageTip")));
		HBox playerAndLanguageBox = new HBox(playerLbl, languageSelectComboBox);
		
		//playerAndLanguageBox.setPrefSize(360, 40);
		playerAndLanguageBox.setId("playerAndLanguageBox");
		playerAndLanguageBox.setAlignment(Pos.CENTER_LEFT);
		
		
		// Label mainMenuLbl
		mainMenuLbl = new Label(t.getString("menu.mainMenuLbl"));
		mainMenuLbl.setId("mainMenuLbl");
		
		// Select game mode over buttons
		selectModeLbl = new Label(t.getString("menu.selectModeLbl"));
		selectModeLbl.setId("selectModeLbl");
		singlePlayerBtn = new Button(t.getString("menu.singlePlayerBtn"));
		singlePlayerBtn.setId("singlePlayerBtn");
		multiPlayerBtn = new Button(t.getString("menu.multiPlayerBtn"));
		multiPlayerBtn.setId("multiPlayerBtn");
		HBox singleAndMultiplayerBox = new HBox(singlePlayerBtn, multiPlayerBtn);
		singleAndMultiplayerBox.setId("singleAndMultiplayerBox");
		gameModeBox.getChildren().addAll(selectModeLbl, singleAndMultiplayerBox);
		
		// warning message, if start game (single- or multiplayer) fails
		startGameAlert = new Alert(AlertType.WARNING);
		startGameAlert.setTitle(t.getString("menu.startGameAlert"));
		
		// Table view showing the top 5 players 
		// Input is a AbservableList filled in the GameAp_Model with data from the DB 
		highscoreLbl = new Label(t.getString("menu.highscoreLbl"));
		highscoreLbl.setId("highscoreLbl");
		
		nameColumn = new TableColumn<Highscore, String>(t.getString("menu.table.player"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setPrefWidth(240);
		
		pointColumn = new TableColumn<Highscore, String>(t.getString("menu.table.points"));
		pointColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
		pointColumn.setPrefWidth(55);
		pointColumn.setId("pointColumn");
		
		roundColumn = new TableColumn<Highscore, String>(t.getString("menu.table.rounds"));
		roundColumn.setCellValueFactory(new PropertyValueFactory<>("moves"));
		roundColumn.setPrefWidth(55);
		roundColumn.setId("roundColumn");
		
		table = new TableView<Highscore>();
		table.setId("table");
		table.setItems(model.sendHighScoreRequest());
		table.getColumns().addAll(nameColumn, roundColumn, pointColumn);
		table.setPrefSize(340, 180);
		table.setPlaceholder(new Label(""));
		highscoreBox.getChildren().addAll(highscoreLbl, table);
		
		
		// buttons
		quitBtn = new Button(t.getString("menu.quitBtn"));
		quitBtn.setId("quitBtn");
		logoutBtn = new Button(t.getString("menu.logoutBtn"));
		logoutBtn.setId("logoutBtn");
		HBox startAndQuitBox = new HBox(logoutBtn, quitBtn);
		startAndQuitBox.setId("startAndQuitBox");
		
		// VBox for layout and spacing
		VBox gameModeAndHigscoreBox = new VBox();
		gameModeAndHigscoreBox.setId("gameModeAndHigscoreBox");
		gameModeAndHigscoreBox.getChildren().addAll(gameModeBox, highscoreBox);

		
		// Layout and size configurations
		root.setPrefSize(1280, 720);
		root.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(playerAndLanguageBox, mainMenuLbl, gameModeAndHigscoreBox, startAndQuitBox );
		
		structureBox.getChildren().addAll(logoLbl, typingBox, centerBox);
		root.getChildren().add(structureBox);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("MainMenu.css").toExternalForm());
		this.stage.setScene(scene);
		stage.setFullScreen(true); // set Full Screen
		stage.setFullScreenExitHint(""); // set full screen message -> shows nothing
		
		startGameAlert.initOwner(stage); // focus stays on full screen when alert message appears
		
		return scene;
	}
	
	// Switch text language if chanced over dropdown menu from ComboBox
	public void updateTexts() {
		Translator t = sl.getTranslator();
		languageSelectComboBox.setTooltip(new Tooltip(t.getString("program.languageTip")));
		mainMenuLbl.setText(t.getString("menu.mainMenuLbl"));
		playerLbl.setText(t.getString("menu.player")+" "+model.getClientName());
		selectModeLbl.setText(t.getString("menu.selectModeLbl"));
		singlePlayerBtn.setText(t.getString("menu.singlePlayerBtn"));
		multiPlayerBtn.setText(t.getString("menu.multiPlayerBtn"));
		highscoreLbl.setText(t.getString("menu.highscoreLbl"));
		nameColumn.setText(t.getString("menu.table.player"));
		pointColumn.setText(t.getString("menu.table.points"));
		roundColumn.setText(t.getString("menu.table.rounds"));
		quitBtn.setText(t.getString("menu.quitBtn"));
		logoutBtn.setText(t.getString("menu.logoutBtn"));
		startGameAlert.setTitle(t.getString("menu.startGameAlert"));
	}
	
}// end MainMenu_View