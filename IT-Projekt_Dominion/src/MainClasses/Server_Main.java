package MainClasses;

import java.util.logging.Level;
import java.util.logging.Logger;

import Server_MVC.Server_Controller;
import Server_MVC.Server_Model;
import Server_MVC.Server_View;
import Server_MVC.TextAreaHandler;
import javafx.application.Application;
import javafx.stage.Stage;

/** 
 *  Launches the Server Application and implements a TextAreaHandler.
 * 
 *  @author Bodo Gruetter
 *  source: Prof. Bradley Richards
 */
public class Server_Main extends Application {

	private Server_Model model;
	private Server_View view;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// GUI will contain log output from our own handler
		TextAreaHandler textAreaHandler = new TextAreaHandler();
		textAreaHandler.setLevel(Level.INFO);
		Logger defaultLogger = Logger.getLogger("");
		defaultLogger.addHandler(textAreaHandler);

		// Initialize the GUI
		this.model = new Server_Model();
		this.view = new Server_View(stage, this.model, textAreaHandler.getTextArea());
		new Server_Controller(this.model, this.view);

		// Display the GUI
		view.start();
	}
}//end Server_Main