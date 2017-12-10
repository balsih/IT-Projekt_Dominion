package Messages;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.istack.internal.logging.Logger;

import Cards.Card;
import Cards.CardName;
import Server_GameLogic.Phase;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:22
 */
public class UpdateGame_Message extends Message {

	private static final String ELEMENT_DECKPILE = "deckPile";
	private static final String ELEMENT_NEW_HANDCARD = "newHandCard";

	private static final String ELEMENT_CHAT = "chat";
	private static final String ELEMENT_CURRENT_PHASE = "currentPhase";
	private static final String ELEMENT_CURRENT_PLAYER = "currentPlayer";
	private static final String ELEMENT_LOG = "log";
	private String chat = null;
	// enum Phase
	private String currentPhase = null;
	private String currentPlayer = null;
	private String log = null;

	private static final String ELEMENT_BUYS = "buys";
	private static final String ELEMENT_ACTIONS = "actions";
	private static final String ELEMENT_COINS = "coins";
	private static final String ATTR_DECKPILE_CARD_NUMBER = "deckPileCardNumber";
	private static final String ATTR_DISCARDPILE_CARD_NUMBER = "discardPileCardNumber";
	private Integer buys = null;
	private Integer actions = null;
	private Integer coins = null;
	private Integer deckPileCardNumber = null;
	private Integer discardPileCardNumber = null;

	private static final String ELEMENT_BUYEDCARD = "buyedCard";
	private static final String ELEMENT_PLAYEDCARD = "playedCard";
	private static final String ELEMENT_DISCARDPILE_TOP_CARD = "discardPileTopCard";
	private Card buyedCard = null;
	private Card playedCard = null;
	private Card discardPileTopCard = null;

	private static final String ELEMENT_INTERACTION = "interaction";
	private static final String ATTR_INTERACTION_TYPE = "interactionType";
	private static final String ELEMENT_CARDSELECTION = "cardSelection";
	private Interaction interactionType = null;
	private HashMap<String, LinkedList<CardName>> cardSelectionElements;

	private static final String ELEMENT_NEW_HANDCARDS = "newHandCards";
	private HashMap<String, String> stringElements;
	private HashMap<String, Integer> integerElements;
	private HashMap<String, Card> cardElements;
	private HashMap<String, LinkedList<Card>> handCardListElements;

	private HashMap<String, String> attrElements;
	private HashMap<String, String> attrValues;

	/**
	 * Constructor fills the Top-Level XML List for simpler adding
	 */
	public UpdateGame_Message() {
		super();
		// Top_Level Elements
		this.stringElements = new HashMap<String, String>();
		this.integerElements = new HashMap<String, Integer>();
		this.cardElements = new HashMap<String, Card>();
		this.handCardListElements = new HashMap<String, LinkedList<Card>>();
		this.handCardListElements.put(ELEMENT_NEW_HANDCARD, null);
		this.cardSelectionElements = new HashMap<String, LinkedList<CardName>>();
		this.cardSelectionElements.put(ELEMENT_CARDSELECTION, null);

		// Top_Level Attributes
		this.attrValues = new HashMap<String, String>();
		this.attrElements = new HashMap<String, String>();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn) {
		Element root = docIn.getDocumentElement();

		initializeMaps();

		// adds the Top-Level Elements to XML
		this.addContentElements(docIn, root, this.stringElements);
		this.addContentElements(docIn, root, this.integerElements);
		this.addContentElements(docIn, root, this.cardElements);

		// adds the Top-Level Element HANDCARDS to XML and the subElements HANDCARD
		if (this.handCardListElements.get(ELEMENT_NEW_HANDCARD) != null) {
			Element handCardsElement = docIn.createElement(ELEMENT_NEW_HANDCARDS);
			this.addContentElements(docIn, handCardsElement, this.handCardListElements);
			root.appendChild(handCardsElement);
		}

		// adds the Top_Level Element INTERACTION to XML and the subElements CARDSELECTION
		if (this.interactionType != null) {
			Element interaction = docIn.createElement(ELEMENT_INTERACTION);
			this.addContentElements(docIn, interaction, this.cardSelectionElements);
			root.appendChild(interaction);
		}
	}

