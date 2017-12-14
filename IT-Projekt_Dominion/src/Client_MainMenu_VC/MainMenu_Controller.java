package Client_MainMenu_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.GameApp_Model.UserInput;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import MainClasses.Dominion_Main;
import Server_GameLogic.GameMode;
import javafx.application.Platform;

/**
 * @author Rene
 * @version 1.0
 * @created 31-Okt-2017 17:05:00
 */
public class MainMenu_Controller extends Controller<GameApp_Model, MainMenu_View> {
	
	
	private ServiceLocator sl;

	/**
	 * 
	 * @param main
	 * @param model
	 * @param view
	 */
	public MainMenu_Controller(Dominion_Main main, GameApp_Model model, MainMenu_View view){
		super(model, view);
		
		sl = ServiceLocator.getServiceLocator();
			
		
		// set on action and handling for singlePlayerBtn
		view.singlePlayerBtn.setOnAction((event) -> {
			try {
				model.startBtnClickSound();
				String message = model.sendGameMode(GameMode.Singleplayer);
				model.setGameMode(GameMode.Singleplayer);
				if (model.getFailure()) {
					view.startGameAlert.setHeaderText(message);
					view.startGameAlert.showAndWait(); // warning alert if Singleplayer start fails 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		// set on action and handling for multiPlayerBtn
		view.multiPlayerBtn.setOnAction((event) -> {
			try {
				model.startBtnClickSound();
				String message = model.sendGameMode(GameMode.Multiplayer);
				model.setGameMode(GameMode.Multiplayer);
				if (model.getFailure()) {
					view.startGameAlert.setHeaderText(message);
					view.startGameAlert.showAndWait(); // warning alert if Multiplayer start fails 
				}				 
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
				
		
		// sets values of the combobox and updates the texts to selected language 
		// and set a new Translator with the selected language 
		view.languageSelectComboBox.setOnAction((e) -> {
			String newLang = view.languageSelectComboBox.getValue();
			Translator translator = new Translator(newLang);
			sl.setTranslator(translator);
			sl.getConfiguration().setLocalOption("Language", newLang);
			view.updateTexts();
			model.setTranslator(translator);
		});
		
		
		// set on action and handling for quitBtn
		view.quitBtn.setOnAction((event) -> {
			model.startBtnClickSound();
			view.stop();
			model.sendLogout();
			sl.getConfiguration().save();
		});
		
		
//		// gets called when the window is closed -> save the language of the combobox to the local cfg File
//		view.getStage().setOnHiding((e) -> {
//			// save config file
//			sl.getConfiguration().save();
//			//System.out.println("saved config");
//		}); 
		
		
		view.getStage().setOnCloseRequest((event) -> {
			model.sendLogout();
			sl.getConfiguration().save();
		});
		
	}
}//end MainMenu_Controller