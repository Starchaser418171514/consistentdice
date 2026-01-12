public class RandomPlayer extends Player {

    private Random rand = new Random();

    public RandomPlayer() {
        super("Random Player");
    }

    @Override
    public int[] chooseMove(int diceValue) {

        // get possible moves for dice roll
        ArrayList<int[]> possibleMoves = GameState.generatePossibleMoves(diceValue);
        // each int[] is {pieceNumber, newPosition}

        if (possibleMoves.isEmpty()) {
            return null;
        }

        int[] chosenMove = possibleMoves.get(rand.nextInt(possibleMoves.size())); //random choose a move

        int piece = chosenMove[0];
        int newPos = chosenMove[1];

        logMove(piece, newPos); // update and print moves

        return chosenMove;
    }
}