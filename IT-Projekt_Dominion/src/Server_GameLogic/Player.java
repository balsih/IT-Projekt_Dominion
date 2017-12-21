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
import Messages.GameSuccess;
import Messages.Failure_Message;
import Messages.Interaction;
import Messages.Message;
import Messages.PlayerSuccess_Message;
import Messages.UpdateGame_Message;

/**
 * The player class represents a player with his/her card stacks and hand. It
 * allows him/her to interact with the game and play through the four phases
 * 'Action', 'Buy', 'Clean up' and 'Ending'.
 * 
 * @author Bodo Gruetter
 * 
 */
public class Player {
	protected LinkedList<Card> handCards;
	protected LinkedList<Card> playedCards;
	protected Stack<Card> deckPile;
	protected Stack<Card> discardPile;
	protected Card topCard;

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

	protected Socket clientSocket;
	private ServerThreadForClient serverThreadForClient;

	private final Logger logger = Logger.getLogger("");

	public Player(String name, ServerThreadForClient serverThreadForClient) {
		this.deckPile = new Stack<Card>();
		this.discardPile = new Stack<Card>();
		this.handCards = new LinkedList<Card>();
		this.playedCards = new LinkedList<Card>();

		this.playerName = name;
		this.resetStates();
		this.serverThreadForClient = serverThreadForClient;
	}

	/**
	 * Resets the states of the player to start a move.
	 * 
	 * @author Bodo Gruetter
	 */
	public void resetStates() {
		this.actions = 1;
		this.buys = 1;
		this.coins = 0;
		this.actualPhase = Phase.Action;
	}

	/**
	 * Plays an action or a treasure card and executes it, if the conditions to
	 * play a card applies.
	 * 
	 * @author Bodo Gruetter
	 *
	 * @param selectedCard, the card the player selected to play.
	 * @return 
	 * <li>UpdateGame_Message, the message that updates the play process if if all conditions applies.
	 * <li>Failure_Message, if play-method don't works successfully
	 */
	public Message play(Card selectedCard) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		// get the index of the selected card in handCards
		int index = this.handCards.indexOf(selectedCard);

		// plays the selected action card which requires available action-points
		if (this.getActions() > 0 && this.actualPhase == Phase.Action && this.equals(game.getCurrentPlayer())
				&& selectedCard.getType() == CardType.Action) {

			// checks if an card which requires some interaction is played and
			// if the player is allowed to play them.
			if (selectedCard.getCardName().equals(CardName.Mine)
					&& (!(this.containsCard(this.handCards, CardName.Copper)
							|| (this.containsCard(this.handCards, CardName.Silver)))))
				return new Failure_Message();
			else if (selectedCard.getCardName().equals(CardName.Remodel) && this.handCards.size() == 1)
				return new Failure_Message();
			else if (selectedCard.getCardName().equals(CardName.Cellar) && this.handCards.size() == 1)
				return new Failure_Message();

			// executes the selected card
			ugmsg = selectedCard.executeCard(this);
			playedCards.add(this.handCards.remove(index));
			this.actions--;
			ugmsg.setActions(this.actions);

			// if the action phase is terminated, skip to next phase
			if ((this.actions == 0 || !this.containsCardType(this.handCards, CardType.Action))
					&& ugmsg.getInteractionType() == null)
				ugmsg = UpdateGame_Message.merge((UpdateGame_Message) this.skipPhase(), ugmsg);

			this.sendToOpponent(this, ugmsg);

			return ugmsg;

			// plays the selected treasure card which not requires any available
			// action-points
		} else if (this.actualPhase == Phase.Buy && this.equals(game.getCurrentPlayer())
				&& selectedCard.getType() == CardType.Treasure) {
			ugmsg = selectedCard.executeCard(this);
			playedCards.add(this.handCards.remove(index));

			this.sendToOpponent(this, ugmsg);

			return ugmsg;
		}

