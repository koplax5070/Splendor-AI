import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

// Implements a command line interface, handling display of the current state, and parsing of moves, which are
// passed to the game engine
public class CommandLineInterface {
    enum GameMode {PVP, PVC, CVC, NOT_SELECTED}

    static GameState state;
    static GameMode mode;

    public static void main(String[] args) throws IOException {

        mode = GameMode.NOT_SELECTED;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(mode == GameMode.NOT_SELECTED) {
            System.out.println("""
                Welcome to the Splendor CLI! How would you like to play? (Enter a digit 1-3)
                1. Human vs Human
                2. Human vs Computer
                3. Computer vs Computer
                """);
            int option = Integer.parseInt(reader.readLine());

            if(option < 1 || option > 3)
                System.out.println("Error: please enter a single digit between 1 and 3");
            else
                mode = switch(option) {
                    case 1 -> GameMode.PVP;
                    case 2 -> GameMode.PVC;
                    case 3 -> GameMode.CVC;
                    default -> GameMode.NOT_SELECTED;
                };
        }

        state = new GameState();

        if(mode == GameMode.PVP) {
            while(!state.isGameOver()) {

                System.out.println(state.toString());
                Move move = null;
                while(move == null && !state.isGameOver()) {
                    System.out.println("Player " + state.getPlayerToMove() + " to move:");
                    String input = reader.readLine();
                    move = parseMove(input);
                    if(move != null)
                        if(state.handleMove(move) == null) {
                            displayIllegalMoveText();
                            move = null;
                        }
                }
                if(state.getWinner() > 0)
                    System.out.println("Game is over! Player " + state.getWinner() + " won!");
                else if(state.getWinner() == 0)
                    System.out.println("Game is over! Draw!");
            }
        }
        else if(mode == GameMode.PVC) {

            // Choosing agent to play against
            System.out.println("""
                    You are Player 1. Choose agent to play against:
                    1. Random Move Agent
                    2. 3-ply Minimax Agent
                    3. 5-ply Minimax Agent
                    4. 7-ply Minimax Agent""");
            int chosenAgentIndex = 0;
            while(chosenAgentIndex == 0) {
                String input = reader.readLine();
                chosenAgentIndex = Integer.parseInt(input);
                if(chosenAgentIndex > 4 || chosenAgentIndex < 1) {
                    System.out.println("Please enter a valid choice.");
                    chosenAgentIndex = 0;
                }
            }

            Agent agent = null;
            switch (chosenAgentIndex) {
                case 1 -> agent = new RandomMoveAgent();
                case 2 -> {
                    double hasWon = 20000.0;
                    double isWinning = 10000.0;
                    double currentPoints = 300.0;
                    double opponentPoints = -300.0;
                    double maxImmediatePoints = 200;
                    double buyFromMarketIndex = 0;
                    double buyFromReserveIndex = 10;
                    double attractNobleIndex = 10;
                    double numCardsToReserve = 10;
                    double totalTokens = 0;

                    ArrayList<Double> weights = new ArrayList<>();
                    weights.add(hasWon); // hasWon
                    weights.add(isWinning); // isWinning
                    weights.add(currentPoints); // currentPoints
                    weights.add(opponentPoints); // opponentPoints
                    weights.add(maxImmediatePoints); // maxImmediatePoints
                    weights.add(buyFromMarketIndex); // buyFromMarketIndex
                    weights.add(buyFromReserveIndex); // buyFromReserveIndex
                    weights.add(attractNobleIndex); // attractNobleIndex
                    weights.add(numCardsToReserve); // numCardsToReserve
                    weights.add(totalTokens); // totalTokens

                    agent = new MinimaxAgent(3, 0, 0, weights);
                }
                case 3 -> {
                    double hasWon = 20000.0;
                    double isWinning = 10000.0;
                    double currentPoints = 300.0;
                    double opponentPoints = -300.0;
                    double maxImmediatePoints = 200;
                    double buyFromMarketIndex = 0;
                    double buyFromReserveIndex = 10;
                    double attractNobleIndex = 10;
                    double numCardsToReserve = 10;
                    double totalTokens = 0;

                    ArrayList<Double> weights = new ArrayList<>();
                    weights.add(hasWon); // hasWon
                    weights.add(isWinning); // isWinning
                    weights.add(currentPoints); // currentPoints
                    weights.add(opponentPoints); // opponentPoints
                    weights.add(maxImmediatePoints); // maxImmediatePoints
                    weights.add(buyFromMarketIndex); // buyFromMarketIndex
                    weights.add(buyFromReserveIndex); // buyFromReserveIndex
                    weights.add(attractNobleIndex); // attractNobleIndex
                    weights.add(numCardsToReserve); // numCardsToReserve
                    weights.add(totalTokens); // totalTokens

                    agent = new MinimaxAgent(5, 0, 0, weights);
                }
                case 4 -> {
                    double hasWon = 20000.0;
                    double isWinning = 10000.0;
                    double currentPoints = 300.0;
                    double opponentPoints = -300.0;
                    double maxImmediatePoints = 200;
                    double buyFromMarketIndex = 0;
                    double buyFromReserveIndex = 10;
                    double attractNobleIndex = 10;
                    double numCardsToReserve = 10;
                    double totalTokens = 0;

                    ArrayList<Double> weights = new ArrayList<>();
                    weights.add(hasWon); // hasWon
                    weights.add(isWinning); // isWinning
                    weights.add(currentPoints); // currentPoints
                    weights.add(opponentPoints); // opponentPoints
                    weights.add(maxImmediatePoints); // maxImmediatePoints
                    weights.add(buyFromMarketIndex); // buyFromMarketIndex
                    weights.add(buyFromReserveIndex); // buyFromReserveIndex
                    weights.add(attractNobleIndex); // attractNobleIndex
                    weights.add(numCardsToReserve); // numCardsToReserve
                    weights.add(totalTokens); // totalTokens

                    agent = new MinimaxAgent(7, 0, 0, weights);
                }
                default -> {
                    System.out.println("Fatal error. Shutting down.");
                    System.exit(1);
                }
            }

            Move lastMove = null;
            while(!state.isGameOver()) {
                System.out.println(state.toString());
                if(lastMove != null)
                    System.out.println("Last action: " + lastMove);

                Move currentMove = null;
                while(currentMove == null && !state.isGameOver()) {

                    // Get move from player
                    if(state.getPlayerToMove() == 1) {
                        System.out.println("Player " + state.getPlayerToMove() + " to move:");
                        String input = reader.readLine();
                        currentMove = parseMove(input);
                    }
                    // Get move from agent
                    else if(state.getPlayerToMove() == 2) {
                        currentMove = agent.generateMove(state);
                    }

                    // Handle move
                    if(currentMove != null)
                        if(state.handleMove(currentMove) == null) {
                            displayIllegalMoveText();
                            currentMove = null;
                        }
                }
                lastMove = currentMove;

                // Check game over conditions
                if(state.getWinner() > 0) {
                    System.out.println(state.toString());
                    if(lastMove != null)
                        System.out.println("Last action: " + lastMove);

                    System.out.println("Game is over! Player " + state.getWinner() + " won!");
                }
                else if(state.getWinner() == 0) {
                    System.out.println(state.toString());
                    if(lastMove != null)
                        System.out.println("Last action: " + lastMove);
                    System.out.println("Game is over! Draw!");                }
            }

        }
        else if(mode == GameMode.CVC) {

            // Choosing agents to play against each other
            System.out.println("""
                    Choose agent for Player 1:
                    1. Random Move Agent
                    2. 3-ply Minimax Agent
                    3. 5-ply Minimax Agent
                    4. 7-ply Minimax Agent""");
            int chosenAgent1Index = 0;
            while(chosenAgent1Index == 0) {
                String input = reader.readLine();
                chosenAgent1Index = Integer.parseInt(input);
                if(chosenAgent1Index > 4 || chosenAgent1Index < 1) {
                    System.out.println("Please enter a valid choice.");
                    chosenAgent1Index = 0;
                }
            }

            System.out.println("""
                    Choose agent for Player 2:
                    1. Random Move Agent
                    2. 3-ply Minimax Agent
                    3. 5-ply Minimax Agent
                    4. 7-ply Minimax Agent""");
            int chosenAgent2Index = 0;
            while(chosenAgent2Index == 0) {
                String input = reader.readLine();
                chosenAgent2Index = Integer.parseInt(input);
                if(chosenAgent2Index > 4 || chosenAgent2Index < 1) {
                    System.out.println("Please enter a valid choice.");
                    chosenAgent2Index = 0;
                }
            }

            System.out.println("Input number of games to play:");
            int numberOfGames = 0;
            while(numberOfGames <= 0) {
                String input = reader.readLine();
                numberOfGames = Integer.parseInt(input);
                if(numberOfGames <= 0)
                    System.out.println("Please enter a valid number.");
            }

            Agent agent1 = null, agent2 = null;

            switch(chosenAgent1Index) {
                case 1 -> agent1 = new RandomMoveAgent();
                case 2 -> {
                    double hasWon = 20000.0;
                    double isWinning = 10000.0;
                    double currentPoints = 300.0;
                    double opponentPoints = -300.0;
                    double maxImmediatePoints = 200;
                    double buyFromMarketIndex = 0;
                    double buyFromReserveIndex = 10;
                    double attractNobleIndex = 10;
                    double numCardsToReserve = 10;
                    double totalTokens = 0;

                    ArrayList<Double> weights = new ArrayList<>();
                    weights.add(hasWon); // hasWon
                    weights.add(isWinning); // isWinning
                    weights.add(currentPoints); // currentPoints
                    weights.add(opponentPoints); // opponentPoints
                    weights.add(maxImmediatePoints); // maxImmediatePoints
                    weights.add(buyFromMarketIndex); // buyFromMarketIndex
                    weights.add(buyFromReserveIndex); // buyFromReserveIndex
                    weights.add(attractNobleIndex); // attractNobleIndex
                    weights.add(numCardsToReserve); // numCardsToReserve
                    weights.add(totalTokens); // totalTokens

                    agent1 = new MinimaxAgent(3, 0, 0, weights);
                }
                case 3 -> {
                    double hasWon = 20000.0;
                    double isWinning = 10000.0;
                    double currentPoints = 300.0;
                    double opponentPoints = -300.0;
                    double maxImmediatePoints = 200;
                    double buyFromMarketIndex = 0;
                    double buyFromReserveIndex = 10;
                    double attractNobleIndex = 10;
                    double numCardsToReserve = 10;
                    double totalTokens = 0;

                    ArrayList<Double> weights = new ArrayList<>();
                    weights.add(hasWon); // hasWon
                    weights.add(isWinning); // isWinning
                    weights.add(currentPoints); // currentPoints
                    weights.add(opponentPoints); // opponentPoints
                    weights.add(maxImmediatePoints); // maxImmediatePoints
                    weights.add(buyFromMarketIndex); // buyFromMarketIndex
                    weights.add(buyFromReserveIndex); // buyFromReserveIndex
                    weights.add(attractNobleIndex); // attractNobleIndex
                    weights.add(numCardsToReserve); // numCardsToReserve
                    weights.add(totalTokens); // totalTokens

                    agent1 = new MinimaxAgent(5, 0, 0, weights);
                }
                case 4 -> {
                    double hasWon = 20000.0;
                    double isWinning = 10000.0;
                    double currentPoints = 300.0;
                    double opponentPoints = -300.0;
                    double maxImmediatePoints = 200;
                    double buyFromMarketIndex = 0;
                    double buyFromReserveIndex = 10;
                    double attractNobleIndex = 10;
                    double numCardsToReserve = 10;
                    double totalTokens = 0;

                    ArrayList<Double> weights = new ArrayList<>();
                    weights.add(hasWon); // hasWon
                    weights.add(isWinning); // isWinning
                    weights.add(currentPoints); // currentPoints
                    weights.add(opponentPoints); // opponentPoints
                    weights.add(maxImmediatePoints); // maxImmediatePoints
                    weights.add(buyFromMarketIndex); // buyFromMarketIndex
                    weights.add(buyFromReserveIndex); // buyFromReserveIndex
                    weights.add(attractNobleIndex); // attractNobleIndex
                    weights.add(numCardsToReserve); // numCardsToReserve
                    weights.add(totalTokens); // totalTokens

                    agent1 = new MinimaxAgent(7, 0, 0, weights);
                }
                default -> {
                    System.out.println("Fatal Error. Shutting Down.");
                    System.exit(1);
                }
            }

            switch(chosenAgent2Index) {
                case 1 -> agent2 = new RandomMoveAgent();
                case 2 -> {
                    double hasWon = 20000.0;
                    double isWinning = 10000.0;
                    double currentPoints = 300.0;
                    double opponentPoints = -300.0;
                    double maxImmediatePoints = 200;
                    double buyFromMarketIndex = 0;
                    double buyFromReserveIndex = 10;
                    double attractNobleIndex = 10;
                    double numCardsToReserve = 10;
                    double totalTokens = 0;

                    ArrayList<Double> weights = new ArrayList<>();
                    weights.add(hasWon); // hasWon
                    weights.add(isWinning); // isWinning
                    weights.add(currentPoints); // currentPoints
                    weights.add(opponentPoints); // opponentPoints
                    weights.add(maxImmediatePoints); // maxImmediatePoints
                    weights.add(buyFromMarketIndex); // buyFromMarketIndex
                    weights.add(buyFromReserveIndex); // buyFromReserveIndex
                    weights.add(attractNobleIndex); // attractNobleIndex
                    weights.add(numCardsToReserve); // numCardsToReserve
                    weights.add(totalTokens); // totalTokens

                    agent2 = new MinimaxAgent(3, 0, 0, weights);
                }
                case 3 -> {
                    double hasWon = 20000.0;
                    double isWinning = 10000.0;
                    double currentPoints = 300.0;
                    double opponentPoints = -300.0;
                    double maxImmediatePoints = 200;
                    double buyFromMarketIndex = 0;
                    double buyFromReserveIndex = 10;
                    double attractNobleIndex = 10;
                    double numCardsToReserve = 10;
                    double totalTokens = 0;

                    ArrayList<Double> weights = new ArrayList<>();
                    weights.add(hasWon); // hasWon
                    weights.add(isWinning); // isWinning
                    weights.add(currentPoints); // currentPoints
                    weights.add(opponentPoints); // opponentPoints
                    weights.add(maxImmediatePoints); // maxImmediatePoints
                    weights.add(buyFromMarketIndex); // buyFromMarketIndex
                    weights.add(buyFromReserveIndex); // buyFromReserveIndex
                    weights.add(attractNobleIndex); // attractNobleIndex
                    weights.add(numCardsToReserve); // numCardsToReserve
                    weights.add(totalTokens); // totalTokens

                    agent2 = new MinimaxAgent(5, 0, 0, weights);
                }
                case 4 -> {
                    double hasWon = 20000.0;
                    double isWinning = 10000.0;
                    double currentPoints = 300.0;
                    double opponentPoints = -300.0;
                    double maxImmediatePoints = 200;
                    double buyFromMarketIndex = 0;
                    double buyFromReserveIndex = 10;
                    double attractNobleIndex = 10;
                    double numCardsToReserve = 10;
                    double totalTokens = 0;

                    ArrayList<Double> weights = new ArrayList<>();
                    weights.add(hasWon); // hasWon
                    weights.add(isWinning); // isWinning
                    weights.add(currentPoints); // currentPoints
                    weights.add(opponentPoints); // opponentPoints
                    weights.add(maxImmediatePoints); // maxImmediatePoints
                    weights.add(buyFromMarketIndex); // buyFromMarketIndex
                    weights.add(buyFromReserveIndex); // buyFromReserveIndex
                    weights.add(attractNobleIndex); // attractNobleIndex
                    weights.add(numCardsToReserve); // numCardsToReserve
                    weights.add(totalTokens); // totalTokens

                    agent2 = new MinimaxAgent(7, 0, 0, weights);
                }
                default -> {
                    System.out.println("Fatal Error. Shutting Down");
                    System.exit(1);
                }
            }

            // Gameplay
            if(numberOfGames == 1) {
                Move lastMove = null;
                while(!state.isGameOver()) {
                    System.out.println(state.toString());
                    if(lastMove != null)
                        System.out.println("Last action: " + lastMove);

                    Move currentMove = null;
                    while(currentMove == null && !state.isGameOver()) {
                        // Get move from agent 1
                        if(state.getPlayerToMove() == 1) {
                            currentMove = agent1.generateMove(state);
                        }
                        // Get move from agent 2
                        else if(state.getPlayerToMove() == 2) {
                            currentMove = agent2.generateMove(state);
                        }

                        // Handle move
                        if(currentMove != null)
                            if(state.handleMove(currentMove) == null) {
                                displayIllegalMoveText();
                                currentMove = null;
                            }
                    }
                    lastMove = currentMove;
                }
                System.out.println(state.toString());
                if(lastMove != null)
                    System.out.println("Last action: " + lastMove);

                if(state.getWinner() > 0)
                    System.out.println("Game is over! Player " + state.getWinner() + " won!");
                else if(state.getWinner() == 0)
                    System.out.println("Game is over! Draw!");
            }
            else {
                long startTime = System.currentTimeMillis();

                int player1OverallScore = 0, player2OverallScore = 0;
                double averageTurnNumber1 = 0, averageTurnNumber2 = 0;
                int draws = 0;


                for(int i = 0; i < numberOfGames; i ++) {
                    state = new GameState();
                    boolean isError = false;
                    System.out.println("Playing game " + (i + 1)  + " of " + numberOfGames + "...");
                    while(!state.isGameOver()) {
                        Move currentMove = null;
                        while(currentMove == null && !state.isGameOver()) {
                            // Get move from agent 1
                            if(state.getPlayerToMove() == 1) {
                                currentMove = agent1.generateMove(state);
                            }
                            // Get move from agent 2
                            else if(state.getPlayerToMove() == 2) {
                                currentMove = agent2.generateMove(state);
                            }

                            // Handle move
                            if(currentMove != null) {
                                if(state.handleMove(currentMove) == null) {
                                    displayIllegalMoveText();
                                    System.out.println(state.toString());
                                    System.out.println(currentMove);
                                    isError = true;

                                    break;
                                }
                            }
                        }
                        if(isError)
                            break;

                        // Check game over conditions
                        if(state.getWinner() > 0) {
                            switch(state.getWinner()) {
                                case 1 -> {
                                    player1OverallScore ++;
                                    averageTurnNumber1 += state.getTurnNumber();
                                }
                                case 2 -> {
                                    player2OverallScore ++;
                                    averageTurnNumber2 += state.getTurnNumber();
                                }
                            }
                            // Print current information
                            System.out.println("Player 1 won " + player1OverallScore + " games so far. Average turns: " + averageTurnNumber1/player1OverallScore);
                            System.out.println("Player 2 won " + player2OverallScore + " games so far. Average turns: " + averageTurnNumber2/player2OverallScore);
                            System.out.println("There were " + draws + " draws so far.");
                        }
                        else if(state.getWinner() == 0) {
                            draws ++;
                            // Print current information
                            System.out.println("Player 1 won " + player1OverallScore + " games so far. Average turns: " + averageTurnNumber1/player1OverallScore);
                            System.out.println("Player 2 won " + player2OverallScore + " games so far. Average turns: " + averageTurnNumber2/player2OverallScore);
                            System.out.println("There were " + draws + " draws so far.");
                        }
                    }
                }

                if(player1OverallScore > 0)
                    averageTurnNumber1 /= player1OverallScore;
                if(player2OverallScore > 0)
                    averageTurnNumber2 /= player2OverallScore;


                System.out.println("All " + numberOfGames + " games over! Player 1 won " + player1OverallScore + " games (" +
                        player1OverallScore*100.0/numberOfGames + "%)\n" +
                        "Player 2 won " + player2OverallScore + " games (" +
                        player2OverallScore*100.0/numberOfGames + "%)\n" +
                        "There were " + draws + " draws.\n" +
                        "The average turn number for player 1 was " + averageTurnNumber1 + ".\n" +
                        "The average turn number for player 2 was " + averageTurnNumber2 + ".\n" +
                        "Total time for the games: " + (System.currentTimeMillis() - startTime)/1000.0 + " seconds");

            }

        }
    }

