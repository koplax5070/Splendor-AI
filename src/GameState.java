import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// Representation of a Splendor game state, including all information regarding a given position in the game
// Currently tuned for the 2-player version of the game, but might be adapted in the future
public class GameState {
    private int playerToMove, playerStarted;
    private boolean lastTurn, gameOver;
    private int player1Score, player2Score;

    // Tokens in the supply, as well as in player1 and player2's possession, will be represented as arrays
    // The order will be: black, blue, green, red, white, gold
    private int[] supplyTokens = {4, 4, 4, 4, 4, 5};
    private int[] player1Tokens = {0, 0, 0, 0, 0, 0};
    private int[] player2Tokens = {0, 0, 0, 0, 0, 0};

    // Number of development cards of each type in each player's possession
    // Order: black, blue, green, red, white
    private int[] player1Cards = {0, 0, 0, 0, 0};
    private int[] player2Cards = {0, 0, 0, 0, 0};

    private ArrayList<NobleTile> noblesMarket;
    private ArrayList<DevelopmentCard> tier1Market, tier2Market, tier3Market;
    private ArrayList<DevelopmentCard> player1Reserve, player2Reserve;
    private Tier1Deck tier1Deck;
    private Tier2Deck tier2Deck;
    private Tier3Deck tier3Deck;

    // Sets up a 2-player game of splendor, placing 4 cards of each tier in the marketplace,
    // 4 tokens each of black, blue, green, red, white, and 5 gold tokens in the supply,
    // as well as 3 noble tiles available for players to attract. Randomly selects the starting player
    public GameState() {
        Random random = new Random();
        // Select player to go first
        playerToMove = random.nextInt(2) + 1;
        playerStarted = playerToMove;
        lastTurn = false;
        gameOver = false;

        // Initialize scores
        player1Score = 0;
        player2Score = 0;

        // Initialize reserves
        player1Reserve = new ArrayList<>();
        player2Reserve = new ArrayList<>();

        // Lay out noble tiles in play
        SplendorNobles allNobles = new SplendorNobles();
        noblesMarket = new ArrayList<>();
        noblesMarket.addAll(Arrays.asList(allNobles.extract(3)).subList(0, 3));

        // Prepare development card decks
        tier1Deck = new Tier1Deck();
        tier2Deck = new Tier2Deck();
        tier3Deck = new Tier3Deck();

        // Draw cards to populate the market
        tier1Market = new ArrayList<>();
        tier2Market = new ArrayList<>();
        tier3Market = new ArrayList<>();
        for(int i = 0; i < 4; i ++) {
            tier1Market.add(tier1Deck.drawCard());
            tier2Market.add(tier2Deck.drawCard());
            tier3Market.add(tier3Deck.drawCard());
        }
    }

    // Sets up a specific game state for a 2 player game of splendor, given all the needed information
    public GameState(GameState state) {

        // Set player to go first
        playerToMove = state.getPlayerToMove();
        playerStarted = state.getPlayerStarted();
        lastTurn = state.isLastTurn();
        gameOver = state.isGameOver();

        // Set scores
        player1Score = state.getPlayer1Score();
        player2Score = state.getPlayer2Score();

        // Set tokens

        for(int i = 0; i < 6; i ++) {
            supplyTokens[i] = state.getSupplyTokens()[i];
            player1Tokens[i] = state.getPlayer1Tokens()[i];
            player2Tokens[i] = state.getPlayer2Tokens()[i];
        }

        // Set cards

        for(int i = 0; i < 5; i ++) {
            player1Cards[i] = state.getPlayer1Cards()[i];
            player2Cards[i] = state.getPlayer2Cards()[i];
        }

        // Initialize reserves
        player1Reserve = new ArrayList<>();
        player2Reserve = new ArrayList<>();

        // Populate reserves
        for(int i = 0; i < state.getPlayer1Reserve().size(); i ++)
            player1Reserve.add(new DevelopmentCard(state.getPlayer1Reserve().get(i)));
        for(int i = 0; i < state.getPlayer2Reserve().size(); i ++)
            player2Reserve.add(new DevelopmentCard(state.getPlayer2Reserve().get(i)));

        // Initialize nobles market
        noblesMarket = new ArrayList<>();

        // Populate nobles market
        for(int i = 0; i < state.getNoblesMarket().size(); i ++)
            noblesMarket.add(new NobleTile(state.getNoblesMarket().get(i)));

        // Populate decks
        tier1Deck = new Tier1Deck(state.getTier1Deck());
        tier2Deck = new Tier2Deck(state.getTier2Deck());
        tier3Deck = new Tier3Deck(state.getTier3Deck());

        // Initialize card market
        tier1Market = new ArrayList<>();
        tier2Market = new ArrayList<>();
        tier3Market = new ArrayList<>();

        // Populate card market
        for(int i = 0; i < state.getTier1Market().size(); i ++) {
            tier1Market.add(new DevelopmentCard(state.getTier1Market().get(i)));
        }

        for(int i = 0; i < state.getTier2Market().size(); i ++) {
            tier2Market.add(new DevelopmentCard(state.getTier2Market().get(i)));
        }

        for(int i = 0; i < state.getTier3Market().size(); i ++) {
            tier3Market.add(new DevelopmentCard(state.getTier3Market().get(i)));
        }

    }

