package Client_Login_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.GameApp_Model.UserInput;
import MainClasses.Dominion_Main;
import javafx.application.Platform;

/**
 * @author Lukas
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
		
		
		// set on action and handling for ipText
		view.ipText.setOnAction((event) -> {
			try {
				if (!view.ipText.getText().isEmpty()) {
					boolean ipAdresse = model.checkUserInput(view.ipText.getText(), UserInput.ipAddress);
					view.connectBtn.setDisable(!(ipAdresse));
				} else {
					view.connectBtn.setDisable(true);
				}
				// model.doSoSomethingWith setName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for connectBtn
		view.connectBtn.setOnAction((event) -> {
			try {
				//model.init(view.ipText.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for connectBtn
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
				// model.doSoSomethingWith setName;
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
				// model.doSoSomethingWith passwordText;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for loginBtn
		view.loginBtn.setOnAction((event) -> {
			try {
				model.sendLogin(view.nameText.getText(), view.passwordText.getText()); // sends Name and PW
				//sendLogin(String clientName, String password)
				// model.doSoSomethingWith loginBtn;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for createNewPlayerBtn
		view.createNewPlayerBtn.setOnAction((event) -> {
			try {
				main.startCreatePlayer();
				view.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for quitBtn
		view.quitBtn.setOnAction((event) -> {
			Platform.exit();
		});
		
	}

}//end Login_Controller