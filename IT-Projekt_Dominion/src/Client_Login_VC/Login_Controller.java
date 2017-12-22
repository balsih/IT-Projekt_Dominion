package Client_Login_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.GameApp_Model.UserInput;
import MainClasses.Dominion_Main;
import javafx.scene.input.KeyCode;

/**
 * Controller class for Login screen
 * Handling of action events for this GUI 
 * 
 * @author Rene Schwab
 */
public class Login_Controller extends Controller<GameApp_Model, Login_View> {
	
	public Login_Controller(Dominion_Main main, GameApp_Model model, Login_View view){
		super(model, view);
		
		// disables elements before user connects with the server 
		view.nameLbl.setDisable(true);
		view.nameText.setDisable(true);
		view.passwordLbl.setDisable(true);
		view.passwordText.setDisable(true);
		view.createNewPlayerBtn.setDisable(true);
		
		// set on action and handling for ipText
		view.ipText.textProperty().addListener((change) -> {
			try {
				// checks if textFields ipText and portText are empty
				if (!view.ipText.getText().isEmpty() && !view.portText.getText().isEmpty()) {
					// regex check  of textFields ipText and portText, enables connectBtn if ok 
					boolean ipAdresse = model.checkUserInput(view.ipText.getText(), UserInput.ipAddress); 
					boolean portNumber = model.checkUserInput(view.portText.getText(), UserInput.port); 
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
				// checks if textFields ipText and portText are empty
				if (!view.ipText.getText().isEmpty() && !view.portText.getText().isEmpty()) {
					// regex check  of textFields ipText and portText, enables connectBtn if ok 
					boolean ipAdresse = model.checkUserInput(view.ipText.getText(), UserInput.ipAddress);
					boolean portNumber = model.checkUserInput(view.portText.getText(), UserInput.port); 
					view.connectBtn.setDisable(!(ipAdresse && portNumber));
				} else {
					view.connectBtn.setDisable(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for portText, activates connectBtn over Enter Key if port text is focused
		view.portText.setOnKeyPressed((event) -> {
			try {
				// checks if textFields ipText and portText are empty
				if (!view.ipText.getText().isEmpty() && !view.portText.getText().isEmpty()) {
					// regex check  of textFields ipText and portText, enables connectBtn if ok 
					boolean ipAdresse = model.checkUserInput(view.ipText.getText(), UserInput.ipAddress);
					boolean portNumber = model.checkUserInput(view.portText.getText(), UserInput.port);
					view.connectBtn.setDisable(!(ipAdresse && portNumber));
					// activates connectBtn when enter key gets pressed
					if (event.getCode() == KeyCode.ENTER) {
						model.startBtnClickSound();
						String message = model.init(view.ipText.getText(), Integer.parseInt(view.portText.getText()));
						if (!model.getFailure()) {
							// enables the elements after connectBtn got clicked (login ok) 
							view.nameLbl.setDisable(false);
							view.nameText.setDisable(false);
							view.passwordLbl.setDisable(false);
							view.passwordText.setDisable(false);
							view.createNewPlayerBtn.setDisable(false);
						} else {
							view.loginAlert.setHeaderText(message);
							view.loginAlert.showAndWait(); // warning alert if login fails
						}
					}
				} else {
					view.connectBtn.setDisable(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for connectBtn 
		view.connectBtn.setOnAction((event) -> {
			try {	
				model.startBtnClickSound();
				String message = model.init(view.ipText.getText(), Integer.parseInt(view.portText.getText()));
				if(!model.getFailure()){	
					// enables the elements after connectBtn got clicked (login ok) 
					view.nameLbl.setDisable(false);
					view.nameText.setDisable(false);
					view.passwordLbl.setDisable(false);
					view.passwordText.setDisable(false);
					view.createNewPlayerBtn.setDisable(false);
				} else {
					view.loginAlert.setHeaderText(message);
					view.loginAlert.showAndWait(); // warning alert if login fails
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for nameText
		view.nameText.textProperty().addListener((change) -> {
			try {
				// checks if textFields nameText and passwordText are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex check  of textFields nameText and passwordText, enables loginBtn if ok
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
				// checks if textFields nameText and passwordText are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex check  of textFields nameText and passwordText, enables loginBtn if ok
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
		
		// set on action and handling for passwordText, presses loginBtn over Enter Key if passwordText is focused 
		view.passwordText.setOnKeyPressed((event) -> {
			try {
				// check if fields are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex username/password
					boolean userName = model.checkUserInput(view.nameText.getText(), UserInput.clientName);
					boolean password = model.checkUserInput(view.passwordText.getText(), UserInput.password);
					view.loginBtn.setDisable(!(userName && password));
					// activates loginBtn when enter key gets pressed 
					if (event.getCode() == KeyCode.ENTER) {
						model.startBtnClickSound();
						String message = model.sendLogin(view.nameText.getText(), view.passwordText.getText());
						if (model.getFailure()) {
							view.loginAlert.setHeaderText(message);
							view.loginAlert.showAndWait(); // warning alert if login fails
						}
					}
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