	/**
	 * Stores the content of the variables into the HashMaps If the content is
	 * null, the Element just has no content, but probably an attribute. Per
	 * default they're in the stringElements
	 */
	private void initializeMaps() {
		this.stringElements.put(ELEMENT_CURRENT_PLAYER, this.currentPlayer);
		this.stringElements.put(ELEMENT_CURRENT_PHASE, this.currentPhase);
		this.stringElements.put(ELEMENT_CHAT, this.chat);
		this.stringElements.put(ELEMENT_LOG, this.log);
		this.stringElements.put(ELEMENT_DECKPILE, null);

		this.integerElements.put(ELEMENT_ACTIONS, this.actions);
		this.integerElements.put(ELEMENT_COINS, this.coins);
		this.integerElements.put(ELEMENT_BUYS, this.buys);

		this.cardElements.put(ELEMENT_PLAYEDCARD, this.playedCard);
		this.cardElements.put(ELEMENT_BUYEDCARD, this.buyedCard);
		this.cardElements.put(ELEMENT_DISCARDPILE_TOP_CARD, this.discardPileTopCard);

		// The values has to be null if they were not set. Necessary to ask if a content was set (not null)
		if (this.deckPileCardNumber != null) {
			this.attrValues.put(ATTR_DECKPILE_CARD_NUMBER, this.deckPileCardNumber.toString());
		} else {
			this.attrValues.put(ATTR_DECKPILE_CARD_NUMBER, null);
		}
		if (this.discardPileCardNumber != null) {
			this.attrValues.put(ATTR_DISCARDPILE_CARD_NUMBER, this.discardPileCardNumber.toString());
		} else {
			this.attrValues.put(ATTR_DISCARDPILE_CARD_NUMBER, null);
		}
		if (this.interactionType != null) {
			this.attrValues.put(ATTR_INTERACTION_TYPE, this.interactionType.toString());
		} else {
			this.attrValues.put(ATTR_INTERACTION_TYPE, null);
		}

		this.attrElements.put(ELEMENT_DECKPILE, ATTR_DECKPILE_CARD_NUMBER);
		this.attrElements.put(ELEMENT_DISCARDPILE_TOP_CARD, ATTR_DISCARDPILE_CARD_NUMBER);
		this.attrElements.put(ELEMENT_INTERACTION, ATTR_INTERACTION_TYPE);
	}
	
	/**
	 * @author Lukas
	 * Puts the content of the maps into the variables
	 */
	private void initializeVariables() {
		
		this.currentPlayer = this.stringElements.get(ELEMENT_CURRENT_PLAYER);
		this.currentPhase = this.stringElements.get(ELEMENT_CURRENT_PHASE);
		this.chat = this.stringElements.get(ELEMENT_CHAT);
		this.log = this.stringElements.get(ELEMENT_LOG);
		this.playedCard = this.cardElements.get(ELEMENT_PLAYEDCARD);
		this.buyedCard = this.cardElements.get(ELEMENT_BUYEDCARD);
		this.discardPileTopCard = this.cardElements.get(ELEMENT_DISCARDPILE_TOP_CARD);
		if(this.attrValues.get(ATTR_INTERACTION_TYPE) != null)
			this.interactionType = Interaction.parseInteraction(this.attrValues.get(ATTR_INTERACTION_TYPE));
		if (this.attrValues.get(ATTR_DISCARDPILE_CARD_NUMBER) != null)
			this.discardPileCardNumber = Integer.parseInt(this.attrValues.get(ATTR_DISCARDPILE_CARD_NUMBER));
		if (this.attrValues.get(ATTR_DECKPILE_CARD_NUMBER) != null)
			this.deckPileCardNumber = Integer.parseInt(this.attrValues.get(ATTR_DECKPILE_CARD_NUMBER));
		this.actions = this.integerElements.get(ELEMENT_ACTIONS);
		this.coins = this.integerElements.get(ELEMENT_COINS);
		this.buys = this.integerElements.get(ELEMENT_BUYS);
	}

