package Client_Splash_MVC;

import Abstract_MVC.View;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * View class for Splash screen 
 * Defines controls and elements of this GUI, aligns and styles them.
 * 
 * @author Rene Schwab
 */
public class Splash_View extends View<Splash_Model> {

	protected ProgressBar progress;
	private Label lblStatus;
	

	public Splash_View(Stage stage, Splash_Model model){
		super(stage, model);
	}

	@Override
	protected Scene create_GUI(){

		lblStatus = new Label("Loading...");
		lblStatus.setTextAlignment(TextAlignment.CENTER);
		lblStatus.setId("lblStatus");
		
        progress = new ProgressBar();
		progress.setId("progress");
		
		VBox root = new VBox(lblStatus, progress);
		root.setId("root");
		
		root.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(root, 300, 300);
        
        scene.getStylesheets().addAll(this.getClass().getResource("Splash.css").toExternalForm());
		
        return scene;
	}
}//end Splash_View