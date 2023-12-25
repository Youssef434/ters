import game.Game;
import services.ScoreService;


public class Main {
  public static void main(String[] args) {
    Game game = new Game(ScoreService.create());
    System.out.println(game.startGame(new String[] {"P1", "P2", "P3", "P4"}));
  }
}
