package Client_GameApp_MVC;

import Abstract_MVC.View;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
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

	// Controls action cards area
	protected Label lblActionCards;
	protected GridPane gridpActionCards;
	protected Label lblNmbrOfCellarCards;
	protected Label lblNmbrOfMarketCards = new Label();
	protected Label lblNmbrOfRemodelCards = new Label();
	protected Label lblNmbrOfSmithyCards = new Label();
	protected Label lblNmbrOfWoodcutterCards = new Label();
	protected Label lblNmbrOfWorkshopCards = new Label();
	protected Label lblNmbrOfMineCards = new Label();
	protected Label lblNmbrOfVillageCards = new Label();

	protected VBox vboxActionCards = new VBox(lblActionCards, gridpActionCards);
	protected VBox vboxCellarCards;
	protected VBox vboxMarketCards = new VBox(1, lblNmbrOfMarketCards);
	protected VBox vboxRemodelCards = new VBox(1, lblNmbrOfRemodelCards);
	protected VBox vboxSmithyCards = new VBox(1, lblNmbrOfSmithyCards);
	protected VBox vboxWoodcutterCards = new VBox(1, lblNmbrOfWoodcutterCards);
	protected VBox vboxWorkshopCards = new VBox(1, lblNmbrOfWorkshopCards);
	protected VBox vboxMineCards = new VBox(1, lblNmbrOfMineCards);
	protected VBox vboxVillageCards = new VBox(1, lblNmbrOfVillageCards);

	// Controls treasure cards area
	protected Label lblTreasureCards;
	protected HBox hboxTreasureCards = new HBox();
	protected Label lblNmbrOfGoldCards = new Label();
	protected Label lblNmbrOfSilverCards = new Label();
	protected Label lblNmbrOfCopperCards = new Label();

	protected VBox vboxTreasureCards = new VBox(lblTreasureCards, hboxTreasureCards);
	protected VBox vboxGoldCards = new VBox(1, lblNmbrOfGoldCards);
	protected VBox vboxSilverCards = new VBox(1, lblNmbrOfSilverCards);
	protected VBox vboxCopperCards = new VBox(1, lblNmbrOfCopperCards);

	// Controls victory cards area
	protected Label lblVictoryCards;
	protected HBox hboxVictoryCards = new HBox();
	protected Label lblNmbrOfDuchyCards = new Label();
	protected Label lblNmbrOfEstateCards = new Label();
	protected Label lblNmbrOfProvinceCards = new Label();

	protected VBox vboxVictoryCards = new VBox(lblVictoryCards, hboxVictoryCards);
	protected VBox vboxDuchyCards = new VBox(1, lblNmbrOfDuchyCards);
	protected VBox vboxEstateCards = new VBox(1, lblNmbrOfEstateCards);
	protected VBox vboxProvinceCards = new VBox(1, lblNmbrOfProvinceCards);

	// Controls chat area
	protected Label lblChatArea;
	protected ScrollPane scrlpChatArea = new ScrollPane();
	protected TextArea txtaChatArea = new TextArea();
	protected TextField txtfChatArea = new TextField();
	protected Button btnSendChatArea;

	// Controls log area
	protected Label lblLog;
	protected ScrollPane scrlpLog = new ScrollPane();
	protected TextArea txtaLog = new TextArea();

	// Controls discard area
	protected Label lblDiscard;
	protected StackPane stackpDiscard = new StackPane();

	// Controls deck area
	protected Label lblDeck;
	protected StackPane stackpDeck = new StackPane();

	// Controls played cards area
	protected Label lblPlayedCards;
	protected ScrollPane scrlpPlayedCards = new ScrollPane();
	protected HBox hboxPlayedCards = new HBox();

	// Controls hand cards area
	protected Label lblHandCards;
	protected ScrollPane scrlpHandCards = new ScrollPane();
	protected HBox hboxHandCards = new HBox();

	// Controls current player area
	protected Label lblCurrentPlayer;
	protected Label lblNameOfCurrentPlayer = new Label();
	protected Label lblCurrentPhase = new Label();
	protected Label lblCrntHandCards;
	protected Label lblNmbrOfCrntHandCards = new Label();
	protected Label lblCrntDeckCards;
	protected Label lblNmbrOfCrntDeckCards = new Label();
	protected Label lblCrntDiscardCards;
	protected Label lblNmbrOfCrntDiscards = new Label();
	protected Label lblCrntActions;
	protected Label lblNmbrOfCrntActions = new Label();
	protected Label lblCrntBuys;
	protected Label lblNmbrOfCrntBuys = new Label();
	protected Label lblCrntCoins;
	protected Label lblNmbrOfCrntCoins = new Label();

	protected Button btnCommit;

	public GameApp_View(Stage stage, GameApp_Model model){
		super(stage, model);
		// do something in the meantime (until GUI gets created)
	}

	@Override
	protected Scene create_GUI(){
		
		this.lblNmbrOfCellarCards = new Label();
		this.vboxCellarCards = new VBox(1, lblNmbrOfCellarCards);
		this.gridpActionCards  = new GridPane();
		
		// Translates GUI-text
		sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();

		// Action cards area
		lblActionCards = new Label(t.getString("actions.lblActionCards")); // Action cards
		gridpActionCards.add(vboxCellarCards, 0, 0);
		gridpActionCards.add(vboxMarketCards, 1, 0);
		gridpActionCards.add(vboxRemodelCards, 2, 0);
		gridpActionCards.add(vboxSmithyCards, 3, 0);
		gridpActionCards.add(vboxWoodcutterCards, 0, 1);
		gridpActionCards.add(vboxWorkshopCards, 1, 1);
		gridpActionCards.add(vboxMineCards, 2, 1);
		gridpActionCards.add(vboxVillageCards, 3, 1);

		// Treasure cards area
		lblTreasureCards = new Label(t.getString("treasures.lblTreasureCards")); // Treasure cards
		hboxTreasureCards.getChildren().add(0, vboxGoldCards);
		hboxTreasureCards.getChildren().add(1, vboxSilverCards);
		hboxTreasureCards.getChildren().add(2, vboxCopperCards);

		// Victory cards area
		lblVictoryCards = new Label(t.getString("victories.lblVictoryCards")); // Victory cards
		hboxVictoryCards.getChildren().add(0, vboxDuchyCards);
		hboxVictoryCards.getChildren().add(1, vboxEstateCards);
		hboxVictoryCards.getChildren().add(2, vboxProvinceCards);

		// Chat area
		lblChatArea = new Label(t.getString("chat.lblChatArea")); // Chat
		btnSendChatArea = new Button(t.getString("chat.btnSendChatArea")); // Send
		scrlpChatArea.setContent(txtaChatArea);
		txtaChatArea.setDisable(true);
		HBox hboxChatArea = new HBox(txtfChatArea, btnSendChatArea);
		VBox vboxChatArea = new VBox(lblChatArea, scrlpChatArea, hboxChatArea);

		// Log area
		lblLog = new Label(t.getString("log.lblLog")); // Log
		scrlpLog.setContent(txtaLog);
		txtaLog.setDisable(true);
		VBox vboxLog = new VBox(lblLog, scrlpLog);

		// Discard area
		lblDiscard = new Label(t.getString("discard.lblDiscard")); // Discard
		VBox vboxDiscard = new VBox(lblDiscard, stackpDiscard);

		// Deck area
		lblDeck = new Label(t.getString("deck.lblDeck")); // Deck
		VBox vboxDeck = new VBox(lblDeck, stackpDeck);

		// Played cards area
		lblPlayedCards = new Label(t.getString("played.lblPlayedCards")); // Played cards
		scrlpPlayedCards.setContent(hboxPlayedCards);
		VBox vboxPlayedCards = new VBox(lblPlayedCards, scrlpPlayedCards);

		// Hand cards area
		lblHandCards = new Label(t.getString("hand.lblHandCards")); // Hand cards
		scrlpHandCards.setContent(hboxHandCards);
		VBox vboxHandCards = new VBox(lblHandCards, scrlpHandCards);

		// Current player area
		lblCurrentPlayer = new Label(t.getString("current.lblCurrentPlayer")); // Current player:
		lblCrntHandCards = new Label(t.getString("current.lblCrntHandCards")); // Hand cards
		lblCrntDeckCards = new Label(t.getString("current.lblCrntDeckCards")); // Deck cards
		lblCrntDiscardCards = new Label (t.getString("current.lblCrntDiscardCards")); // Discard cards
		lblCrntActions = new Label(t.getString("current.lblCrntActions")); // Actions
		lblCrntBuys = new Label(t.getString("current.lblCrntBuys")); // Buys
		lblCrntCoins = new Label(t.getString("current.lblCrntCoins")); // Coins
		btnCommit = new Button(t.getString("current.btnCommit")); // Commit
		
		GridPane gridpCurrentPlayer = new GridPane();
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

		HBox hboxCurrentPlayer = new HBox(lblCurrentPlayer, lblNameOfCurrentPlayer, lblCurrentPhase);
		HBox hboxContentCurrentPlayer = new HBox (gridpCurrentPlayer, btnCommit);
		hboxContentCurrentPlayer.setAlignment(Pos.BOTTOM_LEFT);
		VBox vboxCurrentPlayer = new VBox(hboxCurrentPlayer, hboxContentCurrentPlayer);

		// Root
		GridPane root = new GridPane();

		// Add the containers to the specified location in the root
		root.add(vboxActionCards, 0, 0, 4, 2);
		root.add(vboxTreasureCards, 4, 0, 3, 1);
		root.add(vboxVictoryCards, 4, 1, 3, 1);
		root.add(vboxChatArea, 7, 0, 2, 1);
		root.add(vboxLog, 7, 1, 2, 1);
		root.add(vboxDiscard, 0, 2, 1, 1);
		root.add(vboxDeck, 0, 3, 1, 1);
		root.add(vboxPlayedCards, 1, 2, 8, 1);
		root.add(vboxHandCards, 1, 3, 7, 1);
		root.add(vboxCurrentPlayer, 8, 3, 1, 1);

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
		scene.getStylesheets().add(getClass().getResource("GameApp.css").toExternalForm());

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

		// Special styling for Current player area
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

		Scene scene = new Scene(root, 1000, 600);
		// Prevent resizing below initial size
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
		stage.setScene(scene);
		stage.setTitle("Dominion");
		stage.show();

		return scene;
	}
}//end GameApp_View