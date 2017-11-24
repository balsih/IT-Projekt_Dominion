package Client_GameApp_MVC;

import java.io.IOException;
import java.net.Socket;

import Abstract_MVC.Controller;
import MainClasses.Dominion_Main;
import Messages.AskForChanges_Message;
import Messages.Interaction;
import Messages.Message;
import Messages.MessageType;
import Messages.PlayerSuccess_Message;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author Adrian
 * 
 */
public class GameApp_Controller extends Controller<GameApp_Model, GameApp_View> {

	public GameApp_Controller(GameApp_Model model, GameApp_View view) {
		super(model, view);

		// Adrian
		// Sends a chat message to the server and sets text into the GUI
		this.view.btnSendChatArea.setOnAction(event -> {
			boolean success = false;
			String existingMessages = this.view.txtaChatArea.getText();
			String newMessage = this.view.txtfChatArea.getText();	
			
			if (newMessage.length() > 0) {
				if (existingMessages.length() == 0) {
					success = this.model.sendChat(newMessage); // newMessage gets saved into the variable newChat
				} else {
					// the chat history also needs to be displayed
					success = this.model.sendChat(existingMessages + "\n" + newMessage); // gets saved into the variable newChat
				}
				if (success) {
					this.view.txtaChatArea.setText(this.model.newChat);
					this.view.txtfChatArea.setText("");
					this.model.newChat = null;
				}
			}
		});

		this.view.btnCommit.setOnAction(event -> {
			
			// send interaction()
			// card --> in card selection, 

			// switch case interaction()
			// skip: keine message
			// Stimmen inhalte?
			// cellar: card.selection() = mind 1 karte
			// workshop: wahl aus feld
			// remodel: 1 karte
			// buy choice: aus feld


			// Phase abfragen, um zu wissen, was der Commit auslösen soll
		});
		
		public class ServerListening implements Runnable{

			@Override
			public void run() {
				while(model.listenToServer){
					try {
						Thread.sleep(100);//10 request per second if something changed in the game
					} catch (InterruptedException e1) {
						System.out.println(e1.toString());
					}
					Socket socket = connect();
					if(socket != null){
						AskForChanges_Message afcmsg = new AskForChanges_Message();	
						try{
							afcmsg.send(socket);
							Message msgIn = Message.receive(socket);
							if(msgIn.getType().equals(MessageType.Commit)){
								//nothing toDo here
							}else if(msgIn.getType().equals(MessageType.UpdateGame)){
								model.processUpdateGame(msgIn);
							}else if(msgIn.getType().equals(MessageType.CreateGame)){
								model.processCreateGame(msgIn);
							}else if(msgIn.getType().equals(MessageType.PlayerSuccess)){
								PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;
								//							main.startSuccess(psmsg.getSuccess());
								model.listenToServer = false;
							}
						}catch(Exception e){
							System.out.println(e.toString());
						}
						try { if (socket != null) socket.close(); } catch (IOException e) {}
					}
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