package com.example.commandlefinalproject;

import java.io.*;
import java.util.*;

public class Commandle {
    public static final int MAX_TRIES = 6;

    /**
     * Requirements:
     * 1. Different words in consecutive games in the same session
     * 2. Default number of tries is 6, but can be changed if specified by the user
     * 3. Only valid words in the list are accepted
     * 4. Words are case-insensitive
     * 5. The same word cannot be played again in a game
     * <p>
     * Hints:
     * * A "?" flags that the input letter is in the word but not in the right position.
     * * A "#" flags the input letter is not in the word.
     * * The actual letter shows that it's in the right position.
     *
     * @param args Optional argument that points to a dictionary file of allowed words.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        start(System.in, System.out, args);
    }

    static void start(InputStream in, PrintStream out, String[] args) throws IOException {
        String dictionaryFileName = "dictionary1.txt";
        if (null != args && args.length > 0) {
            dictionaryFileName = args[0];
        }

            List<String> wordList = getWordList();
            start(in, out, wordList);



    }

    public static void start(InputStream in, PrintStream out, List<String> wordList) throws IOException {
        GameBoard gameBoard = new GameBoard(wordList);

        Scanner scanner = new Scanner(in);

        int round = 0;

do {

    do {
        round++;
        System.out.println("round: " + round);


        gameBoard.startGame();

        boolean result = playOneGame(out, MAX_TRIES, gameBoard, scanner);
        if (result) {
            out.println("Congratulations, you won!");
        } else {
            out.println("Sorry, you lost!");
        }

        if(round >9) {
            System.out.println("You have completed all your rounds for today! please try again tomorrow.");
            break;
        }
        out.println("Play again? (Y/N)");

       // out.println("See you next time!");



    } while ("Y".equalsIgnoreCase(scanner.nextLine().trim()));

    out.println("See you next time!");
    round = 10;

        } while(round <10);
            scanner.close();


    }

//Only used for testing purposes
    public static void startGameWithSetTargetWord(InputStream in, PrintStream out, List<String> wordList, String targetWord) throws IOException {
        GameBoard gameBoard = new GameBoard(wordList);

        Scanner scanner = new Scanner(in);

        gameBoard.setSpecificTargetWord(targetWord);

        do {
            boolean result = playOneGame(out, MAX_TRIES, gameBoard, scanner);
            if (result) {
                out.println("Congratulations, you won!");
            } else {
                out.println("Sorry, you lost!");
            }

            out.println("Play again? (Y/N)");

        } while ("Y".equalsIgnoreCase(scanner.nextLine().trim()));

        out.println("See you next time!");

        scanner.close();
    }





    public static List<String> getWordList() throws IOException {
        final List<String> wordList;
        // File file = new File(Commandle.class.getClassLoader().getResource(dictionaryFileName).getFile());
        // File file = new File("F:\\Monash\\InteliJ Folder\\IntelliJ IDEA Community Edition 2022.2.3\\Projects\\CommandleFinalProject\\src\\dictionary1.txt");
        File file = new File("src\\dictionary1.txt");

        //reads the file
        try (FileReader fr = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            wordList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                wordList.add(line.trim().toLowerCase());
            }
        }
        return wordList;

    }

    public static String getNextValidGuess(Scanner scanner, Set<String> guesses, GameBoard gameBoard) {
        String guess = scanner.nextLine().trim().toLowerCase();

        if (guess.length() != 5) {
            String error = "Please enter a word of 5 letters: \n";
            System.err.print(error);
            return error;
        } else if (guesses.contains(guess)) {
            String error = "Please enter a new word: \n";
            System.err.print(error);
            return error;
        } else if (!gameBoard.containsWord(guess)) {
            String error = "Please enter a valid word: \n";
            System.err.print(error);
            return error;
        } else {
            guesses.add(guess);
            return guess;
        }
       // return getNextValidGuess(scanner, guesses, gameBoard);
    }

    public static boolean playOneGame(PrintStream out, int rounds, GameBoard gameBoard, Scanner scanner) {
        Set<String> guesses = new HashSet<>();

        out.print("Please enter your guess: ");

        String correct = "";


        for (int i = 0; i < rounds; i++) {

            boolean repeat = true;
            while(repeat){
                String attempt = getNextValidGuess(scanner, guesses, gameBoard);
                if(attempt.equals("Please enter a word of 5 letters: \n")){
                } else if (attempt.equals("Please enter a new word: \n")) {
                } else if (attempt.equals("Please enter a valid word: \n")) {
                } else {
                    correct = attempt;
                    repeat = false;
                }
            }

            String guess = correct;

            // Check for correctness here
            GameBoard.Status[] result = gameBoard.isInTarget(guess.toLowerCase().toCharArray());
            String hint = "";
            for (int j = 0; j < result.length; j++) {
                switch (result[j]) {
                    case correct -> hint += guess.charAt(j);
                    case wrong -> hint += "#";
                    case partial -> hint += "?";
                }
            }
            int round = i + 1;
            out.println(round + ": " + guess + "  " + round + ": " + hint);
            if (gameBoard.hasWon(result)) {
                return true;
            }
        }
        return false;
    }
}
