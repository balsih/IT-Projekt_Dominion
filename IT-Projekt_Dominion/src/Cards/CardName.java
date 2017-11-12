package Cards;


/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:22
 */
public enum CardName {
	Cellar,
	Copper,
	Duchy,
	Estate,
	Gold,
	Market,
	Mine,
	Province,
	Remodel,
	Silver,
	Smithy,
	Village,
	Woodcutter,
	Workshop;
	
	/**
	 * @author Bradley Richards
	 * Parses the enum-Type for creating new cards on client
	 * 
	 * @param cardName
	 * @return CardName, enum of the given cardName
	 */
    public static CardName parseType(String cardName) {
    	CardName cardType = null;
    	for (CardName value : CardName.values()) {
    		if (value.toString().equals(cardName)) cardType = value;
    	}
    	return cardType;
    }

}
