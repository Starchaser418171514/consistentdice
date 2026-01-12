package consistentdice;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameLoader {
    protected static List<List<Integer>> levelData;

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Select level (1-4): ");
        int level = 0;

        // Input validation for level selection
        while (level <= 0) {
            while (!input.hasNextInt()) {
                System.out.print("Please enter a valid positive integer (1-4) for the level.\nSelect level: ");
                input.next();
            }
            level = input.nextInt();
            if (level <= 0) {
                System.out.print("Please enter a valid positive integer (1-4) for the level.\nSelect level: ");
            }
        }

        File file = new File("consistentdice", "testcases\\level" + level + ".txt");
        try (Scanner IStream = new Scanner(file)) {
            System.out.println("testcases\\level" + level + ".txt loaded successfully.");

            levelData = new ArrayList<>();
            while (IStream.hasNextLine()) {
                String[] StringRow = IStream.nextLine().split("\\s+");
                List<Integer> numRow = new ArrayList<>();
                for (String s : StringRow) {
                    // Converting strings to int
                    numRow.add(Integer.parseInt(s));
                }
                // Adding row to level data
                levelData.add(numRow);
            }

            IStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

    }

    /**
     * Prints the setup details into moves.txt, call this only at the start of the
     * game.
     */
    public static void printGameDetails(Player player) {
        List<String> OStream = new ArrayList<>();
        OStream.add(player.playerName); // Player name
        OStream.add(levelData.get(2).stream().map(String::valueOf).collect(Collectors.joining(" "))); // Dice values
        OStream.add(levelData.get(0).get(0).toString()); // target piece
        OStream.add(levelData.get(1).stream().map(String::valueOf).collect(Collectors.joining(" "))); //Starting piece positions

        try {
            Files.write(Path.of("consistentdice\\moves.txt"), OStream, StandardCharsets.UTF_8);

            System.out.println("Game details written to consistentdice\\moves.txt successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to moves.txt");
        }
    }
}
