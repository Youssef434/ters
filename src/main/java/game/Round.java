package game;

import players.Team;
import services.ScoreService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Round(List<Cycle.CycleResult> cycleResults, ScoreService scoreService) {
  public Stream<TeamScore> getScore() {
    return cycleResults.stream()
        .map(scoreService::countCycleScore);
  }
}
