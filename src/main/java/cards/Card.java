package cards;

public final class Card {
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
}
