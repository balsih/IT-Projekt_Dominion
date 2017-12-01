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
				view.stackpDiscard.getChildren().clear();
				view.stackpDiscard.getChildren().add(model.cardSelection.get(0).getImage());
				view.lblNmbrOfDiscards.setText(Integer.toString(model.yourDiscardPile.size()));

				//				ist karte gesetzt (nicht null)?, wenn ja auf discard pile setzen
				//				played cards und hand cards aus den boxen leeren?

				model.sendInteraction();
				break;
			case Cellar:
				private boolean onMouseClicked(MouseEvent e) {
					for (int i=0; i<view.hboxHandCards.getChildren().size(); i++) {
						ImageView imgView = (ImageView) view.hboxHandCards.getChildren().get(i);

						if (e.getSource() == imgView){
							view.hboxHandCards.getChildren().remove(i);
						}
					}
				}


				// Ok? --> 
				//				view.hboxHandCards.getChildren().clear();
				//				for (Card card : model.yourHandCards){
				//					view.hboxHandCards.getChildren().add(card.getImage());
				//				}
				view.lblNmbrOfCrntHandCards.setText(Integer.toString((model.yourHandCards.size())));
				view.lblNmbrOfDeckCards.setText(Integer.toString(model.yourDeck.size()));
				view.stackpDiscard.getChildren().add(model.yourDiscardPileTopCard.getImage());
				view.lblNmbrOfDiscards.setText(Integer.toString(model.yourDiscardPile.size()));
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
			case Mine:
				model.sendInteraction();
				break;
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

				if (msgIn instanceof Commit_Message) {
					// nothing toDo here

				} else if (msgIn instanceof UpdateGame_Message) {
					model.processUpdateGame(msgIn);
					
					// Updates the log
					if(model.newLog != null){
						String existingLog = view.txtaLog.getText();
						view.txtaLog.setText(existingLog.concat(model.newLog)+"\n");
						model.newLog = null;
					}

					// Updates the chat
					if(model.newChat != null){
						String existingMessages = view.txtaChatArea.getText();
						view.txtaChatArea.setText(existingMessages.concat(model.newChat)+"\n");
						view.txtfChatArea.setText("");
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

					// Displays the current player
					view.lblNameOfCurrentPlayer.setText(model.currentPlayer);

					// Updates the number of current actions
					view.lblNmbrOfCrntActions.setText(Integer.toString(model.actions));

					// Updates the number of current buys
					view.lblNmbrOfCrntBuys.setText(Integer.toString(model.buys));

					// Updates the number of current coins
					view.lblNmbrOfCrntCoins.setText(Integer.toString(model.coins));

					//					// Updates the number of current hand cards
					//					view.lblNmbrOfCrntHandCards.setText();
					//					
					//					// Updates the number of current discards
					//					view.lblNmbrOfDiscards.setText(value);
					//					
					//					// Updates the number of current deck cards
					//					view.lblNmbrOfDeckCards.setText(value);
					
					// Update the number of action cards, treasure cards and victory cards
					model.buyCards.get(CardName.Cellar); // number of Cellar Cards in buy cards

					//					view.lblNmbrOfCellarCards.setText();
					//					view.lblNmbrOfMarketCards.setText();
					//					view.lblNmbrOfRemodelCards.setText();
					//					view.lblNmbrOfSmithyCards.setText();
					//					view.lblNmbrOfWoodcutterCards.setText();
					//					view.lblNmbrOfWorkshopCards.setText();
					//					view.lblNmbrOfMineCards.setText();
					//					view.lblNmbrOfVillageCards.setText();
					//					
					//					view.lblNmbrOfGoldCards.setText();
					//					view.lblNmbrOfSilverCards.setText();
					//					view.lblNmbrOfCopperCards.setText();
					//					
					//					view.lblNmbrOfDuchyCards.setText();
					//					view.lblNmbrOfEstateCards.setText();
					//					view.lblNmbrOfProvinceCards.setText();

				} else if (msgIn instanceof CreateGame_Message) {
					model.processCreateGame(msgIn);

					// Sets the current phase
					view.lblCurrentPhase.setText(t.getString("buy.lblCurrentPhase")); // Phase: Buy

					// Sets the current player
					view.lblNameOfCurrentPlayer.setText(model.currentPlayer);

					// Sets the number of current actions
					view.lblNmbrOfCrntActions.setText(Integer.toString(model.actions));

					// Sets the number of current buys
					view.lblNmbrOfCrntBuys.setText(Integer.toString(model.buys));

					// Sets the number of current coins
					view.lblNmbrOfCrntCoins.setText(Integer.toString(model.coins));

					// Sets the number of hand cards
					view.lblNmbrOfCrntHandCards.setText("5");

					// Sets the number of discards
					view.lblNmbrOfDiscards.setText("0");

					// Sets the number of deck cards
					view.lblNmbrOfDeckCards.setText("5");

					// Sets the initial number of Action cards
					view.lblNmbrOfCellarCards.setText("10");
					view.lblNmbrOfMarketCards.setText("10");
					view.lblNmbrOfRemodelCards.setText("10");
					view.lblNmbrOfSmithyCards.setText("10");
					view.lblNmbrOfWoodcutterCards.setText("10");
					view.lblNmbrOfWorkshopCards.setText("10");
					view.lblNmbrOfMineCards.setText("10");
					view.lblNmbrOfVillageCards.setText("10");

					// Sets the initial number of Treasure cards
					view.lblNmbrOfGoldCards.setText("30");
					view.lblNmbrOfSilverCards.setText("30");
					view.lblNmbrOfCopperCards.setText("30");

					// Sets the initial number of Victory cards
					view.lblNmbrOfDuchyCards.setText("8");
					view.lblNmbrOfEstateCards.setText("8");
					view.lblNmbrOfProvinceCards.setText("8");

					// Disables chat while playing singleplayer mode
					if(model.gameMode.equals(GameMode.Singleplayer)){
						view.txtfChatArea.setDisable(true);
						view.btnSendChatArea.setDisable(true);
					}

					// Adds hand cards to the pane
					for(Card card : model.yourNewHandCards){
						view.hboxHandCards.getChildren().add(card.getImage());
					}

					// Adds deck flipside card
					Card flipsideCard = Card.getCard(CardName.Flipside);
					view.stackpDeck.getChildren().add(flipsideCard.getImage());

					// Adds Action cards
					Card cellarCard = Card.getCard(CardName.Cellar);
					view.vboxCellarCards.getChildren().add(0, cellarCard.getImage());	
					
					Card marketCard = Card.getCard(CardName.Market);
					view.vboxMarketCards.getChildren().add(0, marketCard.getImage());

					Card remodelCard = Card.getCard(CardName.Remodel);
					view.vboxRemodelCards.getChildren().add(0, remodelCard.getImage());

					Card smithyCard = Card.getCard(CardName.Smithy);
					view.vboxSmithyCards.getChildren().add(0, smithyCard.getImage());

					Card woodcutterCard = Card.getCard(CardName.Woodcutter);
					view.vboxWoodcutterCards.getChildren().add(0, woodcutterCard.getImage());
				
					Card workshopCard = Card.getCard(CardName.Workshop);
					view.vboxWorkshopCards.getChildren().add(0, workshopCard.getImage());
					
					Card mineCard = Card.getCard(CardName.Mine);
					view.vboxMineCards.getChildren().add(0, mineCard.getImage());
					
					Card villageCard = Card.getCard(CardName.Village);
					view.vboxVillageCards.getChildren().add(0, villageCard.getImage());
					
					// Adds Treasure cards
					Card goldCard = Card.getCard(CardName.Gold);
					view.vboxGoldCards.getChildren().add(0, goldCard.getImage());
					
					Card silverCard = Card.getCard(CardName.Silver);
					view.vboxSilverCards.getChildren().add(0, silverCard.getImage());
					
					Card copperCard = Card.getCard(CardName.Copper);
					view.vboxCopperCards.getChildren().add(0, copperCard.getImage());
					
					// Adds Victory cards
					Card duchyCard = Card.getCard(CardName.Duchy);
					view.vboxDuchyCards.getChildren().add(0, duchyCard.getImage());
					
					Card estateCard = Card.getCard(CardName.Estate);
					view.vboxEstateCards.getChildren().add(0, estateCard.getImage());
					
					Card provinceCard = Card.getCard(CardName.Province);
					view.vboxProvinceCards.getChildren().add(0, provinceCard.getImage());
					}

				} else if (msgIn instanceof PlayerSuccess_Message) {
					PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;
					// main.startSuccess(psmsg.getSuccess());
					// Spieler hat gewonnen: z. B. in Log anzeigen oder Bild anzeigen
					listenToServer = false;
				}
			}
		}
	}
}// end GameApp_Controller