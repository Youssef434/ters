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
}
