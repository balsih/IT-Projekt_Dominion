package Messages;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Cards.CardName;

/**
 * @author Lukas
 * @version 1.0
 * @created 09-Nov-2017 19:09:48
 */
public class Interaction_Message extends Message {
	
	private static final String ELEMENT_INTERACTION = "interaction";
	private static final String ATTR_INTERACTIONTYPE = "interactionType";
	private Interaction interactionType = null;
	
	//EOT = End Of Turn
	private static final String ELEMENT_EOTDISCARDCARD = "EOTdiscardCard";
	private CardName EOTdiscardCard = null;
	
	private static final String ELEMENT_CELLARDISCARDCARD = "cellarDiscardCard";
	private LinkedList<CardName> cellarDiscardCards = null;
	
	private static final String ELEMENT_WORKSHOPCHOICE = "workshopChoice";
	private CardName workshopChoice = null;
	
	private static final String ELEMENT_DISPOSEDCARD = "disposedCard";
	private static final String ELEMENT_REMODELCHOICE = "remodelChoice";
	private CardName disposedCard = null;
	private CardName remodelChoice = null;
	
	
	
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
		interaction.setAttribute(ATTR_INTERACTIONTYPE, this.interactionType.toString());
		root.appendChild(interaction);
		
		//content of Interaction_Message depends on which interaction you're acting
		switch(this.interactionType){
		case EndOfTurn://consist the topCard of the discardPile to show to opponent
			Element eotDiscardCard = docIn.createElement(ELEMENT_EOTDISCARDCARD);
			eotDiscardCard.setTextContent(this.EOTdiscardCard.toString());
			interaction.appendChild(eotDiscardCard);
			break;
		case Cellar://consists all cards that the client discarded with Cellar (theoretically "unlimited" number)
			for(CardName cardName: this.cellarDiscardCards){
				Element cellarDiscardCard = docIn.createElement(ELEMENT_CELLARDISCARDCARD);
				cellarDiscardCard.setTextContent(cardName.toString());
				interaction.appendChild(cellarDiscardCard);
			}
			break;
		case Workshop://consists the chosen CardName which the client would like to take (max. cost 4)
			Element workshopChoice = docIn.createElement(ELEMENT_WORKSHOPCHOICE);
			workshopChoice.setTextContent(this.workshopChoice.toString());
			interaction.appendChild(workshopChoice);
			break;
		case Remodel1://consists the disposed card
			Element disposedCard = docIn.createElement(ELEMENT_DISPOSEDCARD);
			disposedCard.setTextContent(this.disposedCard.toString());
			interaction.appendChild(disposedCard);
			break;
		case Remodel2://consist the chosen CardName which the client would like to take (max 2+ of the disposed card)
			Element remodelChoice = docIn.createElement(ELEMENT_REMODELCHOICE);
			remodelChoice.setTextContent(this.remodelChoice.toString());
			interaction.appendChild(remodelChoice);
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
            this.interactionType = Interaction.parseInteraction(interaction.getAttribute(ATTR_INTERACTIONTYPE));
        }
        
        //generate content of the specified Interaction
        switch(this.interactionType){
        case EndOfTurn://get the CardName in the End Of Turn to show the opponent
        	tmpElements = interaction.getElementsByTagName(ELEMENT_EOTDISCARDCARD);
        	if(tmpElements.getLength() > 0){
        		Element EOTDiscardCard = (Element) tmpElements.item(0);
        		this.EOTdiscardCard = CardName.parseName(EOTDiscardCard.getTextContent());
        	}
        	break;
        case Cellar://get the CardNames of the discarded cards with Cellar
        	tmpElements = interaction.getElementsByTagName(ELEMENT_CELLARDISCARDCARD);
        	this.cellarDiscardCards = new LinkedList<CardName>();
        	for(int i = 0; i < tmpElements.getLength(); i++){
        		Element cellarDiscardCard = (Element) tmpElements.item(i);
        		this.cellarDiscardCards.add(CardName.parseName(cellarDiscardCard.getTextContent()));
        	}
        	break;
        case Workshop://get the CardName of the chosen card (max. cost 4)
        	tmpElements = interaction.getElementsByTagName(ELEMENT_WORKSHOPCHOICE);
        	if(tmpElements.getLength() > 0){
        		Element workshopChoice = (Element) tmpElements.item(0);
        		this.workshopChoice = CardName.parseName(workshopChoice.getTextContent());
        	}
        	break;
        case Remodel1://get the disposed card
        	tmpElements = interaction.getElementsByTagName(ELEMENT_DISPOSEDCARD);
        	if(tmpElements.getLength() > 0){
        		Element disposedCard = (Element) tmpElements.item(0);
        		this.disposedCard = CardName.parseName(disposedCard.getTextContent());
        	}
        	break;
        case Remodel2://get the chosen card which the client would like to take (max 2+ of the disposed card)
        	tmpElements = interaction.getElementsByTagName(ELEMENT_REMODELCHOICE);
        	if(tmpElements.getLength() > 0){
        		Element remodelChoice = (Element) tmpElements.item(0);
        		this.remodelChoice = CardName.parseName(remodelChoice.getTextContent());
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

	public CardName getDiscardCard() {
		return this.EOTdiscardCard;
	}

	public void setDiscardCard(CardName EOTdiscardCard) {
		this.EOTdiscardCard = EOTdiscardCard;
	}

	public LinkedList<CardName> getCellarDiscardCards() {
		return this.cellarDiscardCards;
	}

	public void setCellarDiscardCards(LinkedList<CardName> cellarDiscardCards) {
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

	public CardName getDisposeCard() {
		return this.disposedCard;
	}

	public void setDisposeCard(CardName disposedCard) {
		this.disposedCard = disposedCard;
	}

}
