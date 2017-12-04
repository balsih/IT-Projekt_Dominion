package Messages;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Cards.Card;
import Cards.CardName;

/**
 * @author Lukas
 * @version 1.0
 * @created 09-Nov-2017 19:09:48
 */
public class Interaction_Message extends Message {
	
	private static final String ELEMENT_INTERACTION = "interaction";
	private static final String ATTR_INTERACTION_TYPE = "interactionType";
	private Interaction interactionType = null;
	
	//EOT = End Of Turn
	private static final String ELEMENT_ENDOFTURN_DISCARDCARD = "EOTdiscardCard";
	private Card EOTdiscardCard = null;
	
	private static final String ELEMENT_CELLAR_DISCARDCARD = "cellarDiscardCard";
	private LinkedList<Card> cellarDiscardCards = null;
	
	private static final String ELEMENT_WORKSHOP_CHOICE = "workshopChoice";
	private CardName workshopChoice = null;
	
	private static final String ELEMENT_DISPOSED_REMODEL_CARD = "disposedRemodelCard";
	private static final String ELEMENT_REMODEL_CHOICE = "remodelChoice";
	private Card disposedRemodelCard = null;
	private CardName remodelChoice = null;
	
	private static final String ELEMENT_DISPOSED_MINE_CARD = "disposedMineCard";
	private Card disposedMineCard = null;
	
	
	
