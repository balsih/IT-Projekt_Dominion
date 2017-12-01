package Server_GameLogic;

import java.net.Socket;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Logger;

import Cards.Card;
import Cards.CardName;
import Cards.CardType;
import Cards.Cellar_Card;
import Cards.Copper_Card;
import Messages.GameSuccess;
import Messages.Failure_Message;
import Messages.Interaction;
import Messages.Message;
import Messages.PlayerSuccess_Message;
import Messages.UpdateGame_Message;

/**
 * @author Bodo
 * @version 1.0
 * @created 31-Okt-2017 17:08:57
 */
public class Player {

	protected LinkedList<Card> handCards;
	protected LinkedList<Card> playedCards;
	protected Stack<Card> deckPile;
	protected Stack<Card> discardPile;

	protected final int NUM_OF_HANDCARDS = 5;

	protected String playerName;
	protected int actions;
	protected int buys;
	protected int coins;
	protected int moves;
	protected int victoryPoints;
	private GameSuccess status;

	protected Game game;
	protected Phase actualPhase;

	protected int counter;

	protected Socket clientSocket;
	private ServerThreadForClient serverThreadForClient;

	private final Logger logger = Logger.getLogger("");

	public static void main(String[] args) {
		Player player = new Player("Dummy");
		Cellar_Card cellar = new Cellar_Card();
		Copper_Card copper = new Copper_Card();
		Copper_Card copper2 = new Copper_Card();
		player.handCards.add(cellar);
		player.handCards.add(copper);
		player.handCards.add(copper2);
		player.handCards.remove(cellar);
		System.out.println(player.containsCardType(player.handCards, CardType.Action));
	}

	/**
	 * Constructor for Bot
	 * 
	 * @param name
	 */
	public Player(String name) {
		this.deckPile = new Stack<Card>();
		this.discardPile = new Stack<Card>();
		this.handCards = new LinkedList<Card>();
		this.playedCards = new LinkedList<Card>();

		this.playerName = name;
		startMove();
	}

