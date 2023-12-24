package players;

import cards.Card;
import cards.CardType;
import services.GameRulesService;

import java.util.Set;

public record Player(Team team, String name, Set<Card> cards, GameRulesService gameRulesService) {
  public Card play(int number, CardType cardType, CardType dominantCardType) {
    var playedCard = Card.of(number, cardType);

    if (!gameRulesService.isLegalPlay(this, playedCard, dominantCardType) && !cards.remove(playedCard))
      throw new IllegalArgumentException();
    return playedCard;
  }
}