	/**
	 * Helps the addNodes method to add elements in the given root If an element
	 * intends to have an attribute, it will be set
	 * 
	 * @param docIn
	 *            to create further elements
	 * @param root
	 *            the current root to add elements
	 * @param contents
	 *            generic HashMap, consists the elements or attributes as
	 *            keys(<String>) and the input as values(<T>)
	 */
	private <T> void addContentElements(Document docIn, Element root, HashMap<String, T> content) {
		// If the root contains an attribute, it will be set
		if (this.attrElements.containsKey(root.getTagName())
				&& this.attrValues.get(this.attrElements.get(root.getTagName())) != null)
			root.setAttribute(this.attrElements.get(root.getTagName()),
					this.attrValues.get(this.attrElements.get(root.getTagName())));
		Set<String> keys = content.keySet();
		for (String key : keys) {
			// If there is just one element but multiple possible Elements, the
			// content has to be unpacked from the LinkedList
			if (content.get(key) instanceof LinkedList && content.get(key) != null) {
				try {
					LinkedList<Card> cardList = (LinkedList<Card>) content.get(key);
					for (int i = 0; i < cardList.size(); i++) {
						Element element = docIn.createElement(key);
						// If an element contains an attribute, it will be set
						if (this.attrElements.containsKey(key)
								&& this.attrValues.get(this.attrElements.get(key)) != null)
							element.setAttribute(this.attrElements.get(key),
									this.attrValues.get(this.attrElements.get(key)));
						element.setTextContent(cardList.get(i).toString());
						root.appendChild(element);
					}
				} catch (Exception e) {
				}
				try {
					LinkedList<CardName> cardNameList = (LinkedList<CardName>) content.get(key);
					for (int i = 0; i < cardNameList.size(); i++) {
						Element element = docIn.createElement(key);
						// If an element contains an attribute, it will be set
						if (this.attrElements.containsKey(key)
								&& this.attrValues.get(this.attrElements.get(key)) != null)
							element.setAttribute(this.attrElements.get(key),
									this.attrValues.get(this.attrElements.get(key)));
						element.setTextContent(cardNameList.get(i).toString());
						root.appendChild(element);
					}
				} catch (Exception e) {
				}
				// If an element has just one content, it can be resolved the "normal" way (one element, one content)
			} else {
				Element element = docIn.createElement(key);
				boolean addContent = false;
				// If an element contains an attribute, it will be set
				if (this.attrElements.containsKey(key) && this.attrValues.get(this.attrElements.get(key)) != null) {
					element.setAttribute(this.attrElements.get(key),
							this.attrValues.get(this.attrElements.get(key)).toString());
					addContent = true;
				}
				// if content has changed, it will be set
				if (content.get(key) != null && content.get(key).toString().length() > 0) {
					element.setTextContent(content.get(key).toString());
					addContent = true;
				}
				if (addContent)
					root.appendChild(element);
			}
		}
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn) {
		Element root = docIn.getDocumentElement();
		initializeMaps();

		this.parseContent(root, this.stringElements);
		this.parseContent(root, this.integerElements);
		this.parseContent(root, this.cardElements);
		
		//The elements with deeper contents need their specific root for the parseContent method
		Element newHandCardsRoot = (Element) root.getElementsByTagName(ELEMENT_NEW_HANDCARDS).item(0);
		if(newHandCardsRoot != null)
			this.parseContent(newHandCardsRoot, this.handCardListElements);
		
		Element interactionRoot = (Element) root.getElementsByTagName(ELEMENT_INTERACTION).item(0);
		if(interactionRoot != null)
			this.parseContent((Element) root.getElementsByTagName(ELEMENT_INTERACTION).item(0), this.cardSelectionElements);
		
		
		initializeVariables();
	}