    @Override
    public String toString() {
        int colWidth = 40;

        String player1ReserveString = player1Reserve == null ? "" : player1Reserve.toString();
        String player2ReserveString = player2Reserve == null ? "" : player2Reserve.toString();

        return "-".repeat((int)(colWidth * 1.8)) + " State Representation " + "-".repeat((int)(colWidth * 1.8)) + "\n" +
                fillToKCharacters(" +++ Player 1 Information +++", colWidth) +
                fillToKCharacters(" +++ Player 2 Information +++",colWidth) +"\n" +
                fillToKCharacters("1 - Tokens: " + Arrays.toString(player1Tokens), colWidth) +
                fillToKCharacters("2 - Tokens: " + Arrays.toString(player2Tokens), colWidth) + "\n" +
                fillToKCharacters("1 - Cards: " + Arrays.toString(player1Cards), colWidth) +
                fillToKCharacters("2 - Cards: " + Arrays.toString(player2Cards), colWidth) + "\n" +
                fillToKCharacters("1 - Reserve: " + player1ReserveString, colWidth) +
                fillToKCharacters("2 - Reserve: " + player2ReserveString, colWidth) + "\n" +
                fillToKCharacters("1 - Score: " + player1Score, colWidth) +
                fillToKCharacters("2 - Score: " + player2Score, colWidth) + "\n" +
                getMarketPlaceString(colWidth);
    }

    // ----- Public Methods -----

    public GameState handleMove(Move move) {
        return switch(move.getType()) {
            case buyCard -> handleBuyCardMove((BuyCardMove) move);
            case reserveCard -> handleReserveCardMove((ReserveCardMove) move);
            case takeTokens -> handleTakeTokensMove((TakeTokensMove) move);
        };
    }

    // +++ Move Validation +++

    public boolean buyCardMoveIsValid(BuyCardMove move) {
        int player = move.getPlayer();

        // Check game is not over
        if(gameOver)
            return false;

        // If move specifies a noble to attract, check noble can be attracted
        if(move.getChosenNobleIndex() != -1) {
            boolean nobleIsAttracted = false;
            int[] attractedNobles = getAttractedNobles(player, move);
            for(int i = 0; i < 5; i ++)
                if(move.getChosenNobleIndex() == attractedNobles[i]) {
                    nobleIsAttracted = true;
                    break;
                }
            if(!nobleIsAttracted)
                return false;
        }

        // If move does not specify a noble to attract, check whether one could be attracted
        if(move.getChosenNobleIndex() == -1) {
            if(getAttractedNobles(player, move)[0] != -1)
                return false;
        }

        return switch (move.getCardSource()) {
            case 0 -> switch (player) {
                case 1 -> playerAffordsCard(player, player1Reserve.get(move.getCardIndex()));
                case 2 -> playerAffordsCard(player, player2Reserve.get(move.getCardIndex()));
                default -> false;
            };
            case 1 -> playerAffordsCard(player, tier1Market.get(move.getCardIndex()));
            case 2 -> playerAffordsCard(player, tier2Market.get(move.getCardIndex()));
            case 3 -> playerAffordsCard(player, tier3Market.get(move.getCardIndex()));
            default -> false;
        };
    }

