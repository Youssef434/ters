package game;

import players.Player;
import players.Team;
import services.CardsService;
import services.GameRulesService;
import services.ScoreService;

import java.util.*;
import java.util.stream.Stream;

public final class Game {
  private final ScoreService scoreService;
  private final CardsService cardsService;

  public Game(ScoreService scoreService, CardsService cardsService) {
    this.scoreService = scoreService;
    this.cardsService = cardsService;
  }

  public Map<Team, Integer> startGame(String[] playersNames) {
    try (var scanner = new Scanner(System.in)) {
      return startGame(playersNames, 0, Map.of(Team.A, 0, Team.B, 0), scanner);
    }
  }

  private Map<Team, Integer> startGame(String[] playersNames, int beginIndex, Map<Team, Integer> accumulatedScore, Scanner scanner) {
    if (accumulatedScore.values().stream().anyMatch(v -> v >= 21))
      return accumulatedScore;

    System.out.println("Round " + (beginIndex + 1));
    var currentRoundScore = startRound(playersNames, beginIndex, scanner).getScore();
    System.out.println("Round winner : " + currentRoundScore.entrySet()
        .stream()
        .max(Comparator.comparingDouble(Map.Entry::getValue))
        .orElseThrow()
        .getKey());

    if (currentRoundScore.containsValue(0d)) {
      int teamAScore = currentRoundScore.get(Team.A) != 0d ? 21 : 0;
      int teamBScore = currentRoundScore.get(Team.B) != 0d ? 21 : 0;
      return Map.of(Team.A, teamAScore, Team.B, teamBScore);
    }
    return startGame(playersNames, (beginIndex + 1) % 4, merge(accumulatedScore, currentRoundScore), scanner);
  }

  private Round startRound(String[] playersNames, int beginIndex, Scanner scanner) {
    return startRound(0, cardsService.distribute(playersNames), beginIndex, Stream.empty(), null, GameRulesService.create(), scanner);
  }

  private Round startRound(int currentCycle, List<Player> players, int beginIndex, Stream<Cycle.CycleResult> cycleResults, Team lastCycleWinner, GameRulesService gameRulesService, Scanner scanner) {
    if (currentCycle >= 10)
      return new Round(cycleResults.toList(), scoreService, lastCycleWinner);
    System.out.println("___________________________");
    System.out.println("Cycle " + (currentCycle + 1));
    var cycle = Cycle.of(scanner, gameRulesService);
    var cycleResult = cycle.start(beginIndex, players);
    System.out.println("Cycle winner : " + cycleResult.getPlayer());
    return startRound(currentCycle + 1, players, players.indexOf(cycleResult.getPlayer()),
        Stream.concat(cycleResults, Stream.of(cycleResult)), cycleResult.getPlayer().team(), gameRulesService, scanner);
  }

  private static Map<Team, Integer> merge(Map<Team, Integer> first, Map<Team, Double> second) {
    return Map.of(Team.A, Math.min(11, (int) Math.ceil(first.get(Team.A) + second.get(Team.A))),
        Team.B, Math.min(11, (int) Math.ceil(first.get(Team.B) + second.get(Team.B))));
  }
}
