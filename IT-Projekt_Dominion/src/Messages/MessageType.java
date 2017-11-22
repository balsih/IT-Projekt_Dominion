package Messages;

/**
 * @author default: Bradley Richards
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
	PlayCard,
	PlayerSuccess,
	SkipPhase,
	UpdateGame,
	Commit,
	Failure,
	Error,
	GiveUp,
	Interaction;

	/**
	 * Converts a String into enum MessageType if it exists
	 * else it returns type Error
	 * 
	 * @param typeName, given String to convert
	 * @return type, depends on typeName, default Error
	 */
    public static MessageType parseType(String typeName) {
    	MessageType type = MessageType.Error;
    	for (MessageType value : MessageType.values()) {
    		if (value.toString().equals(typeName)) type = value;
    	}
    	return type;
    }

	/**
	 * Reads (parses) the type of a message
	 * 
	 * @param msg, given Message to read
	 * @return type, depends on msg, default Error
	 */
    public static MessageType getType(Message msg) {
    	MessageType type = MessageType.Error;
    	if (msg instanceof AskForChanges_Message) type  = MessageType.AskForChanges;
    	else if (msg instanceof BuyCard_Message) type  = MessageType.BuyCard;
    	else if (msg instanceof Chat_Message) type = MessageType.Chat;
    	else if (msg instanceof CreateGame_Message) type = MessageType.CreateGame;
    	else if (msg instanceof CreateNewPlayer_Message) type = MessageType.CreateNewPlayer;
    	else if (msg instanceof GameMode_Message) type = MessageType.GameMode;
    	else if (msg instanceof HighScore_Message) type = MessageType.HighScore;
    	else if (msg instanceof Login_Message) type = MessageType.Login;
    	else if (msg instanceof PlayCard_Message) type = MessageType.PlayCard;
    	else if (msg instanceof PlayerSuccess_Message) type = MessageType.PlayerSuccess;
    	else if (msg instanceof SkipPhase_Message) type = MessageType.SkipPhase;
    	else if (msg instanceof UpdateGame_Message) type = MessageType.UpdateGame;
    	else if (msg instanceof Commit_Message) type = MessageType.Commit;
    	else if (msg instanceof Failure_Message) type = MessageType.Failure;
    	else if (msg instanceof GiveUp_Message) type = MessageType.GiveUp;
       	else if (msg instanceof Interaction_Message) type = MessageType.Interaction;
    	return type;
    }	
}