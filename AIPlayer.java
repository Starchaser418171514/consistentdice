package consistentdice;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class AIPlayer extends Player {

    public AIPlayer() {
        this("AI Player");
    }

    public AIPlayer(String name) {
        super(name);
    }

    @Override
    public int[] chooseMove(int diceValue) {
        Map<Integer, List<Integer>> possibleMoves = GameState.generatePossibleMoves(diceValue);
        if (possibleMoves.isEmpty()) {
            System.out.println("No available moves for dice value " + diceValue);
            return null;
        }

        float minScore = 32.f;
        Integer bestPiece = null, bestTarget = null;
        for (Map.Entry<Integer, List<Integer>> entry : possibleMoves.entrySet()) {
            Integer piece = entry.getKey();
            List<Integer> moves = entry.getValue();

            for (Integer square : moves) {
                float score = evaluateSquare(square, piece);
                if (score < minScore) {
                    minScore = score;
                    bestPiece = piece;
                    bestTarget = square;
                }
            }
        }

        int[] selectedMove = new int[] { bestPiece, bestTarget };

        System.out.println("AI chose to move piece " + bestPiece + " to square " + bestTarget);

        logMove(selectedMove[0], selectedMove[1]);

        return selectedMove;
    }

    /**
     * Evaluate the desirability of a square for a given piece, lower scores are
     * better
     * 
     * @param square the target square
     * @param piece  the piece being moved
     * @return the score of the square
     */
    private float evaluateSquare(Integer square, int piece) {
        List<List<Integer>> moveData = GameState.getMoveData();
        int targetPiece = moveData.get(1).get(0);
        List<Integer> currentState = moveData.get(moveData.size() - 1);

        if (piece == targetPiece) { // If the moving piece is the target piece, move closer to the goal
            int distance = Math.max(square / 10, square % 10);
            if (square % 11 == 0 && square > 22) {
                distance++;
            }

            float modifier = 0.f;
            for (int i = 0; i < currentState.size(); i++) {
                if (square == currentState.get(i)) {
                    modifier -= 2.f - 0.2f * Math.abs(i + 1 - targetPiece); // Prefer capturing pieces closer to target
                                                                           // piece number
                    
                    // Prefer capturing pieces that would increase the largest contiguous block of
                    // captured pieces connected to the target piece
                    int[] adjacent = getAdjacentToTarget(currentState, targetPiece);
                    if (i == adjacent[0] || i == adjacent[1]) {
                        modifier -= 0.2f * numberOfConnectedCapturedPieces(currentState, i + 1);
                    }
                }
            }
            return distance + modifier;

        } else { // Otherwise, try to capture any non-target piece
            
            if (square == currentState.get(targetPiece - 1)) {
                return 33; // Avoid capturing the target piece
            }
            List<Integer> distances = new ArrayList<>();
            for (Integer piecePos : currentState) {
                if (piecePos == targetPiece || piecePos < 0) { // Skip target piece and captured pieces
                    continue;
                }
                int distToPiece = Math.max(Math.abs(square / 10 - piecePos / 10),
                        Math.abs(square % 10 - piecePos % 10));
                distances.add(distToPiece);
            }
            float totalDistance = 0;
            for (Integer d : distances) {
                totalDistance += d;
            }

            float modifier = 10.f; // Penalty to prefer moving the target piece
            for (int i = 0; i < currentState.size(); i++) {
                if (square == currentState.get(i)) { // Case for capturing target piece is handled above; namely
                                                     // avoid it
                    modifier -= 5.f - 0.5 * Math.abs(i + 1 - targetPiece); // Prefer capturing pieces closer to target
                                                                           // piece number

                    // Prefer capturing pieces that would increase the largest contiguous block of
                    // captured pieces connected to the target piece
                    int[] adjacent = getAdjacentToTarget(currentState, targetPiece);
                    if (i == adjacent[0] || i == adjacent[1]) {
                        modifier -= 1.f * numberOfConnectedCapturedPieces(currentState, i + 1);
                    }
                }
            }
            
            return totalDistance / distances.size() + modifier;
        }
    }

    /**
     * Get the indices of the pieces adjacent (in the array) to the target piece
     * that are not captured
     * 
     * @param currentState the current board state
     * @param targetPiece  the target piece
     * @return an int[] of size 2, where [0] is the left adjacent piece index and
     *         [1] is the right adjacent piece index
     */
    private int[] getAdjacentToTarget(List<Integer> currentState, int targetPiece) {
        int index = targetPiece - 2;
        int[] adjacent = new int[2]; // [0] = left, [1] = right
        try {
            while (currentState.get(index) < 0) {
                index--;
            }
            adjacent[0] = index;
        } catch (IndexOutOfBoundsException e) {
            adjacent[0] = 0;
        }
        index = targetPiece;
        try {
            while (currentState.get(index) < 0) {
                index++;
            }
            adjacent[1] = index;
        } catch (IndexOutOfBoundsException e) {
            adjacent[1] = 5;
        }

        return adjacent;
    }

    /**
     * Count the number of captured pieces connected (in the array) to the given
     * piece
     * 
     * @param currentState the current board state
     * @param piece        the piece to check
     * @return the number of connected captured pieces
     */
    private int numberOfConnectedCapturedPieces(List<Integer> currentState, int piece) {
        int count = 0;
        int index = piece - 2;
        try {
            while (currentState.get(index) < 0) {
                count++;
                index--;
            }
        } catch (IndexOutOfBoundsException e) {
            // reached the beginning of the list
        }
        index = piece;
        try {
            while (currentState.get(index) < 0) {
                count++;
                index++;
            }
        } catch (IndexOutOfBoundsException e) {
            // reached the end of the list
        }
        return count;
    }
}
