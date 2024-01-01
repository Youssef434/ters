package game;

import players.Player;
import players.Team;
import services.GameRulesService;
import services.ScoreService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public final class Round implements Playable {
  private final GameRulesService gameRulesService;
  private final ScoreService scoreService;
  private final Scanner scanner;

  private Round(GameRulesService gameRulesService, ScoreService scoreService, Scanner scanner) {
    this.gameRulesService = gameRulesService;
    this.scoreService = scoreService;
    this.scanner = scanner;
  }

  public record RoundResult(List<Cycle.CycleResult> cycleResults, ScoreService scoreService, Team lastCycleWinner) implements Result {
    @Override
    public Map<Team, Double> get() {
      return scoreService.getRoundScore(this);
    }

    public static RoundResult from(List<Cycle.CycleResult> cycleResults, ScoreService scoreService, Team lastCycleWinner) {
      return new RoundResult(cycleResults, scoreService, lastCycleWinner);
    }
  }

  @Override
  public RoundResult start(int beginIndex, List<Player> players) {
    return start(0, players, beginIndex, Stream.empty(), null);
  }

  private RoundResult start(int currentCycle, List<Player> players, int beginIndex, Stream<Cycle.CycleResult> cycleResults, Team lastCycleWinner) {
    if (currentCycle >= 10)
      return RoundResult.from(cycleResults.toList(), scoreService, lastCycleWinner);
    System.out.println("___________________________");
    System.out.println("Cycle " + (currentCycle + 1));
    var cycle = Cycle.from(scanner, gameRulesService, scoreService);
    var cycleResult = cycle.start(beginIndex, players);
    return start(currentCycle + 1, players, players.indexOf(cycleResult.getPlayer()),
        Stream.concat(cycleResults, Stream.of(cycleResult)), cycleResult.getPlayer().team());
  }

  public static Round from(GameRulesService gameRulesService, ScoreService scoreService, Scanner scanner) {
    return new Round(gameRulesService, scoreService, scanner);
  }
}