	/**
	 * 
	 * @param name
	 */
	public Player(String name, ServerThreadForClient serverThreadForClient) {
		this(name);
		this.serverThreadForClient = serverThreadForClient;
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         Initializes the player to start a move.
	 */
	public void startMove() {
		this.actions = 1;
		this.buys = 1;
		this.coins = 0;
		this.counter = 0;
		this.actualPhase = Phase.Action;
	}

	/**
	 * @author Bodo Gruetter
	 *
	 *         allows the current player to play a card and execute this.
	 *
	 * @param the
	 *            played card and the index of this in the handcards.
	 * @return playersuccess message if the game is finished or an updategame
	 *         message if the move is valid. if the move is not valid the
	 *         methods returns a failure message.
	 */
	public Message play(Card selectedCard) {
		int index = this.handCards.indexOf(selectedCard);
		Card playedCard = this.handCards.remove(index);
		playedCards.add(playedCard);
		UpdateGame_Message ugmsg;

		// Executes the clicked Card, if the player has enough actions
		if (this.getActions() > 0 && this.actualPhase == Phase.Action && this.equals(game.getCurrentPlayer())) {

			ugmsg = playedCard.executeCard(this);
			this.actions--;

			this.sendToOpponent(this, ugmsg);

			if (this.actions == 0 || !this.containsCardType(this.handCards, CardType.Action))
				UpdateGame_Message.merge((UpdateGame_Message) this.skipPhase(), ugmsg);

			return ugmsg;

		} else if (this.actualPhase == Phase.Buy && this.equals(game.getCurrentPlayer())) {
			ugmsg = playedCard.executeCard(this);
			return ugmsg;
		}

		Failure_Message fmsg = new Failure_Message();
		return fmsg;
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         allows the current player to buy a card and stores it in the
	 *         discard pile.
	 * 
	 * @param the
	 *            buyed card
	 * @return playersuccess message if the game is finished or an updategame
	 *         message if the move is valid. if the move is not valid the
	 *         methods returns a failure message.
	 */
	public Message buy(CardName cardName) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		Failure_Message fmsg = new Failure_Message();
		Card buyedCard = null;

		if (Card.getCard(cardName).getCost() <= this.getCoins() && this.getBuys() > 0 && this.actualPhase == Phase.Buy
				&& this.equals(game.getCurrentPlayer())) {

			try {
				buyedCard = this.pick(cardName);
				this.discardPile.push(buyedCard);

				this.coins -= buyedCard.getCost();
				this.buys--;
			} catch (EmptyStackException e) {
				this.logger.severe("The buy card stack is empty!");
				return fmsg;
			}

			if (game.checkGameEnding()) {
				this.actualPhase = Phase.Ending;
				game.checkWinner();

				this.sendToOpponent(this, this.getOpponentSuccessMsg());
				return this.getCurrentPlayerSuccessMsg();
			}

			/**
			 * checks if the buy of the current player is valid, then actualize
			 * the updateGame_Message. else the method returns a
			 * failure_Message.
			 */
			ugmsg.setLog(this.playerName + "#bought# #" + buyedCard.getCardName().toString() + "# #card#");
			ugmsg.setCoins(this.coins);
			ugmsg.setBuys(this.buys);
			ugmsg.setDiscardPileTopCard(this.discardPile.peek());
			ugmsg.setDiscardPileCardNumber(this.discardPile.size());
			ugmsg.setBuyedCard(buyedCard);
			this.sendToOpponent(this, ugmsg);

			if (this.buys == 0) {
				UpdateGame_Message.merge((UpdateGame_Message) skipPhase(), ugmsg);
			}
			return ugmsg;
		}

		return fmsg;
	}
	
	public Card pick(CardName cardName){
		Card pickedCard = null;
		
		// wenn gekauft, noch buys zur verfuegung
		switch (cardName) {
		case Copper:
			pickedCard = this.game.getCopperPile().pop();
			break;
		case Cellar:
			pickedCard = this.game.getCellarPile().pop();
			break;
		case Duchy:
			pickedCard = this.game.getDuchyPile().pop();
			break;
		case Estate:
			pickedCard = this.game.getEstatePile().pop();
			break;
		case Gold:
			pickedCard = this.game.getGoldPile().pop();
			break;
		case Market:
			pickedCard = this.game.getMarketPile().pop();
			break;
		case Mine:
			pickedCard = this.game.getMinePile().pop();
			break;
		case Province:
			pickedCard = this.game.getProvincePile().pop();
			break;
		case Remodel:
			pickedCard = this.game.getRemodelPile().pop();
			break;
		case Silver:
			pickedCard = this.game.getSilverPile().pop();
			break;
		case Smithy:
			pickedCard = this.game.getSmithyPile().pop();
			break;
		case Village:
			pickedCard = this.game.getVillagePile().pop();
			break;
		case Woodcutter:
			pickedCard = this.game.getWoodcutterPile().pop();
			break;
		case Workshop:
			pickedCard = this.game.getWorkshopPile().pop();
			break;
		}
		
		return pickedCard;
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         cleans up automatically if a the current player has finished his
	 *         move and switches the player.
	 */
	public Message cleanUp(Card selectedTopCard) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		boolean endOfTurn = false;
		
		if (this.handCards.size() > 1 && selectedTopCard != null) {
			ugmsg.setInteractionType(Interaction.EndOfTurn);
			ugmsg.setDiscardPileTopCard(selectedTopCard);
			this.sendToOpponent(this, ugmsg);
			endOfTurn = true;
		} else if (this.handCards.size() == 1 && selectedTopCard == null){
			ugmsg.setDiscardPileTopCard(this.handCards.element());
			ugmsg.setDiscardPileCardNumber(this.discardPile.size());
		} else {
			ugmsg.setDiscardPileTopCard(this.discardPile.peek());
		}
		
		if(!endOfTurn){
		while (!playedCards.isEmpty()) {
			this.discardPile.push(playedCards.remove());
		}

		while (!handCards.isEmpty()) {
			this.discardPile.push(handCards.remove());
		}

		this.moves++;
		
		this.draw(this.NUM_OF_HANDCARDS);
		
		this.sendToOpponent(this, ugmsg);
		}
		
		return ugmsg;
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         If Deckpile is empty, the discard pile fills the deckPile.
	 *         Eventually the deckPiles get shuffled and the player draws the
	 *         number of layed down Cards from deckPile to HandPile.
	 *
	 *         Else If the deckpile size is lower than 5, the rest of deckPiles
	 *         will be drawed and the discard pile fills the deckPile.
	 *         eventually the deckPile get shuffled and the player draws the
	 *         number of layed down Cards in the HandPile.
	 *
	 *         Else if they are enough cards in the deckPile, the player draws
	 *         the number of layed down cards respectively 5 Cards into the
	 *         handPile
	 * 
	 * @param the
	 *            number of cards which should be drawn.
	 * @return an updateGame message
	 */
	public UpdateGame_Message draw(int numOfCards) {

		UpdateGame_Message ugmsg = new UpdateGame_Message();
		LinkedList<Card> newHandCards = new LinkedList<Card>();

		for (int i = 0; i < numOfCards; i++) {
			//normal draw
			if (!deckPile.isEmpty()) {
				newHandCards.add(deckPile.pop());
				continue;
				//if deck is empty, put discardPile into deckPile and shuffle
			} else if (deckPile.isEmpty() && !discardPile.isEmpty()) {
				while (!discardPile.isEmpty())
					deckPile.push(discardPile.pop());
				Collections.shuffle(deckPile);
				newHandCards.add(deckPile.pop());
				continue;
				//if deckPile and discardPile are empty, no further draws
			} else if (deckPile.size() < numOfCards - i) {
				break;
			}
		}

		ugmsg.setDeckPileCardNumber(this.deckPile.size());
		ugmsg.setDiscardPileCardNumber(this.discardPile.size());
		ugmsg.setNewHandCards(newHandCards);
		
		this.handCards.addAll(newHandCards);

		return ugmsg;

	}

	/**
	 * @author Bodo Gruetter skips actual phase and goes to the next phase
	 * 
	 * @return an updategamemessage if the skipping works, else an failure
	 *         message
	 */
	public Message skipPhase() {

		UpdateGame_Message ugmsg = new UpdateGame_Message();
		Failure_Message fmsg = new Failure_Message();

		if (this.equals(game.getCurrentPlayer())) {
			switch (this.actualPhase) {
			case Action:
				this.actualPhase = Phase.Buy;
				ugmsg.setCurrentPhase(Phase.Buy);

			case Buy:
					this.actualPhase = Phase.CleanUp;
					ugmsg.setCurrentPhase(Phase.CleanUp);
					// this.cleanUp();

			case CleanUp:
				game.switchPlayer();
				ugmsg.setCurrentPlayer(game.getCurrentPlayer().getPlayerName());
				ugmsg.setCurrentPhase(Phase.Action);

			default:
				break;
			}

			return ugmsg;
		}

		return fmsg;
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         allows the current player to count his victory points.
	 */
	public void countVictoryPoints() {
		while (!this.handCards.isEmpty())
			this.deckPile.push(handCards.remove());
		while (!this.playedCards.isEmpty())
			this.deckPile.push(playedCards.remove());
		while (!this.discardPile.isEmpty())
			this.deckPile.push(discardPile.pop());

		Iterator<Card> iter = deckPile.iterator();
		while (iter.hasNext())
			if (iter.next().getType().equals(CardType.Victory)) {
				iter.next().executeCard(this);
			}
	}

	private boolean containsCardType(LinkedList<Card> list, CardType cardType) {
		Iterator<Card> iter = list.iterator();
		while (iter.hasNext()) {
			if (iter.next().getType() == cardType)
				return true;
		}

		return false;
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         sends an waiting message to the opponent
	 * 
	 * @param the
	 *            sending player and the message which should be send
	 */
	public void sendToOpponent(Player source, Message msg) {
		source.getServerThreadForClient().addWaitingMessages(msg);
	}

	/**
	 * sets the playerSuccess_Message of currentPlayer
	 * 
	 * @return a playerSuccess_Message
	 */
	private PlayerSuccess_Message getCurrentPlayerSuccessMsg() {
		PlayerSuccess_Message psmsg = new PlayerSuccess_Message();

		psmsg.setSuccess(this.status);
		psmsg.setVictoryPoints(this.victoryPoints);

		return psmsg;
	}

	private PlayerSuccess_Message getOpponentSuccessMsg() {
		PlayerSuccess_Message psmsg = new PlayerSuccess_Message();

		psmsg.setSuccess(this.status);
		psmsg.setVictoryPoints(this.victoryPoints);

		return psmsg;
	}

	public int getActions() {
		return actions;
	}

	public void setActions(int actions) {
		this.actions = actions;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public Stack<Card> getDeckPile() {
		return deckPile;
	}

	public void setDeckPile(Stack<Card> deckPile) {
		this.deckPile = deckPile;
	}

	public int getBuys() {
		return buys;
	}

	public void setBuys(int buys) {
		this.buys = buys;
	}

	public LinkedList<Card> getHandCards() {
		return handCards;
	}

	public void setHandCards(LinkedList<Card> handCards) {
		this.handCards = handCards;
	}

	public Game getGame() {
		return game;
	}

	/**
	 * 
	 * @param gameThread
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	public Stack<Card> getDiscardPile() {
		return discardPile;
	}

	public void setDiscardPile(Stack<Card> discardPile) {
		this.discardPile = discardPile;
	}

	public LinkedList<Card> getPlayedCards() {
		return playedCards;
	}

	public void setPlayedCards(LinkedList<Card> playedCards) {
		this.playedCards = playedCards;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	public Phase getActualPhase() {
		return actualPhase;
	}

	public void setActualPhase(Phase actualPhase) {
		this.actualPhase = actualPhase;
	}

	public ServerThreadForClient getServerThreadForClient() {
		return serverThreadForClient;
	}

	public void setServerThreadForClient(ServerThreadForClient serverThreadForClient) {
		this.serverThreadForClient = serverThreadForClient;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}

	public int getMoves() {
		return this.moves;
	}

	public GameSuccess getStatus() {
		return status;
	}

	public void setStatus(GameSuccess status) {
		this.status = status;
	}
}// end Player