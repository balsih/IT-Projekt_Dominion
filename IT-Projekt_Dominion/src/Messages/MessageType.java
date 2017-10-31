package Messages;




/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:18
 */
public enum MessageType {
	AskForChanges,
	BuyCard,
	Chat,
	CreateGame,
	CreateNewPlayer,
	GameMode,
	HighScore,
	Login,
	LoginOrCreateNewPlayerAnswer,
	PlayCard,
	PlayerSuccess,
	SkipPhase,
	UpdateGame;

	/**
	 * 
	 * @param typeName
	 */
	public static MessageType parseType(String typeName){
		return null;
	}

	/**
	 * 
	 * @param msg
	 */
	public static MessageType getType(Message msg){
		return null;
	}
}