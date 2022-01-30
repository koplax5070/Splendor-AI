import java.util.ArrayList;
import java.util.Collections;

// Collection of Nobles, as represented in the game of Splendor, in a random order
// Provides methods for extracting 3, 4, or 5 nobles, for use in a game of Splendor
public class SplendorNobles {
    private ArrayList<NobleTile> nobleTilesArray;

    public SplendorNobles() {
        nobleTilesArray = new ArrayList<>();
        nobleTilesArray.add(new NobleTile("Mary Stuart", new int[]{0,0,4,4,0}));
        nobleTilesArray.add(new NobleTile("Charles Quint", new int[]{3,0,0,3,3}));
        nobleTilesArray.add(new NobleTile("Macchiavelli", new int[]{0,4,0,0,4}));
        nobleTilesArray.add(new NobleTile("Isabel of Castille", new int[]{4,0,0,0,4}));
        nobleTilesArray.add(new NobleTile("Soliman the Magnificent", new int[]{0,4,4,0,0}));
        nobleTilesArray.add(new NobleTile("Catherine of Medicis", new int[]{0,3,3,3,0}));
        nobleTilesArray.add(new NobleTile("Anne of Brittany", new int[]{0,3,3,0,3}));
        nobleTilesArray.add(new NobleTile("Henri VIII", new int[]{4,0,0,4,0}));
        nobleTilesArray.add(new NobleTile("Elisabeth of Austria", new int[]{3,3,0,0,3}));
        nobleTilesArray.add(new NobleTile("Francis I of France", new int[]{3,0,3,3,0}));

        Collections.shuffle(nobleTilesArray);
    }

    // Extracts k Noble tiles (2<k<6) and returns an array containing the tiles; Returns null if k is invalid
    public NobleTile[] extract(int k) {
        return switch (k) {
            case 3 -> new NobleTile[]{nobleTilesArray.get(0), nobleTilesArray.get(1), nobleTilesArray.get(2)};
            case 4 -> new NobleTile[]{nobleTilesArray.get(0), nobleTilesArray.get(1), nobleTilesArray.get(2),
                    nobleTilesArray.get(3)};
            case 5 -> new NobleTile[]{nobleTilesArray.get(0), nobleTilesArray.get(1), nobleTilesArray.get(2),
                    nobleTilesArray.get(3), nobleTilesArray.get(4)};
            default -> null;
        };
    }
}
