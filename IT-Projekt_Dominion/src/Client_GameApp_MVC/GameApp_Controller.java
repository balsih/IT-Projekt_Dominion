package Client_GameApp_MVC;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import Abstract_MVC.Controller;
import Cards.Card;
import Cards.CardType;
import MainClasses.Dominion_Main;
import Messages.AskForChanges_Message;
import Messages.Interaction;
import Messages.Message;
import Messages.MessageType;
import Messages.PlayerSuccess_Message;
import Messages.UpdateGame_Message;
import Server_GameLogic.Phase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author Adrian 
 */
public class GameApp_Controller extends Controller<GameApp_Model, GameApp_View> {

	private boolean listenToServer;

	public GameApp_Controller(GameApp_Model model, GameApp_View view) {
		super(model, view);

		// Adrian
		// Sends a chat message to the server and sets text into the GUI
		view.btnSendChatArea.setOnAction(event -> {
			boolean success = false;
			String existingMessages = view.txtaChatArea.getText();
			String newMessage = view.txtfChatArea.getText();	

			if (newMessage.length() > 0) {
				success = model.sendChat(existingMessages.concat(newMessage)+"\n");
			}
			if (success) {
				view.txtaChatArea.setText(model.newChat);
				view.txtfChatArea.setText("");
				model.newChat = null;
			}
		});

		view.btnCommit.setOnAction(event -> {
			switch(model.interaction){
			case Skip:
				model.sendInteraction();
				break;
			case EndOfTurn:
				view.stackpDiscard.getChildren().add(model.yourDiscardPileTopCard.getImage());
				view.lblNmbrOfDiscards.setText(Integer.toString(model.yourDiscardPile.size()));

				//				ist karte gesetzt?, wenn ja auf discard pile setzen
				//						fragen ob null
				//				played cards und hand cards aus den boxen leeren

				model.sendInteraction();
				break;
			case Cellar:
				//				view.hboxHandCards.getChildren().clear();
				//				for (Card card : model.yourHandCards){
				//					view.hboxHandCards.getChildren().add(card.getImage());
				//				}
				//				view.lblNmbrOfCrntHandCards.setText(Integer.toString((model.yourHandCards.size())));
				//				view.lblNmbrOfDeckCards.setText(Integer.toString(model.yourDeck.size()));
				//				view.stackpDiscard.getChildren().add(model.yourDiscardPileTopCard.getImage());
				//				view.lblNmbrOfDiscards.setText(Integer.toString(model.yourDiscardPile.size()));
				model.sendInteraction();
				break;
			case Workshop:
				// workshop: wahl aus feld
				model.sendInteraction();
				break;
			case Remodel1:
				// remodel: 1 Karte wegnehmen aus Hand, (1 nehmen --> erst bei remodel2)
				model.sendInteraction();
				break;
			case Remodel2:
				model.sendInteraction();
				break;
				// kommt nach remodel 1: 1 karte neu nehmen --> aus einer liste von wählbaren karten (andere disablen)
			}
		});

		// Adrian
		// Handles window-closing event
		this.view.getStage().setOnCloseRequest(event -> {
			view.stop();
			Platform.exit();
		});
	}

	/**
	 * @author Adrian
	 * This method starts the ServerListening
	 */
	public void initializeServerListening(){
		new Thread(new ServerListening()).start();
	}