    public boolean reserveCardMoveIsValid(ReserveCardMove move) {
        int player = move.getPlayer();

        // Check game is not over
        if(gameOver)
            return false;

        // Check player has room to reserve a card
        if(player == 1) {
            if(player1Reserve.size() >= 3)
                return false;
        }
        if(player == 2) {
            if(player2Reserve.size() >= 3)
                return false;
        }

        // Check player returns no tokens if they have 10 or less after taking a gold (or cannot take a gold)
        int totalPlayerTokens = 0;
        int totalReturnedTokens = 0;

        for(int i = 0; i < move.getTokensToReturn().length; i ++)
            totalReturnedTokens += move.getTokensToReturn()[i];

        if(totalReturnedTokens > 1)
            return false;

        if(player == 1) {
            for (int number : player1Tokens) totalPlayerTokens += number;
        }
        if(player == 2)
            for (int number : player2Tokens) totalPlayerTokens += number;

        if(totalPlayerTokens >= 10 && supplyTokens[5] > 0 && totalReturnedTokens == 0)
            return false;

        // Check card to reserve exists
        return switch(move.getCardTier()) {
            case 1 -> switch(move.getCardIndex()) {
                case 0 -> tier1Market.size() >= 1;
                case 1 -> tier1Market.size() >= 2;
                case 2 -> tier1Market.size() >= 3;
                case 3 -> tier1Market.size() >= 4;
                case 4 -> !tier1Deck.isEmpty();
                default -> false;
            };
            case 2 -> switch (move.getCardIndex()) {
                case 0 -> tier2Market.size() >= 1;
                case 1 -> tier2Market.size() >= 2;
                case 2 -> tier2Market.size() >= 3;
                case 3 -> tier2Market.size() >= 4;
                case 4 -> !tier2Deck.isEmpty();
                default -> false;
            };
            case 3 -> switch (move.getCardIndex()) {
                case 0 -> tier3Market.size() >= 1;
                case 1 -> tier3Market.size() >= 2;
                case 2 -> tier3Market.size() >= 3;
                case 3 -> tier3Market.size() >= 4;
                case 4 -> !tier3Deck.isEmpty();
                default -> false;
            };
            default -> false;
        };
    }

    public boolean takeTokensMoveIsValid(TakeTokensMove move) {
        int player = move.getPlayer();

        // Check game is not over
        if(gameOver)
            return false;

        // Check tokens can be taken
        if(!canTakeTokens(move.getTokensToTake()))
            return false;

        if(!canReturnTokens(player, move.getTokensToTake(), move.getTokensToReturn()))
            return false;

        // Check player will have 10 tokens or less after the move
        int numberPlayerTokens = 0, numberTokensToTake, numberTokensToReturn;
        if(player == 1)
            numberPlayerTokens = Arrays.stream(player1Tokens).sum();
        if(player == 2)
            numberPlayerTokens = Arrays.stream(player2Tokens).sum();
        numberTokensToTake = Arrays.stream(move.getTokensToTake()).sum();
        numberTokensToReturn = Arrays.stream(move.getTokensToReturn()).sum();
        if(numberPlayerTokens + numberTokensToTake > 10 && numberTokensToReturn !=
                numberPlayerTokens + numberTokensToTake - 10)
            return false;

        return true;
    }

    public GameState handleBuyCardMove(BuyCardMove move) {
        int player = move.getPlayer();

        // Check move is legal
        if(!buyCardMoveIsValid(move))
            return null;

        // Handle payment and card acquisition
        switch(move.getCardSource()) {
            case 0:
                switch (player) {
                    case 1 -> {
                        payForCard(player, player1Reserve.get(move.getCardIndex()));
                        addCardToPlayer(player, player1Reserve.get(move.getCardIndex()));
                        removeCardFromReserve(player, move.getCardIndex());
                    }
                    case 2 -> {
                        payForCard(player, player2Reserve.get(move.getCardIndex()));
                        addCardToPlayer(player, player2Reserve.get(move.getCardIndex()));
                        removeCardFromReserve(player, move.getCardIndex());
                    }
                }
                break;
            case 1:
                payForCard(player, tier1Market.get(move.getCardIndex()));
                addCardToPlayer(player, tier1Market.get(move.getCardIndex()));
                replaceCardFromMarketplace(move.getCardIndex(), 1);
                break;
            case 2:
                payForCard(player, tier2Market.get(move.getCardIndex()));
                addCardToPlayer(player, tier2Market.get(move.getCardIndex()));
                replaceCardFromMarketplace(move.getCardIndex(), 2);
                break;
            case 3:
                payForCard(player, tier3Market.get(move.getCardIndex()));
                addCardToPlayer(player, tier3Market.get(move.getCardIndex()));
                replaceCardFromMarketplace(move.getCardIndex(), 3);
                break;
        }

        // Award player the chosen noble if applicable
        if(move.getChosenNobleIndex() != -1)
            awardNobleTile(player, move.getChosenNobleIndex());

        // Check game over conditions
        checkGameIsEnding();
        if(lastTurn && player != playerStarted) {
            lastTurn = false;
            gameOver = true;
        }

        // Update playerToMove
        if(playerToMove == 1)
            playerToMove = 2;
        else playerToMove = 1;

        return this;
    }

