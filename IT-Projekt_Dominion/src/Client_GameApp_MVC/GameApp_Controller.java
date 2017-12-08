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

	// Translates GUI-text
	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	Translator t = sl.getTranslator();

	public GameApp_Controller(GameApp_Model model, GameApp_View view) {
		super(model, view);

		// Sends a chat message to the server and sets text into the GUI
		view.btnSendChatArea.setOnAction(event -> {
			boolean success = false;
			String existingMessages = view.txtaChatArea.getText();
			String newMessage = view.txtfChatArea.getText();

			if (newMessage.length() > 0) {
				success = model.sendChat(newMessage + "\r\n");
			}
			if (success) {
				view.txtaChatArea.setText(existingMessages.concat(model.newChat) + "\r\n");
				view.txtfChatArea.setText("");
				model.newChat = null;
			}
		});

		// Defines what happens after clicking the button "commit"
		view.btnCommit.setOnAction(event -> {
			switch (model.interaction) {
			case Skip:
				model.sendInteraction();
				break;
			case EndOfTurn:
				updateGUI();
				model.sendInteraction();
				break;
			case Cellar:
				if (model.cellarDiscards.size() >= 1) {
					model.sendInteraction();
				}
				break;
			case Workshop:
				model.sendInteraction();
				break;
			case Remodel1:
				model.sendInteraction();
				break;
			case Remodel2:
				model.sendInteraction();
				break;
			case Mine:
				model.sendInteraction();
				break;
			}
			updateGUI();
		});

		// Handles window-closing event
		this.view.getStage().setOnCloseRequest(event -> {
			view.stop();
			Platform.exit();
			this.listenToServer = false; // Stops the thread
			// model.sendGiveUp_Message();
		});

		// Starts the thread
		this.initializeServerListening();
	}

	// Updates the whole GUI
	private void updateGUI() {

		// Ensures the update happens on the JavaFX Application Thread, by using Platform.runLater()
		Platform.runLater(() -> {

			// Updates the log
			if (model.newLog != null) {
				String existingLog = view.txtaLog.getText();
				view.txtaLog.setText(existingLog.concat(model.newLog) + "\r\n");
				model.newLog = null;
			}

			// Updates the chat
			if (model.newChat != null) {
				String existingMessages = view.txtaChatArea.getText();
				view.txtaChatArea.setText(existingMessages.concat(model.newChat) + "\r\n");

				view.txtfChatArea.setText(""); // Removes the entered text
				model.newChat = null;
			}

			// Displays the current phase
			switch (model.currentPhase) {
			case Action:
				view.lblCurrentPhase.setText(t.getString("action.lblCurrentPhase")); // Phase:
				// Action
				break;
			case Buy:
				view.lblCurrentPhase.setText(t.getString("buy.lblCurrentPhase")); // Phase:
				// Buy
				break;
			case CleanUp:
				view.lblCurrentPhase.setText(t.getString("cleanUp.lblCurrentPhase")); // Phase:
				// Clean
				// up
				break;
			}

			// If the deck is empty, the discard pile needs to be added to the deck pile. In the GUI we only remove the discard pile top card.
			if (model.yourDeck.isEmpty()) {
				view.stackpDiscard.getChildren().clear();
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

			// Adds new hand cards and event handlers
			if (!model.yourNewHandCards.isEmpty()) {
				for (Card card : model.yourNewHandCards) {
					ImageView img = card.getImage();
					setInitialHandCardsEvents(card, resizeImage(img));
					view.hboxHandCards.getChildren().add(img);
					model.yourHandCards.add(card);
				}
				model.yourNewHandCards.clear();
			}
		});
	}

	// Resizes the image to the optimal fit size
	private ImageView resizeImage(ImageView img) {
		img.setFitWidth(80);
		img.setFitHeight(120);
		return img;
	}

	// Sets events on hand cards when the game starts
	private void setInitialHandCardsEvents(Card card, ImageView img) {
		// Stores the image height and width
		int imageHeight = (int) img.getFitHeight();
		int imageWidth = (int) img.getFitWidth();

		// Stores the initial and brighter brightness
		ColorAdjust initial = new ColorAdjust();
		initial.setBrightness(0);
		ColorAdjust brighter = new ColorAdjust();
		brighter.setBrightness(+0.5);
		ColorAdjust darker = new ColorAdjust();
		darker.setBrightness(-0.5);

		// Describes what happens when the user clicks a hand card
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (model.interaction == Interaction.Skip || model.interaction == Interaction.Cellar
					|| model.interaction == Interaction.Remodel1 || model.interaction == Interaction.Mine) {

				if (model.interaction == Interaction.Skip && card.getType() != CardType.Victory
						&& model.sendPlayCard(card)) {
					view.hboxHandCards.getChildren().remove(img);
					view.hboxPlayedCards.getChildren().add(0, img);
					updateGUI();
				}

				if (model.interaction == Interaction.Cellar) {
					if (model.cellarDiscards.contains(card)) {
						model.cellarDiscards.remove(card);
						img.setEffect(initial);
					} else {
						model.cellarDiscards.add(card);
						img.setEffect(darker);
					}
				}

				if (model.interaction == Interaction.Remodel1) {
					model.discardCard = card;
					if (model.sendInteraction()) {
						view.hboxHandCards.getChildren().remove(img);
						updateGUI();
					}
				}

				if (model.interaction == Interaction.Mine
						&& (card.getCardName() == CardName.Copper || card.getCardName() == CardName.Silver)) {
					model.discardCard = card;
					if (model.sendInteraction()) {
						view.hboxHandCards.getChildren().remove(img);
						updateGUI();
					}
				}
			}
		});

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

	// Sets events on action, treasure and victory cards when the game starts
	private void setInitialATVCardEvents(Card card, ImageView img) {
		// Stores the image height and width
		int imageHeight = (int) img.getFitHeight();
		int imageWidth = (int) img.getFitWidth();

		// Stores the initial and brighter brightness
		ColorAdjust initial = new ColorAdjust();
		initial.setBrightness(0);
		ColorAdjust brighter = new ColorAdjust();
		brighter.setBrightness(+0.5);

		// If the user clicks a card, he wants to buy it. This handler sends a message with the chosen card.
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			Card newCard = Card.getCard(card.getCardName());
			if (model.sendBuyCard(newCard.getCardName())) {
				ImageView newImg = resizeImage(newCard.getImage());
				newImg.addEventHandler(ZoomEvent.ZOOM, zoomEvent -> {
					newImg.setFitWidth(imageWidth * 3);
					newImg.setFitHeight(imageHeight * 3);
					newImg.setEffect(initial);
				});
				newImg.addEventHandler(MouseEvent.MOUSE_EXITED, exitEvent -> {
					newImg.setFitWidth(imageWidth);
					newImg.setFitHeight(imageHeight);
					newImg.setEffect(initial);
				});
				view.stackpDiscard.getChildren().add(0, newImg);
				updateGUI();
			}
		});

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
					Thread.sleep(1000); // 1 request per second if something
					// changed in the game
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

					updateGUI();

					// Ensures the update happens on the JavaFX Application Thread, by using Platform.runLater()
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
						setInitialATVCardEvents(woodcutterCard,
								(ImageView) view.vboxWoodcutterCards.getChildren().get(0));

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

						// Adds deck flipside card
						Card flipsideCard = Card.getCard(CardName.Flipside);
						view.stackpDeck.getChildren().add(resizeImage(flipsideCard.getImage()));

					});

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