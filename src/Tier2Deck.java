// A deck of development cards, initialized to contain all Tier 2 cards in Splendor, in random order
public class Tier2Deck extends CardDeck {
    public Tier2Deck() {
        super();

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,2,2,0,3},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{2,0,3,0,3},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,1,4,2,0},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,0,5,3,0},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{0,0,0,0,5},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLACK, new int[]{6,0,0,0,0},3));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{0,2,2,3,0},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{3,2,3,0,0},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{0,3,0,0,5},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{4,0,0,1,2},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{0,5,0,0,0},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.BLUE, new int[]{0,6,0,0,0},3));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,0,2,3,3},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{2,3,0,0,2},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{1,2,0,0,4},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,5,3,0,0},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,0,5,0,0},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.GREEN, new int[]{0,0,6,0,0},3));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{3,0,0,2,2},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{3,3,0,2,0},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{0,4,2,0,1},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{5,0,0,0,3},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{5,0,0,0,0},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.RED, new int[]{0,0,0,6,0},3));

        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{2,0,3,2,0},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{0,3,0,3,2},1));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{2,0,1,4,0},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{3,0,0,5,0},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{0,0,0,5,0},2));
        this.addCard(new DevelopmentCard(DevelopmentCard.Color.WHITE, new int[]{0,0,0,0,6},3));

        this.shuffle();
    }

    // Deep copy of a Tier2Deck objects
    public Tier2Deck(Tier2Deck deck) {
        super(deck);
    }
}
