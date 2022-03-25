import java.util.ArrayList;
import java.util.Arrays;

abstract class Agent {
    public abstract Move generateMove(GameState state);

    /**
     * @param state - game state at some point in the game
     * @return array of available moves the player can make at the given state
     */
    public ArrayList<Move> getAvailableMoves(final GameState state) {
        ArrayList<Move> resultArray = new ArrayList<>(getAvailableBuyCardMoves(state));
        resultArray.addAll(getAvailableReserveCardMoves(state));
        resultArray.addAll(getAvailableTakeTokensMoves(state));
        
        return resultArray;
    }

    /**
     * @param state - game state at some point in the game
     * @return array of available TakeTokensMoves for the given state
     */
    public ArrayList<TakeTokensMove> getAvailableTakeTokensMoves(final GameState state) {
        int player = state.getPlayerToMove();
        int[] playerTokens = new int[6];
        if(player == 1)
            playerTokens = state.getPlayer1Tokens();
        if(player == 2)
            playerTokens = state.getPlayer2Tokens();
        int[] supplyTokens = state.getSupplyTokens();
        ArrayList<TakeTokensMove> moveList = new ArrayList<>();

        // Generate all possibilities for taking tokens

        // Generate moves taking 2 identical tokens
        for(int i = 0; i < supplyTokens.length - 1; i ++) {
            // Need pile to have at least 4 tokens
            if(supplyTokens[i] >= 4) {
                int[] tokensToTake = {0, 0, 0, 0, 0};
                tokensToTake[i] = 2;
                int[] tokensToReturn = {0, 0, 0, 0, 0};

                // Need to return tokens
                if(Arrays.stream(playerTokens).sum() + 2 > 10) {
                    // Need to return at most 2 tokens
                    int numTokensToReturn = Arrays.stream(playerTokens).sum() + 2 - 10;

                    if(numTokensToReturn == 1) {
                        // Return a single token
                        for(int j = 0; j < playerTokens.length - 1; j ++)
                            if(playerTokens[j] + tokensToTake[j] >= 1) {
                                tokensToReturn[j] = 1;
                                moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                tokensToReturn[j] = 0;
                            }
                    }
                    else {
                        // Return two identical tokens
                        for(int j = 0; j < playerTokens.length - 1; j ++)
                            if(playerTokens[j] + tokensToTake[j] >= 2) {
                                tokensToReturn[j] = 2;
                                moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                tokensToReturn[j] = 0;
                            }

                        // Return two different tokens
                        for(int j = 0; j < playerTokens.length - 2; j ++)
                            if(playerTokens[j] + tokensToTake[j] >= 1)
                                for(int k = j + 1; k < playerTokens.length - 1; k ++)
                                    if(playerTokens[k] + tokensToTake[k] >= 1) {
                                        tokensToReturn[j] = 1;
                                        tokensToReturn[k] = 1;
                                        moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                        tokensToReturn[j] = 0;
                                        tokensToReturn[k] = 0;
                                    }
                    }
                }
                // No returns needed
                else {
                    moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                }
            }
        }

        // Generate moves taking 3 (or fewer) different tokens

        // Count available supply piles
        int availableSupplyPiles = 0;
        for (int i = 0; i < supplyTokens.length - 1; i++)
            if (supplyTokens[i] > 0)
                availableSupplyPiles++;

        // Need special case for 1 available supply pile
        if(availableSupplyPiles == 1) {
            for(int i = 0; i < supplyTokens.length - 1; i ++)
                if(supplyTokens[i] >= 1) {
                    int[] tokensToTake = {0, 0, 0, 0, 0};
                    tokensToTake[i] = 1;
                    int[] tokensToReturn = {0, 0, 0, 0, 0};

                    // Need to return tokens
                    if(Arrays.stream(playerTokens).sum() + 1 > 10) {
                        // Need to return a single token
                        for(int j = 0; j < playerTokens.length - 1; j ++)
                            if(playerTokens[j] + tokensToTake[j] >= 1) {
                                tokensToReturn[j] = 1;
                                moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                tokensToReturn[j] = 0;
                            }
                    }
                    // No returns needed
                    else {
                        moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                    }

                    // Break loop, as all moves for this case will have now been added
                    break;
                }
        }
        // Need special case for 2 available supply piles
        else if(availableSupplyPiles == 2) {
            for(int i = 0; i < supplyTokens.length - 2; i ++)
                if(supplyTokens[i] >= 1)
                    for(int j = i + 1; j < supplyTokens.length - 1; j ++)
                        if(supplyTokens[j] >= 1) {
                            int[] tokensToTake = {0, 0, 0, 0, 0};
                            tokensToTake[i] = 1;
                            tokensToTake[j] = 1;
                            int[] tokensToReturn = {0, 0, 0, 0, 0};

                            // Need to return tokens
                            if(Arrays.stream(playerTokens).sum() + 2 > 10) {
                                // Need to return at most 2 tokens
                                int numTokensToReturn = Arrays.stream(playerTokens).sum() + 2 - 10;

                                if(numTokensToReturn == 1) {
                                    // Return a single token
                                    for(int k = 0; k < playerTokens.length - 1; k ++)
                                        if(playerTokens[k] + tokensToTake[k] >= 1) {
                                            tokensToReturn[k] = 1;
                                            moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                            tokensToReturn[k] = 0;
                                        }
                                }
                                else {
                                    // Return two identical tokens
                                    for(int k = 0; k < playerTokens.length - 1; k ++)
                                        if(playerTokens[k] + tokensToTake[k] >= 2) {
                                            tokensToReturn[k] = 2;
                                            moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                            tokensToReturn[k] = 0;
                                        }

                                    // Return two different tokens
                                    for(int k = 0; k < playerTokens.length - 2; k ++)
                                        if(playerTokens[k] + tokensToTake[k] >= 1)
                                            for(int t = k + 1; t < playerTokens.length - 1; t ++)
                                                if(playerTokens[t] + tokensToTake[t] >= 1) {
                                                    tokensToReturn[k] = 1;
                                                    tokensToReturn[t] = 1;
                                                    moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                                    tokensToReturn[k] = 0;
                                                    tokensToReturn[t] = 0;
                                                }
                                }
                            }
                            // No returns needed
                            else {
                                moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                            }

                            // Break both loops, as all moves for this case will have been added
                            i = supplyTokens.length;
                            break;
                        }
        }
        // Base case - 3 or more available supply piles
        else {
            for(int i = 0; i < supplyTokens.length - 3; i ++)
                if(supplyTokens[i] >= 1)
                    for(int j = i + 1; j < supplyTokens.length - 2; j ++)
                        if(supplyTokens[j] >= 1)
                            for(int k = j + 1; k < supplyTokens.length - 1; k ++)
                                if(supplyTokens[k] >= 1) {
                                    int[] tokensToTake = {0, 0, 0, 0, 0};
                                    tokensToTake[i] = 1;
                                    tokensToTake[j] = 1;
                                    tokensToTake[k] = 1;
                                    int[] tokensToReturn = {0, 0, 0, 0, 0};

                                    // Need to return tokens
                                    if(Arrays.stream(playerTokens).sum() + 3 > 10) {
                                        // Need to return at most 3 tokens
                                        int numTokensToReturn = Arrays.stream(playerTokens).sum() + 3 - 10;

                                        if(numTokensToReturn == 1) {
                                            // Return a single token
                                            for(int t = 0; t < playerTokens.length - 1; t ++)
                                                if(playerTokens[t] + tokensToTake[t] >= 1) {
                                                    tokensToReturn[t] = 1;
                                                    moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                                    tokensToReturn[t] = 0;
                                                }
                                        }
                                        else if(numTokensToReturn == 2) {
                                            // Return two identical tokens
                                            for(int t = 0; t < playerTokens.length - 1; t ++)
                                                if(playerTokens[t] + tokensToTake[t] >= 2) {
                                                    tokensToReturn[t] = 2;
                                                    moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                                    tokensToReturn[t] = 0;
                                                }

                                            // Return two different tokens
                                            for(int t = 0; t < playerTokens.length - 2; t ++)
                                                if(playerTokens[t] + tokensToTake[t] >= 1)
                                                    for(int u = t + 1; u < playerTokens.length - 1; u ++)
                                                        if(playerTokens[u] + tokensToTake[u] >= 1) {
                                                            tokensToReturn[t] = 1;
                                                            tokensToReturn[u] = 1;
                                                            moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                                            tokensToReturn[t] = 0;
                                                            tokensToReturn[u] = 0;
                                                        }
                                        }
                                        else {
                                            // Return three identical tokens
                                            for(int t = 0; t < playerTokens.length - 1; t ++)
                                                if(playerTokens[t] + tokensToTake[t] >= 3) {
                                                    tokensToReturn[t] = 3;
                                                    moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                                    tokensToReturn[t] = 0;
                                                }

                                            // Return two tokens of one type and one of another type
                                            for(int t = 0; t < playerTokens.length - 2; t ++) {
                                                if(playerTokens[t] + tokensToTake[t] >= 1) {
                                                    for(int u = t + 1; u < playerTokens.length - 1; u ++)
                                                        if(playerTokens[u] + tokensToTake[u] >= 2) {
                                                            tokensToReturn[t] = 1;
                                                            tokensToReturn[u] = 2;
                                                            moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                                            tokensToReturn[t] = 0;
                                                            tokensToReturn[u] = 0;
                                                        }
                                                }
                                                if(playerTokens[t] + tokensToTake[t] >= 2) {
                                                    for(int u = t + 1; u < playerTokens.length - 1; u ++)
                                                        if(playerTokens[u] + tokensToTake[u] >= 1) {
                                                            tokensToReturn[t] = 2;
                                                            tokensToReturn[u] = 1;
                                                            moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                                            tokensToReturn[t] = 0;
                                                            tokensToReturn[u] = 0;
                                                        }

                                                }
                                            }

                                            // Return three different tokens
                                            for(int t = 0; t < playerTokens.length - 3; t ++)
                                                if(playerTokens[t] + tokensToTake[t] >= 1)
                                                    for(int u = t + 1; u < playerTokens.length - 2; u ++)
                                                        if(playerTokens[u] + tokensToTake[u] >= 1)
                                                            for(int v = u + 1; v < playerTokens.length - 1; v ++)
                                                                if(playerTokens[v] + tokensToTake[v] >= 1) {
                                                                    tokensToReturn[t] = 1;
                                                                    tokensToReturn[u] = 1;
                                                                    tokensToReturn[v] = 1;
                                                                    moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                                                    tokensToReturn[t] = 0;
                                                                    tokensToReturn[u] = 0;
                                                                    tokensToReturn[v] = 0;
                                                                }
                                        }

                                    }
                                    // No returns needed
                                    else {
                                        moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
                                    }
                                }

        }

        // Exception case - player has no available moves but opponent does, forcing player to take 0 tokens
        if(state.playerHasNoValidMoves(player) && !state.isGameOver()) {
            int[] tokensToTake = {0, 0, 0, 0, 0};
            int[] tokensToReturn = {0, 0, 0, 0, 0};
            moveList.add(new TakeTokensMove(player, tokensToTake, tokensToReturn));
        }

        // Remove moves leading to identical states
        return removeIdenticalTakeTokensMoves(state, moveList);
    }

