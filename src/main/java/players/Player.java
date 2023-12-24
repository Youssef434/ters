package players;

import cards.Card;
import cards.CardType;

import java.util.Set;

public record Player(Team team, Set<Card> cards) {
  public void play(int number, CardType cardType) {
    var playedCard = Card.of(number, cardType);

    if (!cards.contains(playedCard))
      throw new IllegalArgumentException("You can't play a card that you don't have in your hand");
    cards.remove(playedCard);
  }
}
