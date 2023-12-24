package services;

import game.Game;

import java.util.Map;

@FunctionalInterface
public interface GameEligibilityService {
  boolean isEligible(Game game);
  static GameEligibilityService create() {
    return new GameEligibilityServiceImpl();
  }
}

class GameEligibilityServiceImpl implements GameEligibilityService {

  @Override
  public boolean isEligible(Game game) {
    return false;
  }
}
