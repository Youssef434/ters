package game;

import players.Team;
import services.CardsService;
import services.GameRulesService;
import services.ScoreService;

import java.util.*;

public final class Game {
  private final ScoreService scoreService;
  private final CardsService cardsService;

  public Game(ScoreService scoreService, CardsService cardsService) {
    this.scoreService = scoreService;
    this.cardsService = cardsService;
  }

  public Map<Team, Integer> start(String[] playersNames) {
    try (var scanner = new Scanner(System.in)) {
      return start(playersNames, 0, Map.of(Team.A, 0, Team.B, 0), scanner);
    }
  }

  private Map<Team, Integer> start(String[] playersNames, int beginIndex, Map<Team, Integer> accumulatedScore, Scanner scanner) {
    if (accumulatedScore.values().stream().anyMatch(v -> v >= 21))
      return accumulatedScore;
    System.out.println("Round " + (beginIndex + 1));
    var round = Round.from(GameRulesService.create(), scoreService, scanner);
    var roundScore = round.start(beginIndex, cardsService.distribute(playersNames)).get();
    System.out.println("Round result : " + roundScore);

    if (roundScore.containsValue(0d)) {
      int teamAScore = roundScore.get(Team.A) != 0d ? 21 : 0;
      int teamBScore = roundScore.get(Team.B) != 0d ? 21 : 0;
      return Map.of(Team.A, teamAScore, Team.B, teamBScore);
    }
    return start(playersNames, (beginIndex + 1) % 4, merge(accumulatedScore, roundScore), scanner);
  }

  private static Map<Team, Integer> merge(Map<Team, Integer> first, Map<Team, Double> second) {
    return Map.of(Team.A, Math.min(11, (int) Math.ceil(first.get(Team.A) + second.get(Team.A))),
        Team.B, Math.min(11, (int) Math.ceil(first.get(Team.B) + second.get(Team.B))));
  }
}
