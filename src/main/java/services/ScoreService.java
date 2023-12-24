package services;

import cards.Card;
import players.Player;

import java.util.Map;

public interface ScoreService {
  double countScore(Player player);

  static ScoreService create() {
    return new ScoreServiceImpl();
  }
}

class ScoreServiceImpl implements ScoreService {
  private final Map<Integer, Double> scoreMap;

  public ScoreServiceImpl() {
    scoreMap = Map.of(
        3, .33,
        2, .33,
        1, 1d,
        12, .33,
        11, .33,
        10, .33);
  }

  @Override
  public double countScore(Player player) {
    return player.cards()
        .stream()
        .map(Card::getNumber)
        .filter(scoreMap::containsKey)
        .mapToDouble(scoreMap::get)
        .sum();
  }
}
