package MainClasses;

import Abstract_MVC.Controller;
import Abstract_MVC.Model;
import Abstract_MVC.View;
import Cards.CardName;
import Client_CreatePlayer_VC.CreatePlayer_Controller;
import Client_CreatePlayer_VC.CreatePlayer_View;
import Client_GameApp_MVC.GameApp_Controller;
import Client_GameApp_MVC.GameApp_Model;
import Client_GameApp_MVC.GameApp_View;
import Client_Login_VC.Login_Controller;
import Client_Login_VC.Login_View;
import Client_MainMenu_VC.MainMenu_Controller;
import Client_MainMenu_VC.MainMenu_View;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import Client_Splash_MVC.Splash_Controller;
import Client_Splash_MVC.Splash_Model;
import Client_Splash_MVC.Splash_View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Rene
 * @version 1.0
 * @created 31-Okt-2017 17:28:23
 */
public class Dominion_Main extends Application {
	
	Splash_View splash_View = null;
	MainMenu_View mainMenu_View = null;
	Login_View login_View = null;
	CreatePlayer_View createPlayer_View = null;
	GameApp_View gameApp_View = null;
	ServiceLocator sl;
	
	GameApp_Model model;

	public Dominion_Main(){

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception{
		Splash_Model splashModel = new Splash_Model();
		splash_View = new Splash_View(stage, splashModel);
		Splash_Controller splashController = new Splash_Controller(this, splashModel, splash_View);
		
		splash_View.start();
		splashModel.initialize();
		
	}
	
	
	public void startLogin(){
		splash_View.stop(); // Hides splashscreen
		sl = ServiceLocator.getServiceLocator();
		
		if (this.model == null) {
			this.model = new GameApp_Model(this);
		}
		this.login_View = new Login_View(new Stage(), this.model);
		Login_Controller controller = new Login_Controller(this, this.model, this.login_View);
		
		this.login_View.start();
	}
	
	
	public void startCreatePlayer(){
		this.createPlayer_View = new CreatePlayer_View(new Stage(), model);
		CreatePlayer_Controller controller = new CreatePlayer_Controller(this, model, this.createPlayer_View);
		this.createPlayer_View.start();

		if(this.login_View != null){
			this.login_View.stop();
			this.login_View = null;
		}
	}
	
	// Create game mvc
	public void startGameApp(){		
		this.gameApp_View = new GameApp_View(new Stage(), model);
		GameApp_Controller controller = new GameApp_Controller(model, this.gameApp_View);
		this.gameApp_View.start();
		this.mainMenu_View.stop();
		model.startMediaPlayer("Celtic_Music.mp3"); // starts new sound for GameApp 
	}
	
	
	public void startMainMenu(){
		this.mainMenu_View = new MainMenu_View(new Stage(), model);
		MainMenu_Controller controller = new MainMenu_Controller(this, model, this.mainMenu_View);
		this.mainMenu_View.start();
		
		if(this.login_View != null){
			this.login_View.stop();
			this.login_View = null;
		} else if(this.createPlayer_View != null){
			this.createPlayer_View.stop();
			this.createPlayer_View = null;
		}
	}
	
	@Override
	public void init(){
	
	}
	
	@Override
	public void stop(){
	
	}
}//end Dominion_Main