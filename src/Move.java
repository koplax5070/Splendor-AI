public abstract class Move {
    enum Type {takeTokens, reserveCard, buyCard}
    private final int player;
    private final Type type;

    public Move(int player, Type type) {
        this.player = player;
        this.type = type;
    }

    public int getPlayer() {
        return player;
    }

    public Type getType() {
        return type;
    }
}
