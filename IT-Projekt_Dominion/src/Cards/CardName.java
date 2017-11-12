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
	 * This method provides all classes the cardName in a String format
	 * 
	 * @param cardName, enum
	 * @return String cardName
	 */
	public static String getName(CardName cardName){
		if(cardName.equals(Cellar))
			return "Cellar";
		if(cardName.equals(Copper))
			return "Copper";
		if(cardName.equals(Duchy))
			return "Duchy";
		if(cardName.equals(Estate))
			return "Estate";
		if(cardName.equals(Gold))
			return "Gold";
		if(cardName.equals(Market))
			return "Market";
		if(cardName.equals(Mine))
			return "Mine";
		if(cardName.equals(Province))
			return "Province";
		if(cardName.equals(Remodel))
			return "Remodel";
		if(cardName.equals(Silver))
			return "Silver";
		if(cardName.equals(Smithy))
			return "Smithy";
		if(cardName.equals(Village))
			return "Village";
		if(cardName.equals(Woodcutter))
			return "Woodcutter";
		if(cardName.equals(Workshop))
			return "Workshop";
		return null;
	}

}