    public GameState handleReserveCardMove(ReserveCardMove move) {
        int player = move.getPlayer();

        if(!reserveCardMoveIsValid(move))
            return null;

        // Give player gold token and return tokens if necessary
        int [] tokensToTransfer = {0, 0, 0, 0, 0, 1};
        transferTokensToPlayer(player, tokensToTransfer);
        transferTokensToSupply(player, move.getTokensToReturn());

        switch(move.getCardTier()) {
            case 1:
                if(move.getCardIndex() == 4)
                    addCardToReserve(player, tier1Deck.drawCard());
                else {
                    addCardToReserve(player, tier1Market.get(move.getCardIndex()));
                    replaceCardFromMarketplace(move.getCardIndex(), move.getCardTier());
                }
                break;
            case 2:
                if(move.getCardIndex() == 4)
                    addCardToReserve(player, tier2Deck.drawCard());
                else {
                    addCardToReserve(player, tier2Market.get(move.getCardIndex()));
                    replaceCardFromMarketplace(move.getCardIndex(), move.getCardTier());
                }
                break;
            case 3:
                if(move.getCardIndex() == 4)
                    addCardToReserve(player, tier3Deck.drawCard());
                else {
                    addCardToReserve(player, tier3Market.get(move.getCardIndex()));
                    replaceCardFromMarketplace(move.getCardIndex(), move.getCardTier());
                }
                break;
            default:
                return null;
        }

        // Check game over conditions
        if(lastTurn && player != playerStarted) {
            lastTurn = false;
            gameOver = true;
        }

        // Update playerToMove
        if(playerToMove == 1)
            playerToMove = 2;
        else playerToMove = 1;

        return this;
    }

    public GameState handleTakeTokensMove(TakeTokensMove move) {
        int player = move.getPlayer();

        // Check move is valid
        if(!takeTokensMoveIsValid(move))
            return null;

        // Carry out transfer, and any return necessary
        transferTokensToPlayer(player, move.getTokensToTake());
        transferTokensToSupply(player, move.getTokensToReturn());

        // Check game over conditions
        if(lastTurn && player != playerStarted) {
            lastTurn = false;
            gameOver = true;
        }

        // Update playerToMove
        if(playerToMove == 1)
            playerToMove = 2;
        else playerToMove = 1;

        return this;
    }

    // ----- Private methods -----

    // +++ Game State Manipulation +++

    // Adds a card to a player, adding its point value to the player's card
    // Does not handle noble attraction, win condition checking or paying for the card
    private boolean addCardToPlayer(int player, DevelopmentCard card) {

        DevelopmentCard.Color cardColor = card.getBonusColor();
        if(player == 1) {
            player1Cards[cardColor.getIndex()] ++;
            player1Score += card.getPointValue();
        }
        if(player == 2) {
            player2Cards[cardColor.getIndex()] ++;
            player2Score += card.getPointValue();
        }
        return true;
    }

    // Awards a noble tile to a player, adding its point value to the player's score and removing the tile from the
    // marketplace
    // Checks whether the player can attract the noble or not
    private boolean awardNobleTile(int player, int nobleTileIndex) {
        // Check that the player can attract the noble
        if(player == 1)
            for(int i = 0; i < player1Cards.length; i ++)
                if(player1Cards[i] < noblesMarket.get(nobleTileIndex).getRequiredArray()[i])
                    return false;
        if(player == 2)
            for(int i = 0; i < player2Cards.length; i ++)
                if(player2Cards[i] < noblesMarket.get(nobleTileIndex).getRequiredArray()[i])
                    return false;

        // Add the noble's point value to the player's score
        if(player == 1)
            player1Score += noblesMarket.get(nobleTileIndex).getPointValue();
        if(player == 2)
            player2Score += noblesMarket.get(nobleTileIndex).getPointValue();

        // Remove the noble from the marketplace
        noblesMarket.remove(nobleTileIndex);
        return true;
    }

    // Adds a card to a player's reserve
    // Checks whether the player has enough room in their reserve
    // Does not handle acquisition of a gold token
    private boolean addCardToReserve(int player, DevelopmentCard card) {
        if(player == 1) {
            if(player1Reserve.size() >= 3)
                return false;
            else player1Reserve.add(card);
        }
        if(player == 2) {
            if(player2Reserve.size() >= 3)
                return false;
            else player2Reserve.add(card);
        }
        return true;
    }

