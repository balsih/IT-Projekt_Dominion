package Server_MVC;

import java.io.IOException;

import Abstract_MVC.Controller;
import javafx.collections.ListChangeListener;

/**
 * @author Bodo
 * @version 1.0
 * @created 31-Okt-2017 17:09:04
 */
public class Server_Controller extends Controller<Server_Model, Server_View> {

	private boolean portValid = false;

	/**
	 * 
	 * @param model
	 * @param view
	 */
	public Server_Controller(Server_Model model, Server_View view) {
		super(model, view);

		// changeListener that checks during the typing the portnumber
		view.txtPort.textProperty().addListener((observable, oldValue, newValue) -> {
			validatePortNumber(newValue);
		});

		// starting the server with a button click and disables this button
		view.btnStart.setOnAction((event) -> {
			try {
				view.btnStart.setDisable(true);
				view.btnStop.setDisable(false);
				model.startServerSocket(Integer.parseInt(view.txtPort.getText()));
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		view.btnStop.setOnAction((event) -> {
			view.btnStart.setDisable(false);
			view.btnStop.setDisable(true);
			model.stopServer();
		});
		
		view.stage.setOnCloseRequest(event -> model.stopServer());
		
		model.clients.addListener((ListChangeListener<Client>) (event -> view.updateClients()));
	}

	/*
	 * sets port text green or red depending on if port value is valid Adapted
	 * from: Prof. Bradley Richards, Package:
	 * ch.fhnw.richards.lecture02.email_validator, Class: EmailValidator_Model
	 */
	private void validatePortNumber(String newValue) {
		boolean valid = model.isValidPortNumber(newValue);

		if (valid) {
			view.txtPort.setStyle("-fx-text-inner-color: green;");
		} else {
			view.txtPort.setStyle("-fx-text-inner-color: red;");
		}

		portValid = valid;

		enableDisableButton();
	}

	// enables Button if the Port is not valid
	private void enableDisableButton() {
		boolean valid = portValid;
		view.btnStart.setDisable(!valid);
	}
}// end Server_Controller