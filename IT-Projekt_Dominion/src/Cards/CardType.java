package Cards;


/**
 * Represents the type of a card.
 * <li>Action: 		Cards with some functionality. Can be played in the action-phase.
 * <li>Victory:		Cards without functionality. Can never be played. At the end of the game the player with the most victory-points wins the game.
 * <li>Treasure:	Cards with little functionality. Can be played in the buy-phase. Increases the amount of coins of the player.
 * 
 * @author Lukas
 */
public enum CardType {
	Action,
	Victory,
	Treasure;
}
