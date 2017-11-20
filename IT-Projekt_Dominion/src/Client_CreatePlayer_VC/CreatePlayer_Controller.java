package Client_CreatePlayer_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import MainClasses.Dominion_Main;

/**
 * @author Rene
 * @version 1.0
 * @created 31-Okt-2017 17:03:44
 */
public class CreatePlayer_Controller extends Controller<GameApp_Model, CreatePlayer_View> {

	private Dominion_Main main;

	/**
	 * 
	 * @param main
	 * @param model
	 * @param view
	 */
	public CreatePlayer_Controller(Dominion_Main main, GameApp_Model model, CreatePlayer_View view) {
		super(model, view);

		// set on action and handling for ipText
		view.nameText.setOnAction((event) -> {
			try {
				// model.doSoSomethingWith IP;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// set on action and handling for connectBtn
		view.passwordText.setOnAction((event) -> {
			try {
				// model.connect;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// set on action and handling for connectBtn
		view.languageSelectComboBox.setOnAction((event) -> {
			try {
				// model.doSoSomethingWith setName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// set on action and handling for passwordText
		view.saveBtn.setOnAction((event) -> {
			try {
				// model.doSoSomethingWith passwordText;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// set on action and handling for loginBtn
		view.backBtn.setOnAction((event) -> {
			try {
				 main.startLogin();;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

}// end CreatePlayer_Controller