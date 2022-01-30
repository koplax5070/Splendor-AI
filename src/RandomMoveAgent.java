import java.util.ArrayList;

public class RandomMoveAgent extends Agent {
    public Move generateMove(GameState state) {
        ArrayList<Move> availableMoves = getAvailableMoves(state);
        if(availableMoves.isEmpty())
            return null;

        int numberAvailableMoves = availableMoves.size();
        int chosenMoveIndex = (int)Math.floor(Math.random() * numberAvailableMoves);
        return availableMoves.get(chosenMoveIndex);
    }
}
