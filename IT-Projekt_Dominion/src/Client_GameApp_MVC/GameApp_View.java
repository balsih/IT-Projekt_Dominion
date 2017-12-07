package Client_GameApp_MVC;

import Abstract_MVC.View;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import MainClasses.Dominion_Main;
import Messages.GameMode_Message;
import Server_GameLogic.GameMode;
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
import javafx.scene.layout.TilePane;
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
	
	protected HBox hboxCurrentPlayer;
	protected HBox hboxContentCurrentPlayer;
	protected VBox vboxCurrentPlayer;
	
	// Root
	protected GridPane root;
	
	public GameApp_View(Stage stage, GameApp_Model model){
		super(stage, model);
		// do something in the meantime (until GUI gets created)
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

		gridpActionCards.add(vboxCellarCards, 0, 0);
		gridpActionCards.add(vboxMarketCards, 1, 0);
		gridpActionCards.add(vboxRemodelCards, 2, 0);
		gridpActionCards.add(vboxSmithyCards, 3, 0);
		gridpActionCards.add(vboxWoodcutterCards, 0, 1);
		gridpActionCards.add(vboxWorkshopCards, 1, 1);
		gridpActionCards.add(vboxMineCards, 2, 1);
		gridpActionCards.add(vboxVillageCards, 3, 1);
		
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

		hboxTreasureCards.getChildren().add(0, vboxGoldCards);
		hboxTreasureCards.getChildren().add(1, vboxSilverCards);
		hboxTreasureCards.getChildren().add(2, vboxCopperCards);
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

		hboxVictoryCards.getChildren().add(0, vboxDuchyCards);
		hboxVictoryCards.getChildren().add(1, vboxEstateCards);
		hboxVictoryCards.getChildren().add(2, vboxProvinceCards);
		this.vboxVictoryCards = new VBox(lblVictoryCards, hboxVictoryCards);

		// Creates chat area
		this.lblChatArea = new Label(t.getString("chat.lblChatArea")); // Chat
		this.scrlpChatArea = new ScrollPane();
		this.txtaChatArea = new TextArea();
		this.txtfChatArea = new TextField();

		this.btnSendChatArea = new Button(t.getString("chat.btnSendChatArea")); // Send
		scrlpChatArea.setContent(txtaChatArea);
		txtaChatArea.setDisable(true);
		this.hboxChatArea = new HBox(txtfChatArea, btnSendChatArea);
		this.vboxChatArea = new VBox(lblChatArea, scrlpChatArea, hboxChatArea);

		// Creates log area
		this.lblLog = new Label(t.getString("log.lblLog")); // Log
		this.scrlpLog = new ScrollPane();
		this.txtaLog = new TextArea();

		scrlpLog.setContent(txtaLog);
		txtaLog.setDisable(true);
		this.vboxLog = new VBox(lblLog, scrlpLog);

		// Creates discard area
		this.lblDiscard = new Label(t.getString("discard.lblDiscard")); // Discard
		this.stackpDiscard = new StackPane();

		this.vboxDiscard = new VBox(lblDiscard, stackpDiscard);

		// Creates deck area
		this.lblDeck = new Label(t.getString("deck.lblDeck")); // Deck
		this.stackpDeck = new StackPane();

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
		this.lblCrntHandCards = new Label(t.getString("current.lblCrntHandCards")); // Hand cards
		this.lblCrntDeckCards = new Label(t.getString("current.lblCrntDeckCards")); // Deck cards
		this.lblCrntDiscardCards = new Label (t.getString("current.lblCrntDiscardCards")); // Discard cards
		this.lblCrntActions = new Label(t.getString("current.lblCrntActions")); // Actions
		this.lblCrntBuys = new Label(t.getString("current.lblCrntBuys")); // Buys
		this.lblCrntCoins = new Label(t.getString("current.lblCrntCoins")); // Coins
		this.btnCommit = new Button(t.getString("current.btnCommit")); // Commit
		
		this.lblNameOfCurrentPlayer = new Label();
		this.lblCurrentPhase = new Label();
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

		this.hboxCurrentPlayer = new HBox(lblCurrentPlayer, lblNameOfCurrentPlayer, lblCurrentPhase);
		this.hboxContentCurrentPlayer = new HBox (gridpCurrentPlayer, btnCommit);
		hboxContentCurrentPlayer.setAlignment(Pos.BOTTOM_LEFT);
		this.vboxCurrentPlayer = new VBox(hboxCurrentPlayer, hboxContentCurrentPlayer);

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
		vboxActionCards.getStyleClass().add("vbox");
		gridpActionCards.getStyleClass().add("cardGaps");
		lblActionCards.getStyleClass().add("Label");

		vboxTreasureCards.getStyleClass().add("vbox");
		hboxTreasureCards.getStyleClass().add("cardGaps");
		hboxTreasureCards.getStyleClass().add("hbox");

		vboxVictoryCards.getStyleClass().add("vbox");
		hboxVictoryCards.getStyleClass().add("cardGaps");
		hboxVictoryCards.getStyleClass().add("hbox");

		vboxChatArea.getStyleClass().add("vbox");
		vboxChatArea.setPrefWidth(150);
		hboxChatArea.getStyleClass().add("hbox");

		vboxLog.getStyleClass().add("vbox");
		vboxLog.setPrefWidth(150);

		vboxDiscard.getStyleClass().add("vbox");

		vboxDeck.getStyleClass().add("vbox");

		vboxPlayedCards.getStyleClass().add("vbox");
		vboxPlayedCards.setPrefWidth(Double.MAX_VALUE);
		hboxPlayedCards.getStyleClass().add("hbox");

		vboxHandCards.getStyleClass().add("vbox");
		vboxHandCards.setPrefWidth(Double.MAX_VALUE);
		hboxHandCards.getStyleClass().add("hbox");

		scrlpPlayedCards.setStyle("-fx-background-color: transparent;");
		scrlpHandCards.setStyle("-fx-background-color: transparent;");

		// Adds special styling to the current player area
		hboxCurrentPlayer.getStyleClass().add("hbox");
		hboxContentCurrentPlayer.getStyleClass().add("hbox");
		vboxCurrentPlayer.getStyleClass().add("vboxCurrentPlayer");
		gridpCurrentPlayer.getStyleClass().add("gridpCurrentPlayer");
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
		Scene scene = new Scene(root, 1000, 600);
		scene.getStylesheets().add(GameApp_View.class.getResource("GameApp.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Dominion");
		stage.show();
		
		// Prevents resizing below initial size
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());

		return scene;
	}
}//end GameApp_View