    // Removes a card from a player's reserve
    // Does not perform any checks
    // Does not handle paying for the card
    private boolean removeCardFromReserve(int player, int cardIndex) {
        if(player == 1)
            player1Reserve.remove(cardIndex);
        if(player == 2)
            player2Reserve.remove(cardIndex);
        return false;
    }

    // Adds tokens to a player from the supply
    // Checks that the tokensToTransfer array conforms with the game rules
    // Checks that the supply has enough tokens for the transfer
    // Checking for move validity isn't performed at this stage, as the function might be followed by a call to
    // transferTokensToSupply()
    private boolean transferTokensToPlayer(int player, int[] tokensToTransfer) {

        // Carry out transfer if it is legal
        if(canTakeTokens(tokensToTransfer)) {
            if(player == 1) {
                for(int i = 0; i < tokensToTransfer.length; i ++) {
                    player1Tokens[i] += tokensToTransfer[i];
                    supplyTokens[i] -= tokensToTransfer[i];
                }
            }
            if(player == 2) {
                for(int i = 0; i < tokensToTransfer.length; i ++) {
                    player2Tokens[i] += tokensToTransfer[i];
                    supplyTokens[i] -= tokensToTransfer[i];
                }
            }
        }
        else return false;
        return true;
    }

    // Checks that the player has 10 tokens or less after transfer
    // Checks that the player has enough tokens for the transfer
    private boolean transferTokensToSupply(int player, int[] tokensToTransfer) {
        int totalPlayerTokens = 0;
        int numberTokensToTransfer = 0;

        for(int i : tokensToTransfer)
            numberTokensToTransfer += i;

        if(player == 1) {
            // Check if the player's number of tokens is legal after transfer
            for(int i : player1Tokens)
                totalPlayerTokens += i;
            if(totalPlayerTokens - numberTokensToTransfer > 10)
                return false;

            // Check player has enough tokens for transfer
            for(int i  = 0; i < tokensToTransfer.length; i ++)
                if(player1Tokens[i] - tokensToTransfer[i] < 0)
                    return false;

            // Carry out transfer
            for(int i = 0; i < tokensToTransfer.length; i ++) {
                player1Tokens[i] -= tokensToTransfer[i];
                supplyTokens[i] += tokensToTransfer[i];
            }
        }
        if(player == 2) {
            // Check if the player's number of tokens is legal after transfer
            for(int i : player2Tokens)
                totalPlayerTokens += i;
            if(totalPlayerTokens - numberTokensToTransfer > 10)
                return false;

            // Check player has enough tokens for transfer
            for(int i  = 0; i < tokensToTransfer.length; i ++)
                if(player2Tokens[i] - tokensToTransfer[i] < 0)
                    return false;

            // Carry out transfer
            for(int i = 0; i < tokensToTransfer.length; i ++) {
                player2Tokens[i] -= tokensToTransfer[i];
                supplyTokens[i] += tokensToTransfer[i];
            }
        }
        return true;
    }

    // Return true if the player can afford the card, or false otherwise
    public boolean playerAffordsCard(int player, DevelopmentCard card) {
        // Gold tokens that would be needed for the player to afford the card
        int goldTokensNeeded = 0;

        if(player == 1) {
            for(int i = 0; i < card.getCostArray().length; i ++) {
                goldTokensNeeded += Math.max(card.getCostArray()[i] - player1Tokens[i] - player1Cards[i], 0);
            }
            return goldTokensNeeded <= player1Tokens[5];
        }
        if(player == 2) {
            for(int i = 0; i < card.getCostArray().length; i ++) {
                goldTokensNeeded += Math.max(card.getCostArray()[i] - player2Tokens[i] - player2Cards[i], 0);
            }
            return goldTokensNeeded <= player2Tokens[5];
        }
        return false;
    }

    private boolean replaceCardFromMarketplace(int cardIndex, int cardTier) {
        switch(cardTier) {
            case 1:
                if(tier1Deck.isEmpty())
                    tier1Market.remove(cardIndex);
                else
                    tier1Market.set(cardIndex, tier1Deck.drawCard());
                return true;
            case 2:
                if(tier2Deck.isEmpty())
                    tier2Market.remove(cardIndex);
                else
                    tier2Market.set(cardIndex, tier2Deck.drawCard());
                return true;
            case 3:
                if(tier3Deck.isEmpty())
                    tier3Market.remove(cardIndex);
                else
                    tier3Market.set(cardIndex, tier3Deck.drawCard());
                return true;
            default:
                return false;
        }
    }

