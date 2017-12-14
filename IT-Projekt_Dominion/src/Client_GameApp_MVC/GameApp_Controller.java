package Client_GameApp_MVC;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import Abstract_MVC.Controller;
import Cards.Card;
import Cards.CardName;
import Cards.CardType;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import MainClasses.Dominion_Main;
import Messages.AskForChanges_Message;
import Messages.Commit_Message;
import Messages.CreateGame_Message;
import Messages.GameMode_Message;
import Messages.Interaction;
import Messages.Message;
import Messages.MessageType;
import Messages.PlayerSuccess_Message;
import Messages.UpdateGame_Message;
import Server_GameLogic.GameMode;
import Server_GameLogic.Phase;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.util.Duration;

/**
 * @author Adrian
 */
public class GameApp_Controller extends Controller<GameApp_Model, GameApp_View> {

	private boolean listenToServer;

	private ColorAdjust initial = new ColorAdjust();
	private ColorAdjust brighter = new ColorAdjust();
	private ColorAdjust darker = new ColorAdjust();

	private LinkedList<ImageView> tmpViews = new LinkedList<ImageView>();

	// Translates GUI-text
	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	Translator t = sl.getTranslator();

	public GameApp_Controller(GameApp_Model model, GameApp_View view) {
		super(model, view);

		// If a player gives up, he gets back to main menu
		view.btnGiveUp.setOnAction(event -> {

			// Creates a new alert that asks the user if he really wants to give up
			Alert giveUpAlert = new Alert(AlertType.CONFIRMATION);
			giveUpAlert.setTitle(t.getString("giveUpAlert.title")); // Are you sure?
			giveUpAlert.setHeaderText(t.getString("giveUpAlert.header")); // If you click ok, you'll lose.
			giveUpAlert.setContentText(t.getString("giveUpAlert.content")); // Do you really want to leave this game?

			// Special styling for the alert
			giveUpAlert.initOwner(view.getStage());

			DialogPane alertDialogPane = giveUpAlert.getDialogPane();
			alertDialogPane.getStyleClass().add("giveUpAlert");

			Optional<ButtonType> result = giveUpAlert.showAndWait();
			if (result.get() == ButtonType.OK){
				model.sendGiveUp();
				this.listenToServer = false; // Stops the thread
				model.main.startMainMenu();
				model.startMediaPlayer("Medieval_Camelot.mp3"); // Starts new sound
				view.stop();
			} else {
				giveUpAlert.hide();
			}
		});

		// Adds the new chat message to the GUI
		view.btnSendChatArea.setOnAction(event -> {
			String newMessage = view.txtfChatArea.getText();

			if (newMessage.length()>0){
				model.sendChat(newMessage);
				updateGUI();
				view.txtfChatArea.setText(""); // Removes the entered text
			}
		});

		// Defines what happens after clicking the button "commit"
		view.btnCommit.setOnAction(event -> {
			switch (model.interaction) {
			case Skip:
				model.sendInteraction();
				break;
			case Cellar:
				if (model.cellarDiscards.size() >= 1) {
					if(model.sendInteraction()){
						view.hboxHandCards.getChildren().removeAll(tmpViews);
						tmpViews.clear();
						model.cellarDiscards.clear();
					}
				}
				break;
			}
			updateGUI();
		});

		// By closing the window, the player gives up and exits the game
		this.view.getStage().setOnCloseRequest(event -> { 
			model.sendGiveUp();
			model.sendLogout();
			this.listenToServer = false; // Stops the thread
			view.stop();
			Platform.exit();
		});

		// Starts the thread
		this.initializeServerListening();
	}