    /**
     * <h1>parseMove(String input)</h1>
     * <p>Parses a string representing a Splendor move. Does not check move validity,
     * but does check string validity.</p>
     * <p>String format: "moveType <parameters>"<br>
     * <ul>
     *     <li>
     *         "take" <tokensToTake> [tokensToReturn]
     *          <ul>
     *              <li>TakeTokens move</li>
     *              <li>tokensToTake - array of 6 elements, representing the tokens to take
     *              from the supply</li>
     *              <li>tokensToReturn - optional parameter - array of 6 elements representing the tokens
     *              to return to the supply</li>
     *          </ul>
     *     </li>
     *     <li>
     *         "reserve" cardTier cardIndex [tokenToReturn]
     *         <ul>
     *             <li>ReserveCard move</li>
     *             <li>cardTier - integer from 1 to 3, representing tier of card to reserve</li>
     *             <li>cardIndex - integer from 0 to 4, representing index of card to reserve (4 is blind reserve)</li>
     *             <li>tokenToReturn - optional parameter - integer from 0 to 5 representing the index of the token
     *             to return to the supply</li>
     *         </ul>
     *     </li>
     *     <li>
     *         "buy" cardSource cardIndex [nobleIndex]
     *         <ul>
     *              <li>BuyCard move</li>
     *              <li>cardSource - integer from 0 to 3, representing the source of the card to buy (0 is reserve,
     *              1-3 represent the card tiers)</li>
     *              <li>cardIndex - integer from 0 to 3, representing index of card to buy</li>
     *              <li>nobleIndex - integer from 0 to 2, representing index of noble to attract</li>
     *         </ul>
     *     </li>
     * </ul>
     * </p>
     *
     * @param input - String representing
     * @return null - input is an invalid string
     *         Move - a Move object representing the move corresponding to the string
     */
    public static Move parseMove(String input) {
        String[] args = input.split(" ");
        switch(args[0]) {
            case "take":
                return parseTakeMove(args);
            case "reserve":
                return parseReserveMove(args);
            case "buy":
                return parseBuyMove(args);
            default:
                displayUsage();
                return null;
        }
    }

