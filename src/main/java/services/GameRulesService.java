package services;

import cards.Card;
import cards.CardType;
import players.Player;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;


public interface GameRulesService {
  default Set<Card> playableCards(Player player, CardType dominantCardType) {
    if (dominantCardType == null)
      return player.cards();
    var dominantTypeCards = player.cards()
        .stream()
        .filter(c -> c.getCardType() == dominantCardType)
        .collect(Collectors.toCollection(LinkedHashSet::new));
    return dominantTypeCards.isEmpty() ? player.cards() : dominantTypeCards;
  }
  static GameRulesService create() {
    return new GameRulesService() {};
  }
}
