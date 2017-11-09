package Client_Splash_MVC;

import java.net.URISyntaxException;

import Abstract_MVC.View;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 17:06:18
 */
public class Splash_View extends View<Splash_Model> {

	protected ProgressBar progress;
	private Label lblStatus;
	
	private ImageView imageView;

	/**
	 * 
	 * @param stage
	 * @param model
	 */
	public Splash_View(Stage stage, Splash_Model model){
		super(stage, model);
		stage.initStyle(StageStyle.TRANSPARENT); // also undecorated
	}

	@Override
	protected Scene create_GUI(){

		BorderPane root = new BorderPane();
        root.setId("splash");
        
        lblStatus = new Label("Loading please wait...");
        root.setCenter(lblStatus);
        
		//Image image = new Image(getClass().getResource("waiting.gif").toURI().toString());
		//imageView = new ImageView(image);
        
        //root.setBottom(imageView);
        progress = new ProgressBar();
        HBox bottomBox = new HBox();
        bottomBox.setId("progressbox");
        bottomBox.getChildren().add(progress);
        root.setBottom(bottomBox);
        
        Scene scene = new Scene(root, 300, 300, Color.TRANSPARENT);
        //scene.getStylesheets().addAll(
       
			// TODO Auto-generated catch block
			//e.printStackTrace();
	    //this.getClass().getResource("splash.css").toExternalForm());
		
        return scene;
	}
}//end Splash_View