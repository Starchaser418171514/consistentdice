package consistentdice;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameState {
    protected static List<List<Integer>> moveData;

    public static void main(String[] args) {
        File file = new File("consistentdice", "moves.txt");
        try (Scanner IStream = new Scanner(file)) {
            System.out.println("moves.txt loaded successfully.");

            String player = IStream.nextLine(); // Handle this later

            moveData = new ArrayList<>();

            while (IStream.hasNextLine()) {
                String[] StringRow = IStream.nextLine().split("\\s+");
                List<Integer> numRow = new ArrayList<>();
                for (String s : StringRow) {
                    // Converting strings to int
                    numRow.add(Integer.parseInt(s));
                }
                // Adding row to move data
                moveData.add(numRow);
            }

            System.out.println("Move Data: ");
            for (List<Integer> row : moveData) {
                for (Integer num : row) {
                    System.out.print(num + " ");
                }
                System.out.println();
            }

            IStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        System.out.println(generatePossibleMoves(3)); // Example dice value

    }

    /**
     * Generate possible moves based on dice value and valid pieces from moveData
     * 
     * @param diceValue the value of the dice
     * @return the Map of possible moves, key is the corresponding piece, value is the list of possible moves for that piece
     */
    public static Map<Integer, List<Integer>> generatePossibleMoves(Integer diceValue) {
        Map<Integer, List<Integer>> possibleMoves = new HashMap<>();
        List<Integer> validPieces = new ArrayList<>(moveData.get(moveData.size() - 1));

        if (validPieces.get(diceValue - 1) > 0) {
            possibleMoves.put(diceValue, new ArrayList<>());
        } else {
            int index = diceValue - 2;
            try {
                while (validPieces.get(index) < 0) {
                    index--;
                }
                possibleMoves.put(index + 1, new ArrayList<>());
            } catch (IndexOutOfBoundsException e) {
                // reached the beginning of the list
            }
            index = diceValue;
            try {
                while (validPieces.get(index) < 0) {
                    index++;
                }
                possibleMoves.put(index + 1, new ArrayList<>());
            } catch (IndexOutOfBoundsException e) {
                // reached the end of the list
            }
        }

        // System.out.println("Valid Pieces: " + possibleMoves.keySet());
        for (Map.Entry<Integer, List<Integer>> entry : possibleMoves.entrySet()) {
            // System.out.println("Generating moves for piece: " + entry.getKey());
            Integer key = entry.getKey();
            List<Integer> moves = new ArrayList<>();
            Integer piecePos = validPieces.get(key - 1);
            for (int i = piecePos / 10 - 1; i <= piecePos / 10 + 1; i++) { //Assuming a 10 x 10 board
                if (i >= 0 && i < 10) {
                    for (int j = piecePos % 10 - 1; j <= piecePos % 10 + 1; j++) {
                        //Add the move as a valid move if it's within bounds and not the square 22
                        if (j >= 0 && j < 10 && i * 10 + j != 22 && i * 10 + j != piecePos) {
                            moves.add(i * 10 + j);
                        }
                    }
                }
            }
            possibleMoves.put(key, moves);
            // System.out.println("Possible moves for piece " + key + ": " + moves);
        }
        return possibleMoves;
    }

public static boolean isWinning(int targetPiece) {
    if (moveData == null || moveData.isEmpty()) {
        return false;
        }
    //Get the current board stat
    List<Integer> currentPieces = moveData.get(moveData.size() - 1);
    // Get the position of the target piece
    int currentPosition = currentPieces.get(targetPiece - 1);
    return currentPosition == 0;
    }

}

