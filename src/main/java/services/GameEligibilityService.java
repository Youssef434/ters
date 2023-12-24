package services;

import cards.Card;
import game.Game;
import players.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@FunctionalInterface
public interface GameEligibilityService {
  boolean isEligible(List<Player> players);
  static GameEligibilityService create() {
    return new GameEligibilityServiceImpl(ScoreService.create());
  }
}

final class GameEligibilityServiceImpl implements GameEligibilityService {
  private final ScoreService scoreService;

  public GameEligibilityServiceImpl(ScoreService scoreService) {
    this.scoreService = scoreService;
  }

  @Override
  public boolean isEligible(List<Player> players) {
    return arePlayerScoresLegal(players) && areCardTypesLegal(players);
  }
  private boolean arePlayerScoresLegal(List<Player> players) {
    return players.stream()
        .map(scoreService::countScore)
        .allMatch(score -> score > 1.33);
  }
  private boolean areCardTypesLegal(List<Player> players) {
    return players.stream()
        .parallel()
        .map(Player::cards)
        .map(Set::stream)
        .map(cards -> cards.collect(Collectors.groupingBy(Card::getCardType, Collectors.counting())))
        .map(Map::values)
        .flatMap(Collection::stream)
        .allMatch(freq -> freq <= 6);
  }
}
