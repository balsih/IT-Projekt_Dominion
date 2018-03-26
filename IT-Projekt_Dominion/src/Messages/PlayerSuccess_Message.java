package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Server_GameLogic.Player;

/**
 * This Message is to tell the client's weather they won or lost.
 * <ul>
 * It includes the winner's AND the loser's data.
 * <li>playerName:		the player's name <String>
 * <li>success:			the player's success (<GameSuccess> Won or Lost)
 * <li>victoryPoints:	the player's collected victoryPoints <Integer>
 * </ul>
 * <p><li>Communication: server --> client
 * 
 * @author Lukas
 */
public class PlayerSuccess_Message extends Message {

	private static final String ELEMENT_PLAYER = "player";
	private static final String ATTR_PLAYER_NAME = "playerName";
	private static final String ELEMENT_SUCCESS = "success";
	private static final String ELEMENT_VICTORY_POINTS = "victoryPoints";
	private static final String ELEMENT_MOVES = "moves";
	private Integer numOfPlayers = 2;
	
	private Player player1 = null;
	private Player player2 = null;


	public PlayerSuccess_Message(){
		super();
	}

	/**
	 * Adds the player's (Player) content to XML 
	 * (playerName (String), success (GameSuccess), victoryPoints (Integer))
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
        
        for(int i = 0; i < this.numOfPlayers; i++){
        	Player player = null;
        	if(i == 0){
        		player = this.player1;
        	}else if(i == 1){
        		player = this.player2;
        	}
        	
            Element playerElement = docIn.createElement(ELEMENT_PLAYER);
            playerElement.setAttribute(ATTR_PLAYER_NAME, player.getPlayerName());
            root.appendChild(playerElement);
            
            Element success = docIn.createElement(ELEMENT_SUCCESS);
            success.setTextContent(player.getStatus().toString());
            playerElement.appendChild(success);
            
            Element victoryPoints = docIn.createElement(ELEMENT_VICTORY_POINTS);
            victoryPoints.setTextContent(Integer.toString(player.getVictoryPoints()));
            playerElement.appendChild(victoryPoints);
            
            Element moves = docIn.createElement(ELEMENT_MOVES);
            moves.setTextContent(Integer.toString(player.getMoves()));
            playerElement.appendChild(moves);
        }
	}


	/**
	 * Creates the object player1 and player2 (Player) from XML
	 * (playerName (String), success (GameSuccess), victoryPoints (Integer))
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_PLAYER);
        for(int i = 0; i < tmpElements.getLength(); i ++) {
        	
            Element playerElement = (Element) tmpElements.item(i);
            if(playerElement.hasAttribute(ATTR_PLAYER_NAME)){
            	String playerName = playerElement.getAttribute(ATTR_PLAYER_NAME);
            	
                Player player = new Player(playerName, null);
                
        		NodeList subElements = playerElement.getElementsByTagName(ELEMENT_SUCCESS);
                if(subElements.getLength() > 0) {
                    Element success = (Element) subElements.item(0);
                    player.setStatus(GameSuccess.parseGameSuccess(success.getTextContent()));
                }
                
                subElements = playerElement.getElementsByTagName(ELEMENT_VICTORY_POINTS);
                if(subElements.getLength() > 0){
                	Element victoryPoints = (Element) subElements.item(0);
                	player.setVictoryPoints(Integer.parseInt(victoryPoints.getTextContent()));;
                }
                
                subElements = playerElement.getElementsByTagName(ELEMENT_MOVES);
                if(subElements.getLength() > 0){
                	Element moves = (Element) subElements.item(0);
                	player.setMoves(Integer.parseInt(moves.getTextContent()));
                }
                
                if(i == 0){
            		this.player1 = player;
            	}else{
            		this.player2 = player;
            	}
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
	
	//just used if there is just 1 player to set
	public void setNumOfPlayers(Integer number){
		this.numOfPlayers = number;
	}
}//end PlayerSuccess_Message