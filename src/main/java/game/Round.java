package game;

import players.Team;
import services.ScoreService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Round(List<Cycle.CycleResult> cycleResults, ScoreService scoreService, Team lastCycleWinner) {
  public Map<Team, Double> getScore() {
    var scoreMap = cycleResults.stream()
        .map(scoreService::countCycleScore)
        .collect(Collectors.groupingBy(TeamScore::team, Collectors.summingDouble(TeamScore::score)));
    scoreMap.compute(lastCycleWinner, (k, v) -> v + 1);
    return scoreMap;
  }
}
