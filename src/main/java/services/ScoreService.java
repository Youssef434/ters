package services;

import cards.Card;
import game.Cycle;
import game.Round;
import game.TeamScore;
import players.Team;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public interface ScoreService {
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

    private TeamScore countCycleScore(Cycle.CycleResult cycleResult) {
      return new TeamScore(cycleResult.getPlayer().team(), countScore(cycleResult.getCards()));
    }

    @Override
    public Map<Team, Double> getRoundScore(Round.RoundResult roundResult) {
      var resultMap = roundResult.cycleResults().stream()
          .map(this::countCycleScore)
          .collect(Collectors.groupingBy(TeamScore::team, Collectors.summingDouble(TeamScore::score)));
      resultMap.computeIfPresent(roundResult.lastCycleWinner(), (k, v) -> v + 1);
      return resultMap;
    }
  }
}
