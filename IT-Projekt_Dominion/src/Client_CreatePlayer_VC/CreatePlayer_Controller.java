package Client_CreatePlayer_VC;

import java.util.Locale;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.GameApp_Model.UserInput;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import MainClasses.Dominion_Main;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;

/**
 * @author Rene
 * @version 1.0
 * @created 31-Okt-2017 17:03:44
 */
public class CreatePlayer_Controller extends Controller<GameApp_Model, CreatePlayer_View> {

	private Dominion_Main main;
	
	private ServiceLocator sl;
	private Translator t; 

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
			} catch (Exception e) {
				e.printStackTrace();
			}
		});	
		
		
		
		// set on action and handling for passwordText, activates saveBtn over Enter Key if PW text is focused
		view.passwordText.setOnKeyPressed((event) -> {
			try {
				// check if fields are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex username/password
					boolean userName = model.checkUserInput(view.nameText.getText(), UserInput.clientName);
					boolean password = model.checkUserInput(view.passwordText.getText(), UserInput.password);
					view.saveBtn.setDisable(!(userName && password));
					if (event.getCode() == KeyCode.ENTER) {
						model.startBtnClickSound();
						String message = model.sendCreateNewPlayer(view.nameText.getText(),view.passwordText.getText());
						if (model.getFailure()) {
							view.saveAlert.setHeaderText(message);
							view.saveAlert.showAndWait(); // warning alert if save fails
						}
					}

				} else {
					view.saveBtn.setDisable(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
				
				
		
		// set on action and handling for saveBtn
		view.saveBtn.setOnAction((event) -> {
			try {
				model.startBtnClickSound();
				String message = model.sendCreateNewPlayer(view.nameText.getText(), view.passwordText.getText());
				if (model.getFailure()) {
					view.saveAlert.setHeaderText(message);
					view.saveAlert.showAndWait(); // warning alert if save fails
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		// set on action and handling for backBtn
		view.backBtn.setOnAction((event) -> {
			try {
				model.startBtnClickSound();
				 main.startLogin();
				 view.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

}// end CreatePlayer_Controller