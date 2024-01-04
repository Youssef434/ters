package game;

import cards.Card;
import cards.CardType;
import players.Player;
import players.Team;
import services.GameRulesService;
import services.ScoreService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static shared.CardsComparing.numberDominanceOrder;

public final class Cycle implements Playable {
  private record PlayedCard(Player player, Card card, CardType dominantCardType) implements Comparable<PlayedCard> {
    @Override
    public int compareTo(PlayedCard playedCard) {
      return Comparator
          .comparing((PlayedCard pc) -> pc.card().getCardType() != dominantCardType)
          .thenComparing(pc -> numberDominanceOrder.indexOf(pc.card().getNumber()))
          .compare(this, playedCard);
    }
  }
  public static final class CycleResult implements Result {
    private final Player winningPlayer;
    private final List<Card> cards;
    private final ScoreService scoreService;

    private CycleResult(Player winningPlayer, List<Card> cards, ScoreService scoreService) {
      this.winningPlayer = winningPlayer;
      this.cards = cards;
      this.scoreService = scoreService;
    }

    public static CycleResult from(SortedSet<PlayedCard> playedCards, ScoreService scoreService) {
      var winingPlayer = playedCards.first().player;
      return new CycleResult(winingPlayer, playedCards.stream()
          .map(PlayedCard::card)
          .toList(), scoreService);
    }

    public Player getWinningPlayer() {
      return winningPlayer;
    }

    public List<Card> getCards() {
      return cards;
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", CycleResult.class.getSimpleName() + "[", "]")
          .add("player=" + winningPlayer)
          .add("cards=" + cards)
          .toString();
    }

    @Override
    public Map<Team, Double> get() {
      return scoreService.getCycleScore(this);
    }
  }
  private final Scanner scanner;
  private final GameRulesService gameRulesService;
  private final ScoreService scoreService;

  private Cycle(Scanner scanner, GameRulesService gameRulesService, ScoreService scoreService) {
    this.scanner = scanner;
    this.gameRulesService = gameRulesService;
    this.scoreService = scoreService;
  }

  public CycleResult start(int beginIndex, List<Player> players) {
    return start(orderPlayers(beginIndex, players), 0, null, new TreeSet<>());
  }

  private CycleResult start(List<Player> players, int currentPlayerIndex, CardType dominantCardType, SortedSet<PlayedCard> playedCards) {
    if (currentPlayerIndex >= players.size())
      return CycleResult.from(playedCards, scoreService);
    var player = players.get(currentPlayerIndex);
    var createdCard = createCardFromUserInput(player, dominantCardType);
    var newDominantCardType = dominantCardType == null ? createdCard.getCardType() : dominantCardType;
    var newPlayedCards = Stream.concat(playedCards.stream(),
            Stream.of(new PlayedCard(player, createdCard, newDominantCardType)))
        .collect(Collectors.toCollection(TreeSet::new));
    System.out.println("_______________________________________");
    System.out.println("table : " + newPlayedCards.stream().map(PlayedCard::card).toList());
    return start(players, currentPlayerIndex + 1, newDominantCardType, newPlayedCards);
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

  public static Cycle from(Scanner scanner, GameRulesService gameRulesService, ScoreService scoreService) {
    return new Cycle(scanner, gameRulesService, scoreService);
  }
}
