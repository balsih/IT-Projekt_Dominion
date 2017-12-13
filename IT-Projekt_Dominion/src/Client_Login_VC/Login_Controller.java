package Client_Login_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.GameApp_Model.UserInput;
import MainClasses.Dominion_Main;
import javafx.application.Platform;

/**
 * @author Rene
 * @version 1.0
 * @created 31-Okt-2017 17:04:51
 */
public class Login_Controller extends Controller<GameApp_Model, Login_View> {

	/**
	 * 
	 * @param main
	 * @param model
	 * @param view
	 */
	
	private Login_View view;
	
	public Login_Controller(Dominion_Main main, GameApp_Model model, Login_View view){
		super(model, view);
		
		// disables elements before user presses connectBtn after filled in IP and port
		// -> ev. mit change listener steuern... 
		view.nameLbl.setDisable(true);
		view.nameText.setDisable(true);
		view.passwordLbl.setDisable(true);
		view.passwordText.setDisable(true);
		view.createNewPlayerBtn.setDisable(true);
		//view.quitBtn.setDisable(true);
		
		// set on action and handling for ipText
		view.ipText.textProperty().addListener((change) -> {
			try {
				if (!view.ipText.getText().isEmpty() && !view.portText.getText().isEmpty()) {
					boolean ipAdresse = model.checkUserInput(view.ipText.getText(), UserInput.ipAddress); 
					boolean portNumber = model.checkUserInput(view.portText.getText(), UserInput.clientName); // provisorisch
					view.connectBtn.setDisable(!(ipAdresse && portNumber));
				} else {
					view.connectBtn.setDisable(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for portText
		view.portText.textProperty().addListener((change) -> {
			try {
				if (!view.ipText.getText().isEmpty() && !view.portText.getText().isEmpty()) {
					boolean ipAdresse = model.checkUserInput(view.ipText.getText(), UserInput.ipAddress);
					boolean portNumber = model.checkUserInput(view.portText.getText(), UserInput.clientName); // provisorisch
					view.connectBtn.setDisable(!(ipAdresse && portNumber));
				} else {
					view.connectBtn.setDisable(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		// set on action and handling for connectBtn -> Methode nur noch aktuell bis init() angepasst wurde -> String return und ABfrage von boolean failure
		view.connectBtn.setOnAction((event) -> {
			try {	
				model.startBtnClickSound();
				String message = model.init(view.ipText.getText(), Integer.parseInt(view.portText.getText()));
				if(!model.getFailure()){
					//view.connectAlert.showAndWait(); -> keien Fehlermeldung vorgesehen
	
					// enables the elements again after connectBtn got clicked 
					view.nameLbl.setDisable(false);
					view.nameText.setDisable(false);
					view.passwordLbl.setDisable(false);
					view.passwordText.setDisable(false);
					view.createNewPlayerBtn.setDisable(false);
					//view.quitBtn.setDisable(false);
				} else {
					view.loginAlert.setHeaderText(message);
					view.loginAlert.showAndWait(); // warning alert if login fails
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// adapted/new method below:
		
		
//		// set on action and handling for connectBtn
//		view.connectBtn.setOnAction((event) -> {
//			try {
//				model.startBtnClickSound();
//				String message = model.init(view.ipText.getText(), Integer.parseInt(view.portText.getText()));
//				if (model.getFailure()) {
//					view.loginAlert.setHeaderText(message);
//					view.loginAlert.showAndWait(); // warning alert if login fails
//					view.connectAlert.showAndWait();
//				} else {
//					// enables the elements again after connectBtn got clicked
//					view.nameLbl.setDisable(false);
//					view.nameText.setDisable(false);
//					view.passwordLbl.setDisable(false);
//					view.passwordText.setDisable(false);
//					view.createNewPlayerBtn.setDisable(false);
//					view.quitBtn.setDisable(false);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		});

		
		
		// set on action and handling for nameText
		view.nameText.textProperty().addListener((change) -> {
			try {
				// check if fields are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex username/password
					boolean userName = model.checkUserInput(view.nameText.getText(), UserInput.clientName);
					boolean password = model.checkUserInput(view.passwordText.getText(), UserInput.password);
					view.loginBtn.setDisable(!(userName && password));
				} else {
					view.loginBtn.setDisable(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for passwordText
		view.passwordText.textProperty().addListener((change) -> {
			try {
				// check if fields are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex username/password
					boolean userName = model.checkUserInput(view.nameText.getText(), UserInput.clientName);
					boolean password = model.checkUserInput(view.passwordText.getText(), UserInput.password);
					view.loginBtn.setDisable(!(userName && password));
				} else {
					view.loginBtn.setDisable(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		// set on action and handling for loginBtn
		view.loginBtn.setOnAction((event) -> {
			try {
				model.startBtnClickSound();
				String message = model.sendLogin(view.nameText.getText(), view.passwordText.getText());
				if (model.getFailure()) {
					view.loginAlert.setHeaderText(message);
					view.loginAlert.showAndWait(); // warning alert if login fails
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		// set on action and handling for createNewPlayerBtn
		view.createNewPlayerBtn.setOnAction((event) -> {
			try {
				model.startBtnClickSound();
				main.startCreatePlayer();
				view.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for quitBtn
		view.quitBtn.setOnAction((event) -> {
			model.startBtnClickSound();
			view.stop();
		});
		
	}

}//end Login_Controller