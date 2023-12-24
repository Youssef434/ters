package cards;

import players.Player;
import players.Team;
import services.GameRulesService;

import java.util.*;
import java.util.stream.IntStream;

public final class Cycle implements AutoCloseable {
  private record PlayedCard(Player player, Card card, CardType dominantCardType) implements Comparable<PlayedCard> {
    private static final List<Integer> numberDominanceOrder = List.of(3, 2, 1, 12, 11, 10, 4, 5, 6, 7);
    @Override
    public int compareTo(PlayedCard playedCard) {
      if (card.getCardType() == dominantCardType && playedCard.card().getCardType() != dominantCardType)
        return 1;
      if (card.getCardType() != dominantCardType && playedCard.card().getCardType() == dominantCardType)
        return -1;
      return Integer.compare(
          numberDominanceOrder.indexOf(playedCard.card().getNumber()),
          numberDominanceOrder.indexOf(this.card.getNumber()));
    }
  }
  public static class CycleResult {
    private final Team team;
    private final List<Card> cards;

    private CycleResult(Team team, List<Card> cards) {
      this.team = team;
      this.cards = cards;
    }

    public static CycleResult of(SortedSet<PlayedCard> playedCards) {
      var winingTeam = playedCards.first().player.team();
      return new CycleResult(winingTeam, playedCards.stream()
          .map(PlayedCard::card)
          .toList());
    }

    public Team getTeam() {
      return team;
    }

    public List<Card> getCards() {
      return cards;
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
      System.out.println(player.name() + " turn.");
      Card createdCard;

      do {
        createdCard = createCardFromUserInput();
        if (createdCard == null)
          continue;
        if (player == orderedPlayers.get(0))
          dominantCardType = createdCard.getCardType();
      } while (createdCard == null || !gameRulesService.isLegalPlay(player, createdCard.getCardType(), dominantCardType));

      playedCards.add(new PlayedCard(player, createdCard, dominantCardType));
    }
    return CycleResult.of(playedCards);
  }

  private List<Player> orderPlayers(int beginIndex, List<Player> players) {
    return IntStream
        .iterate(beginIndex, i -> (i + 1) % 4)
        .skip(1)
        .limit(4)
        .mapToObj(players::get)
        .toList();
  }

  private Card createCardFromUserInput() {
    System.out.print("provide the card's number : ");
    int cardNumber = scanner.nextInt();
    System.out.print("provide the card's type : ");
    CardType cardType = Enum.valueOf(CardType.class, scanner.next());
    try {
      return Card.of(cardNumber, cardType);
    } catch (Exception unused) {
      return null;
    }
  }

  @Override
  public void close() {
    scanner.close();
  }
}
