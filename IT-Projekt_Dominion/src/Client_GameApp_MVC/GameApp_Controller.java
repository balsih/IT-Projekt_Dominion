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
				success = model.sendChat(existingMessages.concat(newMessage)+"\r\n");
			}
			if (success) {
				view.txtaChatArea.setText(model.newChat);
				view.txtfChatArea.setText("");
				model.newChat = null;
			}
		});

		// Defines what happens after clicking the button "commit"
		view.btnCommit.setOnAction(event -> {
			switch(model.interaction){
			case Skip:
				model.sendInteraction();
				break;
			case EndOfTurn:
				if (model.interaction == Interaction.EndOfTurn) {
					view.hboxPlayedCards.getChildren().clear();
					view.hboxHandCards.getChildren().clear();
				}
				model.sendInteraction();
				break;
			case Cellar:
				if (model.interaction == Interaction.Cellar) {
					// already done in method setInitialHandCardsEvents 
				}
				model.sendInteraction();
				break;
			case Workshop:
				if (model.interaction == Interaction.Workshop) {
				}
				// workshop: wahl aus feld
				model.sendInteraction();
				break;
			case Remodel1:
				if (model.interaction == Interaction.Remodel1) {
				}
				// remodel: 1 Karte wegnehmen aus Hand, (1 nehmen --> erst bei remodel2)
				model.sendInteraction();
				break;
			case Remodel2:
				if (model.interaction == Interaction.Remodel2) {
				}
				model.sendInteraction();
				break;
				// kommt nach remodel 1: 1 karte neu nehmen --> aus einer liste von wählbaren karten (andere disablen)
			case Mine:
				if (model.interaction == Interaction.Mine) {
				}
				model.sendInteraction();
				break;
			}
		});

		// Handles window-closing event
		this.view.getStage().setOnCloseRequest(event -> {
			view.stop();
			Platform.exit();
		});
		
		// Starts the thread
		this.initializeServerListening();
	}

	// Resizes the image to the optimal fit size
	private ImageView resizeImage(ImageView img){
		img.setFitWidth(50);
		img.setFitHeight(80);
		return img;
	}

	// Sets events on hand cards when the game starts
	private void setInitialHandCardsEvents(ImageView image){
		// Stores the image height and width
		int imageHeight = (int) image.getFitHeight();
		int imageWidth = (int) image.getFitWidth();

		// Stores the initial and brighter brightness
		ColorAdjust initial = new ColorAdjust();
		initial.setBrightness(0);
		ColorAdjust brighter = new ColorAdjust();
		brighter.setBrightness(+0.5);

		// // If the user clicks a hand card, he either wants play it (action card) or he wants to pay with it (treasure card).
		image.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			// In the buy phase, the card is used to pay for another card
			if (model.currentPhase.equals(Phase.Buy)) {
				view.hboxHandCards.getChildren().remove(image);
				view.hboxPlayedCards.getChildren().add(0, image);
			}
			// In the action phase, the card is being played
			if (model.currentPhase.equals(Phase.Action)) {
				view.hboxHandCards.getChildren().remove(image);
				view.hboxPlayedCards.getChildren().add(0, image);

				for (Card card : model.yourHandCards) {
					if (card.getType() == CardType.Action) {
						if (!view.hboxHandCards.getChildren().contains(resizeImage(card.getImage()))){
							model.sendPlayCard(card);

							// If a cellar card has been played, discard a user-defined number of hand cards and draw new hand cards
							if (card.getCardName() == CardName.Cellar){
								// Adds new event handlers to the hand cards
								for (Node child : view.hboxHandCards.getChildren()) {
									ImageView img = (ImageView) child;
									img.addEventHandler(MouseEvent.MOUSE_CLICKED, cellarEvent -> {
										view.hboxHandCards.getChildren().remove(img);
									});
								}

								// Store the discarded cards
								for (Card card2 : model.yourHandCards){
									if (!view.hboxHandCards.getChildren().contains(resizeImage(card2.getImage()))){
										model.cellarDiscards.add(card2);
									}
								}

								// Adds initial event handlers to the hand cards
								for (Node child : view.hboxHandCards.getChildren()) {
									ImageView img = (ImageView) child;
									setInitialHandCardsEvents(img);
								}
								model.interaction = Interaction.Cellar;
							} else if (card.getCardName() == CardName.Mine){
								model.interaction = Interaction.Mine;
							} else if (card.getCardName() == CardName.Remodel){
								model.interaction = Interaction.Remodel1;
							} else if (card.getCardName() == CardName.Workshop){
								model.interaction = Interaction.Workshop;
							} 
						}
					}
				}
			}
		});

		// If the user enters an image, it gets brighter
		image.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			image.setEffect(brighter);
		});

		// If the user zooms an image, it gets bigger and gets back its original brightness
		image.addEventHandler(ZoomEvent.ZOOM, event -> {
			image.setFitWidth(imageWidth*3);
			image.setFitHeight(imageHeight*3);
			image.setEffect(initial);
		});

		// If the user exits an image, it changes back to its original size and brightness
		image.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			image.setFitWidth(imageWidth);
			image.setFitHeight(imageHeight);
			image.setEffect(initial);
		});
	}

	// Sets events on action cards when the game starts
	private void setInitialActionCardsEvents(ImageView image, CardName cardName){
		// Stores the image height and width
		int imageHeight = (int) image.getFitHeight();
		int imageWidth = (int) image.getFitWidth();

		// Stores the initial and brighter brightness
		ColorAdjust initial = new ColorAdjust();
		initial.setBrightness(0);
		ColorAdjust brighter = new ColorAdjust();
		brighter.setBrightness(+0.5);

		// If the user clicks a card, he wants to buy it. This handler sends a message with the chosen card.
		image.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (model.currentPhase.equals(Phase.Buy)) {
				if(model.sendBuyCard(cardName)){
					view.stackpDiscard.getChildren().add(0, image);
				}

			}
		});

		// If the user enters an image, it gets brighter
		image.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			image.setEffect(brighter);
		});

		// If the user zooms an image, it gets bigger and gets back its original brightness
		image.addEventHandler(ZoomEvent.ZOOM, event -> {
			image.setFitWidth(imageWidth*3);
			image.setFitHeight(imageHeight*3);
			image.setEffect(initial);
		});

		// If the user exits an image, it changes back to its original size and brightness
		image.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			image.setFitWidth(imageWidth);
			image.setFitHeight(imageHeight);
			image.setEffect(initial);
		});
	}

	// Starts the ServerListening
	public void initializeServerListening(){
		new Thread(new ServerListening()).start();
		this.listenToServer = true;
	}

	/**
	 * @author Adrian
	 * Depending on which message was received, this class updates the contents of the components of the GUI
	 */
	public class ServerListening implements Runnable{

		@Override
		public void run() {
			while(listenToServer){
				try {
					Thread.sleep(100); // 10 requests per second if something changed in the game
				} catch (InterruptedException e1) {
					System.out.println(e1.toString());
				}

				Message msgIn = model.processMessage(new AskForChanges_Message());

				if (msgIn instanceof Commit_Message) {
					// nothing toDo here

				} else if (msgIn instanceof UpdateGame_Message) {
					model.processUpdateGame(msgIn);

					// Updates the log
					if(model.newLog != null){
						String existingLog = view.txtaLog.getText();
						view.txtaLog.setText(existingLog.concat(model.newLog)+"\r\n");
						model.newLog = null;
					}

					// Updates the chat
					if(model.newChat != null){
						String existingMessages = view.txtaChatArea.getText();
						view.txtaChatArea.setText(existingMessages.concat(model.newChat)+"\r\n");
						view.txtfChatArea.setText(""); // Removes the entered text
						model.newChat = null;
					}

					// Displays the current phase
					switch(model.currentPhase){
					case Action:
						view.lblCurrentPhase.setText(t.getString("action.lblCurrentPhase")); // Phase: Action
						break;
					case Buy:
						view.lblCurrentPhase.setText(t.getString("buy.lblCurrentPhase")); // Phase: Buy
						break;
					case CleanUp:
						view.lblCurrentPhase.setText(t.getString("cleanUp.lblCurrentPhase")); // Phase: Clean up
						break;
					}

					// If the deck is empty, the discard pile needs to be added to the deck pile. In the GUI we only remove the discard pile top card.
					if(model.yourDeck.size() == 0){
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
					if (model.currentPlayer.compareTo(model.clientName)==0) {
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

					// Adds hand cards
					if (model.yourNewHandCards != null){
						for(Card card : model.yourNewHandCards){
							view.hboxHandCards.getChildren().add(resizeImage(card.getImage()));
						}

						// Adds event handlers to the hand cards
						for (Node child : view.hboxHandCards.getChildren()) {
							ImageView img = (ImageView) child;
							setInitialHandCardsEvents(img);
						}
					}

					// Updates the discard pile top card
					view.stackpDiscard.getChildren().add(0, resizeImage(model.yourDiscardPileTopCard.getImage()));

				} else if (msgIn instanceof CreateGame_Message) {
					model.processCreateGame(msgIn);

					// Sets the current phase
					view.lblCurrentPhase.setText(t.getString("buy.lblCurrentPhase")); // Phase: Buy

					// Sets the name of the current player
					view.lblNameOfCurrentPlayer.setText(model.currentPlayer);

					// Sets the number of current actions
					view.lblNmbrOfCrntActions.setText(Integer.toString(model.actions));

					// Sets the number of current buys
					view.lblNmbrOfCrntBuys.setText(Integer.toString(model.buys));

					// Sets the number of current coins
					view.lblNmbrOfCrntCoins.setText(Integer.toString(model.coins));

					// Sets the number of current hand cards
					view.lblNmbrOfCrntHandCards.setText("5");

					// Sets the number of current discard cards
					view.lblNmbrOfCrntDiscards.setText("0");

					// Sets the number of current deck cards
					view.lblNmbrOfCrntDeckCards.setText("5");

					// Disables chat while playing singleplayer mode
					if(model.gameMode.equals(GameMode.Singleplayer)){
						view.txtfChatArea.setDisable(true);
						view.btnSendChatArea.setDisable(true);
					}

					// Sets the initial number of Action cards
					view.lblNmbrOfCellarCards.setText(Integer.toString(model.buyCards.get(CardName.Cellar)));
					view.lblNmbrOfMarketCards.setText(Integer.toString(model.buyCards.get(CardName.Market)));
					view.lblNmbrOfRemodelCards.setText(Integer.toString(model.buyCards.get(CardName.Remodel)));
					view.lblNmbrOfSmithyCards.setText(Integer.toString(model.buyCards.get(CardName.Smithy)));
					view.lblNmbrOfWoodcutterCards.setText(Integer.toString(model.buyCards.get(CardName.Woodcutter)));
					view.lblNmbrOfWorkshopCards.setText(Integer.toString(model.buyCards.get(CardName.Workshop)));
					view.lblNmbrOfMineCards.setText(Integer.toString(model.buyCards.get(CardName.Mine)));
					view.lblNmbrOfVillageCards.setText(Integer.toString(model.buyCards.get(CardName.Village)));

					// Adds Action cards
					Card cellarCard = Card.getCard(CardName.Cellar);
					view.vboxCellarCards.getChildren().add(0, resizeImage(cellarCard.getImage()));

					Card marketCard = Card.getCard(CardName.Market);
					view.vboxMarketCards.getChildren().add(0, resizeImage(marketCard.getImage()));

					Card remodelCard = Card.getCard(CardName.Remodel);
					view.vboxRemodelCards.getChildren().add(0, resizeImage(remodelCard.getImage()));

					Card smithyCard = Card.getCard(CardName.Smithy);
					view.vboxSmithyCards.getChildren().add(0, resizeImage(smithyCard.getImage()));

					Card woodcutterCard = Card.getCard(CardName.Woodcutter);
					view.vboxWoodcutterCards.getChildren().add(0, resizeImage(woodcutterCard.getImage()));

					Card workshopCard = Card.getCard(CardName.Workshop);
					view.vboxWorkshopCards.getChildren().add(0, resizeImage(workshopCard.getImage()));

					Card mineCard = Card.getCard(CardName.Mine);
					view.vboxMineCards.getChildren().add(0, resizeImage(mineCard.getImage()));

					Card villageCard = Card.getCard(CardName.Village);
					view.vboxVillageCards.getChildren().add(0, resizeImage(villageCard.getImage()));

					// Sets the initial number of Treasure cards
					view.lblNmbrOfGoldCards.setText(Integer.toString(model.buyCards.get(CardName.Gold)));
					view.lblNmbrOfSilverCards.setText(Integer.toString(model.buyCards.get(CardName.Silver)));
					view.lblNmbrOfCopperCards.setText(Integer.toString(model.buyCards.get(CardName.Copper)));

					// Adds Treasure cards
					Card goldCard = Card.getCard(CardName.Gold);
					view.vboxGoldCards.getChildren().add(0, resizeImage(goldCard.getImage()));

					Card silverCard = Card.getCard(CardName.Silver);
					view.vboxSilverCards.getChildren().add(0, resizeImage(silverCard.getImage()));

					Card copperCard = Card.getCard(CardName.Copper);
					view.vboxCopperCards.getChildren().add(0, resizeImage(copperCard.getImage()));

					// Sets the initial number of Victory cards
					view.lblNmbrOfDuchyCards.setText(Integer.toString(model.buyCards.get(CardName.Duchy)));
					view.lblNmbrOfEstateCards.setText(Integer.toString(model.buyCards.get(CardName.Estate)));
					view.lblNmbrOfProvinceCards.setText(Integer.toString(model.buyCards.get(CardName.Province)));

					// Adds Victory cards
					Card duchyCard = Card.getCard(CardName.Duchy);
					view.vboxDuchyCards.getChildren().add(0, resizeImage(duchyCard.getImage()));

					Card estateCard = Card.getCard(CardName.Estate);
					view.vboxEstateCards.getChildren().add(0, resizeImage(estateCard.getImage()));

					Card provinceCard = Card.getCard(CardName.Province);
					view.vboxProvinceCards.getChildren().add(0, resizeImage(provinceCard.getImage()));

					// Adds deck flipside card
					Card flipsideCard = Card.getCard(CardName.Flipside);
					view.stackpDeck.getChildren().add(resizeImage(flipsideCard.getImage()));

					// Adds hand cards
					for(Card card : model.yourNewHandCards){
						view.hboxHandCards.getChildren().add(resizeImage(card.getImage()));
					}

					// Adds event handlers to the hand cards
					for (Node child : view.hboxHandCards.getChildren()) {
						ImageView img = (ImageView) child;
						setInitialHandCardsEvents(img);
					}

					// Adds event handlers to the action cards
					setInitialActionCardsEvents((ImageView) view.vboxCellarCards.getChildren().get(0), CardName.Cellar);
					setInitialActionCardsEvents((ImageView) view.vboxMarketCards.getChildren().get(0), CardName.Market);
					setInitialActionCardsEvents((ImageView) view.vboxRemodelCards.getChildren().get(0), CardName.Remodel);
					setInitialActionCardsEvents((ImageView) view.vboxSmithyCards.getChildren().get(0), CardName.Smithy);
					setInitialActionCardsEvents((ImageView) view.vboxWoodcutterCards.getChildren().get(0), CardName.Woodcutter);
					setInitialActionCardsEvents((ImageView) view.vboxWorkshopCards.getChildren().get(0), CardName.Workshop);
					setInitialActionCardsEvents((ImageView) view.vboxMineCards.getChildren().get(0), CardName.Mine);
					setInitialActionCardsEvents((ImageView) view.vboxVillageCards.getChildren().get(0), CardName.Village);

					// Adds event handlers to the treasure cards
					setInitialActionCardsEvents((ImageView) view.vboxGoldCards.getChildren().get(0), CardName.Gold);
					setInitialActionCardsEvents((ImageView) view.vboxSilverCards.getChildren().get(0), CardName.Silver);
					setInitialActionCardsEvents((ImageView) view.vboxCopperCards.getChildren().get(0), CardName.Copper);

					// Adds event handlers to the victory cards
					setInitialActionCardsEvents((ImageView) view.vboxDuchyCards.getChildren().get(0), CardName.Duchy);
					setInitialActionCardsEvents((ImageView) view.vboxEstateCards.getChildren().get(0), CardName.Estate);
					setInitialActionCardsEvents((ImageView) view.vboxProvinceCards.getChildren().get(0), CardName.Province);

				} else if (msgIn instanceof PlayerSuccess_Message) {
					PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;
					// main.startSuccess(psmsg.getSuccess());
					psmsg.getSuccess(); // won/lost
					// Spieler hat gewonnen: z. B. in Log anzeigen oder Bild anzeigen
					listenToServer = false;
				}
			}
		}
	}
}// end GameApp_Controller