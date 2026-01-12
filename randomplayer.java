public class RandomPlayer extends Player {
public Move chooseMove(GameState state) {
    ArrayList<Move> moves = state.generatePossibleMoves();

    if (moves.isEmpty()) {
        return null; // no possible moves
    }

    Random rand = new Random();
    return moves.get(rand.nextInt(moves.size()));
}
}