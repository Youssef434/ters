package services;

import cards.Card;
import game.Cycle;
import game.Round;
import players.Team;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public interface ScoreService {
  Map<Team, Double> getCycleScore(Cycle.CycleResult cycleResult);
  Map<Team, Double> getRoundScore(Round.RoundResult roundResult);

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

    public Map<Team, Double> getCycleScore(Cycle.CycleResult cycleResult) {
      var winingTeam = cycleResult.getPlayer().team();
      var nonWiningTeam = winingTeam == Team.A ? Team.B : Team.A;
      return Map.of(
          winingTeam, countScore(cycleResult.getCards()),
          nonWiningTeam, 0d);
    }

    @Override
    public Map<Team, Double> getRoundScore(Round.RoundResult roundResult) {
      var resultMap = roundResult.cycleResults().stream()
          .map(this::getCycleScore)
          .flatMap(map -> map.entrySet().stream())
          .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));
      resultMap.computeIfPresent(roundResult.lastCycleWinner(), (k, v) -> v + 1);
      return resultMap;
    }
  }
}
