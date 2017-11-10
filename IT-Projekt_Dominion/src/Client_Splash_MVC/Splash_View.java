package Client_Splash_MVC;

import java.net.URISyntaxException;

import Abstract_MVC.View;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
	
		Translator t;
		ServiceLocator sl; 
		
		sl = ServiceLocator.getServiceLocator();
		sl.setTranslator(new Translator("en"));
		t = sl.getTranslator();
		
		BorderPane root = new BorderPane();
		root.setId("splash");
		
        
		
		lblStatus = new Label(t.getString("splash.loading"));
        progress = new ProgressBar();
		
	
		VBox vBox = new VBox();
		root.setCenter(vBox);
		
		//vBox.setVgrow(lblStatus, Priority.ALWAYS);
		
		//root.setAlignment(vBox, Pos.CENTER);
		
		//vBox.setSpacing(50);

       
		vBox.getChildren().addAll(lblStatus, progress);
		
        
        Scene scene = new Scene(root, 300, 300, Color.TRANSPARENT);
        //scene.getStylesheets().addAll(
        
			// TODO Auto-generated catch block
			//e.printStackTrace();
	    //this.getClass().getResource("splash.css").toExternalForm());
		
        return scene;
	}
}//end Splash_View