package Client_GameApp_MVC;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainTest extends Application {
	private GameApp_Model model;
	private GameApp_View view;
	private GameApp_Controller controller;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		model = new GameApp_Model();
		view = new GameApp_View(stage, model);
		controller = new GameApp_Controller(model, view);
		view.start();
		
	}
}
