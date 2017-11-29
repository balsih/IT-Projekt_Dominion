package Client_CreatePlayer_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.GameApp_Model.UserInput;
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

		// set on action and handling for nameText
		view.nameText.textProperty().addListener((change) -> {	
			try {
				// check if fields are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex username/password
					boolean userName = model.checkUserInput(view.nameText.getText(), UserInput.clientName);
					boolean password = model.checkUserInput(view.passwordText.getText(), UserInput.password);
					view.saveBtn.setDisable(!(userName && password));
				} else {
					view.saveBtn.setDisable(true);
				}
				// model.doSoSomethingWith IP;
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
					view.saveBtn.setDisable(!(userName && password));
				} else {
					view.saveBtn.setDisable(true);
				}
				// model.doSoSomethingWith passwordText;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// set on action and handling for languageSelectComboBox
		view.languageSelectComboBox.setOnAction((event) -> {
			try {
				// model.doSoSomethingWith language;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// set on action and handling for saveBtn
		view.saveBtn.setOnAction((event) -> {
			try {
				model.sendCreateNewPlayer(view.nameText.getText(), view.passwordText.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// set on action and handling for backBtn
		view.backBtn.setOnAction((event) -> {
			try {
				 main.startLogin();
				 view.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

}// end CreatePlayer_Controller