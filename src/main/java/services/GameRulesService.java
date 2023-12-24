package services;

import cards.Card;
import cards.CardType;
import players.Player;


public interface GameRulesService {
  default boolean isLegalPlay(Player player, Card card, CardType dominantCardType) {
    if (dominantCardType == null || card.getCardType() == dominantCardType)
      return true;
    return playerDoesntHaveDominantTypeCard(player, dominantCardType);
  }
  private boolean playerDoesntHaveDominantTypeCard(Player player, CardType dominantCardType) {
    return player.cards()
        .stream()
        .noneMatch(c -> c.getCardType() == dominantCardType);
  }

  static GameRulesService create() {
    return new GameRulesService() {};
  }
}
