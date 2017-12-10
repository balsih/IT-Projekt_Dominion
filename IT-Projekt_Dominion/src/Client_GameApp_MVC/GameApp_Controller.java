package Client_GameApp_MVC;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.VBox;

/**
 * @author Adrian
 */
public class GameApp_Controller extends Controller<GameApp_Model, GameApp_View> {

	private boolean listenToServer;
	private Dominion_Main main;

	private ColorAdjust initial = new ColorAdjust();
	private ColorAdjust brighter = new ColorAdjust();
	private ColorAdjust darker = new ColorAdjust();

	// Translates GUI-text
	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	Translator t = sl.getTranslator();

	public GameApp_Controller(GameApp_Model model, GameApp_View view) {
		super(model, view);

		// If a player gives up, get him back to the main menu
		view.btnGiveUp.setOnAction(event -> {
			model.sendGiveUp();
			this.listenToServer = false; // Stops the thread
			this.main.startMainMenu();	
		});

		// Adds the new chat message to the GUI
		view.btnSendChatArea.setOnAction(event -> {
			String newMessage = view.txtfChatArea.getText();

			if (newMessage.length()>0){
				model.sendChat(newMessage);
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
					model.sendInteraction();
				}
				break;
			}
			updateGUI();
		});

		// By closing the window, the player gives up and exits the game
		this.view.getStage().setOnCloseRequest(event -> {
			model.sendGiveUp();
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
				view.txtaLog.setText(model.newLog+"\r\n".concat(existingLog));
				model.newLog = null;
			}

			// Updates the chat; newest message on top
			if (model.newChat != null) {
				String existingMessages = view.txtaChatArea.getText();
				view.txtaChatArea.setText(model.newChat+"\r\n".concat(existingMessages));

				view.txtfChatArea.setText(""); // Removes the entered text
				model.newChat = null;
			}

			// Displays the current phase
			switch (model.currentPhase) {
			case Action:
				view.lblNameOfCurrentPhase.setText(t.getString("action.lblNameOfCurrentPhase")); // Action
				break;
			case Buy:
				view.lblNameOfCurrentPhase.setText(t.getString("buy.lblNameOfCurrentPhase")); // Buy
				break;
			case CleanUp:
				view.lblNameOfCurrentPhase.setText(t.getString("cleanUp.lblNameOfCurrentPhase")); // Clean up
				break;
			}

			// Adds new hand cards with event handlers
			if (!model.yourNewHandCards.isEmpty()) {
				for (Card card : model.yourNewHandCards) {
					ImageView img = card.getImage();
					setInitialHandCardsEvents(card, resizeImage(img));
					view.hboxHandCards.getChildren().add(img);
					model.yourHandCards.add(card);
				}
				model.yourNewHandCards.clear();
			}

			// Adds the discard pile top card to the GUI
			if(model.yourDiscardPileTopCard != null){
				view.stackpDiscard.getChildren().add(resizeImage(model.yourDiscardPileTopCard.getImage()));
			}

			// Clears the hand cards and played cards after the player's turn ended
			if (model.turnEnded){
				view.hboxHandCards.getChildren().clear();
				view.hboxPlayedCards.getChildren().clear();
			}

			// Clears the discard pile when it is empty
			if (model.yourDiscardPile.isEmpty()) {
				view.stackpDiscard.getChildren().clear();
			}

			// Clears the deck pile when it is empty
			if (model.yourDeck.isEmpty()){
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
				view.lblNmbrOfCrntHandCards.setText(Integer.toString(model.yourHandCards.size()));
				view.lblNmbrOfCrntDiscards.setText(Integer.toString(model.yourDiscardPile.size()));
				view.lblNmbrOfCrntDeckCards.setText(Integer.toString(model.yourDeck.size()));
			} else {
				view.lblNmbrOfCrntHandCards.setText(Integer.toString(model.opponentHandCards));
				view.lblNmbrOfCrntDiscards.setText(Integer.toString(model.opponentDiscardPile));
				view.lblNmbrOfCrntDeckCards.setText(Integer.toString(model.opponentDeck));
			}

			// Updates the number of action cards, treasure cards and victory cards
			view.lblNmbrOfCellarCards.setText(Integer.toString(model.buyCards.get(CardName.Cellar)));
			view.lblNmbrOfMarketCards.setText(Integer.toString(model.buyCards.get(CardName.Market)));
			view.lblNmbrOfRemodelCards.setText(Integer.toString(model.buyCards.get(CardName.Remodel)));
			view.lblNmbrOfSmithyCards.setText(Integer.toString(model.buyCards.get(CardName.Smithy)));
			view.lblNmbrOfWoodcutterCards.setText(Integer.toString(model.buyCards.get(CardName.Woodcutter)));
			view.lblNmbrOfWorkshopCards.setText(Integer.toString(model.buyCards.get(CardName.Workshop)));
			view.lblNmbrOfMineCards.setText(Integer.toString(model.buyCards.get(CardName.Mine)));
			view.lblNmbrOfVillageCards.setText(Integer.toString(model.buyCards.get(CardName.Village)));

			view.lblNmbrOfGoldCards.setText(Integer.toString(model.buyCards.get(CardName.Gold)));
			view.lblNmbrOfSilverCards.setText(Integer.toString(model.buyCards.get(CardName.Silver)));
			view.lblNmbrOfCopperCards.setText(Integer.toString(model.buyCards.get(CardName.Copper)));

			view.lblNmbrOfDuchyCards.setText(Integer.toString(model.buyCards.get(CardName.Duchy)));
			view.lblNmbrOfEstateCards.setText(Integer.toString(model.buyCards.get(CardName.Estate)));
			view.lblNmbrOfProvinceCards.setText(Integer.toString(model.buyCards.get(CardName.Province)));
		});
	}

	// Resizes the image to the optimal fit size
	private ImageView resizeImage(ImageView img) {
		img.setFitWidth(80);
		img.setFitHeight(120);
		return img;
	}

	// Defines changes in size and brightness of images after triggering an event
	private void setGeneralImageEvents(ImageView img) {

		// Stores the image height and width
		int imageHeight = (int) img.getFitHeight();
		int imageWidth = (int) img.getFitWidth();

		// Sets an initial and brighter image brightness
		initial.setBrightness(0);
		brighter.setBrightness(+0.5);

		// If the user enters an image, it gets brighter
		img.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			img.setEffect(brighter);
		});

		// If the user zooms an image, it gets bigger and gets back its original brightness
		img.addEventHandler(ZoomEvent.ZOOM, event -> {
			img.setFitWidth(imageWidth * 3);
			img.setFitHeight(imageHeight * 3);
			img.setEffect(initial);
		});

		// If the user exits an image, it changes back to its original size and brightness
		img.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			img.setFitWidth(imageWidth);
			img.setFitHeight(imageHeight);
			img.setEffect(initial);
		});
	}

	// Sets events on hand cards
	private void setInitialHandCardsEvents(Card card, ImageView img) {
	
		// Image brightness
		initial.setBrightness(0);
		darker.setBrightness(-0.5);

		// Describes what happens when the user clicks a hand card
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			
			if (model.currentPlayer.compareTo(model.clientName) == 0) {

				// Removes a treasure or action card from the hand and adds it to the played cards
				if (model.interaction == Interaction.Skip && card.getType() != CardType.Victory
						&& model.sendPlayCard(card)) {
					view.hboxHandCards.getChildren().remove(img);
					view.hboxPlayedCards.getChildren().add(0, img);
					updateGUI();
				}

				// During the cellar interaction, any clicked card gets discarded
				else if (model.interaction == Interaction.Cellar) {
					if (model.cellarDiscards.contains(card)) {
						model.cellarDiscards.remove(card);
						img.setEffect(initial);
					} else {
						model.cellarDiscards.add(card);
						img.setEffect(darker);
					}
					updateGUI();
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

	// Sets events on action, treasure and victory cards
	private void setInitialATVCardEvents(Card card, ImageView img) {

		// Describes what happens when the user clicks a action, treasure or victory card
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

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

						// Disables chat while playing singleplayer mode
						if (model.gameMode.equals(GameMode.Singleplayer)) {
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
						model.opponentDeck = model.yourDeck.size();
						model.opponentDiscardPile = model.yourDiscardPile.size();
						model.opponentHandCards = model.yourHandCards.size();
					});

					updateGUI();

				} else if (msgIn instanceof PlayerSuccess_Message) {
					PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;

					// Ensure the update happens on the JavaFX Application
					// Thread, by using Platform.runLater()
					Platform.runLater(() -> {

						// main.startSuccess(psmsg.getSuccess());
						psmsg.getSuccess(); // won/lost
						// Spieler hat gewonnen: z. B. in Log anzeigen oder Bild
						// anzeigen
						listenToServer = false;

					});
				}
			}
		}
	}
}// end GameApp_Controller