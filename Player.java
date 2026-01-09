package consistentdice;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class Player {
    protected String playerName;

    public Player(String name) {
        this.playerName = name;
    }

    public void printMove(List<Integer> piecePositions) {
        try (FileWriter fw = new FileWriter("consistentdice\\moves.txt", true)) {
            StringBuilder sb = new StringBuilder();
            for (Integer pos : piecePositions) {
                sb.append(pos).append(" ");
            }
            fw.write(sb.toString().trim() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error writing move to moves.txt");
        }
    }

    public void logMove(int piece, int newPos) {
        List<Integer> currentState = GameState.moveData.get(GameState.moveData.size() - 1);
        List<Integer> newState = new java.util.ArrayList<>(currentState);

        newState.set(piece - 1, newPos);
        GameState.moveData.add(newState);

        printMove(newState);
    }
  
    public abstract int[] chooseMove(int diceValue);
}