    public static TakeTokensMove parseTakeMove(String[] args) {
        // Check number of arguments
        if(args.length != 6 && args.length != 11) {
            displayUsage();
            return null;
        }

        int[] tokensToTake = {0,0,0,0,0}, tokensToReturn = {0,0,0,0,0};
        for(int i = 1; i <= 5; i ++) {
            if(!stringIsDigit(args[i])) {
                System.out.println("Error: Values in tokensToTake must be single digits");
                displayUsage();
                return null;
            }
            tokensToTake[i - 1] = Integer.parseInt(args[i]);
        }
        // Only accessed if there are tokens to return
        for(int i = 6; i < args.length; i ++) {
            if(!stringIsDigit(args[i])) {
                System.out.println("Error: Values in tokensToReturn must be single digits");
                displayUsage();
                return null;
            }
            tokensToReturn[i - 6] = Integer.parseInt(args[i]);
        }

        int player = state.getPlayerToMove();

        return new TakeTokensMove(player, tokensToTake, tokensToReturn);
    }

    public static ReserveCardMove parseReserveMove(String[] args) {
        // Check number of arguments
        if(args.length != 3 && args.length != 4) {
            displayUsage();
            return null;
        }

        int player = state.getPlayerToMove();
        int cardTier, cardIndex;
        int[] tokensToReturn = {0, 0, 0, 0, 0};

        if(!stringIsDigit(args[1])) {
            System.out.println("Error: cardTier must be a single digit");
            displayUsage();
            return null;
        }

        cardTier = Integer.parseInt(args[1]);

        if(cardTier < 1 || cardTier > 3) {
            System.out.println("Error: cardTier must be an integer between 1 and 3");
            displayUsage();
            return null;
        }

        if(!stringIsDigit(args[2])) {
            System.out.println("Error: cardIndex must be a single digit");
            displayUsage();
            return null;
        }

        cardIndex = Integer.parseInt(args[2]);
        if(cardIndex < 0 || cardIndex > 4) {
            System.out.println("Error: cardIndex must be an integer between 0 and 4");
            displayUsage();
            return null;
        }

        if(args.length == 4) {
            if(!stringIsDigit(args[3])) {
                System.out.println("Error: tokenToReturn must be a single digit");
                displayUsage();
                return null;
            }

            int indexOfToken = Integer.parseInt(args[3]);
            if(indexOfToken < 0 || indexOfToken > 4) {
                System.out.println("Error: tokenToReturn must be an integer between 0 and 4");
                displayUsage();
                return null;
            }

            tokensToReturn[indexOfToken] = 1;
        }

        return new ReserveCardMove(player, cardIndex, cardTier, tokensToReturn);
    }

