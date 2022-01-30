// A development card in the game of Splendor, represented by color of bonus provided, cost in each of
// the colors, and point value
public class DevelopmentCard {
    public enum Color{BLACK(0), BLUE(1), GREEN(2), RED(3), WHITE(4);
        private final int index;
        Color(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    private final Color bonusColor;
    private final int blackCost, blueCost, greenCost, redCost, whiteCost;
    private final int[] costArray;
    private final int pointValue;

    // Construct a DevelopmentCard object, taking as parameters its color, an array representing its
    // cost (in the order black, blue, green, red, white), and point value
    public DevelopmentCard(Color cardColor, int[] costArray, int points) {
        bonusColor = cardColor;
        blackCost = costArray[0];
        blueCost = costArray[1];
        greenCost = costArray[2];
        redCost = costArray[3];
        whiteCost = costArray[4];
        this.costArray = costArray;
        pointValue = points;
    }

    // Deep copy of a DevelopmentCard object
    public DevelopmentCard(DevelopmentCard card) {
        bonusColor = card.getBonusColor();
        blackCost = card.getBlackCost();
        blueCost = card.getBlueCost();
        greenCost = card.getGreenCost();
        redCost = card.getRedCost();
        whiteCost = card.getWhiteCost();
        costArray = new int[]{blackCost, blueCost, greenCost, redCost, whiteCost};
        pointValue = card.getPointValue();
    }

    // Point value can be omitted if zero
    public DevelopmentCard(Color cardColor, int[] costArray) {
        this(cardColor, costArray, 0);
    }

    public DevelopmentCard(Color cardColor, int blackCost, int blueCost, int greenCost,
                           int redCost, int whiteCost, int pointValue) {
        this(cardColor, new int[]{blackCost, blueCost, greenCost, redCost, whiteCost}, pointValue);
    }

    public DevelopmentCard(Color cardColor, int blackCost, int blueCost, int greenCost,
                           int redCost, int whiteCost) {
        this(cardColor, new int[]{blackCost, blueCost, greenCost, redCost, whiteCost}, 0);
    }

    @Override
    public String toString() {
        String colorString;
        StringBuilder costStringBuilder = new StringBuilder();
        switch (this.bonusColor) {
            case BLACK -> colorString = "Black";
            case BLUE -> colorString = "Blue";
            case GREEN -> colorString = "Green";
            case RED -> colorString = "Red";
            case WHITE -> colorString = "White";
            default -> colorString = "NullError";
        }

        if(blackCost > 0) {
            costStringBuilder.append(blackCost);
            costStringBuilder.append(" Black ");
        }
        if(blueCost > 0) {
            costStringBuilder.append(blueCost);
            costStringBuilder.append(" Blue ");
        }
        if(greenCost > 0) {
            costStringBuilder.append(greenCost);
            costStringBuilder.append(" Green ");
        }
        if(redCost > 0) {
            costStringBuilder.append(redCost);
            costStringBuilder.append(" Red ");
        }
        if(whiteCost > 0) {
            costStringBuilder.append(whiteCost);
            costStringBuilder.append(" White ");
        }

        return "Card color: " + colorString + "\n" +
                "Card points: " + pointValue + "\n" +
                "Card cost: " + costStringBuilder + "\n";
    }

    // Getters
    public Color getBonusColor() {
        return bonusColor;
    }

    public int getBlackCost() {
        return blackCost;
    }

    public int getBlueCost() {
        return blueCost;
    }

    public int getGreenCost() {
        return greenCost;
    }

    public int getRedCost() {
        return redCost;
    }

    public int getWhiteCost() {
        return whiteCost;
    }

    public int[] getCostArray() {
        return costArray;
    }

    public int getPointValue() {
        return pointValue;
    }
}