    /**
     * @param state - game state at some point in the game
     * @return array of available ReserveCardMoves for the given state
     */
    public ArrayList<ReserveCardMove> getAvailableReserveCardMoves(final GameState state) {
        ArrayList<ReserveCardMove> resultArray = new ArrayList<>();
        int player = state.getPlayerToMove();

        // Check if reserve limit has been reached
        if((player == 1 && state.getPlayer1Reserve().size() < 3) || (player == 2 && state.getPlayer2Reserve().size() < 3)) {
            // Check if returning tokens is needed
            int[] tokensToReturn = {0, 0, 0, 0, 0};
            boolean needToReturn = false;
            if(state.getSupplyTokens()[5] > 0)
                if((player == 1 && Arrays.stream(state.getPlayer1Tokens()).sum() >= 10)
                        || (player == 2 && Arrays.stream(state.getPlayer2Tokens()).sum() >= 10))
                    needToReturn = true;

            if(needToReturn) {
                // Go through all possibilities for returning the token, and through all cards for each case
                int[] playerColoredTokens = {0, 0, 0, 0, 0};
                if(player == 1) {
                    for(int i = 0; i < state.getPlayer1Tokens().length - 1; i ++)
                        playerColoredTokens[i] = state.getPlayer1Tokens()[i];

                }
                else {
                    for(int i = 0; i < state.getPlayer2Tokens().length - 1; i ++)
                        playerColoredTokens[i] = state.getPlayer2Tokens()[i];

                }

                for(int i = 0; i < playerColoredTokens.length - 1; i ++)
                    if(playerColoredTokens[i] > 0) {
                        tokensToReturn[i] = 1;

                        // Go through all tiers and cards, including blind reserve
                        for(int j = 0; j < state.getTier1Market().size(); j ++)
                            resultArray.add(new ReserveCardMove(player, j, 1, tokensToReturn));
                        if(!state.getTier1Deck().isEmpty())
                            resultArray.add(new ReserveCardMove(player, 4, 1, tokensToReturn));

                        for(int j = 0; j < state.getTier2Market().size(); j ++)
                            resultArray.add(new ReserveCardMove(player, j, 2, tokensToReturn));
                        if(!state.getTier2Deck().isEmpty())
                            resultArray.add(new ReserveCardMove(player, 4, 2, tokensToReturn));

                        for(int j = 0; j < state.getTier3Market().size(); j ++)
                            resultArray.add(new ReserveCardMove(player, j, 3, tokensToReturn));
                        if(!state.getTier3Deck().isEmpty())
                            resultArray.add(new ReserveCardMove(player, 4, 3, tokensToReturn));

                        tokensToReturn[i] = 0;
                    }
            }
            else {
                // Go through all tiers and cards, including blind reserve
                for(int i = 0; i < state.getTier1Market().size(); i ++)
                    resultArray.add(new ReserveCardMove(player, i, 1, tokensToReturn));
                if(!state.getTier1Deck().isEmpty())
                    resultArray.add(new ReserveCardMove(player, 4, 1, tokensToReturn));

                for(int i = 0; i < state.getTier2Market().size(); i ++)
                    resultArray.add(new ReserveCardMove(player, i, 2, tokensToReturn));
                if(!state.getTier2Deck().isEmpty())
                    resultArray.add(new ReserveCardMove(player, 4, 2, tokensToReturn));

                for(int i = 0; i < state.getTier3Market().size(); i ++)
                    resultArray.add(new ReserveCardMove(player, i, 3, tokensToReturn));
                if(!state.getTier3Deck().isEmpty())
                    resultArray.add(new ReserveCardMove(player, 4, 3, tokensToReturn));
            }
        }

        return resultArray;
    }

