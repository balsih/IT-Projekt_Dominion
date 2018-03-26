package Client_MainMenu_VC;

import Abstract_MVC.Controller;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import MainClasses.Dominion_Main;
import Server_GameLogic.GameMode;

/**
 * Controller class for MainMenu screen
 * Handling of action events for this GUI 
 * 
 * @author Rene Schwab
 */
public class MainMenu_Controller extends Controller<GameApp_Model, MainMenu_View> {
	
	private ServiceLocator sl;

	
	public MainMenu_Controller(Dominion_Main main, GameApp_Model model, MainMenu_View view){
		super(model, view);
		
		sl = ServiceLocator.getServiceLocator();
			
		
		// set on action and handling for singlePlayerBtn
		view.singlePlayerBtn.setOnAction((event) -> {
			try {
				// set game mode, leads to the start of a singleplayer game
				model.startBtnClickSound();
				String message = model.sendGameMode(GameMode.Singleplayer);
				model.setGameMode(GameMode.Singleplayer);
				if (model.getFailure()) {
					view.startGameAlert.setHeaderText(message);
					view.startGameAlert.showAndWait(); // warning alert if singleplayer start fails 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for multiPlayerBtn
		view.multiPlayerBtn.setOnAction((event) -> {
			try {
				// set game mode, leads to the start of a multiplayer game
				model.startBtnClickSound();
				String message = model.sendGameMode(GameMode.Multiplayer);
				model.setGameMode(GameMode.Multiplayer);
				if (model.getFailure()) {
					view.startGameAlert.setHeaderText(message);
					view.startGameAlert.showAndWait(); // warning alert if multiplayer start fails 
				}				 
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
				
		// set on action and handling for languageSelectComboBox
		// sets values of the ComboBox and set a new Translator with the selected language
		view.languageSelectComboBox.setOnAction((e) -> {
			String newLang = view.languageSelectComboBox.getValue();
			Translator translator = new Translator(newLang);
			sl.setTranslator(translator);
			sl.getConfiguration().setLocalOption("Language", newLang);
			// updates immediately the texts to selected language
			view.updateTexts();
			model.setTranslator(translator);
		});
		
		// set on action and handling for backBtn
		view.logoutBtn.setOnAction((event) -> {
			try {
				model.startBtnClickSound();
				main.startLogin();
				view.stop();
				model.sendLogout();
				sl.getConfiguration().save(); // saves the language to the local.cfg file
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for quitBtn
		view.quitBtn.setOnAction((event) -> {
			try {
			model.startBtnClickSound();
			view.stop();
			model.sendLogout();
			sl.getConfiguration().save(); // saves the language to the local.cfg file
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// gets called when the window is closed 
		view.getStage().setOnCloseRequest((event) -> {
			try {
				model.sendLogout();
				sl.getConfiguration().save(); // saves the language to the local.cfg file
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
	}
}//end MainMenu_Controller