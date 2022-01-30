import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MinimaxAgent extends Agent {

    public Move generateMove(final GameState state) {
        return minimaxSearch(state, 1, 1);
    }

    /**
     * @param state - the game state of a game of splendor
     * @return list of unique game states that can result from performing one action
     *
     * If the move is a TakeTokensMove, there will be only one possible next state. However,
     * if it is a ReserveCardMove or BuyCardMove, we need to account for all possible cards that might replace
     * the taken card in the marketplace
     */
    private ArrayList<GameState> getPossibleNextStates(final GameState state,final Move move) {
        ArrayList<GameState> possibleNextStates = new ArrayList<>();

        if(move instanceof TakeTokensMove) {
            possibleNextStates.add(new GameState(state).handleMove(move));
        }

        if(move instanceof BuyCardMove || move instanceof ReserveCardMove) {
            int tier = -1;
            int index = -1;
            if(move instanceof  BuyCardMove) {
                tier = ((BuyCardMove) move).getCardSource();
                index = ((BuyCardMove) move).getCardIndex();
            }
            else {
                tier = ((ReserveCardMove) move).getCardTier();
                index = ((ReserveCardMove) move).getCardIndex();
            }

            // Add each state that can be obtained after buying the card
            possibleNextStates.add(new GameState(state).handleMove(move));

            if(tier > 0  || index < 4) {
                int deckSize = switch(tier) {
                    case 1 -> state.getTier1Deck().getSize();
                    case 2 -> state.getTier2Deck().getSize();
                    case 3 -> state.getTier3Deck().getSize();
                    default -> 0;
                };

                // To obtain possible outcomes, first handle the move. Then, swap the newly replaced card with
                // another card in the same deck, that it could have been instead. Since the agent cannot see the
                // content of the deck, there is no need to actually return the card to the deck as long as the deck
                // size remains correct
                for(int i = 0; i < deckSize - 1; i ++) {
                    GameState newState = new GameState(state).handleMove(move);
                    DevelopmentCard cardFromDeck = switch(tier) {
                        case 1 -> newState.getTier1Deck().getCard(i);
                        case 2 -> newState.getTier2Deck().getCard(i);
                        case 3 -> newState.getTier3Deck().getCard(i);
                        default -> null;
                    };

                    ArrayList<DevelopmentCard> market = switch(tier) {
                        case 1 -> newState.getTier1Market();
                        case 2 -> newState.getTier2Market();
                        case 3 -> newState.getTier3Market();
                        default -> null;
                    };

                    market.set(index, cardFromDeck);
                    possibleNextStates.add(newState);
                }
            }
        }

        return possibleNextStates;
    }

    /**
     * @param state - state of the game from which minimax search starts
     * @param plyNumber - maximum depth of search tree to explore
     * @param expectSamplingRate - ratio of next possible states to be considered for exploration for any given move
     * @return move chosen by the minimax algorithm for the current state
     */
    private Move minimaxSearch(final GameState state, int plyNumber, double expectSamplingRate) {
        int chosenMove = (minimax(state, 0, plyNumber, expectSamplingRate)).intValue();
        return getAvailableMoves(state).get(chosenMove);
    }

    /**
     * @param state - state from which to start (or continue) minimax search
     * @param currentPly - the level of the graph the search is currently in
     * @param plyNumber - the maximum level of the graph to be explored
     * @param expectSamplingRate - the proportion of possible states to be sampled during the search for any individual move
     * @return if ply is 1, return index of the best move. Otherwise, return the expected value of the state resulted from
     * picking the best move for the current player
     */
    private Double minimax(final GameState state, int currentPly, int plyNumber, double expectSamplingRate) {
        ArrayList<Move> availableMoves = getAvailableMoves(state);

        if(currentPly == plyNumber) {
            return linearHeuristic(state);
        }
        else {

            // Value for every move is calculated as an average of the values of (some of the) states that could result
            // from it
            int bestMoveIndex = 0;
            double bestMoveValue = 0;
            for(int i = 0; i < availableMoves.size(); i ++) {

                // Generate all possible states that may result from move i, then reduce the search space by selecting a
                // random subset of states to consider further, according to the sampling rate
                ArrayList<GameState> possibleStates = getPossibleNextStates(state, availableMoves.get(i));
                Collections.shuffle(possibleStates);
                int numberStatesToSearch = (int)Math.max(Math.floor(possibleStates.size() * expectSamplingRate), 1);
                int numberPossibleStates = possibleStates.size();
                for(int j = 0; j < numberPossibleStates - numberStatesToSearch; j ++)
                    possibleStates.remove(0);

                double valueSum = 0;
                for(int j = 0; j < possibleStates.size(); j ++)
                    valueSum += minimax(possibleStates.get(j), currentPly + 1, plyNumber, expectSamplingRate);
                double moveValue = valueSum/possibleStates.size();

                if(i == 0) {
                    bestMoveIndex = i;
                    bestMoveValue = moveValue;
                }
                else {
                    if(currentPly % 2 == 1) {
                        if(moveValue > bestMoveValue) {
                            bestMoveIndex = i;
                            bestMoveValue = moveValue;
                        }

                    }
                    else {
                        if(moveValue < bestMoveValue) {
                            bestMoveIndex = i;
                            bestMoveValue = moveValue;
                        }
                    }
                }
            }
            if(currentPly != 1)
                return bestMoveValue;
            else
                return (double)bestMoveIndex;
        }
    }

    /**
     * @param state - the game state of a game of splendor
     * @return heuristic value of the state for the current player to move
     */
    private Double linearHeuristic(final GameState state) {
        return null;
    }
}