	/**
	 * Helps the init method to parse the content in the appropriate structure
	 * 
	 * @param root
	 *            consists the parsing elements
	 * @param content
	 *            generic HashMap, consist the elements or attributes as keys
	 *            (<String>) and the input as values (<T>)
	 */
	private <T> void parseContent(Element root, HashMap<String, T> content) {
		Set<String> keys = content.keySet();
		Set<String> attrKeys = this.attrValues.keySet();
		// If the root has an attribute, it will be stored in the HashMap
		// attrValues
		for (String attrKey : attrKeys) {
			if (root.hasAttribute(attrKey)) {
				this.attrValues.put(attrKey, root.getAttribute(attrKey));
			}
		}
		for (String key : keys) {
			NodeList tmpElements = root.getElementsByTagName(key);
			if (tmpElements.getLength() > 0) {
				Element element = (Element) tmpElements.item(0);
				// If the element has an attribute, it will be stored in the HashMap attrValues
				for (String attrKey : attrKeys) {
					if (element.hasAttribute(attrKey)) {
						this.attrValues.put(attrKey, element.getAttribute(attrKey));
					}
				}
				//Try to put the inputs into the generic specified HashMap. If it's not the correct map, it will skip into the next try
				try {
					this.stringElements.put(key, element.getTextContent());
				} catch (Exception e) {}
				try {
					this.integerElements.put(key, Integer.parseInt(element.getTextContent()));
				} catch (Exception e) {}
				try {
					this.cardElements.put(key, Card.getCard(CardName.parseName(element.getTextContent())));
				} catch (Exception e) {}
				try {
					if (key == ELEMENT_NEW_HANDCARD) {
						LinkedList<Card> newHandCards = new LinkedList<Card>();
						for (int i = 0; i < tmpElements.getLength(); i++) {
							Element cardElement = (Element) tmpElements.item(i);
							newHandCards.add(Card.getCard(CardName.parseName(cardElement.getTextContent())));
						}
						this.handCardListElements.put(key, newHandCards);
					}
				} catch (Exception e) {}
				try {
					if (key == ELEMENT_CARDSELECTION) {
						LinkedList<CardName> cardSelection = new LinkedList<CardName>();
						for (int i = 0; i < tmpElements.getLength(); i++) {
							Element cardNameElement = (Element) tmpElements.item(i);
							cardSelection.add(CardName.parseName(cardNameElement.getTextContent()));
						}
						this.cardSelectionElements.put(key, cardSelection);
					}
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * @author Bodo Gruetter & Lukas merges two UpdateGame_Messages together
	 * 
	 * @param first
	 * @param second
	 *            first message which gets merged with a second message
	 * @return the first message with the merged content
	 */
	public static UpdateGame_Message merge(UpdateGame_Message first, UpdateGame_Message second) {
		
		//If there was a change of currentPlayer, the states of the UpdateGame_Message has to be the reseted states
		if (first.actions != null && second.actions != null){
			if (first.currentPlayer == null){
				first.actions = second.actions;
			}	
		} else if (first.actions == null) {
			first.actions = second.actions;
		}
		if (first.buys != null && second.buys != null){
			if (first.currentPlayer == null){
				first.buys = second.buys;
			}	
		} else if (first.buys == null) {
			first.buys = second.buys;
		}
		if (first.coins != null && second.coins != null){
			if (first.currentPlayer == null){
				first.coins = second.coins;
			}	
		} else if (first.coins == null) {
			first.coins = second.coins;
		}
		
		//If the phase skipped twice automatically, the Phase.CleanUp will not be the correct phase
		if(first.currentPhase != null && second.currentPhase != null){
			if(Phase.parsePhase(first.currentPhase) == Phase.CleanUp){
				first.currentPhase = second.currentPhase;
			}
		}else if (first.currentPhase == null){
			first.currentPhase = second.currentPhase;
		}	
		
		//Concat the logs if there are multiple logs
		if (first.log != null && second.log != null){
			first.log += "=="+second.log;
		} else if (first.log == null){
			first.log = second.log;
		}
		
		if (first.chat == null)
			first.chat = second.chat;
		if (first.currentPlayer == null)
			first.currentPlayer = second.currentPlayer;
		if (first.deckPileCardNumber == null)
			first.deckPileCardNumber = second.deckPileCardNumber;
		if (first.discardPileCardNumber == null)
			first.discardPileCardNumber = second.discardPileCardNumber;
		if (first.discardPileTopCard == null)
			first.discardPileTopCard = second.discardPileTopCard;
		if (first.interactionType == null)
			first.interactionType = second.interactionType;
		if (first.getNewHandCards() == null)
			first.setNewHandCards(second.getNewHandCards());
		if (first.playedCard == null)
			first.playedCard = second.playedCard;
		if (first.buyedCard == null)
			first.buyedCard = second.buyedCard;
		if (first.interactionType == null)
			first.interactionType = second.interactionType;
		if (first.getCardSelection() == null)
			first.setCardSelection(second.getCardSelection());

		return first;
	}
	

	public String getCurrentPlayer() {
		return this.currentPlayer;
	}

	public Phase getCurrentPhase() {
		if (this.currentPhase != null)
			return Phase.parsePhase(this.currentPhase);
		return null;
	}

	public String getChat() {
		return this.chat;
	}

	public String getLog() {
		return this.log;
	}

	public Interaction getInteractionType() {
		return this.interactionType;
	}

	public Card getPlayedCard() {
		return this.playedCard;
	}

	public Card getBuyedCard() {
		return this.buyedCard;
	}

	public Card getDiscardPileTopCard() {
		return this.discardPileTopCard;
	}

	public Integer getDiscardPileCardNumber() {
		return this.discardPileCardNumber;
	}

	public Integer getDeckPileCardNumber() {
		return this.deckPileCardNumber;
	}

	public Integer getActions() {
		return this.actions;
	}

	public Integer getCoins() {
		return this.coins;
	}

	public Integer getBuys() {
		return this.buys;
	}

	public LinkedList<Card> getNewHandCards() {
		return this.handCardListElements.get(ELEMENT_NEW_HANDCARD);
	}

	public LinkedList<CardName> getCardSelection() {
		return this.cardSelectionElements.get(ELEMENT_CARDSELECTION);
	}
	
	

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void setCurrentPhase(Phase currentPhase) {
		this.currentPhase = currentPhase.toString();
	}

	public void setChat(String chat) {
		this.chat = chat;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public void setInteractionType(Interaction interactionType) {
		this.interactionType = interactionType;
	}

	public void setPlayedCards(Card playedCard) {
		this.playedCard = playedCard;
	}

	public void setBuyedCard(Card buyedCard) {
		this.buyedCard = buyedCard;
	}

	public void setDiscardPileTopCard(Card discardPileTopCard) {
		this.discardPileTopCard = discardPileTopCard;
	}

	public void setDiscardPileCardNumber(Integer discardPileCardNumber) {
		this.discardPileCardNumber = discardPileCardNumber;
	}

	public void setDeckPileCardNumber(Integer deckPileCardNumber) {
		this.deckPileCardNumber = deckPileCardNumber;
	}

	public void setActions(Integer actions) {
		this.actions = actions;
	}

	public void setCoins(Integer coins) {
		this.coins = coins;
	}

	public void setBuys(Integer buys) {
		this.buys = buys;
	}

	// fill the cardListElement to use addContentElements
	public void setNewHandCards(LinkedList<Card> newHandCards) {
		this.handCardListElements.put(ELEMENT_NEW_HANDCARD, newHandCards);
	}

	// fill the cardListElement to use addContentElements
	public void setCardSelection(LinkedList<CardName> cardSelection) {
		this.cardSelectionElements.put(ELEMENT_CARDSELECTION, cardSelection);
	}
}// end UpdateGame_Message