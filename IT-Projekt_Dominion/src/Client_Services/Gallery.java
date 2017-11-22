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
	//private ServiceLocator sl = ServiceLocator.getServiceLocator();


	public Gallery(String language){

	}
	
	/**
	 * 
	 * @param key
	 */
	public ImageView getImage(String cardName){
		String path = this.getClass().getResource("/Client_Services/ImageGallery/" + cardName + ".jpg").toExternalForm();
		System.out.println(path);
		Image image = new Image(path);
		return new ImageView(image);
	}
	

	
	
}//end Gallery