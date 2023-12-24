package services;

import cards.Card;
import cards.CardType;
import players.Player;


public interface GameRulesService {
  default boolean isLegalPlay(Player player, Card card, CardType dominantCardType) {
    if (dominantCardType == null)
      return doesPlayerHaveCard(player, card);
    var cardType = card.getCardType();
    return ;
  }

  private boolean doesPlayerHaveCard(Player player, Card card) {
    return player.cards().stream().anyMatch(c -> c.equals(card));
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
