package game;

import players.Player;

import java.util.List;

public interface Playable {
  Result start(int beginIndex, List<Player> players);
}
