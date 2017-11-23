package Client_GameApp_MVC;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		GameApp_Model model = new GameApp_Model();
		GameApp_View view = new GameApp_View(stage, model);
		GameApp_Controller controller = new GameApp_Controller(model, view);
		view.start();
		
	}
}