	// Updates the whole GUI
	private void updateGUI() {

		// Ensures the update happens on the JavaFX Application Thread, by using Platform.runLater()
		Platform.runLater(() -> {

			// Updates the log; newest text on top
			if (model.newLog != null) {
				String existingLog = view.txtaLog.getText();
				view.txtaLog.setText(model.newLog+System.lineSeparator().concat(existingLog));

				model.newLog = null;
			}

			// Updates the chat; newest message on top
			if (model.newChat != null) {
				String existingMessages = view.txtaChatArea.getText();
				view.txtaChatArea.setText(model.newChat+System.lineSeparator().concat(existingMessages));

				model.newChat = null;

				// Shows an alert that informs the player about newly received chat messages
				Alert chatAlert = new Alert(AlertType.INFORMATION);
				chatAlert.setTitle(t.getString("chatAlert.title")); // Chat message
				chatAlert.setHeaderText(null);
				chatAlert.setContentText(t.getString("chatAlert.content")); // A new chat message has been sent.

				chatAlert.initOwner(view.getStage());

				DialogPane chatDialogPane = chatAlert.getDialogPane();
				chatDialogPane.getStyleClass().add("generalAlert");

				// Auto-hides the alert after the specified duration
				PauseTransition delay = new PauseTransition(Duration.seconds(2));
				delay.setOnFinished(e -> chatAlert.hide());
				chatAlert.show();
				delay.play();
			}

			// Displays a popup, that informs the user about the current phase
			if (model.phaseChanged == true){ // phase changed
				startPhasePopup();
				model.phaseChanged = false;
			}

			// Displays the current phase
			switch (model.currentPhase) {
			case Action:
				view.lblNameOfCurrentPhase.setText(t.getString("action.lblNameOfCurrentPhase")); // Action
				view.btnCommit.setDisable(false);
				break;
			case Buy:
				view.lblNameOfCurrentPhase.setText(t.getString("buy.lblNameOfCurrentPhase")); // Buy
				view.btnCommit.setDisable(false);
				break;
			// The cleanUp phase cannot be skipped. Therefore, the button is disabled.
			case CleanUp:
				view.lblNameOfCurrentPhase.setText(t.getString("cleanUp.lblNameOfCurrentPhase")); // Clean up
				view.btnCommit.setDisable(true);
				break;
			}

			// During the cellar interaction, the button serves to commit the user's choice
			if (model.interaction == Interaction.Cellar){
				view.btnCommit.setDisable(false);
				view.btnCommit.setText(t.getString("current.btnCommit"));
			} else {
				view.btnCommit.setText(t.getString("current.btnSkip"));
			}

			// Clears the played cards and if it was the client's turn the hand cards after a player's turn ended
			if (model.turnEnded){
				view.hboxPlayedCards.getChildren().clear();
				if (model.clientName.compareTo(model.currentPlayer) != 0)
					view.hboxHandCards.getChildren().clear();
			}

			// Adds new hand cards with event handlers
			if (!model.yourNewHandCards.isEmpty()) {
				for (Card card : model.yourNewHandCards) {
					ImageView img = card.getImage();
					setInitialHandCardsEvents(card, resizeImage(img));
					view.hboxHandCards.getChildren().add(img);
				}
				model.yourNewHandCards.clear();
			}

			//Adds the played cards with event handlers
			if (model.newPlayedCard != null){
				ImageView img = model.newPlayedCard.getImage();
				setGeneralImageEvents(resizeImage(img));
				view.hboxPlayedCards.getChildren().add(img);
				model.newPlayedCard = null;
			}

			// Adds the discard pile top card to the GUI
			if (model.yourDiscardPile == 0){
				view.stackpDiscard.getChildren().clear();
			} else 	if (model.yourDiscardPileTopCard != null){
				view.stackpDiscard.getChildren().clear();
				ImageView img = model.yourDiscardPileTopCard.getImage();
				setGeneralImageEvents(resizeImage(img));
				view.stackpDiscard.getChildren().add(img);
			}

			// Clears the deck pile when it is empty
			if (model.yourDeck == 0){
				view.stackpDeck.getChildren().clear();
			} else {
				// Adds a flipside card to the deck pile when it is empty to simulate the presence of deck cards
				if (view.stackpDeck.getChildren().isEmpty())
					view.stackpDeck.getChildren().add(resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			// Updates the name of the current player
			view.lblNameOfCurrentPlayer.setText(model.currentPlayer);

			// Updates the number of current actions
			view.lblNmbrOfCrntActions.setText(Integer.toString(model.actions));

			// Updates the number of current buys
			view.lblNmbrOfCrntBuys.setText(Integer.toString(model.buys));

			// Updates the number of current coins
			view.lblNmbrOfCrntCoins.setText(Integer.toString(model.coins));

			// Updates the number of current hand cards, discard cards and deck cards
			if (model.currentPlayer.compareTo(model.clientName) == 0) {
				view.lblNmbrOfCrntHandCards.setText(Integer.toString(view.hboxHandCards.getChildren().size()));
				view.lblNmbrOfCrntDiscards.setText(Integer.toString(model.yourDiscardPile));
				view.lblNmbrOfCrntDeckCards.setText(Integer.toString(model.yourDeck));
			} else {
				view.lblNmbrOfCrntHandCards.setText(Integer.toString(model.opponentHandCards));
				view.lblNmbrOfCrntDiscards.setText(Integer.toString(model.opponentDiscardPile));
				view.lblNmbrOfCrntDeckCards.setText(Integer.toString(model.opponentDeck));
			}

			// Updates the number of action, treasure and victory cards
			// Adds a flipside card to the card's area when the stack is empty
			view.lblNmbrOfCellarCards.setText(Integer.toString(model.buyCards.get(CardName.Cellar)));
			if (model.buyCards.get(CardName.Cellar)==0){
				view.vboxCellarCards.getChildren().remove(0);
				view.vboxCellarCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfMarketCards.setText(Integer.toString(model.buyCards.get(CardName.Market)));
			if (model.buyCards.get(CardName.Market)==0){
				view.vboxMarketCards.getChildren().remove(0);
				view.vboxMarketCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfRemodelCards.setText(Integer.toString(model.buyCards.get(CardName.Remodel)));
			if (model.buyCards.get(CardName.Remodel)==0){
				view.vboxRemodelCards.getChildren().remove(0);
				view.vboxRemodelCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfSmithyCards.setText(Integer.toString(model.buyCards.get(CardName.Smithy)));
			if (model.buyCards.get(CardName.Smithy)==0){
				view.vboxSmithyCards.getChildren().remove(0);
				view.vboxSmithyCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfWoodcutterCards.setText(Integer.toString(model.buyCards.get(CardName.Woodcutter)));
			if (model.buyCards.get(CardName.Woodcutter)==0){
				view.vboxWoodcutterCards.getChildren().remove(0);
				view.vboxWoodcutterCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfWorkshopCards.setText(Integer.toString(model.buyCards.get(CardName.Workshop)));
			if (model.buyCards.get(CardName.Workshop)==0){
				view.vboxWorkshopCards.getChildren().remove(0);
				view.vboxWorkshopCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfMineCards.setText(Integer.toString(model.buyCards.get(CardName.Mine)));
			if (model.buyCards.get(CardName.Mine)==0){
				view.vboxMineCards.getChildren().remove(0);
				view.vboxMineCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfVillageCards.setText(Integer.toString(model.buyCards.get(CardName.Village)));
			if (model.buyCards.get(CardName.Village)==0){
				view.vboxVillageCards.getChildren().remove(0);
				view.vboxVillageCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfGoldCards.setText(Integer.toString(model.buyCards.get(CardName.Gold)));
			if (model.buyCards.get(CardName.Gold)==0){
				view.vboxGoldCards.getChildren().remove(0);
				view.vboxGoldCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfSilverCards.setText(Integer.toString(model.buyCards.get(CardName.Silver)));
			if (model.buyCards.get(CardName.Silver)==0){
				view.vboxSilverCards.getChildren().remove(0);
				view.vboxSilverCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfCopperCards.setText(Integer.toString(model.buyCards.get(CardName.Copper)));
			if (model.buyCards.get(CardName.Copper)==0){
				view.vboxCopperCards.getChildren().remove(0);
				view.vboxCopperCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfDuchyCards.setText(Integer.toString(model.buyCards.get(CardName.Duchy)));
			if (model.buyCards.get(CardName.Duchy)==0){
				view.vboxDuchyCards.getChildren().remove(0);
				view.vboxDuchyCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfEstateCards.setText(Integer.toString(model.buyCards.get(CardName.Estate)));
			if (model.buyCards.get(CardName.Estate)==0){
				view.vboxEstateCards.getChildren().remove(0);
				view.vboxEstateCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			view.lblNmbrOfProvinceCards.setText(Integer.toString(model.buyCards.get(CardName.Province)));
			if (model.buyCards.get(CardName.Province)==0){
				view.vboxProvinceCards.getChildren().remove(0);
				view.vboxProvinceCards.getChildren().add(0, resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}
		});
	}

	// Displays a popup that informs the player about the current phase
	private void startPhasePopup() {
		
		ImageView imgActionPhase = new ImageView(new Image(getClass().getResourceAsStream("Images/actionPhase.png")));
		ImageView imgBuyPhase = new ImageView(new Image(getClass().getResourceAsStream("Images/buyPhase.png")));
		ImageView imgCleanUpPhase = new ImageView(new Image(getClass().getResourceAsStream("Images/cleanUpPhase.png")));
		ImageView imgAktionsphase = new ImageView(new Image(getClass().getResourceAsStream("Images/Aktionsphase.png")));
		ImageView imgKaufphase = new ImageView(new Image(getClass().getResourceAsStream("Images/Kaufphase.png")));
		ImageView imgAufräumphase = new ImageView(new Image(getClass().getResourceAsStream("Images/Aufräumphase.png")));
		
		Popup popupPhase = new Popup();
		
		if (t.getCurrentLocale().getLanguage().equals("de")){
			switch (model.currentPhase){
			case Action:
				popupPhase.getContent().add(imgAktionsphase);
				break;
			case Buy:
				popupPhase.getContent().add(imgKaufphase);
				break;
			case CleanUp:
				popupPhase.getContent().add(imgAufräumphase);
				break;
			}
		} else {
			switch (model.currentPhase){
			case Action:
				popupPhase.getContent().add(imgActionPhase);
				break;
			case Buy:
				popupPhase.getContent().add(imgBuyPhase);
				break;
			case CleanUp:
				popupPhase.getContent().add(imgCleanUpPhase);
				break;
			}
		}

		popupPhase.centerOnScreen();
		popupPhase.setAutoHide(true);

		// Auto-hides the popup after the specified duration
		PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
		delay.setOnFinished(e -> popupPhase.hide());
		popupPhase.show(view.getStage());
		delay.play();
	}

	// Resizes the image to the optimal fit size
	private ImageView resizeImage(ImageView img) {
		img.setFitWidth(80);
		img.setFitHeight(120);
		return img;
	}

	// Defines changes in size and brightness of images after triggering an event
	private void setGeneralImageEvents(ImageView img) {

		// Sets an initial and brighter image brightness
		initial.setBrightness(0);
		brighter.setBrightness(+0.5);

		// If the user enters an image, it gets brighter
		img.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			if ((model.interaction != Interaction.Cellar) || (model.interaction == Interaction.Cellar && !tmpViews.contains(img)))
				img.setEffect(brighter);
		});

		// If the user zooms an image, it gets bigger
		img.addEventHandler(ZoomEvent.ZOOM, event -> {
			setZoomEvents(img);
		});

		// If the user scrolls an image, it gets bigger
		img.addEventHandler(ScrollEvent.ANY, event -> {
			setZoomEvents(img);
		});

		// If the user exits an image, it gets back its initial brightness
		img.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			if ((model.interaction != Interaction.Cellar) || (model.interaction == Interaction.Cellar && !tmpViews.contains(img)))
				img.setEffect(initial);
		});
	}

	private void setZoomEvents(ImageView img) {
		// Stores the image height and width
		int imageHeight = (int) img.getFitHeight();
		int imageWidth = (int) img.getFitWidth();

		// Creates a new popup with the zoomed image and shows/autohides it
		Popup popupZoom = new Popup();
		ImageView popupImage = new ImageView(img.getImage());
		popupImage.setFitHeight(imageHeight*5);
		popupImage.setFitWidth(imageWidth*5);
		popupZoom.getContent().add(popupImage);
		popupZoom.centerOnScreen();
		popupZoom.show(view.getStage());
		popupZoom.setAutoHide(true);
	}

	// Sets events on hand cards
	private void setInitialHandCardsEvents(Card card, ImageView img) {

		// Image brightness
		initial.setBrightness(0);
		darker.setBrightness(-0.5);

		// Describes what happens when the user clicks a hand card
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

			// If model.success isn't null, someone has won the game
			if (model.success != null){
				createWinnerPopup();
			}

			if (model.currentPlayer.compareTo(model.clientName) == 0) {

				// Removes a treasure or action card from the hand and adds it to the played cards
				if (model.interaction == Interaction.Skip && card.getType() != CardType.Victory
						&& model.sendPlayCard(card)) {
					view.hboxHandCards.getChildren().remove(img);
					updateGUI();
				}

				// During the cellar interaction, any clicked card gets discarded
				else if (model.interaction == Interaction.Cellar) {
					if (model.cellarDiscards.contains(card)) {
						model.cellarDiscards.remove(card);
						tmpViews.remove(img);
						img.setEffect(initial);
					} else {
						model.cellarDiscards.add(card);
						tmpViews.add(img);
						img.setEffect(darker);
					}
				}

				// During the remodel1 interaction, the clicked card gets discarded
				else if (model.interaction == Interaction.Remodel1) {
					model.discardCard = card;
					if (model.sendInteraction()) {
						view.hboxHandCards.getChildren().remove(img);
						updateGUI();
					}
				}

				// During the mine interaction, a clicked copper or silver card gets discarded
				else if (model.interaction == Interaction.Mine
						&& (card.getCardName() == CardName.Copper || card.getCardName() == CardName.Silver)) {
					model.discardCard = card;
					if (model.sendInteraction()) {
						view.hboxHandCards.getChildren().remove(img);
						updateGUI();
					}
				}

				// During the end of turn interaction, the clicked card gets put on top of the discard pile
				else if(model.interaction == Interaction.EndOfTurn){
					model.discardCard = card;
					if(model.sendInteraction()){
						updateGUI();
					}
				}
			}
		});

		setGeneralImageEvents(img);
	}

	private void createWinnerPopup() {

		// Ensures the update happens on the JavaFX Application Thread, by using Platform.runLater()
		Platform.runLater(() -> {

			String winner;
			if (model.success == model.success.Won) {
				winner = model.getClientName();
			} else {
				winner = model.opponent;
			}

			// Shows a popup with information about the winner
			Popup popupPlayerSuccess = new Popup();
			Label lblWinner = new Label("Winner: ");
			Label lblNameOfWinner = new Label(winner);
			Label lblVictoryPoints = new Label("Victory points: ");
			Label lblNmbrOfVictoryPoints = new Label (Integer.toString(model.victoryPoints));
			HBox hboxWinnerName = new HBox(lblWinner, lblNameOfWinner);
			HBox hboxWinnerVictoryPoints = new HBox(lblVictoryPoints, lblNmbrOfVictoryPoints);
			ImageView confettiGIF = new ImageView(new Image(getClass().getResourceAsStream("Images/confetti.gif")));

			VBox vboxWinner = new VBox(hboxWinnerName, hboxWinnerVictoryPoints, confettiGIF);

			popupPlayerSuccess.getContent().add(vboxWinner);
			popupPlayerSuccess.centerOnScreen();
			popupPlayerSuccess.show(view.getStage());

			listenToServer = false;
		});

	}

	// Sets events on action, treasure and victory cards
	private void setInitialATVCardEvents(Card card, ImageView img) {

		// Describes what happens when the user clicks a action, treasure or victory card
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

			// If model.success isn't null, someone has won the game
			if (model.success != null){
				createWinnerPopup();
			}

			if (model.currentPlayer.compareTo(model.clientName) == 0) {

				// During the skip interaction in the buy phase, the clicked card gets bought and rearranged in the GUI
				if (model.interaction == Interaction.Skip && model.currentPhase == Phase.Buy) {

					if (model.sendBuyCard(card.getCardName()))
						updateGUI();
				}

				// During the interaction remodel2 or workshop in the action phase, the clicked card gets chosen.
				else if ((model.interaction == Interaction.Remodel2 || model.interaction == Interaction.Workshop)
						&& model.currentPhase == Phase.Action) {

					if (model.cardSelection != null && model.cardSelection.contains(card.getCardName())){
						model.buyChoice = card.getCardName();
						if (model.sendInteraction()){
							updateGUI();
							model.cardSelection = null;
						}
					}
				}			
			}
		});

		setGeneralImageEvents(img);
	}

	// Starts the ServerListening
	public void initializeServerListening() {
		new Thread(new ServerListening()).start();
		this.listenToServer = true;
	}

	/**
	 * @author Adrian 
	 * Depending on which message was received, this class updates the contents of the components of the GUI
	 */
	public class ServerListening implements Runnable {

		@Override
		public void run() {
			while (listenToServer) {
				try {
					Thread.sleep(1000); // 1 request per second
				} catch (InterruptedException e1) {
					System.out.println(e1.toString());
				}

				Message msgIn = model.processMessage(new AskForChanges_Message());

				if (msgIn instanceof Commit_Message) {
					// nothing toDo here

				} else if (msgIn instanceof UpdateGame_Message) {
					model.processUpdateGame(msgIn);

					updateGUI();

				} else if (msgIn instanceof CreateGame_Message) {
					model.processCreateGame(msgIn);

					// Ensures the update happens on the JavaFX Application Thread by using Platform.runLater()
					Platform.runLater(() -> {
						
						startPhasePopup();

						// Disables chat while playing singleplayer mode
						if (model.gameMode.equals(GameMode.Singleplayer)) {
							view.txtaChatArea.setText(t.getString("chatArea.info")); // only available in multiplayer mode 
							view.txtfChatArea.setDisable(true);
							view.btnSendChatArea.setDisable(true);
						}

						// Adds Action cards and event handlers
						Card cellarCard = Card.getCard(CardName.Cellar);
						view.vboxCellarCards.getChildren().add(0, resizeImage(cellarCard.getImage()));
						setInitialATVCardEvents(cellarCard, (ImageView) view.vboxCellarCards.getChildren().get(0));

						Card marketCard = Card.getCard(CardName.Market);
						view.vboxMarketCards.getChildren().add(0, resizeImage(marketCard.getImage()));
						setInitialATVCardEvents(marketCard, (ImageView) view.vboxMarketCards.getChildren().get(0));

						Card remodelCard = Card.getCard(CardName.Remodel);
						view.vboxRemodelCards.getChildren().add(0, resizeImage(remodelCard.getImage()));
						setInitialATVCardEvents(remodelCard, (ImageView) view.vboxRemodelCards.getChildren().get(0));

						Card smithyCard = Card.getCard(CardName.Smithy);
						view.vboxSmithyCards.getChildren().add(0, resizeImage(smithyCard.getImage()));
						setInitialATVCardEvents(smithyCard, (ImageView) view.vboxSmithyCards.getChildren().get(0));

						Card woodcutterCard = Card.getCard(CardName.Woodcutter);
						view.vboxWoodcutterCards.getChildren().add(0, resizeImage(woodcutterCard.getImage()));
						setInitialATVCardEvents(woodcutterCard, (ImageView) view.vboxWoodcutterCards.getChildren().get(0));

						Card workshopCard = Card.getCard(CardName.Workshop);
						view.vboxWorkshopCards.getChildren().add(0, resizeImage(workshopCard.getImage()));
						setInitialATVCardEvents(workshopCard, (ImageView) view.vboxWorkshopCards.getChildren().get(0));

						Card mineCard = Card.getCard(CardName.Mine);
						view.vboxMineCards.getChildren().add(0, resizeImage(mineCard.getImage()));
						setInitialATVCardEvents(mineCard, (ImageView) view.vboxMineCards.getChildren().get(0));

						Card villageCard = Card.getCard(CardName.Village);
						view.vboxVillageCards.getChildren().add(0, resizeImage(villageCard.getImage()));
						setInitialATVCardEvents(villageCard, (ImageView) view.vboxVillageCards.getChildren().get(0));

						// Adds Treasure cards and event handlers
						Card goldCard = Card.getCard(CardName.Gold);
						view.vboxGoldCards.getChildren().add(0, resizeImage(goldCard.getImage()));
						setInitialATVCardEvents(goldCard, (ImageView) view.vboxGoldCards.getChildren().get(0));

						Card silverCard = Card.getCard(CardName.Silver);
						view.vboxSilverCards.getChildren().add(0, resizeImage(silverCard.getImage()));
						setInitialATVCardEvents(silverCard, (ImageView) view.vboxSilverCards.getChildren().get(0));

						Card copperCard = Card.getCard(CardName.Copper);
						view.vboxCopperCards.getChildren().add(0, resizeImage(copperCard.getImage()));
						setInitialATVCardEvents(copperCard, (ImageView) view.vboxCopperCards.getChildren().get(0));

						// Adds Victory cards and event handlers
						Card duchyCard = Card.getCard(CardName.Duchy);
						view.vboxDuchyCards.getChildren().add(0, resizeImage(duchyCard.getImage()));
						setInitialATVCardEvents(duchyCard, (ImageView) view.vboxDuchyCards.getChildren().get(0));

						Card estateCard = Card.getCard(CardName.Estate);
						view.vboxEstateCards.getChildren().add(0, resizeImage(estateCard.getImage()));
						setInitialATVCardEvents(estateCard, (ImageView) view.vboxEstateCards.getChildren().get(0));

						Card provinceCard = Card.getCard(CardName.Province);
						view.vboxProvinceCards.getChildren().add(0, resizeImage(provinceCard.getImage()));
						setInitialATVCardEvents(provinceCard, (ImageView) view.vboxProvinceCards.getChildren().get(0));

						// Initializes opponent's variables
						model.opponentDeck = model.yourDeck;
						model.opponentDiscardPile = model.yourDiscardPile;
						model.opponentHandCards = view.hboxHandCards.getChildren().size();
					});

					updateGUI();

				} else if (msgIn instanceof PlayerSuccess_Message) {

					PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;
					createWinnerPopup();
					
					//					// Ensures the update happens on the JavaFX Application Thread, by using Platform.runLater()
					//					Platform.runLater(() -> {
					//
					//						// main.startSuccess(psmsg.getSuccess());
					//						
					//						// Shows a popup with information about the winner
					//						Popup popupPlayerSuccess = new Popup();
					//						Label lblWinner = new Label(t.getString("popupPlayerSuccess.lblWinner")); // Winner:
					//						Label lblNameOfWinner = new Label(psmsg.getClient());
					//						Label lblVictoryPoints = new Label(t.getString("popupPlayerSuccess.lblVictoryPoints")); // Victory points:
					//						Label lblNmbrOfVictoryPoints = new Label (Integer.toString(psmsg.getVictoryPoints()));
					//						HBox hboxWinnerName = new HBox(lblWinner, lblNameOfWinner);
					//						HBox hboxWinnerVictoryPoints = new HBox(lblVictoryPoints, lblNmbrOfVictoryPoints);
					//						ImageView confettiGIF = new ImageView(new Image(getClass().getResourceAsStream("Images/confetti.gif")));
					//						
					//						VBox vboxWinner = new VBox(hboxWinnerName, hboxWinnerVictoryPoints, confettiGIF);
					//						
					//						popupPlayerSuccess.getContent().add(vboxWinner);
					//						popupPlayerSuccess.centerOnScreen();
					//						popupPlayerSuccess.show(view.getStage());
					//						
					//						listenToServer = false;
					//
					//					});
				}
			}
		}
	}
}// end GameApp_Controller