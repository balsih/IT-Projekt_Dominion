package Client_Services.ImageGallery;

import Cards.CardName;
import Client_Services.Configuration;
import Client_Services.Gallery;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
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
			// temp
			ServiceLocator sl;
			sl = ServiceLocator.getServiceLocator();
        	String language = "en";
        	sl.setTranslator(new Translator(language));
        	sl.setGallery(new Gallery(language));
			
		BorderPane root = new BorderPane();
		//Image image = new Image(Gallery.class.getResourceAsStream("Gold_Card.jpg"));
		Enum card = CardName.Gold;
//		String s = cardName.toString();
		
//		Image image = new Image(Gallery.class.getResourceAsStream("cardName.toString()"));
		Gallery g = ServiceLocator.getServiceLocator().getGallery();
		/*Image image = new Image("/Gold_Card.jpg");
	    ImageView iv = new ImageView();*/
//	    iv.setImage(image);
		ImageView iv = g.getImage(CardName.Village);
	    
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
