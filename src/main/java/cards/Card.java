package cards;


import java.util.Comparator;
import java.util.StringJoiner;

import static shared.CardsComparing.numberDominanceOrder;

public final class Card implements Comparable<Card> {
  private final int number;
  private final CardType cardType;

  private Card(int number, CardType cardType) {
    this.number = number;
    this.cardType = cardType;
  }

  public static Card of(int number, CardType cardType) {
    if (number <= 0 || number > 12)
      throw new IllegalArgumentException(number + " is not a valid card number");
    return new Card(number, cardType);
  }

  public int getNumber() {
    return number;
  }

  public CardType getCardType() {
    return cardType;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Card.class.getSimpleName() + "[", "]")
        .add("number=" + number)
        .add("cardType=" + cardType)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Card card = (Card) o;

    if (number != card.number) return false;
    return cardType == card.cardType;
  }

  @Override
  public int hashCode() {
    int result = number;
    result = 31 * result + cardType.hashCode();
    return result;
  }

  @Override
  public int compareTo(Card c) {
    return Comparator
        .comparing(Card::getCardType)
        .thenComparing(cc -> numberDominanceOrder.indexOf(cc.number))
        .compare(this, c);
  }
}
