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
				//				fragen ob null
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

				if (msgIn.getType().equals(MessageType.Commit)) {
					// nothing toDo here

				} else if (msgIn.getType().equals(MessageType.UpdateGame)) {
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
						view.lblCurrentPhase.setText("Phase: Action");
						break;
					case Buy:
						view.lblCurrentPhase.setText("Phase: Buy");
						break;
					case CleanUp:
						view.lblCurrentPhase.setText("Phase: CleanUp");
						break;
					}

					// Displays the current player
					view.lblCurrentPlayer.setText(model.currentPlayer);
					
					// Updates the number of current actions
					view.lblNmbrOfCrntActions.setText(Integer.toString(model.actions));

					// Updates the number of current buys
					view.lblNmbrOfCrntBuys.setText(Integer.toString(model.buys));

					// Updates the number of current coins
					view.lblNmbrOfCrntCoins.setText(Integer.toString(model.coins));
					
					// Update the number of action cards, treasure cards and victory cards
					// Count hashmap
					// Labels für alle

				} else if (msgIn.getType().equals(MessageType.CreateGame)) {
					model.processCreateGame(msgIn);
					

				} else if (msgIn.getType().equals(MessageType.PlayerSuccess)) {
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