    public static BuyCardMove parseBuyMove(String[] args) {
        // Check number of arguments
        if(args.length != 3 && args.length != 4)
            return null;

        int player = state.getPlayerToMove();
        int cardSource, cardIndex, nobleIndex = -1;

        if(!stringIsDigit(args[1])) {
            System.out.println("Error: cardSource must be a single digit");
            displayUsage();
            return null;
        }

        cardSource = Integer.parseInt(args[1]);

        if(cardSource < 0 || cardSource > 3) {
            System.out.println("Error: cardSource must be an integer between 0 and 3");
            displayUsage();
            return null;
        }

        if(!stringIsDigit(args[2])) {
            System.out.println("Error: cardIndex must be a single digit");
            displayUsage();
            return null;
        }

        cardIndex = Integer.parseInt(args[2]);

        if(cardIndex < 0 || cardIndex > 3) {
            System.out.println("Error: cardIndex must be an integer between 0 and 3");
            displayUsage();
            return null;
        }

        if(args.length == 4) {
            if(!stringIsDigit(args[3])) {
                System.out.println("Error: nobleIndex must be a single digit");
                displayUsage();
                return null;
            }

            nobleIndex = Integer.parseInt(args[3]);

            if(nobleIndex < 0 || nobleIndex > 2) {
                System.out.println("Error: nobleIndex must be an integer between 0 and 3");
                displayUsage();
                return null;
            }
        }

        return new BuyCardMove(player, cardSource, cardIndex, nobleIndex);

    }

