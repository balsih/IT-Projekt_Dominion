package Client_GameApp_MVC;

import java.util.LinkedList;
import java.util.Optional;

import Cards.Card;
import Cards.CardName;
import Messages.GameSuccess;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author Adrian
 * Defines the controls and elements of the GUI, aligns and styles them.
 */
public class GUI_Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void setInitialHandCardsEvents(ImageView image){
		int imageHeight = (int) image.getFitHeight();
		int imageWidth = (int) image.getFitWidth();

		// Adjust the brightness for clickable cards
		ColorAdjust initial = new ColorAdjust();
		initial.setBrightness(0);
		ColorAdjust brighter = new ColorAdjust();
		brighter.setBrightness(+0.5);
		ColorAdjust darker = new ColorAdjust();
		darker.setBrightness(-0.5);

		image.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

			// do stuff here

		});

		image.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			image.setEffect(darker);
		});

		image.addEventHandler(ZoomEvent.ZOOM, event -> {
			//			image.setFitWidth(imageWidth*3);
			//			image.setFitHeight(imageHeight*3);
			//			image.setEffect(initial);
		});

		image.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			image.setFitWidth(imageWidth);
			image.setFitHeight(imageHeight);
			image.setEffect(initial);
		});
	}

	public void setInitialActionCardsEvents(ImageView image){
		int imageHeight = (int) image.getFitHeight();
		int imageWidth = (int) image.getFitWidth();

		// Adjust the brightness for clickable cards
		ColorAdjust initial = new ColorAdjust();
		initial.setBrightness(0);
		ColorAdjust brighter = new ColorAdjust();
		brighter.setBrightness(+0.5);

		image.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			// do stuff here
		});

		image.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			image.setEffect(brighter);
		});

		image.addEventHandler(ZoomEvent.ZOOM, event -> { // MouseEvent.MOUSE_ENTERED
			image.setFitWidth(imageWidth*3);
			image.setFitHeight(imageHeight*3);
			image.setEffect(initial);
		});

		image.addEventHandler(MouseEvent.MOUSE_EXITED, event -> { // MouseEvent.MOUSE_EXITED
			image.setFitWidth(imageWidth);
			image.setFitHeight(imageHeight);
			image.setEffect(initial);
		});
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
		//ScrollPane scrlpChatArea = new ScrollPane();
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
		StackPane stackpDiscard = new StackPane();

		// Controls deck area
		Label lblDeck = new Label("Deck");
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
		Label lblCrntHandCards = new Label("Hand cards");
		Label lblNmbrOfCrntHandCards = new Label("0");
		Label lblCrntDeckCards = new Label("Deck cards");
		Label lblNmbrOfCrntDeckCards = new Label("0");
		Label lblCrntDiscardCards = new Label("Discard cards");
		Label lblNmbrOfCrntDiscards = new Label("0");
		Label lblCrntActions = new Label("Actions");
		Label lblNmbrOfCrntActions = new Label("0");
		Label lblCrntBuys = new Label("Buys");
		Label lblNmbrOfCrntBuys = new Label("0");
		Label lblCrntCoins = new Label("Coins");
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
		// scrlpChatArea.setMaxWidth(400);
		txtaLog.setStyle("-fx-control-inner-background: oldlace;");
		txtaChatArea.setMaxWidth(400);
		// scrlpChatArea.setMaxHeight(100);
		txtaChatArea.setMaxHeight(100);
		txtfChatArea.setMinWidth(320);
		txtfChatArea.setStyle("-fx-background-color: oldlace;");
		// scrlpChatArea.setContent(txtaChatArea);
		HBox hboxChatArea = new HBox(txtfChatArea, btnSendChatArea);
		VBox vboxChatArea = new VBox(lblChatArea, txtaChatArea, hboxChatArea);

		// Log area
		scrlpLog.setMaxWidth(400);
		scrlpLog.setMaxHeight(130);
		scrlpLog.setContent(txtaLog);
		VBox vboxLog = new VBox(lblLog, scrlpLog);

		// Discard area
		VBox vboxDiscard = new VBox(lblDiscard, stackpDiscard);

		// Deck area
		VBox vboxDeck = new VBox(lblDeck, stackpDeck);

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

		// Scene and stage
		Scene scene = new Scene(root);
		stage.setScene(scene);

		//set Stage boundaries to visible bounds of the main screen
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());

		stage.setTitle("Dominion");
		stage.show();

		// Prevent resizing below initial size
		//		stage.setMinWidth(stage.getWidth());
		//		stage.setMinHeight(stage.getHeight());

		// Styles the elements of the GUI
		scene.getStylesheets().add(getClass().getResource("GUI_Test.css").toExternalForm());

		vboxActionCards.getStyleClass().add("vbox");
		gridpActionCards.getStyleClass().add("cardGaps");
		// lblActionCards.getStyleClass().add("Label");
		lblActionCards.getStyleClass().add("hboxCurrentPlayer");

		vboxTreasureCards.getStyleClass().add("vbox");
		hboxTreasureCards.getStyleClass().add("cardGaps");
		hboxTreasureCards.getStyleClass().add("hbox");
		
		btnCommit.getStyleClass().add("btnCommit");

		vboxVictoryCards.getStyleClass().add("vbox");
		hboxVictoryCards.getStyleClass().add("cardGaps");
		hboxVictoryCards.getStyleClass().add("hbox");

		vboxChatArea.getStyleClass().add("vbox");
		vboxChatArea.setPrefWidth(180);
		hboxChatArea.getStyleClass().add("hbox");

		vboxLog.getStyleClass().add("vbox");
		vboxLog.setPrefWidth(180);
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
		ImageView img19 = new ImageView(new Image(getClass().getResourceAsStream("Images/confetti.gif")));

		// Test: set width and height of images	
		img1.setFitHeight(120);
		img1.setFitWidth(80);
		img2.setFitWidth(80);
		img2.setFitHeight(120);
		img3.setFitWidth(80);
		img3.setFitHeight(120);
		img4.setFitWidth(80);
		img4.setFitHeight(120);
		img5.setFitWidth(80);
		img5.setFitHeight(120);
		img6.setFitWidth(80);
		img6.setFitHeight(120);
		img7.setFitWidth(80);
		img7.setFitHeight(120);
		img8.setFitWidth(80);
		img8.setFitHeight(120);
		img9.setFitWidth(80);
		img9.setFitHeight(120);
		img10.setFitWidth(80);
		img10.setFitHeight(120);
		img11.setFitWidth(80);
		img11.setFitHeight(120);
		img12.setFitWidth(80);
		img12.setFitHeight(120);
		img13.setFitWidth(80);
		img13.setFitHeight(120);
		img14.setFitWidth(80);
		img14.setFitHeight(120);
		img15.setFitWidth(80);
		img15.setFitHeight(120);
		img16.setFitWidth(80);
		img16.setFitHeight(120);
		img17.setFitWidth(80);
		img17.setFitHeight(120);
		img18.setFitWidth(80);
		img18.setFitHeight(120);
		img19.setFitWidth(80);
		img19.setFitHeight(120);

		// Test: add buy cards to the containers
		vboxCellarCards.getChildren().add(0, img1);
		vboxMarketCards.getChildren().add(0, img2);
		vboxRemodelCards.getChildren().add(0, img3);
		vboxSmithyCards.getChildren().add(0, img4);
		vboxWoodcutterCards.getChildren().add(0, img5);
		vboxWorkshopCards.getChildren().add(0, img6);
		vboxMineCards.getChildren().add(0, img7);
		// vboxVillageCards.getChildren().add(0, img19);

		vboxGoldCards.getChildren().add(0, img9);
		vboxSilverCards.getChildren().add(0, img10);
		vboxCopperCards.getChildren().add(0, img11);

		vboxDuchyCards.getChildren().add(0, img12);
		vboxEstateCards.getChildren().add(0, img13);
		vboxProvinceCards.getChildren().add(0, img14);

		// Test: add cards to stacks deck & discard
		stackpDeck.getChildren().add(img6);
		stackpDiscard.setPrefHeight(120);
		//stackpDiscard.getChildren().add(img5);

		// Test: add played cards & hand cards to containers
		// hboxPlayedCards.getChildren().add(img4);

		hboxHandCards.getChildren().add(0, img3);
		hboxHandCards.getChildren().add(0, img1);
		hboxHandCards.getChildren().add(0, img2);

		// To implement: Zooming an image
		for (Node child : hboxHandCards.getChildren()) {
			ImageView img = (ImageView) child;
			// setInitialHandCardsEvents(img);

			img.addEventHandler(ZoomEvent.ZOOM, event -> { // ScrollEvent.ANY
				//				Popup popup = new Popup();
				//				ImageView popupImage = new ImageView(img.getImage());
				//				popupImage.setFitHeight(500);
				//				popupImage.setFitWidth(300);
				//				popup.getContent().add(popupImage);
				//				popup.centerOnScreen();
				//				popup.show(stage);
				//				popup.setAutoHide(true);

				//				// can use an Alert, Dialog, or PopupWindow as needed...
				//				Stage popup = new Stage();
				//				// configure UI for popup etc...
				//
				//				// hide popup after 3 seconds:
				//				PauseTransition delay = new PauseTransition(Duration.seconds(3));
				//				delay.setOnFinished(e -> popup.hide());
				//
				//				popup.show();
				//				delay.play();

				//				Alert alert = new Alert(AlertType.INFORMATION);
				//				alert.setTitle("Action phase started");
				//				alert.setHeaderText(null);
				//				alert.setContentText("Action phase has just startet for currentPlayer");
				//
				//				PauseTransition delay = new PauseTransition(Duration.seconds(2));
				//				delay.setOnFinished(e -> alert.hide());
				//				alert.show();
				//				delay.play();

				//_____________________________

				//				Popup popupPlayerSuccess = new Popup();
				//				Label lblWinner = new Label("Winner: ");
				//				Label lblNameOfWinner = new Label("Hanspeter");
				//				Label lblVictoryPoints = new Label("Victory Points: ");
				//				Label lblNmbrOfVictoryPoints = new Label ("22");
				//				HBox hboxWinnerName = new HBox(lblWinner, lblNameOfWinner);
				//				HBox hboxWinnerVictoryPoints = new HBox(lblVictoryPoints, lblNmbrOfVictoryPoints);
				//				ImageView confettiGIF = new ImageView(new Image(getClass().getResourceAsStream("Images/confetti.gif")));
				//
				//				VBox vboxWinner = new VBox(hboxWinnerName, hboxWinnerVictoryPoints, confettiGIF);
				//
				//				popupPlayerSuccess.getContent().add(vboxWinner);
				//				popupPlayerSuccess.centerOnScreen();
				//				popupPlayerSuccess.show(stage);

				//_____________________________

				//				Popup popupPhase = new Popup();
				//				Label lblPopupCurrentPhase = new Label();
				//
				//				// switch case Action/Buy/CleanUp
				//				lblPopupCurrentPhase.setText("Phase: Action"); // (t.getString("action.lblNameOfCurrentPhase"));
				//				lblPopupCurrentPhase.getStyleClass().add("lblPopupCurrentPhase");
				//
				//				popupPhase.getContent().add(lblPopupCurrentPhase);
				//
				//				// Positions and auto-hides the popup after the specified duration
				//				popupPhase.centerOnScreen();
				//				popupPhase.setAutoHide(true);
				//
				//				PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
				//				delay.setOnFinished(e -> popupPhase.hide());
				//				popupPhase.show(stage);
				//				delay.play();

				//_____________________________

				// Displays the winner and loser after a game has ended
				String loserName = "Hausi";
				String winnerName = "Adrian";
				int winnerVictoryPoints = 15;
				int loserVictoryPoints = 14;
				int winnerMoves = 27;
				int loserMoves = 25;

				// Displays a popup with information about the winner
				Popup popupPlayerSuccess = new Popup();

				Label lblPlayer = new Label("Player:");

				Label lblWinner = new Label("Winner:");
				Label lblNameOfWinner = new Label(winnerName);
				Label lblLoser = new Label("Loser:");
				Label lblNameOfLoser = new Label(loserName);

				Label lblVictoryPoints = new Label("Victory points:");
				Label lblNmbrOfWinnerVictoryPoints = new Label(Integer.toString(winnerVictoryPoints));
				Label lblNmbrOfLoserVictoryPoints = new Label(Integer.toString(loserVictoryPoints));

				Label lblMoves = new Label("Moves:");
				Label lblNmbrOfWinnerMoves = new Label(Integer.toString(winnerMoves));
				Label lblNmbrOfLoserMoves = new Label(Integer.toString(loserMoves));

				Button btnGetBackToMainMenu = new Button("Get back to main menu");
				Label lblCongratulations = new Label("Congatulations, "+winnerName+"!");
				lblCongratulations.getStyleClass().add("lblCongratulations");

				// Dummy
				btnGetBackToMainMenu.setOnAction(event2 -> {
					popupPlayerSuccess.hide();
				});

				GridPane gridpGameSuccess = new GridPane();
				gridpGameSuccess.getStyleClass().add("gridpGameSuccess");
				gridpGameSuccess.setMinWidth(500);

				gridpGameSuccess.add(lblWinner, 1, 0);
				gridpGameSuccess.add(lblNameOfWinner, 1, 1);
				gridpGameSuccess.add(lblLoser, 2, 0);
				gridpGameSuccess.add(lblNameOfLoser, 2, 1);
				gridpGameSuccess.add(lblPlayer, 0, 1);

				gridpGameSuccess.add(lblVictoryPoints, 0, 2);
				gridpGameSuccess.add(lblNmbrOfWinnerVictoryPoints, 1, 2);
				gridpGameSuccess.add(lblNmbrOfLoserVictoryPoints, 2, 2);

				gridpGameSuccess.add(lblMoves, 0, 3);
				gridpGameSuccess.add(lblNmbrOfWinnerMoves, 1, 3);
				gridpGameSuccess.add(lblNmbrOfLoserMoves, 2, 3);

				// root
				BorderPane bpGameSuccess = new BorderPane();
				bpGameSuccess.getStyleClass().add("bpGameSuccess");

				bpGameSuccess.setTop(lblCongratulations);
				bpGameSuccess.setLeft(gridpGameSuccess);
				bpGameSuccess.setBottom(btnGetBackToMainMenu);
				bpGameSuccess.setAlignment(lblCongratulations, Pos.TOP_CENTER);
				bpGameSuccess.setAlignment(btnGetBackToMainMenu, Pos.BOTTOM_RIGHT);

				popupPlayerSuccess.getContent().add(bpGameSuccess);
				popupPlayerSuccess.centerOnScreen();
				popupPlayerSuccess.show(stage);
			});
		}

		vboxCellarCards.getChildren().add(0, img7);
		vboxMarketCards.getChildren().add(0, img8);
		vboxRemodelCards.getChildren().add(0, img9);
		vboxVillageCards.getChildren().add(0, img10);

		img10.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> { 
			int imageHeight = (int) img10.getFitHeight()*5;
			int imageWidth = (int) img10.getFitWidth()*5;

			stage.setTitle("Popup Example");
			final Popup popup = new Popup();
			popup.getContent().add(img10);

			popup.show(stage);
		});

		// Sets action cards on action
		setInitialActionCardsEvents((ImageView) vboxCellarCards.getChildren().get(0));
		setInitialActionCardsEvents((ImageView) vboxMarketCards.getChildren().get(0));
		setInitialActionCardsEvents((ImageView) vboxRemodelCards.getChildren().get(0));
		setInitialActionCardsEvents((ImageView) vboxVillageCards.getChildren().get(0));

		// Already implemented: Sends a chat message to the server 
		//		btnSendChatArea.setOnAction(event -> {
		//			String existingMessages = txtaChatArea.getText();
		//			String newMessage = txtfChatArea.getText();
		//
		//			if (newMessage.length() > 0)
		//				txtaChatArea.setText(existingMessages.concat(newMessage)+"\n");
		//			txtfChatArea.setText("");
		//		});

		// Test: button click to open popup
		btnSendChatArea.setOnAction(event3 -> {
			//			Popup popup = new Popup();
			//			
			//			Button btnYes = new Button("Yes");
			//			Button btnNo = new Button("No");
			//			Text text = new Text();
			//			text.setText("Do you really want to leave the game and give up?");
			//			
			//			HBox hbox = new HBox(btnYes, btnNo);
			//			VBox vbox = new VBox(text, hbox);
			//			vbox.setStyle("-fx-spacing: 20px");
			//			hbox.setStyle("-fx-spacing: 20px");
			//			vbox.setStyle("-fx-background-color: white;");
			//			vbox.setAlignment(Pos.BOTTOM_LEFT);
			//			hbox.setAlignment(Pos.BOTTOM_RIGHT);
			//			
			//			popup.setWidth(200);
			//			popup.setHeight(100);
			//			
			//			popup.getContent().add(vbox);
			//			popup.centerOnScreen();
			//			popup.show(stage);
			//			popup.setAutoHide(true);

			//			Alert alert = new Alert(AlertType.CONFIRMATION);
			//			alert.setTitle("Are you sure?");
			//			alert.setHeaderText("If you click ok, you'll lose.");
			//			alert.setContentText("Do you really want to leave this game?");
			//
			//			Optional<ButtonType> result = alert.showAndWait();
			//			if (result.get() == ButtonType.OK){
			//			   Platform.exit();
			//			} else {
			//			    alert.hide();
			//			}


			txtaChatArea.setDisable(false);
			txtaChatArea.setEditable(false);

			String existingMessages = txtaChatArea.getText();
			txtaChatArea.setText(txtfChatArea.getText()+System.lineSeparator().concat(existingMessages));
			txtfChatArea.setText("");

		});

		txtfChatArea.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode().equals(KeyCode.ENTER)) {
				String existingMessages = txtaChatArea.getText();
				String newMessage = txtfChatArea.getText();

				if (newMessage.length() > 0)
					txtaChatArea.setText(existingMessages.concat(newMessage)+"\n");
				txtfChatArea.setText("");
			}
		});

		// Set mouse click on action
		//		img1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
		//			txtaChatArea.setText("You've clicked the image");
		//			//event.consume();
		//	     });

		//		vboxPlayedCards.setOnDragOver(event -> {
		//			vboxPlayedCards.setStyle("-fx-border-color: red;");
		//		});
		//		
		//		vboxPlayedCards.setOnDragExited(event -> {
		//			vboxPlayedCards.setStyle("-fx-border-color: white;");
		//			event.consume();
		//		});
		//		
		//		vboxPlayedCards.setOnDragDropped(event -> {
		//			hboxPlayedCards.getChildren().add(img1);
		//		});
		//		
		//		// Test Drag and drop of images
		//		img1.setOnDragDetected(event -> {
		//			Dragboard db = img1.startDragAndDrop(TransferMode.ANY);
		//
		//			ClipboardContent content = new ClipboardContent();
		//			content.putImage(img1.getImage());
		//			db.setContent(content);
		//	        
		//	        event.consume();
		//		});
		//		
		//		img1.setOnDragDone(event -> {
		//			// hboxPlayedCards.getChildren().add(img1);
		//			hboxTreasureCards.getChildren().remove(img1);
		//		});

	}
}
