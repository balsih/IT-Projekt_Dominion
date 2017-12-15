package Client_GameApp_MVC;

import java.util.LinkedList;
import java.util.Optional;
import Abstract_MVC.Controller;
import Cards.Card;
import Cards.CardName;
import Cards.CardType;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import Messages.AskForChanges_Message;
import Messages.Commit_Message;
import Messages.CreateGame_Message;
import Messages.GameSuccess;
import Messages.Interaction;
import Messages.Message;
import Messages.PlayerSuccess_Message;
import Messages.UpdateGame_Message;
import Server_GameLogic.GameMode;
import Server_GameLogic.Phase;
import Server_GameLogic.Player;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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

		// Defines what happens after giving up the game
		view.btnGiveUp.setOnAction(event -> {

			// Creates a new alert that asks the user if he really wants to give up
			Alert giveUpAlert = new Alert(AlertType.CONFIRMATION);
			giveUpAlert.setTitle(t.getString("giveUpAlert.title")); // Are you sure?
			giveUpAlert.setHeaderText(t.getString("giveUpAlert.header")); // If you click ok, you'll lose.
			giveUpAlert.setContentText(t.getString("giveUpAlert.content")); // Do you really want to leave this game?

			// Makes sure that the alert gets displayed in front of the stage
			giveUpAlert.initOwner(view.getStage());

			// Special styling for the alert
			DialogPane alertDialogPane = giveUpAlert.getDialogPane();
			alertDialogPane.getStyleClass().add("giveUpAlert");

			// Handles button clicks
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

		// Defines what happens after sending a new chat message
		view.btnSendChatArea.setOnAction(event -> {
			String newMessage = view.txtfChatArea.getText();

			if (newMessage.length()>0){
				model.sendChat(newMessage);
				updateGUI();
				view.txtfChatArea.setText(""); // Removes the entered text
			}
		});

		// Defines what happens after clicking the button commit/skip
		view.btnCommit.setOnAction(event -> {
			switch (model.interaction) {
			// Skips a game phase
			case Skip:
				if (model.currentPlayer.compareTo(model.clientName) == 0) {
					model.sendInteraction();
				}
				break;
				// Discards user-defined hand cards in cellar interaction
			case Cellar:
				if (model.currentPlayer.compareTo(model.clientName) == 0) {
					if (model.cellarDiscards.size() >= 1) {
						if(model.sendInteraction()){
							view.hboxHandCards.getChildren().removeAll(tmpViews);
							tmpViews.clear();
							model.cellarDiscards.clear();
						}
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

				// Displays an alert that informs the player about newly received chat messages
				Alert chatAlert = new Alert(AlertType.INFORMATION);
				chatAlert.setTitle(t.getString("chatAlert.title")); // Chat message
				chatAlert.setHeaderText(null);
				chatAlert.setContentText(t.getString("chatAlert.content")); // A new chat message has been sent.

				// Makes sure that the alert gets displayed in front of the stage
				chatAlert.initOwner(view.getStage());

				// Styling of the alert
				DialogPane chatDialogPane = chatAlert.getDialogPane();
				chatDialogPane.getStyleClass().add("generalAlert");

				// Auto-hides the alert after the specified duration
				PauseTransition delay = new PauseTransition(Duration.seconds(2));
				delay.setOnFinished(e -> chatAlert.hide());
				chatAlert.show();
				delay.play();
			}

			// Displays a popup that informs the user about the current phase
			if (model.phaseChanged == true){
				startPhasePopup();
				model.phaseChanged = false;
			}

			// Displays the current phase in the current player area
			switch (model.currentPhase) {
			case Action:
				view.lblNameOfCurrentPhase.setText(t.getString("action.lblNameOfCurrentPhase")); // Action

				// Enables the button commit only for the current player during the specified phase
				if (model.currentPlayer.compareTo(model.clientName) == 0) {
					view.btnCommit.setDisable(false);
				} else {
					view.btnCommit.setDisable(true);
				}
				break;
			case Buy:
				view.lblNameOfCurrentPhase.setText(t.getString("buy.lblNameOfCurrentPhase")); // Buy

				// Enables the button commit only for the current player during the specified phase
				if (model.currentPlayer.compareTo(model.clientName) == 0) {
					view.btnCommit.setDisable(false);
				} else {
					view.btnCommit.setDisable(true);
				}
				break;
				// The cleanUp phase cannot be skipped. Therefore, the button skip gets disabled.
			case CleanUp:
				view.lblNameOfCurrentPhase.setText(t.getString("cleanUp.lblNameOfCurrentPhase")); // Clean up
				view.btnCommit.setDisable(true);
				break;
			}

			// During the cellar interaction, the button serves to commit the user's cellar discard choice.
			// Else, it enables the user to skip a game phase.
			if (model.interaction == Interaction.Cellar){
				view.btnCommit.setDisable(false);
				view.btnCommit.setText(t.getString("current.btnCommit")); // Commit
			} else {
				view.btnCommit.setText(t.getString("current.btnSkip")); // Skip phase
			}

			// Clears the played cards and, if it was the client's turn, the hand cards after a player's turn ended
			if (model.turnEnded){
				view.hboxPlayedCards.getChildren().clear();
				if (model.clientName.compareTo(model.currentPlayer) != 0)
					view.hboxHandCards.getChildren().clear();
			}

			// Adds new hand to the hand cards area with event handlers
			if (!model.yourNewHandCards.isEmpty()) {
				for (Card card : model.yourNewHandCards) {
					ImageView img = card.getImage();
					setInitialHandCardsEvents(card, resizeImage(img));
					view.hboxHandCards.getChildren().add(img);
				}
				model.yourNewHandCards.clear();
			}

			// Adds played cards to the played cards area with event handlers
			if (model.newPlayedCard != null){
				ImageView img = model.newPlayedCard.getImage();
				setGeneralImageEvents(resizeImage(img)); // Defines image changes in size and brightness after triggering an event
				view.hboxPlayedCards.getChildren().add(img);
				model.newPlayedCard = null;
			}

			// Adds the discard pile top card to the stack pane
			if (model.yourDiscardPile == 0){
				view.stackpDiscard.getChildren().clear();
			} else 	if (model.yourDiscardPileTopCard != null){
				view.stackpDiscard.getChildren().clear();
				ImageView img = model.yourDiscardPileTopCard.getImage();
				setGeneralImageEvents(resizeImage(img)); // Defines image changes in size and brightness after triggering an event
				view.stackpDiscard.getChildren().add(img);
			}

			// Clears the deck pile when it is empty
			if (model.yourDeck == 0){
				view.stackpDeck.getChildren().clear();
			} else {
				// Adds a flipside card to the deck pile to show that it is not empty
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

		ImageView imgActionPhase_en = new ImageView(new Image(getClass().getResourceAsStream("Images/imgActionPhase_en.png")));
		ImageView imgBuyPhase_en = new ImageView(new Image(getClass().getResourceAsStream("Images/imgBuyPhase_en.png")));
		ImageView imgCleanUpPhase_en = new ImageView(new Image(getClass().getResourceAsStream("Images/imgCleanUpPhase_en.png")));
		ImageView imgActionPhase_de = new ImageView(new Image(getClass().getResourceAsStream("Images/imgActionPhase_de.png")));
		ImageView imgBuyPhase_de = new ImageView(new Image(getClass().getResourceAsStream("Images/imgBuyPhase_de.png")));
		ImageView imgCleanUpPhase_de = new ImageView(new Image(getClass().getResourceAsStream("Images/imgCleanUpPhase_de.png")));

		Popup popupPhase = new Popup();

		// Displays the image in the specified language
		if (t.getCurrentLocale().getLanguage().equals("de")){
			switch (model.currentPhase){
			case Action:
				popupPhase.getContent().add(imgActionPhase_de);
				break;
			case Buy:
				popupPhase.getContent().add(imgBuyPhase_de);
				break;
			case CleanUp:
				popupPhase.getContent().add(imgCleanUpPhase_de);
				break;
			}
		} else {
			switch (model.currentPhase){
			case Action:
				popupPhase.getContent().add(imgActionPhase_en);
				break;
			case Buy:
				popupPhase.getContent().add(imgBuyPhase_en);
				break;
			case CleanUp:
				popupPhase.getContent().add(imgCleanUpPhase_en);
				break;
			}
		}

		// Positions and auto-hides the popup after the specified duration
		popupPhase.centerOnScreen();
		popupPhase.setAutoHide(true);

		PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
		delay.setOnFinished(e -> popupPhase.hide());
		popupPhase.show(view.getStage());
		delay.play();
	}

	// Resizes images to the optimal fit size
	private ImageView resizeImage(ImageView img) {
		img.setFitWidth(80);
		img.setFitHeight(120);
		return img;
	}

	// Defines image changes in size and brightness after triggering an event
	private void setGeneralImageEvents(ImageView img) {

		// Sets the initial and brighter image brightness
		initial.setBrightness(0);
		brighter.setBrightness(+0.5);

		// If the user enters an image, it gets brighter (except for cellar interaction)
		img.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			if ((model.interaction != Interaction.Cellar) || (model.interaction == Interaction.Cellar && !tmpViews.contains(img)))
				img.setEffect(brighter);
		});

		// If the user zooms an image, a popup displays it bigger
		img.addEventHandler(ZoomEvent.ZOOM, event -> {
			setZoomEvents(img);
		});

		// If the user scrolls an image, a popup displays it bigger
		// (this event is an alternative to the ZoomEvent for hardware unable to trigger ZoomEvent.ZOOM)
		img.addEventHandler(ScrollEvent.ANY, event -> {
			setZoomEvents(img);
		});

		// If the user exits an image, it gets back the initial brightness (except for cellar interaction)
		img.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			if ((model.interaction != Interaction.Cellar) || (model.interaction == Interaction.Cellar && !tmpViews.contains(img)))
				img.setEffect(initial);
		});
	}

	// Displays an image in a bigger size
	private void setZoomEvents(ImageView img) {
		// Stores the initial image height and width
		int imageHeight = (int) img.getFitHeight();
		int imageWidth = (int) img.getFitWidth();

		// Creates a new popup with the bigger image and shows/autohides it
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

			// Displays the winner if the card-click caused a player to win the game
			if (model.clientPlayer != null || model.opponentPlayer != null){
				createWinnerPopup();
			}

			// Checks if the player who clicked the image is the current player
			// and therefore is allowed to play cards
			if (model.currentPlayer.compareTo(model.clientName) == 0) {

				// Removes a treasure or action card from the hand
				if (model.interaction == Interaction.Skip && card.getType() != CardType.Victory
						&& model.sendPlayCard(card)) {
					view.hboxHandCards.getChildren().remove(img);
					updateGUI();
				}

				// During the cellar interaction, any clicked hand card gets discarded.
				// The card will be removed from the hand after clicking the button "commit".
				else if (model.interaction == Interaction.Cellar) {
					// Revokes a discard-decision by removing the card from cellarDiscards
					if (model.cellarDiscards.contains(card)) {
						model.cellarDiscards.remove(card);
						tmpViews.remove(img);
						img.setEffect(initial);
					} else {
						// Adds a card to cellarDiscards and makes it darker
						model.cellarDiscards.add(card);
						tmpViews.add(img);
						img.setEffect(darker);
					}
				}

				// During the remodel1 interaction, one clicked hand card gets discarded
				else if (model.interaction == Interaction.Remodel1) {
					model.discardCard = card;
					if (model.sendInteraction()) {
						// Removes the card from the hand
						view.hboxHandCards.getChildren().remove(img);
						updateGUI();
					}
				}

				// During the mine interaction, a clicked copper or silver card gets discarded
				else if (model.interaction == Interaction.Mine
						&& (card.getCardName() == CardName.Copper || card.getCardName() == CardName.Silver)) {
					model.discardCard = card;
					if (model.sendInteraction()) {
						// Removes the card from the hand
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

		// Defines image changes in size and brightness after triggering an event
		setGeneralImageEvents(img);
	}

	// Displays the winner and loser after a game has ended
	private void createWinnerPopup() {

		// Ensures the update happens on the JavaFX Application Thread, by using Platform.runLater()
		Platform.runLater(() -> {

			// Disables all buttons after a player has won
			view.btnCommit.setDisable(true);
			view.btnGiveUp.setDisable(true);
			view.btnSendChatArea.setDisable(true);

			String loserName = null;
			String winnerName = null;
			int winnerVictoryPoints = 0;
			int loserVictoryPoints = 0;

			// Gets loser and winner names with the number of acquired victory points
			if (model.clientPlayer != null) {
				if (model.clientPlayer.getStatus() == GameSuccess.Won){
					winnerName = model.clientPlayer.getPlayerName();
					winnerVictoryPoints = model.clientPlayer.getVictoryPoints();
				} else {
					loserName = model.clientPlayer.getPlayerName();
					loserVictoryPoints = model.clientPlayer.getVictoryPoints();
				}
			}

			if (model.opponentPlayer != null) {
				if (model.opponentPlayer.getStatus() == GameSuccess.Won) {
					winnerName = model.opponentPlayer.getPlayerName();
					winnerVictoryPoints = model.opponentPlayer.getVictoryPoints();
				} else {
					loserName = model.opponentPlayer.getPlayerName();
					loserVictoryPoints = model.opponentPlayer.getVictoryPoints();
				}
			}

			// Displays a popup with information about the winner
			Popup popupPlayerSuccess = new Popup();

			Label lblWinner = new Label("Winner:");
			Label lblNameOfWinner = new Label(winnerName);
			Label lblLoser = new Label("Loser:");
			Label lblNameOfLoser = new Label(loserName);
			Label lblWinnerVictoryPoints = new Label("Winner's victory points:");
			Label lblNmbrOfWinnerVictoryPoints = new Label(Integer.toString(winnerVictoryPoints));
			Label lblLoserVictoryPoints = new Label("Loser's victory points:");
			Label lblNmbrOfLoserVictoryPoints = new Label(Integer.toString(loserVictoryPoints));

			HBox hboxWinnerName = new HBox(lblWinner, lblNameOfWinner);
			HBox hboxWinnerVictoryPoints = new HBox(lblWinnerVictoryPoints, lblNmbrOfWinnerVictoryPoints);
			HBox hboxLoserName = new HBox(lblLoser, lblNameOfLoser);
			HBox hboxLoserVictoryPoints = new HBox(lblLoserVictoryPoints, lblNmbrOfLoserVictoryPoints);

			ImageView confettiGIF = new ImageView(new Image(getClass().getResourceAsStream("Images/confetti.gif")));

			VBox result = new VBox(hboxWinnerName, hboxWinnerVictoryPoints, hboxLoserName, hboxLoserVictoryPoints, confettiGIF);

			popupPlayerSuccess.getContent().add(result);
			popupPlayerSuccess.centerOnScreen();
			popupPlayerSuccess.show(view.getStage());

			listenToServer = false;
		});

	}

	// Sets events on action, treasure and victory cards
	private void setInitialATVCardEvents(Card card, ImageView img) {

		// Describes what happens when the user clicks an action, treasure or victory card
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

			// Displays the winner if the card-click caused a player to win the game (e.g. when province card stack is empty)
			if (model.clientPlayer != null || model.opponentPlayer != null){
				createWinnerPopup();
			}

			// Checks if the player who clicked the image is the current player
			// and therefore is allowed to play cards
			if (model.currentPlayer.compareTo(model.clientName) == 0) {

				// During the skip interaction in the buy phase, the clicked card gets bought
				if (model.interaction == Interaction.Skip && model.currentPhase == Phase.Buy) {

					if (model.sendBuyCard(card.getCardName()))
						updateGUI();
				}

				// During the interaction remodel2 or workshop in the action phase, the clicked card gets chosen ("bought")
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

		// Defines image changes in size and brightness after triggering an event
		setGeneralImageEvents(img);
	}

	// Starts the ServerListening thread
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
					// nothing to do here

				} else if (msgIn instanceof UpdateGame_Message) {
					model.processUpdateGame(msgIn);

					updateGUI();

				} else if (msgIn instanceof CreateGame_Message) {
					model.processCreateGame(msgIn);

					// Ensures the update happens on the JavaFX Application Thread by using Platform.runLater()
					Platform.runLater(() -> {

						// Displays a popup that informs the player about the current phase
						startPhasePopup();

						// Disables the chat while playing singleplayer mode
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
					model.processPlayerSuccess(msgIn);

					// Displays the winner and loser of the game
					createWinnerPopup();
				}
			}
		}
	}
}// end GameApp_Controller