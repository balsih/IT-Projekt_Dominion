package Server_MVC;

import java.io.IOException;
import java.util.logging.Logger;
import Abstract_MVC.Controller;
import javafx.scene.input.KeyCode;

/**
 * The Server_Controller controls the interaction between Server_View and
 * Server_Model and handles the events.
 * 
 * @author Bodo Gruetter source: Prof. Bradley Richards
 */
public class Server_Controller extends Controller<Server_Model, Server_View> {

	private boolean portValid = false;
	private Logger logger = Logger.getLogger("");

	/**
	 * Handles the events between Server_View and Server_Model
	 * 
	 * @author Bodo Gruetter source: Prof. Bradley Richards
	 * 
	 * @param model
	 * , the Server_Model
	 * @param view
	 * , the Server_View
	 */
	public Server_Controller(Server_Model model, Server_View view) {
		super(model, view);

		// changeListener that checks during the typing the portnumber
		this.view.txtPort.textProperty().addListener((observable, oldValue, newValue) -> {
			this.validatePortNumber(newValue);
		});

		// starts the server with enter
		this.view.txtPort.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode().equals(KeyCode.ENTER)) {
				this.startServer();
			}
		});

		// starts the server with a button click
		this.view.btnStart.setOnAction((event) -> {
			this.startServer();
		});

		this.view.btnStop.setOnAction((event) -> {
			this.view.btnStart.setDisable(false);
			this.view.btnStop.setDisable(true);
			this.view.txtPort.setEditable(true);
			this.model.stopServer();
		});

		this.view.stage.setOnCloseRequest(event -> model.stopServer());
	}

	/**
	 * Sets port text green or red depending on if port value is valid.
	 * 
	 * @author Bodo Gruetter source: Prof. Bradley Richards
	 * 
	 * @param newValue
	 *            , the new input value.
	 */
	private void validatePortNumber(String newValue) {
		boolean valid = model.isValidPortNumber(newValue);

		if (valid) {
			this.view.txtPort.setStyle("-fx-text-inner-color: green;");
		} else {
			this.view.txtPort.setStyle("-fx-text-inner-color: red;");
		}

		portValid = valid;

		this.enableDisableButton();
	}

	/**
	 * Enables Button if the Port is not valid.
	 * 
	 * @author Bodo Gruetter source: Prof. Bradley Richards
	 */
	private void enableDisableButton() {
		boolean valid = portValid;
		this.view.btnStart.setDisable(!valid);
	}

	/**
	 * Starts server and enable/disables buttons
	 * 
	 * @author Bodo Gruetter
	 */
	private void startServer() {
		if (this.view.txtPort.getText().length() > 0) {
			try {
				this.view.btnStart.setDisable(true);
				this.view.btnStop.setDisable(false);
				this.view.txtPort.setEditable(false);
				this.model.startServerSocket(Integer.parseInt(view.txtPort.getText()));
			} catch (NumberFormatException | IOException e) {
				this.logger.severe(e.toString());
			}
		}
	}
}// end Server_Controller