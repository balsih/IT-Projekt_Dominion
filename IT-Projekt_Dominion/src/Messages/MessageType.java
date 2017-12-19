package Messages;

/**
 * This class represent a set of enums to identify the type of the Message.
 * <li>AskForChanges:	Asks the server if something changed in the game
 * <li>BuyCard:			Tries to buy a card
 * <li>Chat:			Sends chat-messages
 * <li>CreateGame:		Provides data to create a new game
 * <li>CreateNewPlayer:	Tries to store the client's name and password to server's database
 * <li>GameMode:		Tells the server which GameMode the client chose (Singleplayer or Multiplayer)
 * <li>HighScore:		Request and receive the top5 highscore
 * <li>Login:			Tries to login into the application
 * <li>Logout:			Tells the server that the player left the game
 * <li>PlayCard:		Tries to play a card from the hand
 * <li>PlayerSuccess:	Tells the client's weather they won or lost
 * <li>UpdateGame:		Provides the client's several updates from the game
 * <li>Commit:			Commits some application-specified actions
 * <li>Failure:			Tells the client that his application-specified action failed
 * <li>Error:			To show communication-errors
 * <li>GiveUp:			Tells the server the client gave Up his/her current game
 * <li>Interaction:		Tells the client's which Interaction they have to perform and provides the content from the answers
 * <li>Knock:			Knocks on socket's ip and port weather server is listening
 * <li>Request:			Tries to get the lost Message from server
 * 
 * @author Lukas, source: Bradley Richards
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
	Logout,
	PlayCard,
	PlayerSuccess,
	UpdateGame,
	Commit,
	Failure,
	Error,
	GiveUp,
	Interaction,
	Request,
	Knock;

	/**
	 * Converts a String into enum MessageType if it exists
	 * else it returns type Error
	 * 
	 * @author Lukas, source: Bradley Richards
	 * @param typeName
	 * 				given String to convert
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
	 * @author Lukas, source: Bradley Richards
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
    	else if (msg instanceof Logout_Message) type = MessageType.Logout;
    	else if (msg instanceof PlayCard_Message) type = MessageType.PlayCard;
    	else if (msg instanceof PlayerSuccess_Message) type = MessageType.PlayerSuccess;
    	else if (msg instanceof UpdateGame_Message) type = MessageType.UpdateGame;
    	else if (msg instanceof Commit_Message) type = MessageType.Commit;
    	else if (msg instanceof Failure_Message) type = MessageType.Failure;
    	else if (msg instanceof GiveUp_Message) type = MessageType.GiveUp;
       	else if (msg instanceof Interaction_Message) type = MessageType.Interaction;
       	else if (msg instanceof Knock_Message) type = MessageType.Knock;
       	else if (msg instanceof Request_Message) type = MessageType.Request;
    	return type;
    }	
}