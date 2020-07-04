package tictactoe.player;

import java.util.Objects;

public enum PlayerType {
    HumanPlayer("user"),
    EasyAIPlayer("easy"),
    MediumAIPlayer("medium"),
    HardAIPlayer("hard");

    private final String str;

    PlayerType(String str) {
        this.str = str;
    }

    public static PlayerType fromStr(String str) throws IllegalArgumentException {
        for (PlayerType type : PlayerType.values()) {
            if (Objects.equals(type.str, str)) {
                return type;
            }
        }
        throw new IllegalArgumentException();
    }
}
