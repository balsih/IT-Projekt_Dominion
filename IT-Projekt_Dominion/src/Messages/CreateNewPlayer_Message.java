package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This Message is to send the client's name and password to store it on the server's database.
 * The password should be encrypted by the client.
 * <li>Communication: client --> server
 * 
 * @author Lukas
 */
public class CreateNewPlayer_Message extends Message {

	private static final String ELEMENT_PASSWORD = "password";
	private String password;


	public CreateNewPlayer_Message(){
		super();
	}

	/**
	 * Adds the password <String> to XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element password = docIn.createElement(ELEMENT_PASSWORD);
		password.setTextContent(this.password);
		root.appendChild(password);
	}

	/**
	 * Creates the object chat <String> from XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_PASSWORD);
        if (tmpElements.getLength() > 0) {
            Element password = (Element) tmpElements.item(0);
            this.password = password.getTextContent();
        }
	}
	
	
	public String getPassword(){
		return this.password;
	}

	public void setPassword(String password){
		this.password = password;
	}
}//end CreateNewPlayer_Message