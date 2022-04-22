## Welcome to the Splendor Command Line Interface!

### Simulating games
On starting the program, you will be presented 3 options: playing a game against another human, playing a game against an artificial agent, and making 2 artificial agents play against each other.
Selecting an option involving an artificial agent will further prompt you to select which artificial agent should take part in the game. When making 2 artificial agents play against each other, you will also be asked how many games you would like to simulate. If 1 is chosen, each state of the game will be shown in the command line. If more games are to be simulated, an overview of the final results of the games will be presented instead.

### Making moves
To make moves, the following options are available, with the given syntax. Note that all 'words' in a command should be separated by single spaces, and symbols such as ", <, >, [, ] should not be used when specifying the commands.
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
### Final note
The 5- and 7- ply agents might take a while to reach their decisions, thus games involving them might take longer to complete. Enjoy using the Command Line Splendor Interface!