package services;

import cards.Card;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import players.Player;

import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.*;
import java.util.stream.IntStream;

public class CardsServiceTest {
  private static List<Player> players;

  @BeforeAll
  public static void init() {
    players = CardsService.create().distribute(new String[] {"P1", "P2", "P3", "P4"});
  }

  @Test
  public void testIsDistributedCardsSizeEqualTo40() {
    long overallNumberOfCards = players.stream()
        .map(Player::cards)
        .mapToInt(Set::size)
        .sum();

    assertEquals(40, overallNumberOfCards);
  }

  @Test
  public void testNumberOfPlayersEqualTo4() {
    assertEquals(4, players.size());
  }

  @Test
  public void testNumberOfCardTypesAre4() {
    var overallCardTypesCount = players.stream()
        .map(Player::cards)
        .flatMap(Set::stream)
        .map(Card::getCardType)
        .distinct()
        .count();

    assertEquals(4, overallCardTypesCount);
  }

  @Test
  public void testNumberOfCardsInEachPlayerHandIs10() {
    var playersNumberOfCards = players.stream()
        .map(Player::cards)
        .map(Set::size)
        .mapToInt(c -> c)
        .toArray();

    assertArrayEquals(new int[] {10, 10, 10, 10}, playersNumberOfCards);
  }

  @Test
  public void testNumberOfTeamsIsExactlyTwo() {
    var teamsCount = players.stream()
        .map(Player::team)
        .distinct()
        .count();

    assertEquals(2, teamsCount);
  }

  @Test
  public void testEveryTeamHasExactly20Cards() {
    var teamsCardsCount = players.stream()
        .collect(groupingBy(
            Player::team,
            mapping(
                player -> player.cards().size(),
                summingInt(i -> i))))
        .values()
        .stream()
        .toList();

    assertIterableEquals(List.of(20, 20), teamsCardsCount);
  }

  @Test
  public void testEveryCardTypeHave10Cards() {
    var cardTypesCardCount = players.stream()
        .map(Player::cards)
        .flatMap(Set::stream)
        .collect(groupingBy(
            Card::getCardType,
            collectingAndThen(counting(), Long::intValue)))
        .values()
        .stream().toList();

    assertEquals(List.of(10, 10, 10, 10), cardTypesCardCount);
  }

  @Test
  public void testEveryCardNumberPresent4Times() {
    var cardsNumbersOccurrences = players.stream()
        .map(Player::cards)
        .flatMap(Set::stream)
        .collect(groupingBy(
            Card::getNumber,
            collectingAndThen(counting(), Long::intValue)))
        .values()
        .stream().toList();

    assertEquals(
        IntStream.rangeClosed(1, 10).map(unused -> 4).boxed().toList(),
        cardsNumbersOccurrences
    );
  }

  @Test
  public void testAllCardsAreFrom1To12Excluding8And9() {
    var presentCardsNumbers = players.stream()
        .map(Player::cards)
        .flatMap(Set::stream)
        .map(Card::getNumber)
        .distinct()
        .sorted()
        .toList();

    assertEquals(
        List.of(1, 2, 3, 4, 5, 6, 7, 10, 11, 12),
        presentCardsNumbers);
  }

  @Test
  public void testPlayerCannotHaveMoreThan6CardsOfTheSameType() {
    CardsService cardService = CardsService.create();
    String[] playersNames = new String[] {"P1", "P2", "P3", "P4"};

    boolean isValid = IntStream.iterate(0, i -> i < 10, i -> i + 1)
        .parallel()
        .mapToObj(unused -> playersNames)
        .map(cardService::distribute)
        .map(List::stream)
        .flatMap(players -> players
            .map(Player::cards)
            .map(Set::stream)
            .flatMap(s -> s
                .collect(groupingBy(Card::getCardType, collectingAndThen(counting(), Long::intValue)))
                .values()
                .stream()))
        .noneMatch(c -> c > 6);

    assertTrue(isValid);
  }

  @Test
  public void testPlayerCannotHaveLessThan4QuartersInHisHand() {
    ScoreService scoreService = ScoreService.create();
    GameEligibilityService gameEligibilityService = GameEligibilityService.create(scoreService);
    CardsService cardService = CardsService.create(gameEligibilityService);
    String[] playersNames = new String[] {"P1", "P2", "P3", "P4"};

    boolean isValid = IntStream.iterate(0, i -> i < 10, i -> i + 1)
        .parallel()
        .mapToObj(unused -> playersNames)
        .map(cardService::distribute)
        .map(List::stream)
        .flatMap(players -> players
            .map(Player::cards)
            .map(scoreService::countScore))
        .allMatch(c -> c >= 1.33);

    assertTrue(isValid);
  }
}
