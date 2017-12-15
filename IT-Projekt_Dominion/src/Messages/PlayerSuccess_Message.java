package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Server_GameLogic.Player;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:20
 */
public class PlayerSuccess_Message extends Message {

	private static final String ELEMENT_PLAYER_NAME = "playerName";
	private static final String ELEMENT_SUCCESS = "success";
	private static final String ELEMENT_VICTORY_POINTS = "victoryPoints";
	private final Integer NUM_OF_PLAYERS = 2;
	
	private Player player1;
	private Player player2;


	public PlayerSuccess_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
        
        for(int i = 0; i < this.NUM_OF_PLAYERS; i++){
        	Player player = null;
        	if(i == 0){
        		player = this.player1;
        	}else if(i == 1){
        		player = this.player2;
        	}
        	
            Element playerElement = docIn.createElement(ELEMENT_PLAYER_NAME);
            playerElement.setTextContent(player.getPlayerName());
            root.appendChild(playerElement);
            
            Element success = docIn.createElement(ELEMENT_SUCCESS);
            success.setTextContent(player.getStatus().toString());
            playerElement.appendChild(success);
            
            Element victoryPoints = docIn.createElement(ELEMENT_VICTORY_POINTS);
            victoryPoints.setTextContent(Integer.toString(player.getVictoryPoints()));
            playerElement.appendChild(victoryPoints);
        }
	}


	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_PLAYER_NAME);
		
        for(int i = 0; i < tmpElements.getLength(); i ++) {
        	
            Element playerName = (Element) tmpElements.item(i);
            Player player = new Player(playerName.getTextContent(), null);
            
    		tmpElements = playerName.getElementsByTagName(ELEMENT_SUCCESS);
            if (tmpElements.getLength() > 0) {
                Element success = (Element) tmpElements.item(0);
                player.setStatus(GameSuccess.parseGameSuccess(success.getTextContent()));
            }
            
            tmpElements = playerName.getElementsByTagName(ELEMENT_VICTORY_POINTS);
            if(tmpElements.getLength() > 0){
            	Element victoryPoints = (Element) tmpElements.item(0);
            	player.setVictoryPoints(Integer.parseInt(victoryPoints.getTextContent()));;
            }
            
            if(i == 0){
        		this.player1 = player;
        	}else if(i == 1){
        		this.player2 = player;
        	}
        }
	}
	
	
	
	public Player getPlayer1(){
		return this.player1;
	}
	
	public Player getPlayer2(){
		return this.player2;
	}
	
	
	public void setPlayer1(Player player1){
		this.player1 = player1;
	}
	
	public void setPlayer2(Player player2){
		this.player2 = player2;
	}
}//end PlayerSuccess_Message