    // +++ Utility methods +++

    private boolean checkGameIsEnding() {
        if(player1Score >= 15 || player2Score >= 15)
            lastTurn = true;
        return player1Score >= 15 || player2Score >= 15;
    }

    private boolean canTakeTokens(int[] tokensToTake) {
        // Check that supply has enough tokens for the transfer
        for(int i = 0; i < tokensToTake.length; i ++)
            if(supplyTokens[i] - tokensToTake[i] < 0)
                return false;

        // Check that tokensToTransfer corresponds to a legal action in Splendor

        // Check for one gold token being taken
        boolean legalTakeGold = true;
        for(int i = 0; i < tokensToTake.length - 1; i ++)
            if(tokensToTake[i] != 0) {
                legalTakeGold = false;
                break;
            }
        if(!(legalTakeGold && tokensToTake[tokensToTake.length - 1] == 1) && supplyTokens[5] > 0)
            legalTakeGold = false;

        // Check for two tokens taken from the same pile
        boolean encounteredTwo = false;
        boolean legalTakeTwo = true;
        for(int i = 0; i < tokensToTake.length; i ++) {
            if(encounteredTwo) {
                if(tokensToTake[i] != 0) {
                    legalTakeTwo = false;
                    break;
                }
            }
            else {
                if(tokensToTake[i] == 2) {
                    encounteredTwo = true;
                    if(supplyTokens[i] < 4) {
                        legalTakeTwo = false;
                        break;
                    }
                }
                else if(tokensToTake[i] != 0) {
                    legalTakeTwo = false;
                    break;
                }
            }
        }

        // Check for up to three tokens each from different piles
        // Taking less than 3 tokens is only allowed if there are no options to take 3 tokens
        // Furthermore, taking less than 2 tokens is only allowed if there are no options to take 2 tokens
        boolean legalTakeDifferent = true;
        int availablePiles = 0, correctTokensToTake, totalTokensToTake = 0;
        for (int i = 0; i < 5; i ++)
            if (supplyTokens[i] > 0)
                availablePiles++;

        correctTokensToTake = Math.min(availablePiles, 3);
        for(int i : tokensToTake)
            totalTokensToTake += i;
        if(totalTokensToTake != correctTokensToTake)
            legalTakeDifferent = false;
        else {
            for(int i = 0; i < tokensToTake.length; i ++)
                if(tokensToTake[i] > 1 || supplyTokens[i] - tokensToTake[i] < 0) {
                    legalTakeDifferent = false;
                    break;
                }
        }

        return legalTakeDifferent || legalTakeTwo || legalTakeGold;
    }

    private boolean canReturnTokens(int player, int[] tokensToTake, int[] tokensToReturn) {
        if(player == 1) {
            for(int i = 0; i < tokensToReturn.length; i ++)
                if(player1Tokens[i] + tokensToTake[i] < tokensToReturn[i])
                    return false;
        }
        if(player == 2)
            for(int i = 0; i < tokensToReturn.length; i ++)
                if(player2Tokens[i] + tokensToTake[i] < tokensToReturn[i])
                    return false;
        return true;
    }

    // Returns an array of indices of the nobles attracted by a player after a given BuyCardMove
    private int[] getAttractedNobles(int player, BuyCardMove move) {
        int[] attractedNobles = {-1, -1, -1, -1, -1};
        int[] playerCardsAfterMove = {0, 0, 0, 0, 0};

        // Initialize with player's current cards
        if(player == 1)
            System.arraycopy(player1Cards, 0, playerCardsAfterMove, 0, 5);
        if(player == 2)
            System.arraycopy(player2Cards, 0, playerCardsAfterMove, 0, 5);

        // Simulate the purchase of the new card
        switch(move.getCardSource()) {
            case 0:
                if(player == 1)
                    playerCardsAfterMove[player1Reserve.get(move.getCardIndex()).getBonusColor().getIndex()] ++;
                if(player == 2)
                    playerCardsAfterMove[player2Reserve.get(move.getCardIndex()).getBonusColor().getIndex()] ++;
                break;
            case 1:
                playerCardsAfterMove[tier1Market.get(move.getCardIndex()).getBonusColor().getIndex()] ++;
                break;
            case 2:
                playerCardsAfterMove[tier2Market.get(move.getCardIndex()).getBonusColor().getIndex()]++;
                break;
            case 3:
                playerCardsAfterMove[tier3Market.get(move.getCardIndex()).getBonusColor().getIndex()]++;
                break;
            default: return attractedNobles;
        }

        // Compute attracted nobles
        int numberAttractedNobles = 0;

        for(int i = 0; i < noblesMarket.size(); i ++) {
            boolean nobleAttracted = true;
            for(int j = 0; j < noblesMarket.get(i).getRequiredArray().length; j ++) {
                if (playerCardsAfterMove[j] < noblesMarket.get(i).getRequiredArray()[j]) {
                    nobleAttracted = false;
                    break;
                }
            }

            if(nobleAttracted) {
                attractedNobles[numberAttractedNobles] = i;
                numberAttractedNobles ++;
            }
        }
        return attractedNobles;
    }

