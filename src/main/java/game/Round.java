package game;

import players.Player;
import players.Team;
import services.GameRulesService;
import services.ScoreService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public record Round(GameRulesService gameRulesService, ScoreService scoreService, Scanner scanner) implements Playable {
  public record RoundResult(List<Cycle.CycleResult> cycleResults, ScoreService scoreService, Team lastCycleWinner) implements Result {
    @Override
    public Map<Team, Double> get() {
      return scoreService.getRoundScore(this);
    }
  }

  @Override
  public RoundResult start(int beginIndex, List<Player> players) {
    return start(0, players, beginIndex, Stream.empty(), null);
  }

  private RoundResult start(int currentCycle, List<Player> players, int beginIndex, Stream<Cycle.CycleResult> cycleResults, Team lastCycleWinner) {
    if (currentCycle >= 10)
      return new RoundResult(cycleResults.toList(), scoreService, lastCycleWinner);
    System.out.println("___________________________");
    System.out.println("Cycle " + (currentCycle + 1));
    var cycle = Cycle.of(scanner, gameRulesService, scoreService);
    var cycleResult = cycle.start(beginIndex, players);
    return start(currentCycle + 1, players, players.indexOf(cycleResult.getPlayer()),
        Stream.concat(cycleResults, Stream.of(cycleResult)), cycleResult.getPlayer().team());
  }
}
