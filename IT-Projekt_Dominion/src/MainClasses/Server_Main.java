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
 * @author Bodo Grütter
 * @version 1.0
 * @created 31-Okt-2017 17:27:12
 */
public class Server_Main extends Application {

	private Server_Controller controller;
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
		this.controller = new Server_Controller(this.model, this.view);

		// Display the GUI
		view.start();
	}
	
	@Override
	public void init(){
		
	}

	@Override
	public void stop(){

	}
}//end Server_Main