    // Determines the best way for a player to pay for a card, and transfers the correct tokens to the supply
    // Note: this assumes that the player would want to prioritize saving their gold tokens if they can;
    // Arguably, a strategy based on depriving the opponent of a token type might want to prioritize
    // paying for a card using gold, and saving said token type. However, this situation would be rare, and
    // the benefits of using such a strategy have been decided to be slight, if any
    // Therefore, it has been decided that the simplicity of this implementation outweighs the flexibility of
    // an implementation where the player manually selects which tokens they pay with
    private void payForCard(int player, DevelopmentCard card) {
        // Gold tokens needed to pay for the card
        int goldTokensNeeded = 0;
        int[] tokensNeeded = {0, 0, 0, 0, 0, 0};

        if(player == 1) {
            for(int i = 0; i < card.getCostArray().length; i ++) {
                goldTokensNeeded += Math.max(card.getCostArray()[i] - player1Tokens[i] - player1Cards[i], 0);
                if(card.getCostArray()[i] <= player1Cards[i])
                    tokensNeeded[i] = 0;
                else tokensNeeded[i] = Math.min(card.getCostArray()[i] - player1Cards[i], player1Tokens[i]);
            }
            tokensNeeded[5] = goldTokensNeeded;
            transferTokensToSupply(1, tokensNeeded);
        }
        if(player == 2) {
            for(int i = 0; i < card.getCostArray().length; i ++) {
                goldTokensNeeded += Math.max(card.getCostArray()[i] - player2Tokens[i] - player2Cards[i], 0);
                if(card.getCostArray()[i] <= player2Cards[i])
                    tokensNeeded[i] = 0;
                else tokensNeeded[i] = Math.min(card.getCostArray()[i] - player2Cards[i], player2Tokens[i]);
            }
            tokensNeeded[5] = goldTokensNeeded;
            transferTokensToSupply(2, tokensNeeded);
        }
    }

    // String methods

    private String getMarketPlaceString(int columnWidth) {
        // Nobles
        StringBuilder noblesString = new StringBuilder("-".repeat(columnWidth * 2) +
                " Nobles " + "-".repeat(columnWidth * 2) + "\n");
        StringBuilder requirementString = new StringBuilder();
        for (NobleTile tile : noblesMarket) {
            noblesString.append(fillToKCharacters(tile.getName(), columnWidth));
        }
        noblesString.append("\n");

        for (NobleTile tile : noblesMarket) {
            requirementString.delete(0, requirementString.length());
            for (int j = 0; j < tile.getRequiredArray().length; j++)
                if (tile.getRequiredArray()[j] != 0) {
                    requirementString.append(tile.getRequiredArray()[j]);
                    requirementString.append(switch (j) {
                        case 0 -> "k ";
                        case 1 -> "u ";
                        case 2 -> "g ";
                        case 3 -> "r ";
                        case 4 -> "w ";
                        default -> "";
                    });
                }
            noblesString.append(fillToKCharacters(String.valueOf(requirementString), columnWidth));
        }
        noblesString.append("\n");

        // Supply
        String supplyString = "-".repeat(columnWidth * 2) + " Supply " + "-".repeat(columnWidth * 2) + "\n" +
                supplyTokens[0] + "k " + supplyTokens[1] + "u " + supplyTokens[2] + "g "
                + supplyTokens[3] + "r " + supplyTokens[4] + "w " + supplyTokens[5] + "G\n";

        // Tier 3
        String tier3String = "-".repeat(columnWidth * 2) + " Tier 3 " + "-".repeat(columnWidth * 2) + "\n" +
                getTierMarketString(tier3Market, columnWidth);

        // Tier 2
        String tier2String = "-".repeat(columnWidth * 2) + " Tier 2 " + "-".repeat(columnWidth * 2) + "\n" +
                getTierMarketString(tier2Market, columnWidth);

        // Tier 1
        String tier1String = "-".repeat(columnWidth * 2) + " Tier 1 " + "-".repeat(columnWidth * 2) + "\n" +
                getTierMarketString(tier1Market, columnWidth);

        return noblesString + supplyString + tier3String + tier2String + tier1String;
    }

