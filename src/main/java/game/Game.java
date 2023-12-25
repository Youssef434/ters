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

public class Game {
  private final ScoreService scoreService;
  public Game(ScoreService scoreService) {
    this.scoreService = scoreService;
  }
  public List<TeamScore> startGame(String[] playersNames) {
    return null;
//    return IntStream.iterate(0, i -> (i + 1) % 4)
//        .limit(4)
//        .mapToObj(i -> startRound(playersNames, i))
//        .map(cycleResults -> new Round(cycleResults, scoreService))
//        .flatMap(Round::getScore)
//        .collect(Collectors.groupingByConcurrent(TeamScore::team, Collectors.summingDouble(TeamScore::score)));
  }
  private List<Cycle.CycleResult> startRound(String[] playersNames, int beginIndex) {
    List<Cycle.CycleResult> cycleResults = new ArrayList<>();
    var players = distribute(playersNames);
    var gameRulesService = GameRulesService.create();
    for (int i = 0; i < 10; i++) {
      try (var cycle = new Cycle(new Scanner(System.in), gameRulesService)) {
        var cycleResult = cycle.start(beginIndex, players);
        cycleResults.add(cycleResult);
        beginIndex = players.indexOf(cycleResult.getPlayer());
      }
    }
    return cycleResults;
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
}
