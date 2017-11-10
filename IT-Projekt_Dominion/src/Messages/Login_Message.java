package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The client wants to log in the system. For this purpose he has to be checked
 * in the database if clientName and password are correct. The password should be crypted by the client.
 * 
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:16
 */
public class Login_Message extends Message {

	private static final String ELEMENT_PASSWORD = "password";
	private String password;


	public Login_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element password = docIn.createElement(ELEMENT_PASSWORD);
		password.setTextContent(this.password);
		root.appendChild(password);
	}

	/**
	 * 
	 * @param docIn
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
}//end Login_Message