    private String getTierMarketString(ArrayList<DevelopmentCard> tierMarket, int columnWidth) {
        StringBuilder tierString = new StringBuilder();
        for(int i = 0; i < tierMarket.size(); i ++) {
            tierString.append(fillToKCharacters(Integer.toString(i), columnWidth));
        }
        tierString.append("\n");

        for (DevelopmentCard card : tierMarket) {
            tierString.append(fillToKCharacters("Color: " + switch (card.getBonusColor()) {
                case BLACK -> "Black";
                case BLUE -> "Blue";
                case GREEN -> "Green";
                case RED -> "Red";
                case WHITE -> "White";
            }, columnWidth));
        }
        tierString.append("\n");

        StringBuilder costString = new StringBuilder();
        for(DevelopmentCard card : tierMarket) {
            costString.delete(0, costString.length());
            for(int i = 0; i < card.getCostArray().length; i ++)
                if(card.getCostArray()[i] > 0)
                    costString.append(card.getCostArray()[i]).append(switch (i) {
                        case 0 -> "k ";
                        case 1 -> "u ";
                        case 2 -> "g ";
                        case 3 -> "r ";
                        case 4 -> "w ";
                        default -> "";
                    });
            tierString.append(fillToKCharacters("Cost: " + costString, columnWidth));
        }
        tierString.append("\n");

        for(DevelopmentCard card : tierMarket) {
            tierString.append(fillToKCharacters("Value: " + card.getPointValue() + " points", columnWidth));
        }
        tierString.append("\n");

        return String.valueOf(tierString);
    }

    // Returns string s followed by s.length() - k spaces, so as the returned string has length k
    private String fillToKCharacters(String s, int k) {
        return s + " ".repeat(Math.max(0, k - s.length()));
    }

    // DEBUG METHOD
    private void printMessage(String message) {
        System.out.println(message);
    }

    // Getters
    public int getWinner() {
        if(!gameOver)
            return -1;
        if(player1Score > player2Score)
            return 1;
        if(player2Score > player1Score)
            return 2;
        // Draw
        return 0;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isLastTurn() {
        return lastTurn;
    }

    public int getPlayerStarted() {
        return playerStarted;
    }

    public int getPlayerToMove() {
        return playerToMove;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public int[] getPlayer1Cards() {
        return player1Cards;
    }

    public int[] getPlayer2Cards() {
        return player2Cards;
    }

    public int[] getPlayer1Tokens() {
        return player1Tokens;
    }

    public int[] getPlayer2Tokens() {
        return player2Tokens;
    }

    public int[] getSupplyTokens() {
        return supplyTokens;
    }

    public Tier1Deck getTier1Deck() {
        return tier1Deck;
    }

    public Tier2Deck getTier2Deck() {
        return tier2Deck;
    }

    public Tier3Deck getTier3Deck() {
        return tier3Deck;
    }

    public ArrayList<DevelopmentCard> getPlayer1Reserve() {
        return player1Reserve;
    }

    public ArrayList<DevelopmentCard> getPlayer2Reserve() {
        return player2Reserve;
    }

    public ArrayList<DevelopmentCard> getTier1Market() {
        return tier1Market;
    }

    public ArrayList<DevelopmentCard> getTier2Market() {
        return tier2Market;
    }

    public ArrayList<DevelopmentCard> getTier3Market() {
        return tier3Market;
    }

    public ArrayList<NobleTile> getNoblesMarket() {
        return noblesMarket;
    }

    // Setters (only for use in copied instances of a state)
    public void setTier1Market(ArrayList<DevelopmentCard> cards) {
        if (tier1Market.size() > 0) {
            tier1Market.subList(0, tier1Market.size()).clear();
        }
        for (DevelopmentCard card : cards) tier1Market.add(new DevelopmentCard(card));
    }

    public void setTier2Market(ArrayList<DevelopmentCard> cards) {
        if (tier2Market.size() > 0) {
            tier2Market.subList(0, tier2Market.size()).clear();
        }
        for (DevelopmentCard card : cards) tier2Market.add(new DevelopmentCard(card));
    }

    public void setTier3Market(ArrayList<DevelopmentCard> cards) {
        if (tier3Market.size() > 0) {
            tier3Market.subList(0, tier3Market.size()).clear();
        }
        for (DevelopmentCard card : cards) tier3Market.add(new DevelopmentCard(card));
    }
}
