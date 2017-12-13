package Client_GameApp_MVC;

import Abstract_MVC.View;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Adrian
 * Defines the controls and elements of the GUI, aligns and styles them.
 */
public class GameApp_View extends View<GameApp_Model> {

	// Translates GUI-text
	private ServiceLocator sl;

	// Action cards area
	protected Label lblActionCards;
	protected GridPane gridpActionCards;
	protected Label lblNmbrOfCellarCards;
	protected Label lblNmbrOfMarketCards;
	protected Label lblNmbrOfRemodelCards;
	protected Label lblNmbrOfSmithyCards;
	protected Label lblNmbrOfWoodcutterCards;
	protected Label lblNmbrOfWorkshopCards;
	protected Label lblNmbrOfMineCards;
	protected Label lblNmbrOfVillageCards;

	protected VBox vboxActionCards;
	protected VBox vboxCellarCards;
	protected VBox vboxMarketCards;
	protected VBox vboxRemodelCards;
	protected VBox vboxSmithyCards;
	protected VBox vboxWoodcutterCards;
	protected VBox vboxWorkshopCards;
	protected VBox vboxMineCards;
	protected VBox vboxVillageCards;

	// Treasure cards area
	protected Label lblTreasureCards;
	protected HBox hboxTreasureCards;
	protected Label lblNmbrOfGoldCards;
	protected Label lblNmbrOfSilverCards;
	protected Label lblNmbrOfCopperCards;

	protected VBox vboxTreasureCards;
	protected VBox vboxGoldCards;
	protected VBox vboxSilverCards;
	protected VBox vboxCopperCards;

	// Victory cards area
	protected Label lblVictoryCards;
	protected HBox hboxVictoryCards;
	protected Label lblNmbrOfDuchyCards;
	protected Label lblNmbrOfEstateCards;
	protected Label lblNmbrOfProvinceCards;

	protected VBox vboxVictoryCards;
	protected VBox vboxDuchyCards;
	protected VBox vboxEstateCards;
	protected VBox vboxProvinceCards;

	// Chat area
	protected Label lblChatArea;
	protected ScrollPane scrlpChatArea;
	protected TextArea txtaChatArea;
	protected TextField txtfChatArea;
	protected Button btnSendChatArea;
	
	protected HBox hboxChatArea;
	protected VBox vboxChatArea;

	// Log area
	protected Label lblLog;
	protected ScrollPane scrlpLog;
	protected TextArea txtaLog;
	
	protected VBox vboxLog;

	// Discard area
	protected Label lblDiscard;
	protected StackPane stackpDiscard;
	
	protected VBox vboxDiscard;

	// Deck area
	protected Label lblDeck;
	protected StackPane stackpDeck;
	
	protected VBox vboxDeck;

	// Played cards area
	protected Label lblPlayedCards;
	protected ScrollPane scrlpPlayedCards;
	protected HBox hboxPlayedCards;
	
	protected VBox vboxPlayedCards;

	// Hand cards area
	protected Label lblHandCards;
	protected ScrollPane scrlpHandCards;
	protected HBox hboxHandCards;

	protected VBox vboxHandCards;
	
	// Current player area
	protected GridPane gridpCurrentPlayer;
	protected Label lblCurrentPlayer;
	protected Label lblNameOfCurrentPlayer;
	protected Label lblCurrentPhase;
	protected Label lblNameOfCurrentPhase;
	protected Label lblCrntHandCards;
	protected Label lblNmbrOfCrntHandCards;
	protected Label lblCrntDeckCards;
	protected Label lblNmbrOfCrntDeckCards;
	protected Label lblCrntDiscardCards;
	protected Label lblNmbrOfCrntDiscards;
	protected Label lblCrntActions;
	protected Label lblNmbrOfCrntActions;
	protected Label lblCrntBuys;
	protected Label lblNmbrOfCrntBuys;
	protected Label lblCrntCoins;
	protected Label lblNmbrOfCrntCoins;

	protected Button btnCommit;
	protected Button btnGiveUp;
	
