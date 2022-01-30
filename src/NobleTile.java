// A noble tile, represented by name, required cards to attract noble, and pointValue
// (default 3 as per the rules of Splendor)
public class NobleTile {
    private final String name;
    private final int requiredBlack, requiredBlue, requiredGreen, requiredRed, requiredWhite;
    private final int[] requiredArray;
    private final int pointValue;

    // Construct a NobleTile object, taking as parameters the name of the figure on the tile
    // (for easier identification) and the array representing the required number of cards
    // of each color to attract the noble
    public NobleTile(String name, int[] requiredArray) {
        this.name = name;
        requiredBlack = requiredArray[0];
        requiredBlue = requiredArray[1];
        requiredGreen = requiredArray[2];
        requiredRed = requiredArray[3];
        requiredWhite = requiredArray[4];
        this.requiredArray = requiredArray;
        pointValue = 3;
    }

    // Deep copy of a NobleTile object
    public NobleTile(NobleTile nobleTile) {
        this.name = nobleTile.getName();
        requiredBlack = nobleTile.getRequiredBlack();
        requiredBlue = nobleTile.getRequiredBlue();
        requiredGreen = nobleTile.getRequiredGreen();
        requiredRed = nobleTile.getRequiredRed();
        requiredWhite = nobleTile.getRequiredWhite();
        requiredArray = new int[]{requiredBlack, requiredBlue, requiredGreen, requiredRed, requiredWhite};
        pointValue = nobleTile.getPointValue();
    }

    @Override
    public String toString() {
        StringBuilder requiredString = new StringBuilder();

        if(requiredBlack > 0) {
            requiredString.append(requiredBlack).append(" Black ");
        }
        if(requiredBlue > 0) {
            requiredString.append(requiredBlue).append(" Blue ");
        }
        if(requiredGreen > 0) {
            requiredString.append(requiredGreen).append(" Green ");
        }
        if(requiredRed > 0) {
            requiredString.append(requiredRed).append(" Red ");
        }
        if(requiredWhite > 0) {
            requiredString.append(requiredWhite).append(" White ");
        }

        return "Name: " + name + "\n" +
                "Required Cards: " + requiredString + "\n";

    }

    // Getters
    public String getName() {
        return name;
    }

    public int getRequiredBlack() {
        return requiredBlack;
    }

    public int getRequiredBlue() {
        return requiredBlue;
    }

    public int getRequiredGreen() {
        return requiredGreen;
    }

    public int getRequiredRed() {
        return requiredRed;
    }

    public int getRequiredWhite() {
        return requiredWhite;
    }

    public int[] getRequiredArray() {
        return requiredArray;
    }

    public int getPointValue() {
        return pointValue;
    }
}
