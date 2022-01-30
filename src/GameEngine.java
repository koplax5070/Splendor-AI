import java.util.Arrays;

public class GameEngine {
    private GameState state;

    public GameEngine() {
        state = new GameState();
    }

    public GameState handleMove(Move move, GameState state) {
       return switch(move.getType()) {
            case buyCard -> state.handleBuyCardMove((BuyCardMove) move);
            case takeTokens -> state.handleTakeTokensMove((TakeTokensMove) move);
            case reserveCard -> state.handleReserveCardMove((ReserveCardMove) move);
        };
    }

    public GameState getState() {
        return state;
    }
}
