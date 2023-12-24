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
  private final Scanner scanner;
  private final GameRulesService gameRulesService;

  public Cycle(Scanner scanner, GameRulesService gameRulesService) {
    this.scanner = scanner;
    this.gameRulesService = gameRulesService;
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

  public CycleResult start(int beginIndex, List<Player> players) {
    var orderedPlayers = orderPlayers(beginIndex, players);
    SortedSet<PlayedCard> playedCards = new TreeSet<>();
    CardType dominantCardType = null;

    for (var player : orderedPlayers) {
      int cardNumber = scanner.nextInt();
      CardType cardType = Enum.valueOf(CardType.class, scanner.next());

      if (player == orderedPlayers.get(0))
        dominantCardType = cardType;
      while (!gameRulesService.isLegalPlay(player, cardType, dominantCardType) ||
          !gameRulesService.isValidPlayedCard(player, cardNumber, cardType)) {
        cardNumber = scanner.nextInt();
        cardType = Enum.valueOf(CardType.class, scanner.next());
      }
      playedCards.add(new PlayedCard(player, Card.of(cardNumber, cardType), dominantCardType));
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

  @Override
  public void close() {
    scanner.close();
  }
}
