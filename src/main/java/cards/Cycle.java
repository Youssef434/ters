package cards;

import players.Player;
import players.Team;

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


  public Cycle(Scanner scanner) {
    this.scanner = scanner;
  }

  public CycleResult start(int beginIndex, List<Player> players) {
    var orderedPlayers = orderPlayers(beginIndex, players);
    SortedSet<PlayedCard> playedCards = new TreeSet<>();
    CardType dominantCardType = null;

    for (var player : orderedPlayers) {
      var createdCard = createCardFromUserInput(player);
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

  private Card createCardFromUserInput(Player player) {
    System.out.println(player.name() + " turn.");
    System.out.println("Your playable cards : " + player.cards());
    System.out.print("provide the card's number : ");
    int cardNumber = scanner.nextInt();
    System.out.print("provide the card's type : ");
    var cardType = Enum.valueOf(CardType.class, scanner.next());
    return player.play(cardNumber, cardType);
  }
  @Override
  public void close() {
    scanner.close();
  }
}
