package services;

import cards.Card;
import players.Player;

import java.util.Map;

@FunctionalInterface
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
        3, .34,
        2, .34,
        1, 1d,
        12, .34,
        11, .34,
        10, .34);
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
