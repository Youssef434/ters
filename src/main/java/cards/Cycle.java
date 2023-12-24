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
      Card createdCard;

      do {
        createdCard = createCardFromUserInput(player, dominantCardType);
        if (createdCard != null && player == orderedPlayers.get(0))
          dominantCardType = createdCard.getCardType();
      } while (createdCard == null);
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

  private Card createCardFromUserInput(Player player, CardType dominantCardType) {
    try {
      System.out.println(player.name() + " turn.");
      System.out.println("Your hand : " + player.cards());
      System.out.print("provide the card's number : ");
      int cardNumber = scanner.nextInt();
      System.out.print("provide the card's type : ");
      CardType cardType = Enum.valueOf(CardType.class, scanner.next());
      if (gameRulesService.isLegalPlay(player, cardType, dominantCardType))
        return Card.of(cardNumber, cardType);
      throw new IllegalArgumentException();
    } catch (Exception unused) {
      System.out.println("The card you want to play is either invalid or you are not allowed to play it.");
      return null;
    }
  }

  @Override
  public void close() {
    scanner.close();
  }
}
