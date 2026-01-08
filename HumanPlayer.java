package consistentdice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HumanPlayer extends Player {

    private Scanner scanner;

    public HumanPlayer() {
        scanner = new Scanner(System.in);
    }

    @Override
    public int[] chooseMove(int diceValue) {
        Map<Integer, List<Integer>> possibleMoves = GameState.generatePossibleMoves(diceValue);
        if (possibleMoves.isEmpty()) {
            System.out.println("No available moves for dice value " + diceValue);
            return null;
        }

        System.out.println("\nDice rolled: " + diceValue);
        System.out.println("Available moves:");

        int count = 1;
        Map<Integer, int[]> moveMap = new HashMap<>();

        for (Map.Entry<Integer, List<Integer>> entry : possibleMoves.entrySet()) {
            int piece = entry.getKey();
            List<Integer> moves = entry.getValue();
            for (Integer target : moves) {
                System.out.println(count + ". Move piece " + piece + " to square " + target);
                moveMap.put(count, new int[]{piece, target});
                count++;
            }
        }

        int choice = 0;
        while (true) {
            System.out.print("Enter the number of your chosen move: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (moveMap.containsKey(choice)) {
                    break;
                }
            }
            else {
                scanner.next(); 
            }
            System.out.println("Invalid choice, please try again.");
        }

        int[] selectedMove = moveMap.get(choice);

        super.logMove(selectedMove[0], selectedMove[1]);

        return selectedMove;
    }
}

