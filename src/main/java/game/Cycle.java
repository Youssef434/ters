package game;

import cards.Card;
import cards.CardType;
import players.Player;
import services.GameRulesService;

import java.util.*;
import java.util.stream.IntStream;

public final class Cycle {
  private record PlayedCard(Player player, Card card, CardType dominantCardType) implements Comparable<PlayedCard> {
    private static final List<Integer> numberDominanceOrder = List.of(3, 2, 1, 12, 11, 10, 4, 5, 6, 7);

    @Override
    public int compareTo(PlayedCard playedCard) {
      if (playedCard.card().getCardType() != dominantCardType)
        return Integer.MIN_VALUE + 1;
      return Integer.compare(numberDominanceOrder.indexOf(card().getNumber()),
          numberDominanceOrder.indexOf(playedCard.card().getNumber()));
    }
  }
  public static class CycleResult {
    private final Player player;
    private final List<Card> cards;

    private CycleResult(Player player, List<Card> cards) {
      this.player = player;
      this.cards = cards;
    }

    public static CycleResult of(SortedSet<PlayedCard> playedCards) {
      var winingPlayer = playedCards.first().player;
      return new CycleResult(winingPlayer, playedCards.stream()
          .map(PlayedCard::card)
          .toList());
    }

    public Player getPlayer() {
      return player;
    }

    public List<Card> getCards() {
      return cards;
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", CycleResult.class.getSimpleName() + "[", "]")
          .add("player=" + player)
          .add("cards=" + cards)
          .toString();
    }
  }
  private final Scanner scanner;
  private final GameRulesService gameRulesService;

  public Cycle(Scanner scanner, GameRulesService gameRulesService) {
    this.scanner = scanner;
    this.gameRulesService = gameRulesService;
  }

  public CycleResult start(int beginIndex, List<Player> players) {
    var orderedPlayers = orderPlayers(beginIndex, players);
    SortedSet<PlayedCard> playedCards = new TreeSet<>();
    CardType dominantCardType = null;

    for (var player : orderedPlayers) {
      var createdCard = createCardFromUserInput(player, dominantCardType);
      if (dominantCardType == null)
        dominantCardType = createdCard.getCardType();
      playedCards.add(new PlayedCard(player, createdCard, dominantCardType));
    }
    return CycleResult.of(playedCards);
  }

  private List<Player> orderPlayers(int beginIndex, List<Player> players) {
    return IntStream
        .iterate(beginIndex, i -> (i + 1) % 4)
        .limit(4)
        .mapToObj(players::get)
        .toList();
  }

  private Card createCardFromUserInput(Player player, CardType dominantCardType) {
    System.out.println("__________________________________________");
    System.out.println(player.name() + " turn.");
    System.out.println("Your playable cards : " + gameRulesService.playableCards(player, dominantCardType));
    System.out.print("provide the card's number : ");
    int cardNumber = scanner.nextInt();
    System.out.print("provide the card's type : ");
    var cardType = Enum.valueOf(CardType.class, scanner.next());
    return player.play(cardNumber, cardType);
  }
}
