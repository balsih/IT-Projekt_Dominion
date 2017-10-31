package Client_Splash_MVC;

import Abstract_MVC.View;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

/**
 * @author Adrian
 * @version 1.0
 * @created 31-Okt-2017 17:06:18
 */
public class Splash_View extends View<Splash_Model> {

	private ProgressBar progress;


	/**
	 * 
	 * @param stage
	 * @param model
	 */
	public Splash_View(Stage stage, Splash_Model model){
		super(stage, model);
	}

	@Override
	protected Scene create_GUI(){
		return null;
	}
}//end Splash_View