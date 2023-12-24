package services;

import players.Player;

public interface ScoreService {
  int countScore(Player player);

  static ScoreService create() {
    return new ScoreServiceImpl();
  }
}

class ScoreServiceImpl implements ScoreService {

  @Override
  public int countScore(Player player) {
    return 0;
  }
}