    private static boolean stringIsDigit(String s) {
        if(s == null)
            return false;

        if(s.length() == 0)
            return false;

        return s.length() == 1 && s.charAt(0) >= '0' && s.charAt(0) <= '9';
    }

    private static void displayIllegalMoveText() {
        System.out.println("Error: That move appears to be illegal. Please consult the game rules and game state and try again.");
    }

    private static void displayUsage() {
        System.out.println("""
                Usage:
                1. "take" <tokensToTake> [tokensToReturn]
                   - TakeTokens move
                   - tokensToTake - array of 5 elements, representing the tokens to take from the supply
                   - tokensToReturn - optional parameter - array of 5 elements representing the tokens to return to the supply
                2. "reserve" cardTier cardIndex [tokenToReturn]
                   - ReserveCard move
                   - cardTier - integer from 1 to 3, representing tier of card to reserve
                   - cardIndex - integer from 0 to 4, representing index of card to reserve (4 is blind reserve)
                   - tokenToReturn - optional parameter - integer from 0 to 4 representing the index of the token to return to the supply
                3. "buy" cardSource cardIndex [nobleIndex]
                   - BuyCard move
                   - cardSource - integer from 0 to 3, representing the source of the card to buy (0 is reserve, 1-3 represent the card tiers)
                   - cardIndex - integer from 0 to 3, representing index of card to buy
                   - nobleIndex - optional parameter - integer from 0 to 2, representing index of noble to attract; Necessary if a buy move
                     would attract at least one noble
                """);
    }
}
