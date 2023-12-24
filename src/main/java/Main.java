import cards.Cycle;
import game.Game;
import players.Player;
import players.Team;
import services.GameEligibilityService;
import services.GameRulesService;
import services.ScoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {

    Cycle cycle = new Cycle(new Scanner(System.in), GameRulesService.create());
    Game game = new Game();
    System.out.println(cycle.start(0, game.distribute(new String[] {"P1", "P2", "P3", "P4"})));
//    var game = new Game();
//    var gameEligible = GameEligibilityService.create();
//    boolean res;
//    List<Player> players;
//    do {
//      players = game.distribute();
//      res = gameEligible.isEligible(players);
//    } while (res);
//
//    System.out.println();
  }
}