    /**
     * @param state - game state at some point in the game
     * @return array of available ReserveCardMoves for the given state
     */
    public ArrayList<BuyCardMove> getAvailableBuyCardMoves(final GameState state) {
        ArrayList<BuyCardMove> movesList = new ArrayList<>();
        int player = state.getPlayerToMove();
        int[] playerCards = new int[5];

        if (player == 1) {
            for (int i = 0; i < state.getPlayer1Cards().length; i++)
                playerCards[i] = state.getPlayer1Cards()[i];
        } else if (player == 2) {
            for (int i = 0; i < state.getPlayer2Cards().length; i++)
                playerCards[i] = state.getPlayer2Cards()[i];
        }

        // Only check moves attracting nobles if player has 3 piles larger than 2 cards, or 2 piles larger than 3 cards
        int numStacks2Cards = 0, numStacks3Cards = 0;
        for (int playerCard : playerCards) {
            if (playerCard >= 3) {
                numStacks3Cards++;
                numStacks2Cards++;
            } else if (playerCard >= 2) {
                numStacks2Cards++;
            }
        }

        for (int nobleIndex = -1; nobleIndex < state.getNoblesMarket().size(); nobleIndex ++) {
            if (nobleIndex < 0 || (numStacks2Cards >= 3 || numStacks3Cards >= 2)) {
                for (int tier = 0; tier <= 3; tier++) {
                    int numberTierCards = 0;
                    if (tier == 0) {
                        if (player == 1)
                            numberTierCards = state.getPlayer1Reserve().size();
                        else if (player == 2)
                            numberTierCards = state.getPlayer2Reserve().size();
                    } else if (tier == 1)
                        numberTierCards = state.getTier1Market().size();
                    else if (tier == 2)
                        numberTierCards = state.getTier2Market().size();
                    else
                        numberTierCards = state.getTier3Market().size();

                    for (int cardIndex = 0; cardIndex < numberTierCards; cardIndex++) {
                        BuyCardMove newMove = new BuyCardMove(player, tier, cardIndex, nobleIndex);
                        if (state.buyCardMoveIsValid(newMove))
                            movesList.add(newMove);
                    }
                }
            }
        }

        return movesList;
    }

