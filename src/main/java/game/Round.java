package game;

import players.Team;
import services.ScoreService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Round(List<Cycle.CycleResult> cycleResults, ScoreService scoreService) {
  public Map<Team, Double> getScore() {
    return cycleResults.stream()
        .map(scoreService::countCycleScore)
        .collect(Collectors.groupingBy(TeamScore::team, Collectors.summingDouble(TeamScore::score)));
  }
}