	protected HBox hboxCurrentPlayer;
	protected HBox hboxCurrentPhase;
	protected HBox hboxContentCurrentPlayer;
	protected VBox vboxCurrentPlayer;
	protected VBox vboxCurrentPlayerButtons;
	
	// Root
	protected GridPane root;
	
	public GameApp_View(Stage stage, GameApp_Model model){
		super(stage, model);
	}

	// Creates the GUI with all its containers and contents
	@Override
	protected Scene create_GUI(){

		// Translates GUI-text
		sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();

		// Creates action cards area
		this.gridpActionCards = new GridPane();
		this.lblActionCards = new Label(t.getString("actions.lblActionCards")); // Action cards
		this.lblNmbrOfCellarCards = new Label();
		this.lblNmbrOfMarketCards = new Label();
		this.lblNmbrOfRemodelCards = new Label();
		this.lblNmbrOfSmithyCards = new Label();
		this.lblNmbrOfWoodcutterCards = new Label();
		this.lblNmbrOfWorkshopCards = new Label();
		this.lblNmbrOfMineCards = new Label();
		this.lblNmbrOfVillageCards = new Label();

		this.vboxCellarCards = new VBox(1, lblNmbrOfCellarCards);
		this.vboxMarketCards = new VBox(1, lblNmbrOfMarketCards);
		this.vboxRemodelCards = new VBox(1, lblNmbrOfRemodelCards);
		this.vboxSmithyCards = new VBox(1, lblNmbrOfSmithyCards);
		this.vboxWoodcutterCards = new VBox(1, lblNmbrOfWoodcutterCards);
		this.vboxWorkshopCards = new VBox(1, lblNmbrOfWorkshopCards);
		this.vboxMineCards = new VBox(1, lblNmbrOfMineCards);
		this.vboxVillageCards = new VBox(1, lblNmbrOfVillageCards);

		// Sorted by price
		gridpActionCards.add(vboxCellarCards, 0, 0);
		gridpActionCards.add(vboxWoodcutterCards, 1, 0);
		gridpActionCards.add(vboxWorkshopCards, 2, 0);
		gridpActionCards.add(vboxVillageCards, 3, 0);
		gridpActionCards.add(vboxRemodelCards, 0, 1);
		gridpActionCards.add(vboxSmithyCards, 1, 1);
		gridpActionCards.add(vboxMineCards, 2, 1);
		gridpActionCards.add(vboxMarketCards, 3, 1);
		
		this.vboxActionCards = new VBox(lblActionCards, gridpActionCards);

		// Creates treasure cards area
		this.lblTreasureCards = new Label(t.getString("treasures.lblTreasureCards")); // Treasure cards
		this.hboxTreasureCards = new HBox();
		this.lblNmbrOfGoldCards = new Label();
		this.lblNmbrOfSilverCards = new Label();
		this.lblNmbrOfCopperCards = new Label();

		this.vboxGoldCards = new VBox(1, lblNmbrOfGoldCards);
		this.vboxSilverCards = new VBox(1, lblNmbrOfSilverCards);
		this.vboxCopperCards = new VBox(1, lblNmbrOfCopperCards);

		hboxTreasureCards.getChildren().add(0, vboxCopperCards);
		hboxTreasureCards.getChildren().add(1, vboxSilverCards);
		hboxTreasureCards.getChildren().add(2, vboxGoldCards);
		this.vboxTreasureCards = new VBox(lblTreasureCards, hboxTreasureCards);

		// Creates victory cards area
		this.lblVictoryCards = new Label(t.getString("victories.lblVictoryCards")); // Victory cards
		this.hboxVictoryCards = new HBox();
		this.lblNmbrOfDuchyCards = new Label();
		this.lblNmbrOfEstateCards = new Label();
		this.lblNmbrOfProvinceCards = new Label();

		this.vboxDuchyCards = new VBox(1, lblNmbrOfDuchyCards);
		this.vboxEstateCards = new VBox(1, lblNmbrOfEstateCards);
		this.vboxProvinceCards = new VBox(1, lblNmbrOfProvinceCards);

		hboxVictoryCards.getChildren().add(0, vboxEstateCards);
		hboxVictoryCards.getChildren().add(1, vboxDuchyCards);
		hboxVictoryCards.getChildren().add(2, vboxProvinceCards);
		this.vboxVictoryCards = new VBox(lblVictoryCards, hboxVictoryCards);

		// Creates chat area
		this.lblChatArea = new Label(t.getString("chat.lblChatArea")); // Chat
		this.scrlpChatArea = new ScrollPane();
		this.txtaChatArea = new TextArea();
		this.txtfChatArea = new TextField();

		this.btnSendChatArea = new Button(t.getString("chat.btnSendChatArea")); // Send
		scrlpChatArea.setContent(txtaChatArea);
		scrlpChatArea.setMaxWidth(500);
		txtaChatArea.setEditable(false);
		this.hboxChatArea = new HBox(txtfChatArea, btnSendChatArea);
		this.vboxChatArea = new VBox(lblChatArea, scrlpChatArea, hboxChatArea);

		// Creates log area
		this.lblLog = new Label(t.getString("log.lblLog")); // Log
		this.scrlpLog = new ScrollPane();
		this.txtaLog = new TextArea();

		scrlpLog.setContent(txtaLog);
		scrlpLog.setMaxWidth(500);
		txtaLog.setEditable(false);
		this.vboxLog = new VBox(lblLog, scrlpLog);

		// Creates discard area
		this.lblDiscard = new Label(t.getString("discard.lblDiscard")); // Discard
		this.stackpDiscard = new StackPane();
		this.stackpDiscard.setMinHeight(120);

		this.vboxDiscard = new VBox(lblDiscard, stackpDiscard);

		// Creates deck area
		this.lblDeck = new Label(t.getString("deck.lblDeck")); // Deck
		this.stackpDeck = new StackPane();
		this.stackpDeck.setMinHeight(120);

		this.vboxDeck = new VBox(lblDeck, stackpDeck);

		// Creates played cards area
		this.lblPlayedCards = new Label(t.getString("played.lblPlayedCards")); // Played cards
		this.scrlpPlayedCards = new ScrollPane();
		this.hboxPlayedCards = new HBox();

		scrlpPlayedCards.setContent(hboxPlayedCards);
		this.vboxPlayedCards = new VBox(lblPlayedCards, scrlpPlayedCards);

		// Creates hand cards area
		this.lblHandCards = new Label(t.getString("hand.lblHandCards")); // Hand cards
		this.scrlpHandCards = new ScrollPane();
		this.hboxHandCards = new HBox();

		scrlpHandCards.setContent(hboxHandCards);
		this.vboxHandCards = new VBox(lblHandCards, scrlpHandCards);

		// Creates current player area
		this.lblCurrentPlayer = new Label(t.getString("current.lblCurrentPlayer")); // Current player:
		this.lblCurrentPhase = new Label(t.getString("current.lblCurrentPhase")); // Current phase:
		this.lblCrntHandCards = new Label(t.getString("current.lblCrntHandCards")); // Hand cards
		this.lblCrntDeckCards = new Label(t.getString("current.lblCrntDeckCards")); // Deck cards
		this.lblCrntDiscardCards = new Label (t.getString("current.lblCrntDiscardCards")); // Discard cards
		this.lblCrntActions = new Label(t.getString("current.lblCrntActions")); // Actions
		this.lblCrntBuys = new Label(t.getString("current.lblCrntBuys")); // Buys
		this.lblCrntCoins = new Label(t.getString("current.lblCrntCoins")); // Coins
		this.btnCommit = new Button(t.getString("current.btnSkip")); // Commit/Skip
		btnCommit.setDisable(true);
		this.btnGiveUp = new Button(t.getString("current.btnGiveUp")); // Give up
		
		this.lblNameOfCurrentPlayer = new Label();
		this.lblNameOfCurrentPhase = new Label();
		this.lblNmbrOfCrntHandCards = new Label();
		this.lblNmbrOfCrntDeckCards = new Label();
		this.lblNmbrOfCrntDiscards = new Label();
		this.lblNmbrOfCrntActions = new Label();
		this.lblNmbrOfCrntBuys = new Label();
		this.lblNmbrOfCrntCoins = new Label();

		this.gridpCurrentPlayer = new GridPane();
		gridpCurrentPlayer.add(lblCrntHandCards, 0, 0);
		gridpCurrentPlayer.add(lblNmbrOfCrntHandCards, 1, 0);
		gridpCurrentPlayer.add(lblCrntDeckCards, 0, 1);
		gridpCurrentPlayer.add(lblNmbrOfCrntDeckCards, 1, 1);
		gridpCurrentPlayer.add(lblCrntDiscardCards, 0, 2);
		gridpCurrentPlayer.add(lblNmbrOfCrntDiscards, 1, 2);
		gridpCurrentPlayer.add(lblCrntActions, 0, 3);
		gridpCurrentPlayer.add(lblNmbrOfCrntActions, 1, 3);
		gridpCurrentPlayer.add(lblCrntBuys, 0, 4);
		gridpCurrentPlayer.add(lblNmbrOfCrntBuys, 1, 4);
		gridpCurrentPlayer.add(lblCrntCoins, 0, 5);
		gridpCurrentPlayer.add(lblNmbrOfCrntCoins, 1, 5);

		this.hboxCurrentPlayer = new HBox(lblCurrentPlayer, lblNameOfCurrentPlayer);
		this.hboxCurrentPhase = new HBox(lblCurrentPhase, lblNameOfCurrentPhase);
		this.vboxCurrentPlayerButtons = new VBox (btnGiveUp, btnCommit);
		this.hboxContentCurrentPlayer = new HBox (gridpCurrentPlayer, vboxCurrentPlayerButtons);
		vboxCurrentPlayerButtons.setAlignment(Pos.BOTTOM_LEFT);
		this.vboxCurrentPlayer = new VBox(hboxCurrentPlayer, hboxCurrentPhase, hboxContentCurrentPlayer);

		// Root
		this.root = new GridPane();

		// Adds the containers to the specified location in the root
		root.add(this.vboxActionCards, 0, 0, 4, 2);
		root.add(this.vboxTreasureCards, 4, 0, 3, 1);
		root.add(this.vboxVictoryCards, 4, 1, 3, 1);
		root.add(this.vboxChatArea, 7, 0, 2, 1);
		root.add(this.vboxLog, 7, 1, 2, 1);
		root.add(this.vboxDiscard, 0, 2, 1, 1);
		root.add(this.vboxDeck, 0, 3, 1, 1);
		root.add(this.vboxPlayedCards, 1, 2, 8, 1);
		root.add(this.vboxHandCards, 1, 3, 7, 1);
		root.add(this.vboxCurrentPlayer, 8, 3, 1, 1);

		// Resizes the containers to the available size
		root.setHgrow(vboxChatArea, Priority.ALWAYS);
		root.setVgrow(vboxChatArea, Priority.ALWAYS);
		root.setHgrow(vboxLog, Priority.ALWAYS);
		root.setVgrow(vboxLog, Priority.ALWAYS);
		root.setHgrow(vboxPlayedCards, Priority.ALWAYS);
		root.setVgrow(vboxPlayedCards, Priority.ALWAYS);
		root.setHgrow(vboxHandCards, Priority.ALWAYS);
		root.setVgrow(vboxHandCards, Priority.ALWAYS);

		// Styles the elements of the GUI
		lblActionCards.getStyleClass().add("lblHeaders");
		vboxActionCards.getStyleClass().add("vbox");
		gridpActionCards.getStyleClass().add("cardGaps");
		lblActionCards.getStyleClass().add("Label");

		lblTreasureCards.getStyleClass().add("lblHeaders");
		vboxTreasureCards.getStyleClass().add("vbox");
		hboxTreasureCards.getStyleClass().add("cardGaps");
		hboxTreasureCards.getStyleClass().add("hbox");

		lblVictoryCards.getStyleClass().add("lblHeaders");
		vboxVictoryCards.getStyleClass().add("vbox");
		hboxVictoryCards.getStyleClass().add("cardGaps");
		hboxVictoryCards.getStyleClass().add("hbox");

		lblChatArea.getStyleClass().add("lblHeaders");
		vboxChatArea.getStyleClass().add("vbox");
		vboxChatArea.setPrefWidth(150);
		txtfChatArea.setMinWidth(320);
		hboxChatArea.getStyleClass().add("hbox");

		lblLog.getStyleClass().add("lblHeaders");
		vboxLog.getStyleClass().add("vbox");
		vboxLog.setPrefWidth(150);

		lblDiscard.getStyleClass().add("lblHeaders");
		vboxDiscard.getStyleClass().add("vbox");

		lblDeck.getStyleClass().add("lblHeaders");
		vboxDeck.getStyleClass().add("vbox");

		lblPlayedCards.getStyleClass().add("lblHeaders");
		vboxPlayedCards.getStyleClass().add("vbox");
		vboxPlayedCards.setPrefWidth(Double.MAX_VALUE);
		hboxPlayedCards.getStyleClass().add("hbox");

		lblHandCards.getStyleClass().add("lblHeaders");
		vboxHandCards.getStyleClass().add("vbox");
		vboxHandCards.setPrefWidth(Double.MAX_VALUE);
		hboxHandCards.getStyleClass().add("hbox");

		scrlpPlayedCards.setStyle("-fx-background-color: transparent;");
		scrlpHandCards.setStyle("-fx-background-color: transparent;");

		// Adds special styling to the current player area
		hboxCurrentPlayer.getStyleClass().add("hboxCurrentPlayer");
		hboxCurrentPhase.getStyleClass().add("hboxCurrentPlayer");
		
		hboxContentCurrentPlayer.getStyleClass().add("hbox");
		vboxCurrentPlayer.getStyleClass().add("vboxCurrentPlayer");
		vboxCurrentPlayerButtons.getStyleClass().add("vboxCurrentPlayerButtons");
		btnGiveUp.getStyleClass().add("btnGiveUp");
		btnCommit.getStyleClass().add("btnCommit");
		btnCommit.setMinHeight(40);
		gridpCurrentPlayer.getStyleClass().add("gridpCurrentPlayer");
		lblCurrentPlayer.getStyleClass().add("lblCurrentPlayer");
		lblNameOfCurrentPlayer.getStyleClass().add("lblCurrentPlayer");
		lblCurrentPhase.getStyleClass().add("lblCurrentPlayer");
		lblNameOfCurrentPhase.getStyleClass().add("lblCurrentPlayer");
		lblCrntHandCards.getStyleClass().add("lblCurrentPlayer");
		lblNmbrOfCrntHandCards.getStyleClass().add("lblCurrentPlayer");
		lblCrntDeckCards.getStyleClass().add("lblCurrentPlayer");
		lblNmbrOfCrntDeckCards.getStyleClass().add("lblCurrentPlayer");
		lblCrntDiscardCards.getStyleClass().add("lblCurrentPlayer");
		lblNmbrOfCrntDiscards.getStyleClass().add("lblCurrentPlayer");
		lblCrntActions.getStyleClass().add("lblCurrentPlayer");
		lblNmbrOfCrntActions.getStyleClass().add("lblCurrentPlayer");
		lblCrntBuys.getStyleClass().add("lblCurrentPlayer");
		lblNmbrOfCrntBuys.getStyleClass().add("lblCurrentPlayer");
		lblCrntCoins.getStyleClass().add("lblCurrentPlayer");
		lblNmbrOfCrntCoins.getStyleClass().add("lblCurrentPlayer");

		// Background and gaps of the root
		root.getStyleClass().add("rootFormat");
		
		// Scene and stage
		Scene scene = new Scene(root, 1280, 720);
		scene.getStylesheets().add(GameApp_View.class.getResource("GameApp.css").toExternalForm());
		stage.setScene(scene);
        
        // Sets stage full screen
		stage.setFullScreen(true);
		
		stage.setTitle("Dominion");
		stage.show();
		
		return scene;
	}
}//end GameApp_View