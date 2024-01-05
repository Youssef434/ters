package services;

import cards.Card;
import game.Cycle;
import game.Round;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import players.Player;
import players.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cards.CardType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScoreServiceTest {
  private static ScoreService scoreService;

  @BeforeAll
  public static void init() {
    scoreService = ScoreService.create();
  }

  @Test
  public void testCountScoreOfEmptyCardsIsZero() {
    assertEquals(0d, scoreService.countScore(List.of()));
  }

  @ParameterizedTest
  @CsvSource({
      "1-3-4-5,1.34",
      "3-2-1-12,2.02",
      "1-1-1-1,4",
      "1-1-3-4,2.34",
      "3-7-2-7,.68",
      "4-5-6-7,0",
      "12-7-4-5,.34",
      "1-4-5-6,1",
      "3-2-2-7,1.02",
      "3-2-1-4,1.6800000000000002"})
  public void testCountScore(String cardNumbers, String expectedScore) {
    var cards = Arrays.stream((cardNumbers == null ? "" : cardNumbers).split("-"))
        .map(Integer::parseInt)
        .map(cardNum -> Card.of(cardNum, OROS))
        .toList();

    var score = scoreService.countScore(cards);

    assertEquals(score, Double.parseDouble(expectedScore));
  }

  @Test
  public void testMergeTwoEmptyScores() {
    Executable exec = () -> scoreService.merge(Map.of(), Map.of());
    assertThrows(NullPointerException.class, exec);
  }

  @Test
  public void testMergeOneOfTheScoresContainsMissingTeam() {
    Executable exec = () -> scoreService.merge(Map.of(Team.A, 3), Map.of());
    assertThrows(NullPointerException.class, exec);
  }

  @Test
  public void testMergeTwoInts() {
    var firstScore = Map.of(Team.A, 10, Team.B, 1);
    var secondScore = Map.of(Team.A, 1, Team.B, 3);

    assertEquals(Map.of(Team.A, 11, Team.B, 4), scoreService.merge(firstScore, secondScore));
  }

  @Test
  public void testMergeOneIntAndOneDouble() {
    var firstScore = Map.of(Team.A, 3, Team.B, 1);
    var secondScore = Map.of(Team.A, 6.5, Team.B, 3.5);

    assertEquals(Map.of(Team.A, 9, Team.B, 4), scoreService.merge(firstScore, secondScore));
  }

  @Test
  public void testGetCycleScoreWhenResultIsDouble() {
    var cycleResult = createCycleResult(List.of(
        Card.of(1, BASTOS),
        Card.of(3, BASTOS),
        Card.of(4, OROS),
        Card.of(1, OROS)));

    var cycleScore = scoreService.getCycleScore(cycleResult);

    assertEquals(Map.of(Team.A, 2.34d, Team.B, 0d), cycleScore);
  }

  @Test
  public void testGetCycleScoreWhenResultIsInt() {
    var cycleResult = createCycleResult(List.of(
        Card.of(1, BASTOS),
        Card.of(7, BASTOS),
        Card.of(4, OROS),
        Card.of(1, OROS)));

    var cycleScore = scoreService.getCycleScore(cycleResult);

    assertEquals(Map.of(Team.A, 2d, Team.B, 0d), cycleScore);
  }

  @Test
  public void testGetCycleScoreWhenResultIsZero() {
    var cycleResult = createCycleResult(List.of(
        Card.of(7, BASTOS),
        Card.of(5, BASTOS),
        Card.of(6, OROS),
        Card.of(4, OROS)));

    var cycleScore = scoreService.getCycleScore(cycleResult);

    assertEquals(Map.of(Team.A, 0d, Team.B, 0d), cycleScore);
  }

  @Test
  public void testGetRoundResult() {
    var roundResult = createRoundResult(
        List.of(createCycleResult((List.of(Card.of(7, BASTOS), Card.of(7, BASTOS), Card.of(7, OROS), Card.of(7, OROS)))),
            createCycleResult((List.of(Card.of(6, BASTOS), Card.of(6, BASTOS), Card.of(6, OROS), Card.of(6, OROS)))),
            createCycleResult((List.of(Card.of(5, BASTOS), Card.of(5, BASTOS), Card.of(5, OROS), Card.of(5, OROS)))),
            createCycleResult((List.of(Card.of(4, BASTOS), Card.of(4, BASTOS), Card.of(4, OROS), Card.of(4, OROS)))),
            createCycleResult((List.of(Card.of(3, BASTOS), Card.of(3, BASTOS), Card.of(3, OROS), Card.of(3, OROS)))),
            createCycleResult((List.of(Card.of(2, BASTOS), Card.of(2, BASTOS), Card.of(2, OROS), Card.of(2, OROS)))),
            createCycleResult((List.of(Card.of(1, BASTOS), Card.of(1, BASTOS), Card.of(1, OROS), Card.of(1, OROS)))),
            createCycleResult((List.of(Card.of(10, BASTOS), Card.of(10, BASTOS), Card.of(10, OROS), Card.of(10, OROS)))),
            createCycleResult((List.of(Card.of(11, BASTOS), Card.of(11, BASTOS), Card.of(11, OROS), Card.of(11, OROS)))),
            createCycleResult((List.of(Card.of(12, BASTOS), Card.of(12, BASTOS), Card.of(12, OROS), Card.of(12, OROS)))))
    );

    var roundScore = scoreService.getRoundScore(roundResult);

    assertEquals(Map.of(Team.A, 11, Team.B, 0), roundScore);
  }

  private static Cycle.CycleResult createCycleResult(List<Card> cards) {
    var cycleResult = mock(Cycle.CycleResult.class);
    when(cycleResult.getWinningPlayer()).thenReturn(Player.of(Team.A, "", Set.of()));
    when(cycleResult.getCards()).thenReturn(cards);
    return cycleResult;
  }
  private static Round.RoundResult createRoundResult(List<Cycle.CycleResult> cycleResults) {
    var roundResult = mock(Round.RoundResult.class);
    when(roundResult.cycleResults()).thenReturn(cycleResults);
    when(roundResult.lastCycleWinner()).thenReturn(Team.A);
    return roundResult;
  }
}
