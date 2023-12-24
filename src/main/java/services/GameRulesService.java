package services;

import cards.CardType;
import players.Player;


public interface GameRulesService {
  default boolean isLegalPlay(Player player, CardType cardType, CardType dominantCardType) {
    return cardType != dominantCardType && player.cards().stream().anyMatch(c -> c.getCardType() == dominantCardType);
  }

  default boolean isValidPlayedCard(Player player, int number, CardType cardType) {
    try {
      player.play(number, cardType);
      return true;
    } catch (Exception unused) {
      return false;
    }
  }
  static GameRulesService create() {
    return new GameRulesService() {};
  }
}
