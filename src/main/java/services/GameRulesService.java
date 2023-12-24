package services;

import cards.CardType;
import players.Player;


public interface GameRulesService {
  default boolean isLegalPlay(Player player, CardType cardType, CardType dominantCardType) {
    return cardType != dominantCardType && player.cards().stream().anyMatch(c -> c.getCardType() == dominantCardType);
  }

  static GameRulesService create() {
    return new GameRulesService() {};
  }
}
