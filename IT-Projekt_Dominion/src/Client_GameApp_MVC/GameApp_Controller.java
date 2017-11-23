package Client_GameApp_MVC;

import Abstract_MVC.Controller;
import MainClasses.Dominion_Main;
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
		// Sends a chat message to the server 
		this.view.btnSendChatArea.setOnAction(event -> {
			String existingMessages = this.view.txtaChatArea.getText();
			String newMessage = this.view.txtfChatArea.getText();
			if (newMessage.length() > 0) {
				if (existingMessages.length() == 0)
					// to delete: this.view.txtaChatArea.setText(this.model.sendChat(newMessage));
					this.model.sendChat(newMessage);
				else
					// the chat history also needs to be displayed
					// to delete: this.view.txtaChatArea.setText(this.model.sendChat(existingMessages + "\n" + newMessage));
					this.model.sendChat(existingMessages + "\n" + newMessage);
				// Wo den Text setzen? So?
				this.view.txtfChatArea.setText("");
			}
		});
		
		this.view.btnCommit.setOnAction(event -> {
			// Phase abfragen, um zu wissen, was der Commit auslösen soll
		});

	}

	// Adrian
	// Über Main-Methode starten (nicht in Konstruktor, da die Strukturen
	// anfangs noch nicht befüllt werden sollen)
	public void fillPanes() {
		// Fill panes
	}

}// end GameApp_Controller