	public Interaction_Message(){
		super();
	}


	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn) {
        Element root = docIn.getDocumentElement();
		
		Element interaction = docIn.createElement(ELEMENT_INTERACTION);
		interaction.setAttribute(ATTR_INTERACTION_TYPE, this.interactionType.toString());
		root.appendChild(interaction);
		
		//content of Interaction_Message depends on which interaction you're acting
		switch(this.interactionType){
		case Skip://nothing toDo here (no content)
			break;
		case EndOfTurn://consist the topCard of the discardPile to show to opponent
			Element eotDiscardCard = docIn.createElement(ELEMENT_ENDOFTURN_DISCARDCARD);
			eotDiscardCard.setTextContent(this.EOTdiscardCard.toString());
			interaction.appendChild(eotDiscardCard);
			break;
		case Cellar://consists all cards that the client discarded with Cellar (theoretically "unlimited" number)
			for(Card card: this.cellarDiscardCards){
				Element cellarDiscardCard = docIn.createElement(ELEMENT_CELLAR_DISCARDCARD);
				cellarDiscardCard.setTextContent(card.toString());
				interaction.appendChild(cellarDiscardCard);
			}
			break;
		case Workshop://consists the chosen CardName which the client would like to take (max. cost 4)
			Element workshopChoice = docIn.createElement(ELEMENT_WORKSHOP_CHOICE);
			workshopChoice.setTextContent(this.workshopChoice.toString());
			interaction.appendChild(workshopChoice);
			break;
		case Remodel1://consists the disposed card
			Element disposedRemodelCard = docIn.createElement(ELEMENT_DISPOSED_REMODEL_CARD);
			disposedRemodelCard.setTextContent(this.disposedRemodelCard.toString());
			interaction.appendChild(disposedRemodelCard);
			break;
		case Remodel2://consist the chosen CardName which the client would like to take (max 2+ of the disposed card)
			Element remodelChoice = docIn.createElement(ELEMENT_REMODEL_CHOICE);
			remodelChoice.setTextContent(this.remodelChoice.toString());
			interaction.appendChild(remodelChoice);
			break;
		case Mine://consists the disposed card
			Element disposedMineCard = docIn.createElement(ELEMENT_DISPOSED_MINE_CARD);
			disposedMineCard.setTextContent(this.disposedMineCard.toString());
			interaction.appendChild(disposedMineCard);
			break;
		}
	}
	
	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn) {
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_INTERACTION);
		Element interaction = null;
        if (tmpElements.getLength() > 0) {
            interaction = (Element) tmpElements.item(0);
            this.interactionType = Interaction.parseInteraction(interaction.getAttribute(ATTR_INTERACTION_TYPE));
        }
        
        //generate content of the specified Interaction
        switch(this.interactionType){
        case Skip://nothing toDo here (no content)
        	break;
        case EndOfTurn://get the CardName in the End Of Turn to show the opponent
        	tmpElements = interaction.getElementsByTagName(ELEMENT_ENDOFTURN_DISCARDCARD);
        	if(tmpElements.getLength() > 0){
        		Element EOTDiscardCard = (Element) tmpElements.item(0);
        		this.EOTdiscardCard = TestMessages.getCard((CardName.parseName(EOTDiscardCard.getTextContent())));
        	}
        	break;
        case Cellar://get the CardNames of the discarded cards with Cellar
        	tmpElements = interaction.getElementsByTagName(ELEMENT_CELLAR_DISCARDCARD);
        	this.cellarDiscardCards = new LinkedList<Card>();
        	for(int i = 0; i < tmpElements.getLength(); i++){
        		Element cellarDiscardCard = (Element) tmpElements.item(i);
        		this.cellarDiscardCards.add(TestMessages.getCard(CardName.parseName(cellarDiscardCard.getTextContent())));
        	}
        	break;
        case Workshop://get the CardName of the chosen card (max. cost 4)
        	tmpElements = interaction.getElementsByTagName(ELEMENT_WORKSHOP_CHOICE);
        	if(tmpElements.getLength() > 0){
        		Element workshopChoice = (Element) tmpElements.item(0);
        		this.workshopChoice = CardName.parseName(workshopChoice.getTextContent());
        	}
        	break;
        case Remodel1://get the disposed card
        	tmpElements = interaction.getElementsByTagName(ELEMENT_DISPOSED_REMODEL_CARD);
        	if(tmpElements.getLength() > 0){
        		Element disposedCard = (Element) tmpElements.item(0);
        		this.disposedRemodelCard = TestMessages.getCard(CardName.parseName(disposedCard.getTextContent()));
        	}
        	break;
        case Remodel2://get the chosen card which the client would like to take (max 2+ of the disposed card)
        	tmpElements = interaction.getElementsByTagName(ELEMENT_REMODEL_CHOICE);
        	if(tmpElements.getLength() > 0){
        		Element remodelChoice = (Element) tmpElements.item(0);
        		this.remodelChoice = CardName.parseName(remodelChoice.getTextContent());
        	}
        	break;
        case Mine://get the disposed treasure card
        	tmpElements = interaction.getElementsByTagName(ELEMENT_DISPOSED_MINE_CARD);
        	if(tmpElements.getLength() > 0){
        		Element disposedMineCard = (Element) tmpElements.item(0);
        		this.disposedMineCard = TestMessages.getCard(CardName.parseName(disposedMineCard.getTextContent()));
        	}
        	break;
        }
	}
	

	public Interaction getInteractionType() {
		return this.interactionType;
	}

	public void setInteractionType(Interaction interactionType) {
		this.interactionType = interactionType;
	}

	public Card getDiscardCard() {
		return this.EOTdiscardCard;
	}

	public void setDiscardCard(Card EOTdiscardCard) {
		this.EOTdiscardCard = EOTdiscardCard;
	}

	public LinkedList<Card> getCellarDiscardCards() {
		return this.cellarDiscardCards;
	}

	public void setCellarDiscardCards(LinkedList<Card> cellarDiscardCards) {
		this.cellarDiscardCards = cellarDiscardCards;
	}

	public CardName getRemodelChoice() {
		return this.remodelChoice;
	}

	public void setRemodelChoice(CardName remodelChoice) {
		this.remodelChoice = remodelChoice;
	}

	public CardName getWorkshopChoice() {
		return this.workshopChoice;
	}

	public void setWorkshopChoice(CardName workshopChoice) {
		this.workshopChoice = workshopChoice;
	}

	public Card getDisposeRemodelCard() {
		return this.disposedRemodelCard;
	}

	public void setDisposeRemodelCard(Card disposedRemodelCard) {
		this.disposedRemodelCard = disposedRemodelCard;
	}


	public Card getDisposedMineCard() {
		return disposedMineCard;
	}


	public void setDisposedMineCard(Card disposedMineCard) {
		this.disposedMineCard = disposedMineCard;
	}

}
