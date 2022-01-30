// Class representing all information needed for a buy card move to update the game state
public class BuyCardMove extends Move {
    private final int cardIndex;
    private final int cardSource;
    private final int chosenNobleIndex;

    // Construct a buyCardMove object
    // player represents the player making the move
    // cardIndex, an integer between 0 and 3, represents the index of the card to be bought
    // cardSource, representing the tier of the card, or 0 for a card bought from the reserve
    // paid to buy the card (necessary because gold tokens can act like any of the 5 colored tokens)
    // chosenNobleIndex is the index of the noble chosen by the player, in case they have attracted any.
    // Default value is -1, and in case only one noble is attracted in a turn that will be the one chosen.
    public BuyCardMove(int player, int cardSource, int cardIndex, int chosenNobleIndex) {
        super(player, Type.buyCard);
        this.cardIndex = cardIndex;
        this.cardSource = cardSource;
        this.chosenNobleIndex = chosenNobleIndex;
    }

    @Override public String toString() {
        return "Player: " + getPlayer() + "; Buys: " + getCardSource() + " " + getCardIndex() + "; Noble: "
                + getChosenNobleIndex();
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public int getCardSource() {
        return cardSource;
    }

    public int getChosenNobleIndex() {
        return chosenNobleIndex;
    }

}
