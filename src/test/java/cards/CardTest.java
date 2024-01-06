package cards;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cards.CardType.*;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
  @Test
  public void testCardIsInvalid() {
    assertThrows(IllegalArgumentException.class, () -> Card.of(100, BASTOS));
  }

  @RepeatedTest(10)
  public void testHandsAreSortedByType() {
    List<CardType> cardTypes = Arrays.stream(values()).collect(Collectors.toList());
    Collections.shuffle(cardTypes);
    var cards = cardTypes.stream()
        .map(cardType -> Card.of(10, cardType))
        .collect(Collectors.toCollection(TreeSet::new));

    assertEquals(
        Set.of(Card.of(10, BASTOS), Card.of(10, OROS), Card.of(10, COPAS), Card.of(10, ESPADAS)),
        cards);
  }

  @RepeatedTest(10)
  public void testHandsAreSortedByNumber() {
    List<Integer> cardNumbers = IntStream.rangeClosed(1, 12).filter(i -> i != 8 && i != 9).boxed().collect(Collectors.toList());
    Collections.shuffle(cardNumbers);
    var cards = cardNumbers.stream()
        .map(cardNumber -> Card.of(cardNumber, BASTOS))
        .collect(Collectors.toCollection(TreeSet::new));

    assertEquals(Set.of(Card.of(3, BASTOS),
        Card.of(2, BASTOS),
        Card.of(1, BASTOS),
        Card.of(12, BASTOS),
        Card.of(11, BASTOS),
        Card.of(10, BASTOS),
        Card.of(4, BASTOS),
        Card.of(5, BASTOS),
        Card.of(6, BASTOS),
        Card.of(7, BASTOS)), cards);
  }

  @Test
  public void testHandsSortedByCardTypeAndThenSortedByNumber() {
    List<Integer> cardNumbers = IntStream.rangeClosed(1, 12).filter(i -> i != 8 && i != 9).boxed().toList();
    List<CardType> cardTypes = Arrays.asList(ESPADAS, OROS, BASTOS, COPAS, COPAS, ESPADAS, OROS, ESPADAS, BASTOS, OROS);

    var cards = IntStream.range(0, 10)
        .mapToObj(i -> Card.of(cardNumbers.get(i), cardTypes.get(i)))
        .collect(Collectors.toCollection(TreeSet::new));

    assertEquals(Set.of(Card.of(3, BASTOS),
        Card.of(11, BASTOS),
        Card.of(2, OROS),
        Card.of(12, OROS),
        Card.of(7, OROS),
        Card.of(4, COPAS),
        Card.of(5, COPAS),
        Card.of(1, ESPADAS),
        Card.of(10, ESPADAS),
        Card.of(6, ESPADAS)), cards);
  }
}
