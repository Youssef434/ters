package players;

import cards.Card;
import cards.CardType;

import java.util.Set;

public record Player(Team team, String name, Set<Card> cards) {
  public Card play(int number, CardType cardType) {
    var playedCard = Card.of(number, cardType);
    cards.remove(playedCard);
    return playedCard;
  }
}
