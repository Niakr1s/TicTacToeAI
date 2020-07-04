package tictactoe.game.config;

import tictactoe.player.PlayerType;

public class Config {
    private final PlayerType[] players;

    public Config(PlayerType firstPlayer, PlayerType secondPlayer) {
        this.players = new PlayerType[2];
        this.players[0] = firstPlayer;
        this.players[1] = secondPlayer;
    }

    public PlayerType[] getPlayers() {
        return players.clone();
    }
}
