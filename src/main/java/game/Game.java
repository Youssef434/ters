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

public final class Game {
  private final ScoreService scoreService;

  public Game(ScoreService scoreService) {
    this.scoreService = scoreService;
  }

  public Map<Team, Double> startGame(String[] playersNames) {
    Map<Team, Double> overallScore = Map.of(Team.A, 0d, Team.B, 0d);
    int beginIndex = 0;

    try (var scanner = new Scanner(System.in)) {
      do {
        System.out.println("Round " + (beginIndex + 1));
        var currentRoundScore = startRound(playersNames, beginIndex, scanner).getScore();
        overallScore = merge(overallScore, currentRoundScore);

        if (currentRoundScore.containsValue(0d))
          return overallScore;
        beginIndex = (beginIndex + 1) % 4;
      } while (overallScore.values().stream().noneMatch(v -> v >= 21));
    }
    return overallScore;
  }

  private Round startRound(String[] playersNames, int beginIndex, Scanner scanner) {
    List<Cycle.CycleResult> cycleResults = new ArrayList<>();
    var players = distribute(playersNames);
    var gameRulesService = GameRulesService.create();

    for (int i = 0; i < 10; i++) {
      System.out.println("___________________________");
      System.out.println("Cycle " + (i + 1));
      var cycle = new Cycle(scanner, gameRulesService);
      var cycleResult = cycle.start(beginIndex, players);
      cycleResults.add(cycleResult);
      beginIndex = players.indexOf(cycleResult.getPlayer());System.out.println("Cycle winner : " + cycleResult.getPlayer());
    }
    return new Round(cycleResults, scoreService);
  }

  private List<Player> distribute(String[] names) {
    var cards = shuffleCards(generateCards()).toList();
    return IntStream.range(0, 4)
        .mapToObj(i -> new Player(
            currentTeam(i),
            names[i],
            new HashSet<>(cards.subList(i * 10, Math.min((i + 1) * 10, cards.size())))))
        .toList();
  }

  private Stream<Card> generateCards() {
    return IntStream.rangeClosed(1, 12)
        .unordered()
        .parallel()
        .filter(i -> i != 8 && i != 9)
        .boxed()
        .flatMap(number -> Arrays.stream(CardType.values()).parallel().map(type -> Card.of(number, type)));
  }

  private Stream<Card> shuffleCards(Stream<Card> cards) {
    var shuffledCards = cards.collect(Collectors.toList());
    Collections.shuffle(shuffledCards);
    return shuffledCards.stream();
  }

  private Team currentTeam(int index) {
    return index % 2 == 0 ? Team.A : Team.B;
  }

  private static Map<Team, Double> merge(Map<Team, Double> first, Map<Team, Double> second) {
    return Map.of(
        Team.A, first.get(Team.A) + second.get(Team.A),
        Team.B, first.get(Team.B) + second.get(Team.B));
  }
}
