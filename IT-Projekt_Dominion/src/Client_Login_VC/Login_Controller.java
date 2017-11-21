package Client_Login_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import MainClasses.Dominion_Main;

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
				//model.doSoSomethingWith IP;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for connectBtn
		view.connectBtn.setOnAction((event) -> {
			try {
				//model.connect; 
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for connectBtn
		view.nameText.setOnAction((event) -> {
			try {
				// model.doSoSomethingWith setName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for passwordText
		view.passwordText.setOnAction((event) -> {
			try {
				// model.doSoSomethingWith passwordText;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// set on action and handling for loginBtn
		view.loginBtn.setOnAction((event) -> {
			try {
				// model.doSoSomethingWith loginBtn;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for createNewPlayerBtn
		view.createNewPlayerBtn.setOnAction((event) -> {
			try {
				// model.doSoSomethingWith createNewPlayerBtn;
				main.startCreatePlayer();
				view.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for quitBtn
		view.quitBtn.setOnAction((event) -> {
			try {
				// model.doSoSomethingWith quitBtn;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
	}
}//end Login_Controller