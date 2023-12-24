import game.Game;
import services.ScoreService;

public class Main {
  public static void main(String[] args) {
    var game = new Game();
    var scoreService = ScoreService.create();
    var playerOne = game.distribute().get(0);
    var playerOneScore = scoreService.countScore(playerOne);

    System.out.println();
  }
}
