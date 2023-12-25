import game.Game;
import services.ScoreService;

import java.util.Comparator;
import java.util.Map;


public class Main {
  public static void main(String[] args) {
    Game game = new Game(ScoreService.create());
    var gameResult = game.startGame(new String[] {"P1", "P2", "P3", "P4"});
    System.out.println("______________________________");
    System.out.println("Winner : " + gameResult.entrySet()
        .stream()
        .max(Comparator.comparingDouble(Map.Entry::getValue))
        .orElseThrow()
        .getKey());
    System.out.println("______________________________");
    System.out.println("Result : " + gameResult);
  }
}
