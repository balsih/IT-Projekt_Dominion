package Cards;


/**
 * Represents the name of a card.
 * <li>Cellar
 * <li>Copper
 * <li>Duchy
 * <li>Estate
 * <li>Gold
 * <li>Market
 * <li>Mine
 * <li>Province
 * <li>Remodel
 * <li>Silver
 * <li>Smithy
 * <li>Village
 * <li>Woodcutter
 * <li>Workshop
 * <li>Flipside
 * 
 * @author Lukas
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
	Workshop,
	Flipside;
	
	/**
	 * @author Lukas, source: Bradley Richards
	 * Parses the enum-Name for creating new cards on client
	 * 
	 * @param cardName
	 * 				The cardName in String-format
	 * @return	<li>CardName, enum of the given cardName
	 * 			<li>null, if CardName doesn't exist
	 */
    public static CardName parseName(String cardName) {
    	CardName cardType = null;
    	for (CardName value : CardName.values()) {
    		if (value.toString().equals(cardName)) cardType = value;
    	}
    	return cardType;
    }

}
