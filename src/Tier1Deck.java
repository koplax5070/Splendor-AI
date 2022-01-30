
// A deck of development cards, initialized to contain all Tier 1 cards in Splendor, in random order
public class Tier1Deck extends CardDeck {
    public Tier1Deck() {
        super();
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,1,1,1,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,2,1,1,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,2,0,1,2}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{1,0,1,3,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,0,2,1,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,0,2,0,2}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,0,3,0,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,4,0,0,0}, 1));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{1,0,1,1,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{1,0,1,2,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{0,0,2,2,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{0,1,3,1,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{2,0,0,0,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{2,0,2,0,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{3,0,0,0,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{0,0,0,4,0}, 1));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{1,1,0,1,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{2,1,0,1,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{2,1,0,2,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,3,1,0,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,1,0,0,2}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,2,0,2,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,0,0,3,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{4,0,0,0,0}, 1));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{1,1,1,0,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{1,1,1,0,2}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{2,0,1,0,2}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{3,0,0,1,1}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{0,2,1,0,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{0,0,0,2,2}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{0,0,0,0,3}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{0,0,0,0,4}, 1));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{1,1,1,1,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{1,1,2,1,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{1,2,2,0,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{1,1,0,0,3}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{1,0,0,2,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{2,2,0,0,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{0,3,0,0,0}));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{0,0,4,0,0}, 1));

        this.shuffle();
    }

    // Deep copy of a Tier1Deck objects
    public Tier1Deck(Tier1Deck deck) {
        super(deck);
    }
}
