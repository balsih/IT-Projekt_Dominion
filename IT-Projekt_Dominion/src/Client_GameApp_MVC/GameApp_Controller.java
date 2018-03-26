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
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Adrian
 * Handles events and controls the GUI.
 */
public class GameApp_Controller extends Controller<GameApp_Model, GameApp_View> {

	private boolean listenToServer;

	private ColorAdjust initial = new ColorAdjust();
	private ColorAdjust brighter = new ColorAdjust();
	private ColorAdjust darker = new ColorAdjust();

	private LinkedList<ImageView> tmpViews = new LinkedList<ImageView>();

	// Translates GUI-text
	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	private Translator t = sl.getTranslator();

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

		// Adds a new chat message after clicking the button
		view.btnSendChatArea.setOnAction(event -> {
			addChatMessage();
		});

		// Adds a new chat message after pressing the key enter
		view.txtfChatArea.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode().equals(KeyCode.ENTER)) {
				addChatMessage();
			}
		});

		// Defines what happens after clicking the button commit/skip
		view.btnCommit.setOnAction(event -> {
			switch (model.interaction) {
			case Skip:
				// Skips a game phase
				if (model.currentPlayer.compareTo(model.clientName) == 0) {
					model.sendInteraction();
				}
				break;
			case Cellar:
				// Discards user-defined hand cards in cellar interaction
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

	/**
	 * @author Adrian
	 * Adds a new chat message to the existing chat history (on top)
	 */
	private void addChatMessage() {
		String newMessage = view.txtfChatArea.getText();

		if (newMessage.length()>0){
			model.sendChat(newMessage);
			model.chatSent = true; // Used to inform the player about a received chat message
			updateGUI();
			view.txtfChatArea.setText(""); // Removes the entered text
		}
	}

	/**
	 * @author Adrian
	 * Updates the whole GUI (see comments below for further information)
	 */
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

				// Informs the player about a received chat message (if he didn't send it, he gets informed)
				if (!model.chatSent) {
					colorChatArea(); // draws the player's attention to the received chat message
				}
				model.chatSent = false;
			}

			// Displays a popup that informs the player about the current phase
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
			case CleanUp:
				// The cleanUp phase cannot be skipped. Therefore, the button skip gets disabled.
				view.lblNameOfCurrentPhase.setText(t.getString("cleanUp.lblNameOfCurrentPhase")); // Clean up
				view.btnCommit.setDisable(true);
				break;
			}

			// During the cellar interaction, the button serves to commit the user's cellar discard choice.
			// Otherwise, it enables the user to skip a game phase.
			if (model.interaction == Interaction.Cellar){
				view.btnCommit.setDisable(false);
				view.btnCommit.setText(t.getString("current.btnCommit")); // Commit
			} else {
				view.btnCommit.setText(t.getString("current.btnSkip")); // Skip phase
			}

			// Clears the played cards and, if it was the client's turn, the hand cards, after a player's turn ended
			if (model.turnEnded){
				view.hboxPlayedCards.getChildren().clear();
				if (model.clientName.compareTo(model.currentPlayer) != 0)
					view.hboxHandCards.getChildren().clear();
			}

			// Adds new hand cards to the hand cards area with event handlers
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
				// Adds a flipside card to the deck pile to show that it is not actually empty
				if (view.stackpDeck.getChildren().isEmpty())
					view.stackpDeck.getChildren().add(resizeImage(Card.getCard(CardName.Flipside).getImage()));
			}

			// Updates the name of the current player
			if (model.currentPlayer.compareTo(model.clientName) == 0) {
				view.lblNameOfCurrentPlayer.setText(t.getString("currentPlayerName.you")+" ("+model.currentPlayer+")"); // You
			} else {
				view.lblNameOfCurrentPlayer.setText(model.currentPlayer);
			}

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

	/**
	 * @author Adrian
	 * Colors the chat text area for a specified duration to draw the player's attention to a received chat message.
	 */
	private void colorChatArea() {
		PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
		delay.setOnFinished(e -> view.txtaChatArea.setStyle("-fx-control-inner-background: oldlace;"));
		view.txtaChatArea.setStyle("-fx-control-inner-background: gold;");
		delay.play();
	}

	/**
	 * @author Adrian
	 * Displays a popup that informs the player about the current phase.
	 */
	private void startPhasePopup() {

		Popup popupPhase = new Popup();
		Label lblPopupCurrentPhase = new Label();
		lblPopupCurrentPhase.getStyleClass().add("lblPopupCurrentPhase");

		// Displays the current phase in the label
		switch (model.currentPhase) {
		case Action:
			lblPopupCurrentPhase.setText("  Phase: "+t.getString("action.lblNameOfCurrentPhase")); // Action
			break;
		case Buy:
			lblPopupCurrentPhase.setText("  Phase: "+t.getString("buy.lblNameOfCurrentPhase")); // Buy
			break;
		case CleanUp:
			lblPopupCurrentPhase.setText("  Phase: "+t.getString("cleanUp.lblNameOfCurrentPhase")); // Clean up
			break;
		}

		// Sets the label into the popup
		popupPhase.getContent().add(lblPopupCurrentPhase);

		// Positions and auto-hides the popup after the specified duration
		popupPhase.centerOnScreen();
		popupPhase.setAutoHide(true);

		PauseTransition delay = new PauseTransition(Duration.seconds(1));
		delay.setOnFinished(e -> popupPhase.hide());
		popupPhase.show(view.getStage());
		delay.play();
	}

	/**
	 * @author Adrian
	 * Resizes images to the optimal fit size
	 */
	private ImageView resizeImage(ImageView img) {
		img.setFitWidth(80);
		img.setFitHeight(120);
		return img;
	}

	/**
	 * @author Adrian
	 * Defines image changes in size and brightness after triggering an event
	 */
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

	/**
	 * @author Adrian
	 * Displays an image in a bigger size in a popup
	 */
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

	/**
	 * @author Adrian
	 * Sets events on hand cards. See comments below for further information.
	 */
	private void setInitialHandCardsEvents(Card card, ImageView img) {

		// Image brightness
		initial.setBrightness(0);
		darker.setBrightness(-0.5);

		// Describes what happens when the user clicks a hand card
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

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

			// Displays the winner if the card-click caused a player to win the game
			if (model.clientPlayer != null || model.opponentPlayer != null){
				createWinnerPopup(view.getStage());
			}
		});

		// Defines image changes in size and brightness after triggering an event
		setGeneralImageEvents(img);
	}

	/**
	 * @author Adrian
	 * Displays the winner and loser in a popup after a game has ended
	 */
	private void createWinnerPopup(Stage stage) {

		// Ensures the update happens on the JavaFX Application Thread, by using Platform.runLater()
		Platform.runLater(() -> {

			// Disables all buttons after a player has won
			view.btnCommit.setDisable(true);
			view.btnGiveUp.setDisable(true);
			view.btnSendChatArea.setDisable(true);

			String winnerNameHeader = null;
			String loserName = null;
			String winnerName = null;
			String contentlblWinner = null;
			String contentlblLoser = null;

			int winnerVictoryPoints = 0;
			int loserVictoryPoints = 0;
			int winnerMoves = 0;
			int loserMoves = 0;

			// Gets loser and winner names with the number of acquired victory points and needed moves
			// If both players win (same number of victory points and moves)
			if (model.clientPlayer.getStatus() == GameSuccess.Won && model.opponentPlayer.getStatus() == GameSuccess.Won) {
				winnerNameHeader = model.clientPlayer.getPlayerName().concat(" & "+model.opponentPlayer.getPlayerName());
				contentlblWinner = t.getString("popupPlayerSuccess.lblDraw");
				contentlblLoser = "";

				// Winner 1
				winnerName = model.clientPlayer.getPlayerName();
				winnerVictoryPoints = model.clientPlayer.getVictoryPoints();
				winnerMoves = model.clientPlayer.getMoves();

				// Winner 2
				loserName = model.opponentPlayer.getPlayerName();
				loserVictoryPoints = model.opponentPlayer.getVictoryPoints();
				loserMoves = model.opponentPlayer.getMoves();	
			}

			// If clientPlayer wins and opponentPlayer loses
			if (model.clientPlayer.getStatus() == GameSuccess.Won && model.opponentPlayer.getStatus() == GameSuccess.Lost){
				contentlblWinner = t.getString("popupPlayerSuccess.lblWinner");
				winnerNameHeader = model.clientPlayer.getPlayerName();
				winnerName = model.clientPlayer.getPlayerName();
				winnerVictoryPoints = model.clientPlayer.getVictoryPoints();
				winnerMoves = model.clientPlayer.getMoves();

				contentlblLoser = t.getString("popupPlayerSuccess.lblLoser");
				loserName = model.opponentPlayer.getPlayerName();
				loserVictoryPoints = model.opponentPlayer.getVictoryPoints();
				loserMoves = model.opponentPlayer.getMoves();

				// If clientPlayer loses and opponentPlayer wins
			} else if (model.clientPlayer.getStatus() == GameSuccess.Lost && model.opponentPlayer.getStatus() == GameSuccess.Won) {
				contentlblWinner = t.getString("popupPlayerSuccess.lblWinner");
				winnerNameHeader = model.opponentPlayer.getPlayerName();
				winnerName = model.opponentPlayer.getPlayerName();
				winnerVictoryPoints = model.opponentPlayer.getVictoryPoints();
				winnerMoves = model.opponentPlayer.getMoves();

				contentlblLoser = t.getString("popupPlayerSuccess.lblLoser");
				loserName = model.clientPlayer.getPlayerName();
				loserVictoryPoints = model.clientPlayer.getVictoryPoints();
				loserMoves = model.clientPlayer.getMoves();
			}

			// Defines a popup with its content
			Popup popupPlayerSuccess = new Popup();

			Label lblPlayer = new Label(t.getString("popupPlayerSuccess.lblPlayer")); // Player:

			Label lblWinner = new Label(contentlblWinner); // Winner:
			Label lblNameOfWinner = new Label(winnerName);
			Label lblLoser = new Label(contentlblLoser); // Loser:
			Label lblNameOfLoser = new Label(loserName);

			Label lblVictoryPoints = new Label(t.getString("popupPlayerSuccess.lblVictoryPoints")); // Victory points:
			Label lblNmbrOfWinnerVictoryPoints = new Label(Integer.toString(winnerVictoryPoints));
			Label lblNmbrOfLoserVictoryPoints = new Label(Integer.toString(loserVictoryPoints));

			Label lblMoves = new Label(t.getString("popupPlayerSuccess.lblMoves")); // Moves:
			Label lblNmbrOfWinnerMoves = new Label(Integer.toString(winnerMoves));
			Label lblNmbrOfLoserMoves = new Label(Integer.toString(loserMoves));

			Button btnGetBackToMainMenu = new Button(t.getString("popupPlayerSuccess.btnGetBackToMainMenu")); // Get back to main menu
			Label lblCongratulations = new Label(t.getString("popupPlayerSuccess.lblCongratulations")+", "+winnerNameHeader+"!"); // Congratulations
			lblCongratulations.getStyleClass().add("lblCongratulations");

			// A button click leads the player back to the main menu
			btnGetBackToMainMenu.setOnAction(event -> {
				model.main.startMainMenu();
				model.startMediaPlayer("Medieval_Camelot.mp3"); // Starts new sound
				view.stop();
				this.listenToServer = false; // stops the thread
			});

			// Defines a gridPane with information about the winner and loser
			GridPane gridpGameSuccess = new GridPane();
			gridpGameSuccess.getStyleClass().add("gridpGameSuccess");
			gridpGameSuccess.setMinWidth(500);

			gridpGameSuccess.add(lblPlayer, 0, 1);
			gridpGameSuccess.add(lblWinner, 1, 0);
			gridpGameSuccess.add(lblNameOfWinner, 1, 1);
			gridpGameSuccess.add(lblLoser, 2, 0);
			gridpGameSuccess.add(lblNameOfLoser, 2, 1);

			gridpGameSuccess.add(lblVictoryPoints, 0, 2);
			gridpGameSuccess.add(lblNmbrOfWinnerVictoryPoints, 1, 2);
			gridpGameSuccess.add(lblNmbrOfLoserVictoryPoints, 2, 2);

			gridpGameSuccess.add(lblMoves, 0, 3);
			gridpGameSuccess.add(lblNmbrOfWinnerMoves, 1, 3);
			gridpGameSuccess.add(lblNmbrOfLoserMoves, 2, 3);

			// Defines a border pane as content of the popup
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

	/**
	 * @author Adrian
	 * Sets events on action, treasure and victory cards. See comments below for further information.
	 */
	private void setInitialATVCardEvents(Card card, ImageView img) {

		// Describes what happens when the user clicks an action, treasure or victory card
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

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

			// Displays the winner if the card-click caused a player to win the game (e.g. when province card stack is empty)
			if (model.clientPlayer != null || model.opponentPlayer != null){
				createWinnerPopup(view.getStage());
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
	 * This thread listens to the server and receives messages, depending on which it sets and updates the content of the GUI
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
					
					model.sendStartBot();

				} else if (msgIn instanceof PlayerSuccess_Message) {
					model.processPlayerSuccess(msgIn);

					// Displays the winner and loser of the game
					createWinnerPopup(view.getStage());
				}
			}
		}
	}
}// end GameApp_Controller