package players;

import cards.Card;
import cards.CardType;

import java.util.Set;
import java.util.StringJoiner;

public record Player(Team team, String name, Set<Card> cards) {
  public Card play(int number, CardType cardType) {
    var playedCard = Card.of(number, cardType);
    cards.remove(playedCard);
    return playedCard;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
        .add("team=" + team)
        .add("name='" + name + "'")
        .toString();
  }

  public static Player of(Team team, String name, Set<Card> cards) {
    return new Player(team, name, cards);
  }
}