		return new Failure_Message();
	}

	/**
	 * Buys a Card if all condtions applies.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param cardName, the name of the Card which should been buyed.
	 * @return <li>UpdateGame_Message, the message that updates the buy process, if
	 * all conditions applies.
	 * <li>PlayerSuccess_Message, the message who wons and lost a game, if
	 * the game is finished.
	 * <li>Failure_Message, if no condition applies.
	 */
	public Message buy(CardName cardName) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		Card buyedCard = null;

		// if the player is allowed to buy a card, he picks the card
		if (Card.getCard(cardName).getCost() <= this.getCoins() && this.getBuys() > 0 && this.actualPhase == Phase.Buy
				&& this.equals(game.getCurrentPlayer())) {

			try {
				buyedCard = this.pick(cardName);
				this.discardPile.push(buyedCard);

				this.coins -= buyedCard.getCost();
				this.buys--;

			} catch (EmptyStackException e) {
				this.logger.severe("The buy card stack is empty!");
				return new Failure_Message();
			}

			// checks if the game is ended after buying a card and who wons it
			if (game.checkGameEnding()) {
				this.actualPhase = Phase.Ending;
				game.checkWinner();

				this.sendToOpponent(this, this.getPlayerSuccessMsg());
				return this.getPlayerSuccessMsg();
			}

			// sets all changed attributes of the UpdateGame_Message
			ugmsg.setLog(this.playerName + ": #bought# " + "#" + buyedCard.getCardName().toString() + "#" + " #card#");
			ugmsg.setCoins(this.coins);
			ugmsg.setBuys(this.buys);
			ugmsg.setDiscardPileTopCard(this.discardPile.peek());
			ugmsg.setDiscardPileCardNumber(this.discardPile.size());
			ugmsg.setBoughtCard(buyedCard);

			// if the buy phase is terminated, skip
			if (this.buys == 0) {
				ugmsg = UpdateGame_Message.merge((UpdateGame_Message) skipPhase(), ugmsg);
			}

			this.sendToOpponent(this, ugmsg);

			return ugmsg;
		}

		return new Failure_Message();
	}

	/**
	 * 
	 * Picks a card from a card stack.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param cardName, the name of the picked card.
	 * @return pickedCard - the picked card.
	 */
	public Card pick(CardName selectedCard) {
		Card pickedCard = null;

		// switches through all cards and picks the selected card
		switch (selectedCard) {
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
		default:
			break;
		}

		return pickedCard;
	}

	/**
	 * Cleans up the playing field and draws five cards in the hand. If the
	 * player has more than one card in his hand he chooses the card that should
	 * be on the top of his discardPile.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param selectedTopCard, the card on the top of the discard pile. It is null if the
	 * top card is already known.
	 * @return UpdateGame_Message, the message that updates the clean process.
	 */
	public UpdateGame_Message cleanUp(Card selectedTopCard) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();

		// lays played cards down on the discard pile
		while (!playedCards.isEmpty()) {
			this.discardPile.push(playedCards.remove());
		}

		/*
		 * checks if the player has more than one cards in his hand and if there
		 * is a interaction of the player required
		 */
		if (this.handCards.size() > 1 && selectedTopCard != null) {
			ugmsg.setDiscardPileTopCard(selectedTopCard);
			ugmsg.setLog("#topCard# " + "#" + selectedTopCard.getCardName().toString() + "#");
		} else if (this.handCards.size() >= 1 && selectedTopCard == null) {
			ugmsg.setDiscardPileTopCard(this.handCards.element());
			ugmsg.setLog("#topCard# " + "#" + this.handCards.element().getCardName().toString() + "#");
		} else if (this.handCards.size() == 0 && selectedTopCard == null) {
			ugmsg.setDiscardPileTopCard(this.discardPile.peek());
		}

		/*
		 * the player lays his handcards down on the discardPile and draw five
		 * new cards of his deckPile.
		 */
		while (!handCards.isEmpty()) {
			this.discardPile.push(handCards.remove());
		}

		ugmsg.setDiscardPileCardNumber(this.discardPile.size());

		ugmsg = UpdateGame_Message.merge(this.draw(this.NUM_OF_HANDCARDS), ugmsg);

		// if the cleanUp phase is terminated, skip
		ugmsg = UpdateGame_Message.merge((UpdateGame_Message) this.skipPhase(), ugmsg);

		return ugmsg;
	}
	
	/**
	 * Checks weather all of the player's hand-cards have the same name.
	 * 
	 * @author Lukas
	 * @return 	<li>true if all hand-cards have the same name.
	 * 			<li>false if the hand-cards consists different names.
	 */
	private boolean allSameHandCards(){
		if(this.handCards.size() > 1){
			Card firstCard = this.handCards.get(0);
			for(int i = 1; i < this.handCards.size(); i++){
				if(this.handCards.get(i).getCardName() != firstCard.getCardName())
					return false;
			}
		}else{
			return false;
		}
		return true;
	}

	/**
	 * Draws a variable number of cards in the hand.
	 * 
	 * @author Bodo Gruetter
	 * @param numOfCards, the number of cards which should be drawn.
	 * @return UpdateGame_Message, the message that updates the draw process.
	 */
	public UpdateGame_Message draw(int numOfCards) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		LinkedList<Card> newHandCards = new LinkedList<Card>();

		for (int i = 0; i < numOfCards; i++) {
			// normal draw
			if (!deckPile.isEmpty()) {
				newHandCards.add(deckPile.pop());
				continue;
				// if deck is empty, put discardPile into deckPile and shuffle
			} else if (deckPile.isEmpty() && !discardPile.isEmpty()) {
				while (!discardPile.isEmpty())
					deckPile.push(discardPile.pop());
				Collections.shuffle(deckPile);
				newHandCards.add(deckPile.pop());
				continue;
				// if deckPile and discardPile are empty, no further draws
			} else if (deckPile.size() < numOfCards - i) {
				break;
			}
		}

		// sets all changed attributes in the UpdateGame_Message
		ugmsg.setDeckPileCardNumber(this.deckPile.size());
		ugmsg.setDiscardPileCardNumber(this.discardPile.size());
		ugmsg.setDiscardPileTopCard(null);
		ugmsg.setNewHandCards(newHandCards);

		this.handCards.addAll(newHandCards);

		return ugmsg;
	}

	/**
	 * Skips actual phase and goes to the next phase.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @return <li>UpdateGame_Message, the message that updates the skip process.
	 * <li>Failure_Message, if no condition applies.
	 */
	public Message skipPhase() {
		UpdateGame_Message ugmsg = new UpdateGame_Message();

		if (this.equals(game.getCurrentPlayer())) {
			switch (this.actualPhase) {
			case Action:
				this.actualPhase = Phase.Buy;
				ugmsg.setCurrentPhase(this.actualPhase);
				break;

			case Buy:
				this.actualPhase = Phase.CleanUp;
				ugmsg.setCurrentPhase(this.actualPhase);

				if (this.handCards.size() > 1 && !this.allSameHandCards()) {
					ugmsg.setInteractionType(Interaction.EndOfTurn);
					ugmsg.setLog("#choseTopCard#"); // choose top card for
													// discardPile
				} else
					ugmsg = UpdateGame_Message.merge(this.cleanUp(null), ugmsg);
				break;

			case CleanUp:
				this.moves++;
				game.switchPlayer();
				ugmsg.setCurrentPlayer(game.getCurrentPlayer().getPlayerName());
				ugmsg.setCurrentPhase(game.getCurrentPlayer().actualPhase);
				ugmsg.setActions(game.getCurrentPlayer().getActions());
				ugmsg.setBuys(game.getCurrentPlayer().getBuys());
				ugmsg.setCoins(game.getCurrentPlayer().getCoins());
				break;

			default:
				break;
			}

			return ugmsg;
		}

		return new Failure_Message();
	}

	/**
	 * Counts the victory points.
	 * 
	 * @author Bodo Gruetter
	 */
	public void countVictoryPoints() {
		// Puts all left cards in the deckpile
		while (!this.handCards.isEmpty())
			this.deckPile.push(handCards.remove());
		while (!this.playedCards.isEmpty())
			this.deckPile.push(playedCards.remove());
		while (!this.discardPile.isEmpty())
			this.deckPile.push(discardPile.pop());

		// Counts the number of victory points
		for (Card card : this.deckPile)
			if (card.getType().equals(CardType.Victory)) {
				card.executeCard(this);
			}
	}

	/**
	 * Sends an waiting message to the opponent
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param source, the sending player
	 * @param msg, the message which should be send
	 */
	public void sendToOpponent(Player source, Message msg) {
		if (source instanceof Bot)
			this.serverThreadForClient.addWaitingMessages(msg);
		else if (!(source instanceof Bot) && this.getGame().getGameMode() == GameMode.Multiplayer) {
			this.game.getOpponent(this).getServerThreadForClient().addWaitingMessages(msg);
		}
	}

	/**
	 * Checks if a list contains a specific card type.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param list
	 * , the list which should be checked.
	 * @param cardType
	 * , the type that should be in the list.
	 * @return true or false, depending if list contains the card type or not.
	 */
	public boolean containsCardType(LinkedList<Card> list, CardType cardType) {
		Iterator<Card> iter = list.iterator();
		while (iter.hasNext()) {
			if (iter.next().getType() == cardType)
				return true;
		}

		return false;
	}

	/**
	 * Checks if a list contains a specific card.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param list
	 * , the list which should be checked.
	 * @param cardName
	 * , the name of the card that should be in the list.
	 * @return Boolean - depending if list contains the card or not.
	 */
	public boolean containsCard(LinkedList<Card> list, CardName cardName) {
		Iterator<Card> iter = list.iterator();
		while (iter.hasNext()) {
			if (iter.next().getCardName() == cardName)
				return true;
		}

		return false;
	}

	/**
	 * Sets the status of current player.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @return PlayerSuccess_Message - the message with the status and the
	 *         number of victory points.
	 */
	public PlayerSuccess_Message getPlayerSuccessMsg() {
		PlayerSuccess_Message psmsg = new PlayerSuccess_Message();

		psmsg.setPlayer1(game.getPlayer1());
		psmsg.setPlayer2(game.getPlayer2());

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