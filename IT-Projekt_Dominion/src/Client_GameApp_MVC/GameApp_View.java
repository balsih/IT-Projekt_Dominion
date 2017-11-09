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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Adrian
 * @version 1.0
 * @created 31-Okt-2017 17:04:43
 */
public class GameApp_View extends View<GameApp_Model> {

	private ServiceLocator sl = ServiceLocator.getServiceLocator();

	// Controls Action cards area
	Label lblActionCards = new Label("Action cards");
	GridPane gridpActionCards = new GridPane();
	
	// Controls Treasure cards area
	Label lblTreasureCards = new Label("Treasure cards");
	HBox hboxTreasureCards = new HBox(3);	
	
	// Controls Victory cards area
	Label lblVictoryCards = new Label("Victory cards");
	HBox hboxVictoryCards = new HBox(3);
	
	// Controls chat area
	Label lblChatArea = new Label("Chat area");
	ScrollPane scrlpChatArea = new ScrollPane();
	TextArea txtaChatArea = new TextArea();
	TextField txtfChatArea = new TextField();
	Button btnSendChatArea = new Button("Send");
	
	// Controls log area
	Label lblLog = new Label("Log");
	ScrollPane scrlpLog = new ScrollPane();
	TextArea txtaLog = new TextArea();
	
	// Controls discard area
	Label lblDiscard = new Label("Discard");
	StackPane stackpDiscard = new StackPane();
	
	// Controls deck area
	Label lblDeck = new Label("Deck");
	StackPane stackpDeck = new StackPane();
	
	// Controls played cards area
	Label lblPlayedCards = new Label("Played cards");
	HBox hboxPlayedCards = new HBox(4);
	
	// Controls hand cards area
	Label lblHandCards = new Label("Hand cards");
	HBox hboxHandCards = new HBox(7);
	
	// Controls current player area
	Label lblCurrentPlayer = new Label("Current player");
	Label lblCrntHandCards = new Label("Hand cards");
	Label lblNmbrOfCrntHandCards = new Label("0");
	Label lblCrntActions = new Label("Actions");
	Label lblNmbrOfCrntActions = new Label("0");
	Label lblCrntBuys = new Label("Buys");
	Label lblNmbrOfCrntBuys = new Label("0");
	Label lblCrntCoins = new Label("Coins");
	Label lblNmbrOfCrntCoins = new Label("0");
	
	// Controls commit area
	Button btnCommit = new Button("Commit");
	
	public GameApp_View(Stage stage, GameApp_Model model){
		super(stage, model);
		
		// Action cards area
		VBox vboxActionCards = new VBox(lblActionCards, gridpActionCards);
		
		// Treasure cards area
		VBox vboxTreasureCards = new VBox(lblTreasureCards, hboxTreasureCards);
		
		// Victory cards area
		VBox vboxVictoryCards = new VBox(lblVictoryCards, hboxVictoryCards);
		
		// Chat area
		scrlpChatArea.setContent(txtaChatArea);
		HBox hboxChatArea = new HBox(txtfChatArea, btnSendChatArea);
		VBox vboxChatArea = new VBox(lblChatArea, scrlpChatArea, hboxChatArea);
		
		// Log area
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
		
	}

	@Override
	protected Scene create_GUI(){
		return null;
	}
}//end GameApp_View