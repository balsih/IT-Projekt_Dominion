package Client_GameApp_MVC;

import Abstract_MVC.View;
import Client_Services.ServiceLocator;
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
	protected TilePane tilepActionCards = new TilePane();

	// Controls Treasure cards area
	protected Label lblTreasureCards = new Label("Treasure cards");
	protected HBox hboxTreasureCards = new HBox(3);	

	// Controls Victory cards area
	protected Label lblVictoryCards = new Label("Victory cards");
	protected HBox hboxVictoryCards = new HBox(3);

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
	protected StackPane stackpDiscard = new StackPane();

	// Controls deck area
	protected Label lblDeck = new Label("Deck");
	protected StackPane stackpDeck = new StackPane();

	// Controls played cards area
	protected Label lblPlayedCards = new Label("Played cards");
	protected HBox hboxPlayedCards = new HBox(4);

	// Controls hand cards area
	protected Label lblHandCards = new Label("Hand cards");
	protected HBox hboxHandCards = new HBox(7);

	// Controls current player area
	protected Label lblCurrentPlayer = new Label("Current player");
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
		VBox vboxActionCards = new VBox(lblActionCards, tilepActionCards);

		// Treasure cards area
		VBox vboxTreasureCards = new VBox(lblTreasureCards, hboxTreasureCards);

		// Victory cards area
		VBox vboxVictoryCards = new VBox(lblVictoryCards, hboxVictoryCards);

		// Chat area
		txtaChatArea.setDisable(true);
		scrlpChatArea.setContent(txtaChatArea);
		HBox hboxChatArea = new HBox(txtfChatArea, btnSendChatArea);
		VBox vboxChatArea = new VBox(lblChatArea, scrlpChatArea, hboxChatArea);

		// Log area
		txtaLog.setDisable(true);
		scrlpLog.setContent(txtaLog);
		VBox vboxLog = new VBox(lblLog, scrlpLog);

		// Discard area
		VBox vboxDiscard = new VBox(lblDiscard, stackpDiscard);

		// Deck area
		VBox vboxDeck = new VBox(lblDeck, stackpDeck);

		// Played cards area
		VBox vboxPlayedCards = new VBox(lblPlayedCards, hboxPlayedCards);

		// Hand cards area
		VBox vboxHandCards = new VBox(lblHandCards, hboxHandCards);

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

		VBox vboxCurrentPlayer = new VBox(lblCurrentPlayer, gridpCurrentPlayer, btnCommit);

		// Root
		GridPane root = new GridPane();

		// Add the boxes to the specified location in the root
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
		root.setHgrow(vboxActionCards, Priority.ALWAYS);
		root.setVgrow(vboxActionCards, Priority.ALWAYS);
		root.setHgrow(vboxTreasureCards, Priority.ALWAYS);
		root.setVgrow(vboxTreasureCards, Priority.ALWAYS);
		root.setHgrow(vboxVictoryCards, Priority.ALWAYS);
		root.setVgrow(vboxVictoryCards, Priority.ALWAYS);
		root.setHgrow(vboxChatArea, Priority.ALWAYS);
		root.setVgrow(vboxChatArea, Priority.ALWAYS);
		root.setHgrow(vboxLog, Priority.ALWAYS);
		root.setVgrow(vboxLog, Priority.ALWAYS);
		root.setHgrow(vboxDiscard, Priority.ALWAYS);
		root.setVgrow(vboxDiscard, Priority.ALWAYS);
		root.setHgrow(vboxDeck, Priority.ALWAYS);
		root.setVgrow(vboxDeck, Priority.ALWAYS);
		root.setHgrow(vboxPlayedCards, Priority.ALWAYS);
		root.setVgrow(vboxPlayedCards, Priority.ALWAYS);
		root.setHgrow(vboxHandCards, Priority.ALWAYS);
		root.setVgrow(vboxHandCards, Priority.ALWAYS);
		root.setHgrow(vboxCurrentPlayer, Priority.ALWAYS);
		root.setVgrow(vboxCurrentPlayer, Priority.ALWAYS);

		// Styles the elements of the GUI
		scene.getStylesheets().add(getClass().getResource("GameApp.css").toExternalForm());

		vboxActionCards.getStyleClass().add("vbox");
		tilepActionCards.getStyleClass().add("gaps");
		lblActionCards.getStyleClass().add("Label");

		vboxTreasureCards.getStyleClass().add("vbox");
		hboxTreasureCards.getStyleClass().add("hbox");

		vboxVictoryCards.getStyleClass().add("vbox");
		hboxVictoryCards.getStyleClass().add("hbox");

		vboxChatArea.getStyleClass().add("vbox");
		hboxChatArea.getStyleClass().add("hbox");

		vboxLog.getStyleClass().add("vbox");

		vboxDiscard.getStyleClass().add("vbox");

		vboxDeck.getStyleClass().add("vbox");

		vboxPlayedCards.getStyleClass().add("vbox");
		hboxPlayedCards.getStyleClass().add("hbox");

		vboxHandCards.getStyleClass().add("vbox");
		hboxHandCards.getStyleClass().add("hbox");

		vboxCurrentPlayer.getStyleClass().add("vboxCurrentPlayer");
		gridpCurrentPlayer.setHgap(20);

		root.getStyleClass().add("root_format");

		Scene scene = new Scene(root, 1000, 600);
		// stage.setScene(scene);
		// stage.setTitle("Dominion");

		return scene;
	}
}//end GameApp_View