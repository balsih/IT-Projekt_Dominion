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
		
		// set on action and handling for languageSelectComboBox
//		view.languageSelectComboBox.setOnAction((event) -> {
//			try {
//				if (view.languageSelectComboBox.getValue() == "Deutsch" || view.languageSelectComboBox.getValue() == "German") {
//					sl.setTranslator(new Translator("de"));
//				} else {
//					sl.setTranslator(new Translator("en"));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		});
		
		// set on action and handling for singlePlayerBtn
		view.singlePlayerBtn.setOnAction((event) -> {
			try {
				model.sendGameMode(GameMode.Singleplayer);
				
				// if sucessfull -> back to Login screen
				main.startLogin();
				 view.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// set on action and handling for multiPlayerBtn
		view.multiPlayerBtn.setOnAction((event) -> {
			try {
				model.sendGameMode(GameMode.Multiplayer);
				
				// if sucessfull -> back to Login screen
				main.startLogin();
				 view.stop();
				 
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		//
		view.languageSelectComboBox.setOnAction((e) -> {
			String newLang = view.languageSelectComboBox.getValue();
			sl.setTranslator(new Translator(newLang));
			sl.getConfiguration().setLocalOption("Language", newLang);
			view.updateTexts();
		});
		
		// set on action and handling for quitBtn
		view.quitBtn.setOnAction((event) -> {
			view.stop();
		});
		
		// gets called when the window is closed -> save the language of the combobox to the local cfg File
		view.getStage().setOnHiding((e) -> {
			// save config file
			sl.getConfiguration().save();
			System.out.println("saved config");
		}); 
		
		
	}
}//end MainMenu_Controller