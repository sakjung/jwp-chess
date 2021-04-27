package chess.domain.feature;

import java.util.Arrays;

public enum Color {
    BLACK("black", 0),
    WHITE("white", 1),
    NO_COLOR("blank", 2);

    private final String color;
    private final int playerCount;

    Color(String color, int playerCount) {
        this.color = color;
        this.playerCount = playerCount;
    }

    public static String assignColor(int count) {
        return Arrays.stream(values())
                .limit(2)
                .filter(color -> color.playerCount == count)
                .map(color -> color.color)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Color convert(String color) {
        return Arrays.stream(values())
                .filter(value -> value.color.equals(color))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isWhite() {
        return this.equals(WHITE);
    }

    public boolean isBlack() {
        return this.equals(BLACK);
    }

    public Color getOppositeColor() {
        if (this.isBlack()) {
            return Color.WHITE;
        }
        if (this.isWhite()) {
            return Color.BLACK;
        }
        throw new NoOppositeColorException();
    }

    public String getColor() {
        return color;
    }
}
