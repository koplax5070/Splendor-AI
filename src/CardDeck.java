import java.util.ArrayList;
import java.util.Collections;

public abstract class CardDeck {
    private ArrayList<DevelopmentCard> cardsArray;
    private int size;

    public CardDeck() {
        cardsArray = new ArrayList<>();
        size = 0;
    }

    // Deep copy of a cardDeck object
    public CardDeck(CardDeck deck) {
        cardsArray = new ArrayList<>();
        for(int i = 0; i < deck.cardsArray.size(); i ++) {
            cardsArray.add(new DevelopmentCard(deck.cardsArray.get(i)));
        }
        size = deck.getSize();
    }

    // Adds a development card to the top of the deck
    public void addCard(DevelopmentCard card) {
        cardsArray.add(card);
        size ++;
    }

    // Removes the card from the top of the deck and returns it
    public DevelopmentCard drawCard() {
        DevelopmentCard card;
        if(cardsArray.size() > 0) {
            card = cardsArray.get(cardsArray.size() - 1);
            cardsArray.remove(cardsArray.size() - 1);
            size --;
            return card;
        }
        else
            return null;
    }

    // Shuffles the cards in the deck
    public void shuffle() {
        Collections.shuffle(cardsArray);
    }

    public boolean isEmpty() {
        return cardsArray.size() == 0;
    }

    public int getSize() {return size;}

    public DevelopmentCard getCard(int index) {
        if(index > size || index < 0)
            return null;
        else
            return cardsArray.get(index);
    }

}
