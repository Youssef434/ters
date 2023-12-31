package services;

import cards.Card;
import game.Cycle;
import game.TeamScore;
import java.util.Collection;
import java.util.Map;

public interface ScoreService {
  TeamScore countCycleScore(Cycle.CycleResult cycleResult);

  static ScoreService create() {
    return new ScoreServiceImpl();
  }

  final class ScoreServiceImpl implements ScoreService {
    private final Map<Integer, Double> scoreMap;

    private ScoreServiceImpl() {
      scoreMap = Map.of(
          3, .34,
          2, .34,
          1, 1d,
          12, .34,
          11, .34,
          10, .34);
    }

    private double countScore(Collection<Card> cards) {
      return cards
          .stream()
          .map(Card::getNumber)
          .filter(scoreMap::containsKey)
          .mapToDouble(scoreMap::get)
          .sum();
    }
    @Override
    public TeamScore countCycleScore(Cycle.CycleResult cycleResult) {
      return new TeamScore(cycleResult.getPlayer().team(), countScore(cycleResult.getCards()));
    }
  }
}
