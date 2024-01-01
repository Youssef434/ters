package game;

import players.Team;

import java.util.Map;

public interface Result {
  Map<Team, ? extends Number> get();
}
