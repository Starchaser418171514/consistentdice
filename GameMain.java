package consistentdice;

import java.util.Scanner;

enum Mode {
  HUMAN,
  RANDOM,
  AI
}

public class GameMain {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    System.out.println("Welcome to EinStein würfelt nicht!");
    System.out.println("Select game mode:\n1. Human Player\n2. Random Player\n3. AI Player");

    int selection = 0;

    // Input validation for level selection
    while (selection <= 0 || selection > 3) {
      while (!input.hasNextInt()) {
        System.out.print("Select mode: ");
        input.next();
      }
      selection = input.nextInt();
      if (selection <= 0) {
        System.out.print("Select mode: ");
      }
    }
    Mode mode = Mode.values()[selection - 1];

    Player player;
    switch (mode) {
      case Mode.RANDOM:
        // Random Player
        player = new RandomPlayer();
        break;

      case Mode.AI:
        // AI Player
        player = new AIPlayer();
        break;

      case Mode.HUMAN:
      default:
        // Human Player
        System.out.println("Enter player name: ");
        player = new HumanPlayer(input.next());
        break;
    }

    GameLoader.main(args);
    GameLoader.printGameDetails(player);
    GameState.main(args);
    
    int numMoves = GameState.getMoveData().get(0).size();
    int targetPiece = GameState.getMoveData().get(1).get(0);
    boolean gameWon = false;

    //Game loop
    for (int i = 0; i < numMoves; i++) {
      System.out.println();
      if (GameState.isWinning(targetPiece)) {
        gameWon = true;
        break;
      } else if (GameState.isLosing(targetPiece)) {
        System.out.println("You captured the target piece!");
        break;
      }

      System.out.println("Goal: Get piece " + targetPiece + " to square 0");
      System.out.println("Move " + (i + 1) + ":");

      System.out.println("Upcoming dice: " + GameState.getMoveData().get(0).subList(i, numMoves));
      System.out.println("Current Positions: " + GameState.getMoveData().get(GameState.getMoveData().size() - 1));
      player.chooseMove(GameState.getMoveData().get(0).get(i));
    }

    if (gameWon) {
      System.out.println(player.playerName + " wins!");
    } else {
      System.out.println("Game over! No more moves left.");
    }

    input.close();
  }
}
