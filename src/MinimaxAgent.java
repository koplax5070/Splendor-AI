import java.util.*;

public class MinimaxAgent extends Agent {
    private final int plyNumber;
    private final double stateSamplingRate;
    private final double returnSamplingRate;
    private final ArrayList<Double> weights;

    public MinimaxAgent(int plyNumber, double stateSamplingRate, double returnSamplingRate, final ArrayList<Double> weights) {
        super();
        this.plyNumber = plyNumber;
        this.stateSamplingRate = stateSamplingRate;
        this.returnSamplingRate = returnSamplingRate;
        this.weights = new ArrayList<>(weights);
    }

    public Move generateMove(final GameState state) {
        return minimaxSearch(state);
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
            GameState newState = new GameState(state);
            newState.handleMove(move);
            possibleNextStates.add(newState);
        }

        if(move instanceof BuyCardMove || move instanceof ReserveCardMove) {
            int tier;
            int index;
            if(move instanceof  BuyCardMove) {
                tier = ((BuyCardMove) move).getCardSource();
                index = ((BuyCardMove) move).getCardIndex();
            }
            else {
                tier = ((ReserveCardMove) move).getCardTier();
                index = ((ReserveCardMove) move).getCardIndex();
            }

            // Add each state that can be obtained after buying the card
            GameState newState = new GameState(state);
            newState.handleMove(move);
            possibleNextStates.add(newState);

            if(tier > 0  && index < 4) {
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
                    newState = new GameState(state);
                    newState.handleMove(move);
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
     * @return move chosen by the minimax algorithm for the current state
     */
    private Move minimaxSearch(final GameState state) {
        // For alpha-beta pruning, alpha starts at negative infinity and beta at infinity
        int chosenMove = (minimax(state, state.getPlayerToMove(), 1, plyNumber, -2000000000, 2000000000)).intValue();
        ArrayList<Move> availableMoves = reduceTakeTokensMoves(getAvailableMoves(state), state);

        return availableMoves.get(chosenMove);
    }

    /**
     * @param state - state from which to start (or continue) minimax search
     * @param currentPly - the level of the graph the search is currently in
     * @param plyNumber - the maximum level of the graph to be explored
     * @return if ply is 1, return index of the best move. Otherwise, return the expected value of the state resulted from
     * picking the best move for the current player
     */
    private Double minimax(final GameState state, final int player, final int currentPly,final int plyNumber, double alpha, double beta) {
        ArrayList<Move> availableMoves = getAvailableMoves(state);
        reduceTakeTokensMoves(availableMoves, state);

        if(currentPly == plyNumber) {
            if(currentPly == 1) {
                System.out.println("Minimum value for plyNumber should be 3. Lookahead failed for Minimax agent.");
                System.exit(1);
            }
            return linearHeuristic(state, player);
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
                int numberPossibleStates = possibleStates.size();
                int numberStatesToSearch = (int)Math.max(Math.floor(numberPossibleStates * stateSamplingRate), 1);
                if (numberPossibleStates - numberStatesToSearch > 0) {
                    possibleStates.subList(0, numberPossibleStates - numberStatesToSearch).clear();
                }

                double valueSum = 0;
                for (GameState possibleState : possibleStates) {
                    valueSum += minimax(possibleState, player, currentPly + 1, plyNumber, alpha, beta);
                }
                double moveValue = valueSum/possibleStates.size();

                //System.out.println("Move: " + availableMoves.get(i) + "; Value - " + moveValue);

                if(i == 0) {
                    bestMoveValue = moveValue;
                    if(currentPly % 2 == 1) {
                        if(moveValue > alpha) {
                            alpha = moveValue;
                        }
                    }
                    else {
                        if(moveValue < beta) {
                            beta = moveValue;
                        }
                    }
                }
                else {
                    if(currentPly % 2 == 1) {
                        if(moveValue > bestMoveValue) {
                            bestMoveIndex = i;
                            bestMoveValue = moveValue;
                        }
                        if(moveValue > alpha) {
                            alpha = moveValue;
                        }
                    }
                    else {
                        if(moveValue < bestMoveValue) {
                            bestMoveIndex = i;
                            bestMoveValue = moveValue;
                        }
                        if(moveValue < beta) {
                            beta = moveValue;
                        }
                    }
                }

                // Carry out alpha beta pruning
                if (alpha >= beta) {
                    break;
                }
            }
            if(currentPly != 1)
                return bestMoveValue;
            else {
                return (double)bestMoveIndex;
            }
        }
    }

    /**
     * @param state - the game state of a game of splendor
     * @return heuristic value of the state for the current player to move
     */
    private Double linearHeuristic(final GameState state, final int player)
    {
        /* Obtain parameter values from state
        Parameter list, in order:
        -hasWon
        -isWinning
        -currentPoints
        -opponentPoints
        -maxImmediatePoints
        -buyFromMarketIndex
        -buyFromReserveIndex
        -attractNobleIndex
        -numCardsToReserve
        -totalTokens
         */
        ArrayList<Double> parameters = getParameterValues(state, player);

        double value = 0;
        for(int i = 0; i < parameters.size(); i ++)
            value += parameters.get(i) * weights.get(i);

        return value;
    }

    /**
     * @param state - game state being analyzed (from perspective of playerToMove)
     * @return array of parameter values, as specified below:
     * -hasWon - 1 if the player has won, -1 if the player has lost, 0 in case game is not over or there is a draw
     * -isWinning - 1 if the player can make a move that is a guaranteed win, -1 if the player has no choice but to
     * make a move that allows the opponent to win on their turn (is losing), 0 otherwise
     * -currentPoints - current score of the player, taking integer values between 0 and 22
     * -opponentPoints - current score of the opponent, taking integer values between 0 and 22
     * -maxImmediatePoints - the maximum amount of points the player can gain by making a single move
     * -buyFromMarketIndex - index measuring how close the player is to buying cards in the market (weighed by
     * point value of cards)
     * -buyFromReserveCardIndex - index measuring how close the player is to buying cards in the reserve (weighted by
     * point value of cards)
     * -attractNobleIndex - index measuring how close the player is to attracting a noble
     * -numCardsToReserve - number of cards the player can reserve until they reach the reserve limit
     * -totalTokens - number of tokens the player has
     */
    private ArrayList<Double> getParameterValues(final GameState state, final int player) {

        // If game has ended or is about to end, evaluate whether player has won or will win
        // Can be 1 if winning, 0 if a draw will happen, and -1 if losing, assuming optimal play from opponent
        double hasWon = 0;
        double isWinning = 0;

        // Maximum number of points that can be obtained in a single move from current state
        double maxImmediatePoints = 0;

        // Game has ended
        if(state.isGameOver()) {
            if(state.getWinner() == player)
                hasWon = 1;
            else if(state.getWinner() == 0)
                hasWon = 0;
            else
                hasWon = -1;
        }

        // Current state is last turn of the game - the game ends after playerToMove makes their move
        else if(state.isLastTurn()) {
            // Player has one turn to match or exceed opponent score, else they lose
            if(player == state.getPlayerToMove()) {
                int playerScore, opponentScore;
                if(player == 2) {
                    opponentScore = state.getPlayer1Score();
                    playerScore = state.getPlayer2Score();
                }
                else {
                    opponentScore = state.getPlayer2Score();
                    playerScore = state.getPlayer1Score();
                }

                // Check if player can match or exceed opponent score
                boolean isDraw = false, isWin = false;
                ArrayList<BuyCardMove> availableBuyCardMoves = getAvailableBuyCardMoves(state);
                for(BuyCardMove move : availableBuyCardMoves) {
                    int additionalPoints = getPointsFromMove(state, move);
                    if(additionalPoints > maxImmediatePoints)
                        maxImmediatePoints = additionalPoints;
                    if (playerScore + additionalPoints > opponentScore)
                        isWin = true;
                    if (playerScore + additionalPoints == opponentScore)
                        isDraw = true;
                }

                if(isWin)
                    isWinning = 1;
                else if(isDraw)
                    isWinning = 0;
                else isWinning = -1;
            }
            // Opponent has one turn to match or exceed player score, else they lose
            else {
                int playerScore, opponentScore;
                if(player == 1) {
                    opponentScore = state.getPlayer1Score();
                    playerScore = state.getPlayer2Score();
                }
                else {
                    opponentScore = state.getPlayer2Score();
                    playerScore = state.getPlayer1Score();
                }

                // Check if opponent can match or exceed player score
                boolean isDraw = false, isLoss = false;
                ArrayList<BuyCardMove> opponentBuyCardMoves = getAvailableBuyCardMoves(state);
                for(BuyCardMove move : opponentBuyCardMoves) {
                    int additionalPoints = getPointsFromMove(state, move);
                    if (opponentScore + additionalPoints > playerScore)
                        isLoss = true;
                    if (opponentScore + additionalPoints == playerScore)
                        isDraw = true;
                }

                if(isLoss) {
                    isWinning = -1;
                }
                else if(isDraw) {
                    isWinning = 0;
                }
                else {
                    isWinning = 1;
                }
            }
        }
        // Check if player can get over 15 points in one move (only if it's player's turn)
        // IsWinning stays 0 if opponent has a response that could lead to a draw or loss
        else if(player == state.getPlayerToMove()){
            int playerScore, opponentScore;
            if(player == 2) {
                opponentScore = state.getPlayer1Score();
                playerScore = state.getPlayer2Score();
            }
            else {
                opponentScore = state.getPlayer2Score();
                playerScore = state.getPlayer1Score();
            }

            ArrayList<BuyCardMove> availableBuyCardMoves = getAvailableBuyCardMoves(state);

            // Getting 15 points or more is winning
            if(state.getPlayerStarted() != player) {
                for(BuyCardMove move : availableBuyCardMoves) {
                    int additionalPoints = getPointsFromMove(state, move);
                    if(additionalPoints > maxImmediatePoints)
                        maxImmediatePoints = additionalPoints;
                    if(playerScore + additionalPoints >= 15) {
                        isWinning = 1;
                        break;
                    }
                }

            }
            // Need to evaluate opponent's options before deciding if player is winning
            else {
                for(BuyCardMove move: availableBuyCardMoves) {
                    int additionalPoints = getPointsFromMove(state, move);
                    if(additionalPoints > maxImmediatePoints)
                        maxImmediatePoints = additionalPoints;

                    if(playerScore + additionalPoints >= 15) {
                        int newPlayerScore = playerScore + additionalPoints;

                        // Ignore possibility that freshly replaced card is the one bought by opponent, for performance
                        // purposes
                        GameState nextState = getPossibleNextStates(state, move).get(0);
                        ArrayList<BuyCardMove> opponentBuyCardMoves = getAvailableBuyCardMoves(nextState);

                        boolean opponentHasMove = false;
                        for(BuyCardMove opponentMove : opponentBuyCardMoves)
                            if(opponentMove.getCardIndex() != move.getCardIndex() ||
                                    opponentMove.getCardSource() != move.getCardSource() ||
                                    opponentMove.getCardSource() == 0) {
                                if(opponentScore + getPointsFromMove(nextState, opponentMove) >= newPlayerScore) {
                                    opponentHasMove = true;
                                    break;
                                }
                            }

                        if(!opponentHasMove) {
                            isWinning = 1;
                            break;
                        }
                    }
                }
            }

        }
        // Check if opponent can get over 15 points in one move (only if it's opponent's turn)
        // IsWinning stays 0 if player has a response that could lead to a draw or win
        else {
            int playerScore, opponentScore;
            if(player == 1) {
                opponentScore = state.getPlayer1Score();
                playerScore = state.getPlayer2Score();
            }
            else {
                opponentScore = state.getPlayer2Score();
                playerScore = state.getPlayer1Score();
            }

            ArrayList<BuyCardMove> opponentBuyCardMoves = getAvailableBuyCardMoves(state);

            // Getting 15 points or more is winning for opponent
            if(state.getPlayerStarted() != state.getPlayerToMove()) {
                for (BuyCardMove move : opponentBuyCardMoves) {
                    int additionalPoints = getPointsFromMove(state, move);
                    if (opponentScore + additionalPoints >= 15) {
                        isWinning = -1;
                        break;
                    }
                }
            }
            // Otherwise, need to evaluate player's options before deciding if opponent is winning
            else {
                for(BuyCardMove move: opponentBuyCardMoves) {
                    int additionalPoints = getPointsFromMove(state, move);

                    if(opponentScore + additionalPoints >= 15) {
                        int newOpponentScore = opponentScore + additionalPoints;

                        // Ignore possibility that freshly replaced card is the one bought by player, for performance
                        // purposes
                        GameState nextState = getPossibleNextStates(state, move).get(0);
                        ArrayList<BuyCardMove> playerBuyCardMoves = getAvailableBuyCardMoves(nextState);

                        boolean playerHasMove = false;
                        for(BuyCardMove playerMove : playerBuyCardMoves)
                            if(playerMove.getCardIndex() != move.getCardIndex() ||
                                    playerMove.getCardSource() != move.getCardSource() ||
                                    playerMove.getCardSource() == 0) {
                                if(playerScore + getPointsFromMove(nextState, playerMove) >= newOpponentScore) {
                                    playerHasMove = true;
                                    break;
                                }
                            }

                        if(!playerHasMove) {
                            isWinning = -1;
                            break;
                        }
                    }
                }
            }

            // In this case, we also need to compute the maximum immediate points the player could win, provided
            // opponent doesn't take that card first
            // For this purpose, opponent will first make an imaginary move,if possible a TakeTokens one
            boolean imaginaryMoveMade = false;
            GameState imaginaryState = new GameState(state);
            ArrayList<Move> opponentMoves = getAvailableMoves(imaginaryState);
            for (Move opponentMove : opponentMoves) {
                if (opponentMove instanceof TakeTokensMove) {
                    imaginaryState.handleMove(opponentMove);
                    imaginaryMoveMade = true;
                    break;
                }
            }
            if(!imaginaryMoveMade) {
                imaginaryState.handleMove(opponentMoves.get(0));
            }

            // Get maximum points that can be made by the player
            ArrayList<BuyCardMove> playerImaginaryMoves = getAvailableBuyCardMoves(imaginaryState);
            for(BuyCardMove playerMove : playerImaginaryMoves) {
                int additionalPoints = getPointsFromMove(imaginaryState, playerMove);

                if(additionalPoints > maxImmediatePoints)
                    maxImmediatePoints = additionalPoints;
            }
        }

        double currentPoints = switch (player) {
            case 1 -> state.getPlayer1Score();
            case 2 -> state.getPlayer2Score();
            default -> 0;
        };
        double opponentPoints = switch(player) {
            case 1 -> state.getPlayer2Score();
            case 2 -> state.getPlayer1Score();
            default -> 0;
        };

        // The following indices are aimed at measuring numerically how close the player is to performing actions that
        // are beneficial to them, in order to encourage plays that generate improvement

        // Buy from market index- how close the player is to buying cards from the market
        double buyFromMarketIndex = buyFromMarketIndex(state , player);

        // Attract noble index - how close the player is to attracting a noble
        double attractNobleIndex = attractNobleIndex(state, player);

        // Buy from reserve index - how close the player is to buying cards from their reserve
        double buyFromReserveIndex = buyFromReserveIndex(state, player);

        // The following parameters refer to aspects that may potentially hinder the other player, such as being able
        // to reserve cards or hoard tokens to diminish the opponent's range of moves

        // How many cards the player can still reserve
        ArrayList<DevelopmentCard> reserve = switch(player) {
            case 1 -> state.getPlayer1Reserve();
            case 2 -> state.getPlayer2Reserve();
            default -> null;
        };
        assert reserve != null;
        double numCardsToReserve = 3 - reserve.size();

        // How many tokens the player has
        double totalTokens = 0;
        int[] tokens = switch (player) {
            case 1 -> state.getPlayer1Tokens();
            case 2 -> state.getPlayer2Tokens();
            default -> null;
        };
        assert tokens != null;
        for (int token : tokens) totalTokens += token;

        ArrayList<Double> parameters = new ArrayList<>();
        parameters.add(hasWon);
        parameters.add(isWinning);
        parameters.add(currentPoints);
        parameters.add(opponentPoints);
        parameters.add(maxImmediatePoints);
        parameters.add(buyFromMarketIndex);
        parameters.add(buyFromReserveIndex);
        parameters.add(attractNobleIndex);
        parameters.add(numCardsToReserve);
        parameters.add(totalTokens);

        return parameters;
    }

    /**
     * @param movesList - list of available moves for a given state
     * @param state - state being considered
     * @return - reduced list of moves, such that only the top returnSamplingRate/1.0 % of moves including returns
     * are considered. The moves are selected according to the takeTokensHeuristic. All moves not including any returns
     * will still be considered.
     */
    private ArrayList<Move> reduceTakeTokensMoves(ArrayList<Move> movesList, final GameState state) {
        ArrayList<Double> takeTokensValues = new ArrayList<>();

        // Set heuristic values
        for (Move move : movesList) {
            if (move instanceof TakeTokensMove) {
                takeTokensValues.add(takeTokensHeuristic((TakeTokensMove) move, state));
            }
        }

        if(takeTokensValues.size() == 0)
            return movesList;

        ArrayList<Double> takeTokensValuesSorted = new ArrayList<>(takeTokensValues);
        Collections.sort(takeTokensValuesSorted);

        // Determine number of moves that need to be considered
        int numMovesToConsider = (int)(1.0 * takeTokensValues.size() * returnSamplingRate);
        if(numMovesToConsider < 1 && takeTokensValues.size() > 0)
            numMovesToConsider = 1;

        // Find threshold for moves to be considered
        double thresholdValue = takeTokensValuesSorted.get(takeTokensValuesSorted.size() - numMovesToConsider);

        // Remove moves worse than the threshold
        int index = 0;
        int numberRemoved = 0;
        for(int i = 0; i < movesList.size(); i ++) {
            if(movesList.get(i) instanceof TakeTokensMove) {
                if(takeTokensValues.get(index) < thresholdValue && numberRemoved < takeTokensValues.size()) {
                    movesList.remove(i);
                    i--;
                    numberRemoved ++;
                }
                index ++;
            }
        }

        return movesList;
    }

    /**
     * Determine the value of a takeTokensMove according to indices of buying power, both from market and from reserve
     * @param move - takeTokensMove to be considered
     * @param state - current game state
     * @return value of move according to heuristic
     */
    private double takeTokensHeuristic(final TakeTokensMove move, final GameState state) {
        double marketWeight = 1.0, reserveWeight = 5.0;

        GameState resultState = new GameState(state);
        resultState.handleTakeTokensMove(move);

        return marketWeight * buyFromMarketIndex(resultState, move.getPlayer()) +
                reserveWeight * buyFromReserveIndex(resultState, move.getPlayer());
    }

    /**
     * Evaluate how close the player is to making points by buying a card from the market
     * Calculated using formula: sum_market(pointValue/(numTokensNeeded + 1))
     * That way, the number will be high when the player is close to buying high value cards, and moderate when the
     * player is close to buying low value cards
     * @param state - current state to be considered
     * @return value of index for state
     */
    private double buyFromMarketIndex(final GameState state, final int player) {
        double buyFromMarketIndex = 0;

        for(int i = 0; i < state.getTier1Market().size(); i ++) {
            DevelopmentCard card = state.getTier1Market().get(i);
            int tokensNeeded = countTokensToBuy(state, player, card);

            buyFromMarketIndex += 1.0*card.getPointValue()/(tokensNeeded + 1);
        }
        for(int i = 0; i < state.getTier2Market().size(); i ++) {
            DevelopmentCard card = state.getTier2Market().get(i);
            int tokensNeeded = countTokensToBuy(state, player, card);

            buyFromMarketIndex += 1.0*card.getPointValue()/(tokensNeeded + 1);
        }
        for(int i = 0; i < state.getTier3Market().size(); i ++) {
            DevelopmentCard card = state.getTier3Market().get(i);
            int tokensNeeded = countTokensToBuy(state, player, card);

            buyFromMarketIndex += 1.0*card.getPointValue()/(tokensNeeded + 1);
        }

        return buyFromMarketIndex;
    }

    /**
     * @param state - current state to be considered
     * @return value of attractNobleIndex for current state
     * Evaluate how close the player is to attracting a noble based on the cards they have currently
     * Calculated using formula sum_nobles(1/(cardsNeeded+1))
     */
    private double attractNobleIndex(final GameState state, final int player) {
        double attractNobleIndex = 0;

        for(int i = 0; i < state.getNoblesMarket().size(); i ++) {
            int cardsNeeded = countCardsToAttract(state, player, state.getNoblesMarket().get(i));
                attractNobleIndex += 1.0/(cardsNeeded + 1);
        }

        return attractNobleIndex;
    }

    /**
     * @param state -
     * @return
     * Evaluate how close the player is to making points by buying a card from the reserve
     * Calculated using formula: sum_reserve(pointValue/(numTokensNeeded + 1))
     */
    private double buyFromReserveIndex(final GameState state, final int player) {
        double buyFromReserveIndex = 0;

        ArrayList<DevelopmentCard> reserve = switch(player) {
            case 1 -> state.getPlayer1Reserve();
            case 2 -> state.getPlayer2Reserve();
            default -> null;
        };
        assert reserve != null;
        for (DevelopmentCard developmentCard : reserve) {
            int tokensNeeded = countTokensToBuy(state, player, developmentCard);
            buyFromReserveIndex += 1.0 * developmentCard.getPointValue() / (tokensNeeded + 1);
        }

        return buyFromReserveIndex;
    }

    private int getPointsFromMove(final GameState state, final BuyCardMove move) {
        int points = 0;
        if (move.getChosenNobleIndex() != -1)
            points += 3;
        points += switch (move.getCardSource()) {
            case 0 -> switch (move.getPlayer()) {
                case 1 -> state.getPlayer1Reserve().get(move.getCardIndex()).getPointValue();
                case 2 -> state.getPlayer2Reserve().get(move.getCardIndex()).getPointValue();
                default -> 0;
            };
            case 1 -> state.getTier1Market().get(move.getCardIndex()).getPointValue();
            case 2 -> state.getTier2Market().get(move.getCardIndex()).getPointValue();
            case 3 -> state.getTier3Market().get(move.getCardIndex()).getPointValue();
            default -> 0;
        };
        return points;
    }

    /**
     * @param state - current state of the game
     * @param card - card being considered
     * @return number of tokens the player to move still needs to acquire in order to purchase a card
     */
    private int countTokensToBuy(final GameState state, final int player, final DevelopmentCard card) {
        int[] tokens;
        int[] cards;
        if(player == 1) {
            tokens = state.getPlayer1Tokens();
            cards = state.getPlayer1Cards();
        }
        else {
            tokens = state.getPlayer2Tokens();
            cards = state.getPlayer2Cards();
        }

        int tokensNeeded = 0;

        for(int i = 0; i < card.getCostArray().length; i ++) {
            int colorTokensNeeded = card.getCostArray()[i] - tokens[i] - cards[i];
            if(colorTokensNeeded > 0)
                tokensNeeded += colorTokensNeeded;
        }

        if(tokens[5] > tokensNeeded)
            tokensNeeded = 0;
        else tokensNeeded -= tokens[5];

        return tokensNeeded;
    }

    /**
     * @param state - current state of the game
     * @param tile - noble time being considered
     * @return number of cards the player to move still needs to acquire in order to attract a noble
     */
    private int countCardsToAttract(final GameState state, final int player, final NobleTile tile) {
        int cardsNeeded = 0;
        int[] cards = switch(player) {
            case 1 -> state.getPlayer1Cards();
            case 2 -> state.getPlayer2Cards();
            default -> null;
        };

        for(int i = 0; i < tile.getRequiredArray().length; i ++) {
            assert cards != null;
            int colorCardsNeeded = tile.getRequiredArray()[i] - cards[i];
            if(colorCardsNeeded  > 0)
                cardsNeeded += colorCardsNeeded;
        }

        return cardsNeeded;
    }

}
