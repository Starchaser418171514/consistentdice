package consistentdice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomPlayer extends Player {

    private Random rand = new Random();

    public RandomPlayer() {
        super("Random Player");
    }

    @Override
    public int[] chooseMove(int diceValue) {

        // get possible moves for dice roll
        Map<Integer, List<Integer>> possibleMoves = GameState.generatePossibleMoves(diceValue);
        // each int[] is {pieceNumber, newPosition}

        if (possibleMoves.isEmpty()) {
            return null;
        }

        List<int[]> moveList = new ArrayList<>(possibleMoves.size());
        for (Map.Entry<Integer, List<Integer>> entry : possibleMoves.entrySet()) {
            int piece = entry.getKey();
            List<Integer> moves = entry.getValue();
            for (Integer target : moves) {
                moveList.add(new int[]{piece, target});
            }
        }

        int[] chosenMove = moveList.get(rand.nextInt(moveList.size())); //randomly choose a move

        int piece = chosenMove[0];
        int newPos = chosenMove[1];

        logMove(piece, newPos); // update and print moves

        return chosenMove;
    }
}
