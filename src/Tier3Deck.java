// A deck of development cards, initialized to contain all Tier 3 cards in Splendor, in random order
public class Tier3Deck extends CardDeck {
    public Tier3Deck() {
        super();

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,3,5,3,3}, 3));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,0,0,7,0}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{3,0,3,6,0}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{3,0,0,7,0}, 5));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{5,0,3,3,3}, 3));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{0,0,0,0,7}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{3,3,0,0,6}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{0,3,0,0,7}, 5));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{3,3,0,3,5}, 3));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,7,0,0,0}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,6,3,0,3}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,7,3,0,0}, 5));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{3,5,3,0,3}, 3));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{0,0,7,0,0}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{0,3,6,3,0}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{0,0,7,3,0}, 5));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{3,3,3,5,0}, 3));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{7,0,0,0,0}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{6,0,0,3,3}, 4));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{7,0,0,0,3}, 5));

        this.shuffle();
    }

    // Deep copy of a Tier3Deck objects
    public Tier3Deck(Tier3Deck deck) {
        super(deck);
    }
}
