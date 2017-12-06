package Client_Services;

import java.util.ResourceBundle;

import Cards.CardName;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Rene
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
	public ImageView getImage(CardName cardName){
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
		String localeString = t.getCurrentLocale().getLanguage();
		String path = this.getClass().getResource("/Client_Services/ImageGallery/" + cardName.toString() + "_Card" + "_" + localeString + ".jpg").toExternalForm();
		Image image = new Image(path);
		return new ImageView(image);
	}
	

	
	
}//end Gallery