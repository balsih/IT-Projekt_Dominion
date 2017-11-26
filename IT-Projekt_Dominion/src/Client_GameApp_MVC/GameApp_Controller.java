package Client_GameApp_MVC;

import java.io.IOException;
import java.net.Socket;

import Abstract_MVC.Controller;
import Cards.Card;
import MainClasses.Dominion_Main;
import Messages.AskForChanges_Message;
import Messages.Interaction;
import Messages.Message;
import Messages.MessageType;
import Messages.PlayerSuccess_Message;
import Messages.UpdateGame_Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
				break;
			case EndOfTurn:
				view.stackpDiscard.getChildren().add(model.yourDiscardPileTopCard.getImage());
				view.lblNmbrOfDiscards.setText(Integer.toString(model.yourDiscardPile.size()));

				view.lblCurrentPlayer.setText(model.currentPlayer);
				view.lblNmbrOfCrntHandCards.setText(Integer.toString(model.opponentHandCards));
				view.lblNmbrOfCrntActions.setText(Integer.toString(model.actions));
				view.lblNmbrOfCrntBuys.setText(Integer.toString(model.buys));
				view.lblNmbrOfCrntCoins.setText(Integer.toString(model.coins));
				break;
			case Cellar:
				view.hboxHandCards.getChildren().clear();
				for (Card card : model.yourHandCards){
					view.hboxHandCards.getChildren().add(card.getImage());
				}
				view.lblNmbrOfCrntHandCards.setText(Integer.toString((model.yourHandCards.size())));
				view.lblNmbrOfDeckCards.setText(Integer.toString(model.yourDeck.size()));
				view.stackpDiscard.getChildren().add(model.yourDiscardPileTopCard.getImage());
				view.lblNmbrOfDiscards.setText(Integer.toString(model.yourDiscardPile.size()));
				break;
			case Workshop:
				// workshop: wahl aus feld
				break;
			case Remodel1:
				// remodel: 1 karte
				break;
			case Remodel2:
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
	 * Thread
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
					UpdateGame_Message ugmsg = (UpdateGame_Message) msgIn;

					if(ugmsg.getLog() != null){
						String existingLog = view.txtaLog.getText();
						view.txtaLog.setText(existingLog.concat(model.newLog)+"\n");
						model.newLog = null;
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