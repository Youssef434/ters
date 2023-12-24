import game.Game;
import players.Player;
import services.GameEligibilityService;
import services.ScoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {
    System.out.println(IntStream.iterate(3, i -> (i + 1) % 4)
        .limit(4)
        .boxed().toList());
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
