package Client_Services.Gallery;

import Cards.CardName;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Test extends Application{

	public static void main(String[] args){
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try{
		BorderPane root = new BorderPane();

		
		//Image image = new Image(Gallery.class.getResourceAsStream("Gold_Card.jpg"));
		
		Enum card = CardName.Gold;
		
		String s = cardName.toString();
		
		Image image = new Image(Gallery.class.getResourceAsStream("cardName.toString()"));

		//Image image = new Image("/CardName.Gold_Card.jpg");
	    ImageView iv = new ImageView();
	    iv.setImage(image);
	    
	    root.setCenter(iv);
	    
	    Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
