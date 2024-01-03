package services;

import cards.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cards.CardType.*;
import static org.junit.jupiter.api.Assertions.*;

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

  @Test
  public void testCountScoreOfWhenNoCardIsCounted() {
    var cards = List.of(
        Card.of(4, BASTOS),
        Card.of(5, BASTOS),
        Card.of(6, BASTOS),
        Card.of(7, BASTOS));
    var score = scoreService.countScore(cards);
    assertEquals(0d, score);
  }
}
