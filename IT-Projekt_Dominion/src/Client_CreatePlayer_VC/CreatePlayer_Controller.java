package Client_CreatePlayer_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.GameApp_Model.UserInput;
import MainClasses.Dominion_Main;
import javafx.scene.input.KeyCode;

/**
 * Controller class for CreatePlayer screen 
 * Handling of action events for this GUI 
 * 
 * @author Rene Schwab
 */
public class CreatePlayer_Controller extends Controller<GameApp_Model, CreatePlayer_View> {

	public CreatePlayer_Controller(Dominion_Main main, GameApp_Model model, CreatePlayer_View view) {
		super(model, view);

		// set on action and handling for nameText
		view.nameText.textProperty().addListener((change) -> {	
			try {
				// checks if textFields nameText and passwordText are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex check  of textFields nameText and passwordText, enables saveBtn if ok   
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
				// checks if textFields nameText and passwordText are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex check  of textFields nameText and passwordText, enables save button if ok
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
		
		
		// set on action and handling for passwordText, activates saveBtn over Enter Key if passwordText is focused
		view.passwordText.setOnKeyPressed((event) -> {
			try {
				// checks if textFields nameText and passwordText are empty
				if (!view.nameText.getText().isEmpty() && !view.passwordText.getText().isEmpty()) {
					// regex check  of textFields nameText and passwordText, enables save button if ok
					boolean userName = model.checkUserInput(view.nameText.getText(), UserInput.clientName);
					boolean password = model.checkUserInput(view.passwordText.getText(), UserInput.password);
					view.saveBtn.setDisable(!(userName && password));
					// activates saveBtn when enter key gets pressed 
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
		
		
		// set on action and handling for backBtn, back to login screen 
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