    /**
     * @param movesList - array of available moves generated
     * @return processed array, where multiple moves leading to the same game state are reduced to a single move
     * In some cases, multiple take tokens moves leading to the same state will be generated.
     * This is often the case with moves where there is a need to return card.
     * This function takes an array of generated TakeTokensMove items, and only keeps one move for each resulting state,
     * returning the resulting array
     */
    private ArrayList<TakeTokensMove> removeIdenticalTakeTokensMoves(final GameState state, ArrayList<TakeTokensMove> movesList) {
        ArrayList<int[]> uniquePlayerTokensArrays = new ArrayList<>();
        int[] playerTokens = {0, 0, 0, 0, 0, 0};
        if (state.getPlayerToMove() == 1) {
            for(int i = 0; i < state.getPlayer1Tokens().length; i ++)
                playerTokens[i] = state.getPlayer1Tokens()[i];
        }
        else if(state.getPlayerToMove() == 2) {
            for(int i = 0; i < state.getPlayer1Tokens().length; i ++)
                playerTokens[i] = state.getPlayer2Tokens()[i];
        }

        for(int i = 0; i < movesList.size(); i ++) {
            int[] resultingPlayerTokens = new int[6];

            // See resulting player tokens
            for(int j = 0; j < playerTokens.length - 1; j ++) {
                resultingPlayerTokens[j] = playerTokens[j] + movesList.get(i).getTokensToTake()[j];
            }

            // Check if the layout has been achieved before
            // If not, add to list of unique arrays
            boolean layoutIsUnique = true;
            for (int[] uniqueArray : uniquePlayerTokensArrays) {
                boolean allPilesIdentical = true;
                for (int k = 0; k < playerTokens.length - 1; k++) {
                    if (playerTokens[k] != uniqueArray[k]) {
                        allPilesIdentical = false;
                        break;
                    }
                }

                if (allPilesIdentical) {
                    layoutIsUnique = false;
                    break;
                }
            }

            if(layoutIsUnique) {
                uniquePlayerTokensArrays.add(resultingPlayerTokens);
            }
            else {
                // Remove element from moveList and prepare loop to check the next element
                movesList.remove(movesList.get(i));
                i --;
            }
        }
        return movesList;
    }
}
