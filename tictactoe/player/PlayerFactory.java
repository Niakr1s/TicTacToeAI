package tictactoe.player;

public class PlayerFactory {
    public static Player fromType(PlayerType type) {
        switch (type) {
            case HumanPlayer:
                return new HumanPlayer();
            case EasyAIPlayer:
                return new EasyAIPlayer();
            case MediumAIPlayer:
                return new MediumAIPlayer();
            case HardAIPlayer:
                return new HardAIPlayer();
            default:
                return null;
        }
    }
}
