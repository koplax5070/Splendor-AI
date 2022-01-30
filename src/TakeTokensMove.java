import java.util.Arrays;

// Class representing all information needed for a take tokens move to update the game state
public class TakeTokensMove extends Move {
    private final int[] tokensToTake;
    private final int[] tokensToReturn;

    // Construct a TakeTokensMove object
    // player is the player playing the move
    // tokensToTake is the array representing which tokens the player will take from the supply
    // This array will be full of 0s with 3 elements of 1, or 1 element of 2
    // tokensToReturn will represent the tokens to be returned to the supply, in the case in which a player
    // has more than 10 tokens at the end of their turn
    public TakeTokensMove(int player, int[] tokensToTake, int[] tokensToReturn) {
        super(player, Type.takeTokens);
        this.tokensToTake = new int[]{tokensToTake[0], tokensToTake[1], tokensToTake[2], tokensToTake[3], tokensToTake[4]};
        this.tokensToReturn = new int[]{tokensToReturn[0], tokensToReturn[1], tokensToReturn[2], tokensToReturn[3], tokensToReturn[4]};
    }

    @Override
    public String toString() {
        return "Player: " + this.getPlayer() + "; Takes: " + Arrays.toString(tokensToTake) + "; Returns: " +
                Arrays.toString(tokensToReturn) + "\n";
    }

    public int[] getTokensToTake() {
        return tokensToTake;
    }

    public int[] getTokensToReturn() {
        return tokensToReturn;
    }
}
