package Messages;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import Server_GameLogic.ServerThreadForClient;

/**
 * @author Bradley Richards
 * A simple example showing how to encapsulate messages in a class. This class sends and receives
 * some simple data via sockets. The data is formatted in XML.
 * 
 * Each message is uniquely identified with an ID and a timestamp. This can be useful, for example,
 * if you want to keep a log of messages.
 */
public abstract class Message {
    // Static names of elements and attributes that we use in the XML
    // We use these names to find particular elements in an XML document
    private static final String ELEMENT_MESSAGE = "message";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_CLIENT = "client";
    private static final String ATTR_ID = "id";
    private static final String ATTR_TIMESTAMP = "timestamp";
    
    // The XML document corresponding to a message object
    private Document xmlDocument;
    private String xmlString; // The xmlDocument as a string

    // Data included in a message
    private long id;
    private long timestamp;
    private String client = null;
    private MessageType type;

    // Generator for a unique message ID
    private static long messageID = 0;

    /**
     * Increment the global messageID
     * 
     * @return the next valid ID
     */
    private static long nextMessageID() {
        return messageID++;
    }

    /**
     * Constructor to create a new message
     * 
     * @param type What type of message to construct
     */
    protected Message() {
        this.id = -1;
        xmlDocument = null; // Not yet constructed
        xmlString = null; // Not yet converted
    }
    
    /**
     * Subclasses must fill in their own attributes from a received message
     */
    protected abstract void init(Document docIn);
    
    /**
     * Subclasses must create elements for their attributes in a message to be sent
     */
    protected abstract void addNodes(Document docIn);
    
    /**
     * Send this message, as an XML document, over the given socket
     * 
     * @param s The socket to use when sending the message
     */
    public void send(Socket s, ServerThreadForClient stfc) {
    	// Set the message id before sending (if not already done)
    	if (this.id == -1) this.id = nextMessageID();
    	
    	// Set the timestamp
    	this.timestamp = System.currentTimeMillis();
    	
    	// Convert to XML
        String xmlOut = this.toString();
   
        try { // Ignore IO errors
            OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
            out.write(xmlOut);
            out.flush();
            s.shutdownOutput(); // ends output without closing socket
        } catch (Exception e) {
        	Logger logger = Logger.getLogger("");
        	logger.severe("Message send(): "+e.toString());
        	if(stfc != null)
        		stfc.addUnsentMessages(this);
        }
    }    

    /**
     * Factory method to construct a message-object from XML received via socket
     * 
     * @param socket The socket to read from
     * @return the new message object, or null in case of an error
     * @throws Exception
     */
    public static Message receive(Socket socket) throws Exception {
    	// Read message until newline. We could just pass the socket.InputStream straight to
    	// InputSource (below). However, this way we can inspect the received XML before it
    	// disappears into the JAXP parser.
    	String xmlString = new String();
    	String line;
    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	while ( (line = in.readLine()) != null) {
    		xmlString += line;
    	}
    	byte[] xmlBytes = xmlString.getBytes();

    	// Pass the message to a DocumentBuilder, to extract and parse the XML document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDocument = builder.parse(new InputSource(new ByteArrayInputStream(xmlBytes)));
        
        // Get the root element of the document
        Element root = xmlDocument.getDocumentElement();
        
        // Get the message type from the root element and create the new message
        MessageType type = MessageType.parseType(root.getAttribute(ATTR_TYPE));
        Message newMessage;
        if (type == MessageType.AskForChanges) newMessage = new AskForChanges_Message();
        else if (type == MessageType.BuyCard) newMessage = new BuyCard_Message();
        else if (type == MessageType.Chat) newMessage = new Chat_Message();
        else if (type == MessageType.CreateGame) newMessage = new CreateGame_Message();
        else if (type == MessageType.CreateNewPlayer) newMessage = new CreateNewPlayer_Message();
        else if (type == MessageType.GameMode) newMessage = new GameMode_Message();
        else if (type == MessageType.HighScore) newMessage = new HighScore_Message();
        else if (type == MessageType.Login) newMessage = new Login_Message();
        else if (type == MessageType.Logout) newMessage = new Logout_Message();
        else if (type == MessageType.PlayCard) newMessage = new PlayCard_Message();
        else if (type == MessageType.PlayerSuccess) newMessage = new PlayerSuccess_Message();
        else if (type == MessageType.UpdateGame) newMessage = new UpdateGame_Message();
        else if (type == MessageType.Commit) newMessage = new Commit_Message();
        else if (type == MessageType.Failure) newMessage = new Failure_Message();
        else if (type == MessageType.GiveUp) newMessage = new GiveUp_Message();
        else if (type == MessageType.Interaction) newMessage = new Interaction_Message();
        else if (type == MessageType.Knock) newMessage = new Knock_Message();
        else if (type == MessageType.Request) newMessage = new Request_Message();
        else {
        	Error_Message msg = new Error_Message();
        	msg.setInfo("Error parsing received XML");
        	newMessage = msg;
        }
        newMessage.setType(type);
        if (type != MessageType.Error) {
	        newMessage.setId(Long.parseLong(root.getAttribute(ATTR_ID)));
	        newMessage.setTimestamp(Long.parseLong(root.getAttribute(ATTR_TIMESTAMP)));
	        newMessage.setClient(root.getAttribute(ATTR_CLIENT));
        }
        
        // Let the subclass read its additional attributes from the document
        newMessage.init(xmlDocument);
        
        return newMessage;
    }

    /**
     * Convert to a String representation
     * 
     * @return String representation of an XML document
     */
    @Override
    public String toString() {
        String xmlOut = null;

        buildMessage(); // Create XML document from "this"

        try { // Ignore all sorts of possible exceptions...
            // Set up a transformer that will convert from DOM to XML-text
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Set up the output stream
            StringWriter out = new StringWriter();

            // XML transformer requires special input/output classes
            DOMSource source = new DOMSource(xmlDocument);
            StreamResult result = new StreamResult(out);
            
            // Finally: send the XML to the output stream
            transformer.transform(source, result);
            xmlString = out.toString();
        } catch (Exception e) {
        	System.out.println(e);
        }
        return xmlString;
    }
    
    /**
     * Build an XML document from this message object
     */
    private void buildMessage() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.xmlDocument = builder.newDocument();
            Element eltMessage = xmlDocument.createElement(ELEMENT_MESSAGE);
            xmlDocument.appendChild(eltMessage);
            eltMessage.setAttribute(ATTR_ID, Long.toString(this.id));
            eltMessage.setAttribute(ATTR_TIMESTAMP, Long.toString(this.timestamp));
            eltMessage.setAttribute(ATTR_CLIENT, this.client);
            
            // Create the <type> attribute
            this.type = MessageType.getType(this);
            eltMessage.setAttribute(ATTR_TYPE, this.type.toString());

            // Let the subclass add additional nodes, as required
            this.addNodes(this.xmlDocument);
        } catch (ParserConfigurationException e) {
        }
    }

    // --- Getters and Setters ---
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getXmlString() {
		return xmlString;
	}
	
	public MessageType getType(){
		return this.type;
	}
	
	public void setType(MessageType type){
		this.type= type;
	}
	
}
//end Message