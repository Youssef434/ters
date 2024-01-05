package services;

import cards.Card;
import players.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@FunctionalInterface
public interface GameEligibilityService {
  boolean isEligible(List<Player> players);
  static GameEligibilityService create(ScoreService scoreService) {
    return new GameEligibilityServiceImpl(scoreService);
  }
  static GameEligibilityService create() {
    return create(ScoreService.create());
  }
  final class GameEligibilityServiceImpl implements GameEligibilityService {
    private final ScoreService scoreService;

    private GameEligibilityServiceImpl(ScoreService scoreService) {
      this.scoreService = scoreService;
    }

    @Override
    public boolean isEligible(List<Player> players) {
      return isCardNumbersLegal(players) && isCardTypesLegal(players);
    }
    private boolean isCardNumbersLegal(List<Player> players) {
      return players.stream()
          .map(Player::cards)
          .map(scoreService::countScore)
          .allMatch(score -> score > 1.33);
    }

    private boolean isCardTypesLegal(List<Player> players) {
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
}
