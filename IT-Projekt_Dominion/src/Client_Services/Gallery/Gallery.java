package Client_Services;

import java.util.ResourceBundle;

import Cards.CardName;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:04:31
 */
public class Gallery {

	private ResourceBundle imagesBundle;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();


	public Gallery(String language){

	}
	
	/**
	 * 
	 * @param key
	 */
//	public static ImageView getImage(String cardName){
//		Image image = new Image(Gallery.class.getResourceAsStream(cardName)); // name korrespondiert mit Bildname .jpg
//		return new ImageView(image);
//	}
	
	public static ImageView getImage(Enum cardName){
		Image image = new Image(Gallery.class.getResourceAsStream(cardName.toString())); // name korrespondiert mit Bildname .jpg
		return new ImageView(image);
	}
	
	
}//end Gallery