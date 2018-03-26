package Client_Services;

import Cards.CardName;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** 
 * Gallery class containing the getImage method. 
 * 
 * @author Rene Schwab
 * 
 */
public class Gallery {

	public Gallery(String language){
	
	}
	
	/** 
	 * Gives back a ImageView with the image of the card. Image gets selected 
	 * over the cardName and current selected language. 
	 * 
	 * @author Rene Schwab
	 * 
	 * @param cardName
	 * , name of the card of which the corresponding image is required  
	 * @return ImageView 
	 * , image of the card
	 */
	public ImageView getImage(CardName cardName){
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
		String localeString = t.getCurrentLocale().getLanguage();
		String path = this.getClass().getResource("/Client_Services/ImageGallery/" + cardName.toString() + "_Card_" + localeString + ".jpg").toExternalForm();
		Image image = new Image(path);
		return new ImageView(image);
	}
	
}//end Gallery