	/**
	 * @author Adrian
	 * Updates the content of the components of the GUI
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

				if(msgIn.getType().equals(MessageType.Commit)){
					// nothing toDo here

				}else if(msgIn.getType().equals(MessageType.UpdateGame)){
					model.processUpdateGame(msgIn);

					// Updates the log
					if(model.newLog != null){
						String existingLog = view.txtaLog.getText();
						view.txtaLog.setText(existingLog.concat(model.newLog)+"\n");
						model.newLog = null;
					}

					// The update of the chat messages already happens after the button click (above)


					// Updates the number of current actions
					if(model.actions != 0){
						int existingActions = Integer.parseInt(view.lblNmbrOfCrntActions.getText());
						view.lblNmbrOfCrntActions.setText(Integer.toString(model.actions + existingActions));
						model.actions = 0;
					}

					// Updates the number of current buys
					if(model.buys != 0){
						int existingBuys = Integer.parseInt(view.lblNmbrOfCrntBuys.getText());
						view.lblNmbrOfCrntBuys.setText(Integer.toString(model.buys + existingBuys));
						model.buys = 0;
					}

					// Updates the number of current coins
					if(model.coins != 0){
						int existingCoins = Integer.parseInt(view.lblNmbrOfCrntCoins.getText());
						view.lblNmbrOfCrntCoins.setText(Integer.toString(model.coins + existingCoins));
						model.coins = 0;
					}

					// Reacts to the current phase of the game
					switch(model.currentPhase){
					// The player can play action cards; may be skipped
					case Action:
						model.newLog = model.translate("#Aktionsphase#");

						// sets action cards in the players' hand cards on action; after playing an action card, it displays it in played cards
						for (Card card : model.yourHandCards){
							if (card.getType().equals(CardType.Action)){
								ImageView img1 = new ImageView();
								img1 = card.getImage();

								// Make clickable cards brighter
								ColorAdjust ca = new ColorAdjust();
								ca.setBrightness(+0.5);
								img1.setEffect(ca);

								img1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
									// model.yourHandCards.remove(card);
									view.hboxHandCards.getChildren().remove(img1);
									view.hboxPlayedCards.getChildren().add(img1);
									event.consume();

									// after clicking on one card, remove the event
									for (Card card1 : model.yourHandCards)
										card.getImage().removeEventHandler(MouseEvent.MOUSE_CLICKED, event);
								});
							}
						}
						break;
						// The player can buy cards; may be skipped
					case Buy:
						model.newLog = model.translate("#Kaufphase#");

						// sets treasure cards, victory cards and action cards on action (they can be bought)
						if (model.buyCards.size()>0){
							for (int i=0; i<model.buyCards.size()-1; i++){
								Card card = card.getCard(model.buyCards.get(i), model.buyCards.get(i));
								ImageView img1 = new ImageView();
								img1 = card.getImage();

								// Make clickable cards brighter
								ColorAdjust ca = new ColorAdjust();
								ca.setBrightness(+0.5);
								img1.setEffect(ca);

								// Set hand-treasure cards on action: To buy cards, the player lays trasure-hand-cards form his "hand cards" in the "played cards" area

								img1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
									// removes the bought card from the specified area
									if (model.coins >= card.getCost()){
										if (view.tilepActionCards.getChildren().contains(img1)){
											view.tilepActionCards.getChildren().remove(img1);
										} else if (view.hboxTreasureCards.getChildren().contains(img1)){
											view.hboxTreasureCards.getChildren().remove(img1);
										} else if (view.hboxVictoryCards.getChildren().contains(img1)){
											view.hboxVictoryCards.getChildren().remove(img1);
										}
										// Put the bought card visibly on the current players' discard pile
										view.stackpDiscard.getChildren().add(img1);
										view.lblNmbrOfDiscards.setText(Integer.toString(model.yourDiscardPile.size()));
										event.consume();

										// after clicking on one card, remove the event
										for (Card card1 : model.buyCards)
											card1.getImage().removeEventHandler(MouseEvent.MOUSE_CLICKED, event);
									}
								});
							}
						}

						break;
						// The player must discard all of his played and unplayed cards and draw 5 new cards
					case CleanUp:
						model.newLog = model.translate("#Aufräumphase#");

						// remove all "played cards" and "hand cards" and put "the last one" visibly on the discard pile
						if (view.hboxPlayedCards.getChildren().size()>0 || view.hboxHandCards.getChildren().size()>0){
							view.stackpDiscard.getChildren().add(view.hboxPlayedCards.getChildren().get(0));
							view.hboxPlayedCards.getChildren().clear();
							view.hboxHandCards.getChildren().clear();
							view.lblNmbrOfDiscards.setText(Integer.toString(model.yourDiscardPile.size()));
						}

						ColorAdjust ca = new ColorAdjust();
						ca.setBrightness(+0.5);
						view.stackpDeck.getChildren().get(0).setEffect(ca);

						// set deck on action: draw 5 cards from deck and put them in hand cards
						view.stackpDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
							for (int i=1; i<=5; i++){
								for (Card card : model.yourDeck){
									ImageView img1 = new ImageView();
									img1 = card.getImage();
									view.hboxHandCards.getChildren().add(img1);
								}
							}
							// after clicking on one card, remove the event
							for (Card card1 : model.yourDeck)
								card1.getImage().removeEventHandler(MouseEvent.MOUSE_CLICKED, event);
						});

						view.lblNmbrOfDeckCards.setText(Integer.toString(model.yourDeck.size()));
						view.lblCrntHandCards.setText(Integer.toString(view.hboxHandCards.getChildren().size()));
						break;
					}

					if(model.currentPhase == Phase.Action){

					}


				}else if(msgIn.getType().equals(MessageType.CreateGame)){
					model.processCreateGame(msgIn);

				}else if(msgIn.getType().equals(MessageType.PlayerSuccess)){
					PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;
					// main.startSuccess(psmsg.getSuccess());
					// Spieler hat gewonnen: z. B. in Log anzeigen oder Bild anzeigen
					listenToServer = false;
				}
			}
		}
	}



	// Adrian
	// Über Main-Methode starten (nicht in Konstruktor, da die Strukturen
	// anfangs noch nicht befüllt werden sollen)
	public void fillPanes() {
		// Fill panes
	}

}// end GameApp_Controller