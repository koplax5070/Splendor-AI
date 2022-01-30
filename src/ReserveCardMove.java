import java.util.Arrays;

// Class representing all information needed for a reserve card move to update the state of the game
public class ReserveCardMove extends Move {
    private final int cardTier;
    private final int cardIndex;
    private final int[] tokensToReturn;

    // Construct a ReserveCardMove object
    // player is the player performing the move
    // cardTier is an integer between 1 and 3 representing the tier of the reserved card
    // cardIndex is a value from 0 to 4, representing the index of the newly reserved card in its tier row
    // Note: An index of 4 represents a blind reserve
    // tokensToReturn is an array with 5 elements representing which tokens of each color will be returned
    // Given that a maximum of one token is taken with a reserve move, tokensToReturn will have either 0 or 1 entries
    // equal to 1, and the rest equal to 0
    public ReserveCardMove(int player, int cardIndex, int cardTier, int[] tokensToReturn) {
        super(player, Type.reserveCard);
        this.cardIndex = cardIndex;
        this.cardTier = cardTier;
        this.tokensToReturn = new int[5];
        System.arraycopy(tokensToReturn, 0, this.tokensToReturn, 0, tokensToReturn.length);
    }

    @Override public String toString() {
        return "Player: " + getPlayer() + "; Reserves: " + getCardTier() + " " + getCardIndex() + "; Returns: "
                + Arrays.toString(getTokensToReturn());
    }

    public int getCardTier() {
        return cardTier;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public int[] getTokensToReturn() {
        return tokensToReturn;
    }
}
