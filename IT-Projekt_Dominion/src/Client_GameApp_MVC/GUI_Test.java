package Client_GameApp_MVC;

import java.util.LinkedList;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * @author Adrian
 * Defines the controls and elements of the GUI, aligns and styles them.
 */
public class GUI_Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}
		
	@Override
	public void start(Stage stage) {

		// Controls Action cards area
		Label lblActionCards = new Label("Action cards");
		GridPane gridpActionCards = new GridPane();
		Label lblNmbrOfCellarCards = new Label("nmbr");
		Label lblNmbrOfMarketCards = new Label("nmbr");
		Label lblNmbrOfRemodelCards = new Label("nmbr");
		Label lblNmbrOfSmithyCards = new Label("nmbr");
		Label lblNmbrOfWoodcutterCards = new Label("nmbr");
		Label lblNmbrOfWorkshopCards = new Label("nmbr");
		Label lblNmbrOfMineCards = new Label("nmbr");
		Label lblNmbrOfVillageCards = new Label("nmbr");
		
		// Controls Treasure cards area
		Label lblTreasureCards = new Label("Treasure cards");
		HBox hboxTreasureCards = new HBox();
		Label lblNmbrOfGoldCards = new Label("nmbr");
		Label lblNmbrOfSilverCards = new Label("nmbr");
		Label lblNmbrOfCopperCards = new Label("nmbr");

		// Controls Victory cards area
		Label lblVictoryCards = new Label("Victory cards");
		HBox hboxVictoryCards = new HBox();
		Label lblNmbrOfDuchyCards = new Label("nmbr");
		Label lblNmbrOfEstateCards = new Label("nmbr");
		Label lblNmbrOfProvinceCards = new Label("nmbr");

		// Controls chat area
		Label lblChatArea = new Label("Chat");
		ScrollPane scrlpChatArea = new ScrollPane();
		TextArea txtaChatArea = new TextArea();
		txtaChatArea.setDisable(true);
		TextField txtfChatArea = new TextField();
		Button btnSendChatArea = new Button("Send");

		// Controls log area
		Label lblLog = new Label("Log");
		ScrollPane scrlpLog = new ScrollPane();
		TextArea txtaLog = new TextArea();
		txtaLog.setDisable(true);

		// Controls discard area
		Label lblDiscard = new Label("Discard");
		Label lblNmbrOfDiscards = new Label("nmbr");
		StackPane stackpDiscard = new StackPane();

		// Controls deck area
		Label lblDeck = new Label("Deck");
		Label lblNmbrOfDeckCards = new Label("nmbr");
		StackPane stackpDeck = new StackPane();

		// Controls played cards area
		Label lblPlayedCards = new Label("Played cards");
		ScrollPane scrlpPlayedCards = new ScrollPane();
		HBox hboxPlayedCards = new HBox();

		// Controls hand cards area
		Label lblHandCards = new Label("Hand cards");
		ScrollPane scrlpHandCards = new ScrollPane();
		HBox hboxHandCards = new HBox();

		// Controls current player area
		Label lblCurrentPlayer = new Label("Current player:");
		Label lblNameOfCurrentPlayer = new Label("Thomas");  // Name
		Label lblCurrentPhase = new Label("Current phase");
		Label lblCrntHandCards = new Label("Hand cards (current player)");
		Label lblNmbrOfCrntHandCards = new Label("0");
		Label lblCrntActions = new Label("Actions (current player)");
		Label lblNmbrOfCrntActions = new Label("0");
		Label lblCrntBuys = new Label("Buys (current player)");
		Label lblNmbrOfCrntBuys = new Label("0");
		Label lblCrntCoins = new Label("Coins (current player)");
		Label lblNmbrOfCrntCoins = new Label("0");

		Button btnCommit = new Button("Commit");

		// Action cards area
		VBox vboxActionCards = new VBox(lblActionCards, gridpActionCards);
		VBox vboxCellarCards = new VBox(1, lblNmbrOfCellarCards);
		VBox vboxMarketCards = new VBox(1, lblNmbrOfMarketCards);
		VBox vboxRemodelCards = new VBox(1, lblNmbrOfRemodelCards);
		VBox vboxSmithyCards = new VBox(1, lblNmbrOfSmithyCards);
		VBox vboxWoodcutterCards = new VBox(1, lblNmbrOfWoodcutterCards);
		VBox vboxWorkshopCards = new VBox(1, lblNmbrOfWorkshopCards);
		VBox vboxMineCards = new VBox(1, lblNmbrOfMineCards);
		VBox vboxVillageCards = new VBox(1, lblNmbrOfVillageCards);
		
		gridpActionCards.add(vboxCellarCards, 0, 0);
		gridpActionCards.add(vboxMarketCards, 1, 0);
		gridpActionCards.add(vboxRemodelCards, 2, 0);
		gridpActionCards.add(vboxSmithyCards, 3, 0);
		gridpActionCards.add(vboxWoodcutterCards, 0, 1);
		gridpActionCards.add(vboxWorkshopCards, 1, 1);
		gridpActionCards.add(vboxMineCards, 2, 1);
		gridpActionCards.add(vboxVillageCards, 3, 1);

		// Treasure cards area
		VBox vboxTreasureCards = new VBox(lblTreasureCards, hboxTreasureCards);
		VBox vboxGoldCards = new VBox(1, lblNmbrOfGoldCards);
		VBox vboxSilverCards = new VBox(1, lblNmbrOfSilverCards);
		VBox vboxCopperCards = new VBox(1, lblNmbrOfCopperCards);
		
		hboxTreasureCards.getChildren().add(0, vboxGoldCards);
		hboxTreasureCards.getChildren().add(1, vboxSilverCards);
		hboxTreasureCards.getChildren().add(2, vboxCopperCards);

		// Victory cards area
		VBox vboxVictoryCards = new VBox(lblVictoryCards, hboxVictoryCards);
		VBox vboxDuchyCards = new VBox(1, lblNmbrOfDuchyCards);
		VBox vboxEstateCards = new VBox(1, lblNmbrOfEstateCards);
		VBox vboxProvinceCards = new VBox(1, lblNmbrOfProvinceCards);
		
		hboxVictoryCards.getChildren().add(0, vboxDuchyCards);
		hboxVictoryCards.getChildren().add(1, vboxEstateCards);
		hboxVictoryCards.getChildren().add(2, vboxProvinceCards);

		// Chat area
		scrlpChatArea.setContent(txtaChatArea);
		HBox hboxChatArea = new HBox(txtfChatArea, btnSendChatArea);
		VBox vboxChatArea = new VBox(lblChatArea, scrlpChatArea, hboxChatArea);
		
		// Log area
		scrlpLog.setContent(txtaLog);
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

		HBox hboxCurrentPlayer = new HBox(lblCurrentPlayer, lblNameOfCurrentPlayer, lblCurrentPhase);
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

		// Scene and stage
		Scene scene = new Scene(root, 1000, 600);
		stage.setScene(scene);
		stage.setTitle("Dominion");
		stage.show();
		
		// Prevent resizing below initial size
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());

		// Styles the elements of the GUI
		scene.getStylesheets().add(getClass().getResource("GUI_Test.css").toExternalForm());

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

		// Test: get example cards
		ImageView img1 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img2 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img3 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img4 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img5 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img6 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img7 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img8 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img9 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img10 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img11 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img12 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img13 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img14 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img15 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img16 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img17 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		ImageView img18 = new ImageView(new Image(getClass().getResourceAsStream("Images/Geld_01.jpg")));
		
		// Test: set width and height of images
		img1.setFitWidth(50);
		img1.setFitHeight(80);
		img2.setFitWidth(50);
		img2.setFitHeight(80);
		img3.setFitWidth(50);
		img3.setFitHeight(80);
		img4.setFitWidth(50);
		img4.setFitHeight(80);
		img5.setFitWidth(50);
		img5.setFitHeight(80);
		img6.setFitWidth(50);
		img6.setFitHeight(80);
		img7.setFitWidth(50);
		img7.setFitHeight(80);
		img8.setFitWidth(50);
		img8.setFitHeight(80);
		img9.setFitWidth(50);
		img9.setFitHeight(80);
		img10.setFitWidth(50);
		img10.setFitHeight(80);
		img11.setFitWidth(50);
		img11.setFitHeight(80);
		img12.setFitWidth(50);
		img12.setFitHeight(80);
		img13.setFitWidth(50);
		img13.setFitHeight(80);
		img14.setFitWidth(50);
		img14.setFitHeight(80);
		img15.setFitWidth(50);
		img15.setFitHeight(80);
		img16.setFitWidth(50);
		img16.setFitHeight(80);
		img17.setFitWidth(50);
		img17.setFitHeight(80);
		img18.setFitWidth(50);
		img18.setFitHeight(80);

		// Test: add buy cards to the containers
		vboxCellarCards.getChildren().add(0, img1);
		vboxMarketCards.getChildren().add(0, img2);
		vboxRemodelCards.getChildren().add(0, img3);
		vboxSmithyCards.getChildren().add(0, img4);
		vboxWoodcutterCards.getChildren().add(0, img5);
		vboxWorkshopCards.getChildren().add(0, img6);
		vboxMineCards.getChildren().add(0, img7);
		vboxVillageCards.getChildren().add(0, img8);
		
		vboxGoldCards.getChildren().add(0, img9);
		vboxSilverCards.getChildren().add(0, img10);
		vboxCopperCards.getChildren().add(0, img11);
		
		vboxDuchyCards.getChildren().add(0, img12);
		vboxEstateCards.getChildren().add(0, img13);
		vboxProvinceCards.getChildren().add(0, img14);
		
		// Test: add cards to stacks deck & discard
		stackpDeck.getChildren().add(img15);
		stackpDiscard.getChildren().add(img16);
		
		// Test: add played cards & hand cards to containers
		hboxPlayedCards.getChildren().add(img17);
		
		hboxHandCards.getChildren().add(img18);
		
		// Already implemented: Sends a chat message to the server 
		btnSendChatArea.setOnAction(event -> {
			String existingMessages = txtaChatArea.getText();
			String newMessage = txtfChatArea.getText();
			
			if (newMessage.length() > 0)
				txtaChatArea.setText(existingMessages.concat(newMessage)+"\n");
			txtfChatArea.setText("");
		});
		
		// Set mouse click on action
		img1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			txtaChatArea.setText("You've clicked the image");
			//event.consume();
	     });
		
			
		// Get the initial image height
		int imageHeight = (int) img1.getFitHeight();
		int imageWidth = (int) img1.getFitWidth();
		
		// Adjust the brightness for clickable cards
        ColorAdjust initial = new ColorAdjust();
        initial.setBrightness(+0.5);
        img1.setEffect(initial);
		
		// Zoom the image
		img1.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			img1.setFitWidth(imageWidth*3);
            img1.setFitHeight(imageHeight*3);
            // Brightness
            ColorAdjust ca1 = new ColorAdjust();
            ca1.setBrightness(0);
            img1.setEffect(ca1);
		});
		
		img1.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			img1.setFitWidth(imageWidth);
            img1.setFitHeight(imageHeight);
            img1.setEffect(initial);
		});
		
		vboxPlayedCards.setOnDragOver(event -> {
			vboxPlayedCards.setStyle("-fx-border-color: red;");
		});
		
		vboxPlayedCards.setOnDragExited(event -> {
			vboxPlayedCards.setStyle("-fx-border-color: white;");
			event.consume();
		});
		
		vboxPlayedCards.setOnDragDropped(event -> {
			hboxPlayedCards.getChildren().add(img1);
		});
		
		// Test Drag and drop of images
		img1.setOnDragDetected(event -> {
			Dragboard db = img1.startDragAndDrop(TransferMode.ANY);

			ClipboardContent content = new ClipboardContent();
			content.putImage(img1.getImage());
			db.setContent(content);
	        
	        event.consume();
		});
		
		img1.setOnDragDone(event -> {
			// hboxPlayedCards.getChildren().add(img1);
			hboxTreasureCards.getChildren().remove(img1);
		});

	}
}
