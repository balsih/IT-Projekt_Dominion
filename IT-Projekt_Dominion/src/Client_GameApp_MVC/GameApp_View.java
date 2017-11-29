package Client_GameApp_MVC;

import Abstract_MVC.View;
import Client_Services.ServiceLocator;
import Messages.GameMode_Message;
import Server_GameLogic.GameMode;
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

	private ServiceLocator sl = ServiceLocator.getServiceLocator();

	// Controls Action cards area
	protected Label lblActionCards = new Label("Action cards");
	protected GridPane gridpActionCards = new GridPane();
	protected Label lblNmbrOfCellarCards = new Label("nmbr");
	protected Label lblNmbrOfMarketCards = new Label("nmbr");
	protected Label lblNmbrOfRemodelCards = new Label("nmbr");
	protected Label lblNmbrOfSmithyCards = new Label("nmbr");
	protected Label lblNmbrOfWoodcutterCards = new Label("nmbr");
	protected Label lblNmbrOfWorkshopCards = new Label("nmbr");
	protected Label lblNmbrOfMineCards = new Label("nmbr");
	protected Label lblNmbrOfVillageCards = new Label("nmbr");

	protected VBox vboxActionCards = new VBox(lblActionCards, gridpActionCards);
	protected VBox vboxCellarCards = new VBox(1, lblNmbrOfCellarCards);
	protected VBox vboxMarketCards = new VBox(1, lblNmbrOfMarketCards);
	protected VBox vboxRemodelCards = new VBox(1, lblNmbrOfRemodelCards);
	protected VBox vboxSmithyCards = new VBox(1, lblNmbrOfSmithyCards);
	protected VBox vboxWoodcutterCards = new VBox(1, lblNmbrOfWoodcutterCards);
	protected VBox vboxWorkshopCards = new VBox(1, lblNmbrOfWorkshopCards);
	protected VBox vboxMineCards = new VBox(1, lblNmbrOfMineCards);
	protected VBox vboxVillageCards = new VBox(1, lblNmbrOfVillageCards);

	// Controls Treasure cards area
	protected Label lblTreasureCards = new Label("Treasure cards");
	protected HBox hboxTreasureCards = new HBox();
	protected Label lblNmbrOfGoldCards = new Label("nmbr");
	protected Label lblNmbrOfSilverCards = new Label("nmbr");
	protected Label lblNmbrOfCopperCards = new Label("nmbr");

	protected VBox vboxTreasureCards = new VBox(lblTreasureCards, hboxTreasureCards);
	protected VBox vboxGoldCards = new VBox(1, lblNmbrOfGoldCards);
	protected VBox vboxSilverCards = new VBox(1, lblNmbrOfSilverCards);
	protected VBox vboxCopperCards = new VBox(1, lblNmbrOfCopperCards);

	// Controls Victory cards area
	protected Label lblVictoryCards = new Label("Victory cards");
	protected HBox hboxVictoryCards = new HBox();
	protected Label lblNmbrOfDuchyCards = new Label("nmbr");
	protected Label lblNmbrOfEstateCards = new Label("nmbr");
	protected Label lblNmbrOfProvinceCards = new Label("nmbr");

	protected VBox vboxVictoryCards = new VBox(lblVictoryCards, hboxVictoryCards);
	protected VBox vboxDuchyCards = new VBox(1, lblNmbrOfDuchyCards);
	protected VBox vboxEstateCards = new VBox(1, lblNmbrOfEstateCards);
	protected VBox vboxProvinceCards = new VBox(1, lblNmbrOfProvinceCards);

	// Controls chat area
	protected Label lblChatArea = new Label("Chat area");
	protected ScrollPane scrlpChatArea = new ScrollPane();
	protected TextArea txtaChatArea = new TextArea();
	protected TextField txtfChatArea = new TextField();
	protected Button btnSendChatArea = new Button("Send");

	// Controls log area
	protected Label lblLog = new Label("Log");
	protected ScrollPane scrlpLog = new ScrollPane();
	protected TextArea txtaLog = new TextArea();

	// Controls discard area
	protected Label lblDiscard = new Label("Discard");
	protected Label lblNmbrOfDiscards = new Label("nmbr");
	protected StackPane stackpDiscard = new StackPane();

	// Controls deck area
	protected Label lblDeck = new Label("Deck");
	protected Label lblNmbrOfDeckCards = new Label("nmbr");
	protected StackPane stackpDeck = new StackPane();

	// Controls played cards area
	protected Label lblPlayedCards = new Label("Played cards");
	protected ScrollPane scrlpPlayedCards = new ScrollPane();
	protected HBox hboxPlayedCards = new HBox();

	// Controls hand cards area
	protected Label lblHandCards = new Label("Hand cards");
	protected ScrollPane scrlpHandCards = new ScrollPane();
	protected HBox hboxHandCards = new HBox();

	// Controls current player area
	protected Label lblCurrentPlayer = new Label("Current player");
	protected Label lblCurrentPhase = new Label("Current phase");
	protected Label lblCrntHandCards = new Label("Hand cards");
	protected Label lblNmbrOfCrntHandCards = new Label("0");
	protected Label lblCrntActions = new Label("Actions");
	protected Label lblNmbrOfCrntActions = new Label("0");
	protected Label lblCrntBuys = new Label("Buys");
	protected Label lblNmbrOfCrntBuys = new Label("0");
	protected Label lblCrntCoins = new Label("Coins");
	protected Label lblNmbrOfCrntCoins = new Label("0");

	protected Button btnCommit = new Button("Commit");

	public GameApp_View(Stage stage, GameApp_Model model){
		super(stage, model);
		// do something in the meantime (until GUI gets created)
	}

	@Override
	protected Scene create_GUI(){

		// Action cards area
		gridpActionCards.add(vboxCellarCards, 0, 0);
		gridpActionCards.add(vboxMarketCards, 1, 0);
		gridpActionCards.add(vboxRemodelCards, 2, 0);
		gridpActionCards.add(vboxSmithyCards, 3, 0);
		gridpActionCards.add(vboxWoodcutterCards, 0, 1);
		gridpActionCards.add(vboxWorkshopCards, 1, 1);
		gridpActionCards.add(vboxMineCards, 2, 1);
		gridpActionCards.add(vboxVillageCards, 3, 1);

		// Treasure cards area
		hboxTreasureCards.getChildren().add(0, vboxGoldCards);
		hboxTreasureCards.getChildren().add(1, vboxSilverCards);
		hboxTreasureCards.getChildren().add(2, vboxCopperCards);

		// Victory cards area
		hboxVictoryCards.getChildren().add(0, vboxDuchyCards);
		hboxVictoryCards.getChildren().add(1, vboxEstateCards);
		hboxVictoryCards.getChildren().add(2, vboxProvinceCards);

		// Chat area
		scrlpChatArea.setContent(txtaChatArea);
		txtaChatArea.setDisable(true);
		HBox hboxChatArea = new HBox(txtfChatArea, btnSendChatArea);
		VBox vboxChatArea = new VBox(lblChatArea, scrlpChatArea, hboxChatArea);

		// Log area
		scrlpLog.setContent(txtaLog);
		txtaLog.setDisable(true);
		VBox vboxLog = new VBox(lblLog, scrlpLog);

		// Discard area
		HBox hboxDiscard = new HBox(lblDiscard, lblNmbrOfDiscards);
		VBox vboxDiscard = new VBox(hboxDiscard, stackpDiscard);

		// Deck area
		HBox hboxDeck = new HBox(lblDeck, lblNmbrOfDeckCards);
		VBox vboxDeck = new VBox(hboxDeck, stackpDeck);

		// Played cards area
		scrlpPlayedCards.setContent(hboxPlayedCards);
		VBox vboxPlayedCards = new VBox(lblPlayedCards, scrlpPlayedCards);

		// Hand cards area
		scrlpHandCards.setContent(hboxHandCards);
		VBox vboxHandCards = new VBox(lblHandCards, scrlpHandCards);

		// Current player area
		GridPane gridpCurrentPlayer = new GridPane();
		gridpCurrentPlayer.add(lblCrntHandCards, 0, 0);
		gridpCurrentPlayer.add(lblNmbrOfCrntHandCards, 1, 0);
		gridpCurrentPlayer.add(lblCrntActions, 0, 1);
		gridpCurrentPlayer.add(lblNmbrOfCrntActions, 1, 1);
		gridpCurrentPlayer.add(lblCrntBuys, 0, 2);
		gridpCurrentPlayer.add(lblNmbrOfCrntBuys, 1, 2);
		gridpCurrentPlayer.add(lblCrntCoins, 0, 3);
		gridpCurrentPlayer.add(lblNmbrOfCrntCoins, 1, 3);

		HBox hboxCurrentPlayer = new HBox(lblCurrentPlayer, lblCurrentPhase);
		VBox vboxCurrentPlayer = new VBox(hboxCurrentPlayer, gridpCurrentPlayer, btnCommit);

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

		hboxDiscard.getStyleClass().add("hbox");
		vboxDiscard.getStyleClass().add("vbox");

		hboxDeck.getStyleClass().add("hbox");
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
		vboxCurrentPlayer.getStyleClass().add("vboxCurrentPlayer");
		gridpCurrentPlayer.getStyleClass().add("gridpCurrentPlayer");
		lblCrntHandCards.getStyleClass().add("lblCurrentPlayer");
		lblNmbrOfCrntHandCards.getStyleClass().add("lblCurrentPlayer");
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
		// stage.setScene(scene);
		// stage.setTitle("Dominion");
		// stage.show();

		GameMode_Message gmmsg = new GameMode_Message();
		if (gmmsg.getMode().equals(GameMode.Singleplayer)){
			txtfChatArea.setDisable(true);
		}

		return scene;
	}
}//end GameApp_View