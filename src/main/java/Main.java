import game.Game;
import services.CardsService;
import services.ScoreService;

public final class Main {
  public static void main(String[] args) {
    Game game = new Game(ScoreService.create(), CardsService.create());
    var gameResult = game.start(new String[] {"P1", "P2", "P3", "P4"});
    System.out.println("______________________________");
    System.out.println("Game Result : " + gameResult);
  }
}
