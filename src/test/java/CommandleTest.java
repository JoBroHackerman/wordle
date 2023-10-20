
import com.example.commandlefinalproject.Commandle;
import com.example.commandlefinalproject.GameBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class CommandleTest {
    // See https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println and
    // https://stackoverflow.com/questions/1647907/junit-how-to-simulate-system-in-testing
    // for more information on how to test with system input & output
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    private List<String> wordList;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUp() {
        wordList = new ArrayList<String>();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream("N\n".getBytes());

    }

    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @Test
    @DisplayName("TC001 - Correct guess validation")
    void gameCanBeWon() throws IOException {
        // set up a one-word list for easy testing
        String target = "brave";
        wordList.add(target);

        // provide the correct guess, and then followed by "N" to signal not wanting to play again
        provideInput(target + "\nN");

        // simulate the gameplay start
        Commandle.start(System.in, System.out, wordList);

        // get output
        String result = getOutput();

        // verify that the output contains the word "won"
        assertTrue(result.contains("won"), "You won");
    }

    @Test
    @DisplayName("TC009 - Case insensitivity test")
    public void toLowerCaseCheck() {
        // Capture System.err output
       // ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        //System.setErr(new PrintStream(errContent));

        // Create a mock InputStream with an input that is 5 letters in length and that are all capitals
        String input = "HELLO"; // Input with 5 letters
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Replace System.in with the mock InputStream
        System.setIn(inputStream);

        // Create a mock Scanner using the modified System.in
        Scanner scanner = new Scanner(System.in);

        // Create a set for guesses and a mock GameBoard
        Set<String> guesses = new HashSet<>();
        List<String> wordList = Arrays.asList("hello", "break", "crown"); // Add your words here
        GameBoard gameBoard = new GameBoard(wordList);

        // Call the method
        String result = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Assert that the error message is printed to System.err
        assertEquals("hello", result);

    }

    @Test
    @DisplayName("TC006 - Handling guesses 6 letters in length")
    public void largeGuesses(){

        // Create a mock InputStream with an input that has a length larger than 5 with no capitals or spaces or abstract characters
        String input = "senior"; // Input with 6 letters
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Replace System.in with the mock InputStream
        System.setIn(inputStream);

        // Create a mock Scanner using the modified System.in
        Scanner scanner = new Scanner(System.in);

        // Create a set for guesses and a mock GameBoard
        Set<String> guesses = new HashSet<>();
        List<String> wordList = Arrays.asList("hello", "break", "crown"); // Add your words here
        GameBoard gameBoard = new GameBoard(wordList);

        // Call the method
        String result = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Assert that the error message is printed to System.err
        assertEquals("Please enter a word of 5 letters: \n", result);
    }

    @Test
    @DisplayName("TC010 Handling a guess, 4 letters in length")
    public void smallGuesses(){

        // Create a mock InputStream with an input that has a length shorter than 5 with no capitals or spaces or abstract characters
        String input = "help"; // Input with 4 letters
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Replace System.in with the mock InputStream
        System.setIn(inputStream);

        // Create a mock Scanner using the modified System.in
        Scanner scanner = new Scanner(System.in);

        // Create a set for guesses and a mock GameBoard
        Set<String> guesses = new HashSet<>();
        List<String> wordList = Arrays.asList("hello", "break", "crown"); // Add your words here
        GameBoard gameBoard = new GameBoard(wordList);

        // Call the method
        String result = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Assert that the error message is printed to System.err
        assertEquals("Please enter a word of 5 letters: \n", result);
    }

    @Test
    @DisplayName("TC012 - Handling a guess that uses a word that was used in a previous guess (repeat guess)")
    public void repeatGuesses(){

        // Create a mock InputStream with an input that has a length larger than 5 with no capitals or spaces or abstract characters
        String input = "spice"; // Input with 5 letters
        InputStream inputStream = new ByteArrayInputStream("spice\nspice\n".getBytes());

        // Replace System.in with the mock InputStream
        System.setIn(inputStream);

        // Create a mock Scanner using the modified System.in
        Scanner scanner = new Scanner(System.in);

        // Create a set for guesses and a mock GameBoard
        Set<String> guesses = new HashSet<>();
        List<String> wordList = Arrays.asList("spice", "hello", "break"); // Add your words here
        GameBoard gameBoard = new GameBoard(wordList);

        // Test the method with the first valid guess "spice"
        String result1 = Commandle.getNextValidGuess(scanner, guesses, gameBoard);
        assertEquals("spice", result1);

        // Test the method with the duplicate guess "spice"
        String result2 = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Ensure that the method rejects the duplicate guess
        assertEquals("Please enter a new word: \n", result2);

        // Ensure the set contains only one entry ("spice") after the first valid guess
        assertEquals(1, guesses.size());
        assertTrue(guesses.contains("spice"));
    }

    @Test
    @DisplayName("TC008 - Handling guesses that are 5 letters in length but are not found in the word list")
    public void InvalidWords(){

        // Create a mock InputStream with an input that has a length shorter than 5 with no capitals or spaces or abstract characters
        String input = "aaaaa"; // Input with 5 letters
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Replace System.in with the mock InputStream
        System.setIn(inputStream);

        // Create a mock Scanner using the modified System.in
        Scanner scanner = new Scanner(System.in);

        // Create a set for guesses and a mock GameBoard
        Set<String> guesses = new HashSet<>();
        List<String> wordList = Arrays.asList("hello"); // Add your words here
        GameBoard gameBoard = new GameBoard(wordList);

        // Call the method
        String result = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Assert that the error message is printed to System.err
        assertEquals("Please enter a valid word: \n", result);
    }

    @Test
    @DisplayName("TC013 - Handling guesses that are 5 characters in length but contain special characters")
    public void InvalidSpecialCharacters(){

        // Create a mock InputStream
        String input = "*****"; // Input with 5 characters, all characters being a special characters.
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Replace System.in with the mock InputStream
        System.setIn(inputStream);

        // Create a mock Scanner using the modified System.in
        Scanner scanner = new Scanner(System.in);

        // Create a set for guesses and a mock GameBoard
        Set<String> guesses = new HashSet<>();
        List<String> wordList = Arrays.asList("hello", "crown", "break"); // Add your words here
        GameBoard gameBoard = new GameBoard(wordList);

        // Call the method
        String result = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Assert that the error message is printed to System.err
        assertEquals("Please enter a valid word: \n", result);
    }

    @Test
    @DisplayName("TC014 - Handling guesses that are 5 characters in length but contain numbers")
    public void InvalidNumberCharacters(){

        // Create a mock InputStream
        String input = "hell9"; // Input with 5 charaters, 1 character being a number
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Replace System.in with the mock InputStream
        System.setIn(inputStream);

        // Create a mock Scanner using the modified System.in
        Scanner scanner = new Scanner(System.in);

        // Create a set for guesses and a mock GameBoard
        Set<String> guesses = new HashSet<>();
        List<String> wordList = Arrays.asList("hello", "crown", "break"); // Add your words here
        GameBoard gameBoard = new GameBoard(wordList);

        // Call the method
        String result = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Assert that the error message is printed to System.err
        assertEquals("Please enter a valid word: \n", result);
    }

    /*
    @Test
    @DisplayName("Handling guesses that are partially correct")
    public void CheckingCorrectness(){

        // Create a mock InputStream with an input that has a word with 4 out of 5 letters matching the target word
        String input = "apple"; // Input with 5 letters
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Replace System.in with the mock InputStream
        System.setIn(inputStream);

        // Create a mock Scanner using the modified System.in
        Scanner scanner = new Scanner(System.in);

        // Create a set for guesses and a mock GameBoard
        Set<String> guesses = new HashSet<>();
        List<String> wordList = Arrays.asList("hello", "spice", "super"); // Add your words here
        GameBoard gameBoard = new GameBoard(wordList);

        // Call the method
        String result = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Assert that the error message is printed to System.err
        assertEquals("Please enter a valid word: \n", result);
    }
    */





    @Test
    @DisplayName("TC015(2) - Test that the max number of rounds in the game is 6 in the event of a win on the 6th guess")
    public void testStartMethodMaxTriesWin() throws IOException {
        // Redirect System.in and System.out for testing
        ByteArrayInputStream inputStream = new ByteArrayInputStream("apple\nspice\nhello\nfrizz\nbreak\ncrown\nN".getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Create a dummy word list for testing
        List<String> wordList = new ArrayList<>();
        wordList.add("apple");
        wordList.add("spice");
        wordList.add("hello");
        wordList.add("frizz");
        wordList.add("break");
        wordList.add("crown");
        wordList.add("ddddd");

        // Create a scanner for testing
        Scanner scanner = new Scanner(System.in);

        // Call the start method
        Commandle.startGameWithSetTargetWord(System.in, System.out, wordList, "crown");

        // Restore System.in and System.out
        System.setIn(System.in);
        System.setOut(System.out);

        // Check the output
        String output = outputStream.toString();
        assertTrue(output.contains("Congratulations, you won!")); // The player should win
        assertTrue(output.contains("See you next time!"));

        // Ensure that the number of games played does not exceed MAX_TRIES
        int maxTries = Commandle.MAX_TRIES;
        int gamesPlayed = (output.split("Play again? (Y/N)").length - 1); // Count games played
        assertTrue(gamesPlayed <= maxTries);
    }

    @Test
    @DisplayName("TC015 - Test that the max number of rounds in the game is 6 in the event of a loss on the 6th guess")
    public void testStartMethodMaxTriesLose() throws IOException {
        // Redirect System.in and System.out for testing
        ByteArrayInputStream inputStream = new ByteArrayInputStream("apple\nspice\nhello\nfrizz\nbreak\ncrown\nN".getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Create a dummy word list for testing
        List<String> wordList = new ArrayList<>();
        wordList.add("apple");
        wordList.add("spice");
        wordList.add("hello");
        wordList.add("frizz");
        wordList.add("break");
        wordList.add("crown");
        wordList.add("ddddd");

        // Create a scanner for testing
        Scanner scanner = new Scanner(System.in);

        // Call the start method
        Commandle.startGameWithSetTargetWord(System.in, System.out, wordList, "ddddd");

        // Restore System.in and System.out
        System.setIn(System.in);
        System.setOut(System.out);

        // Check the output
        String output = outputStream.toString();
        assertTrue(output.contains("Sorry, you lost!")); // The player should lose
        assertTrue(output.contains("See you next time!"));

        // Ensure that the number of games played does not exceed MAX_TRIES
        int maxTries = Commandle.MAX_TRIES;
        int gamesPlayed = (output.split("Play again? (Y/N)").length - 1); // Count games played
        assertTrue(gamesPlayed <= maxTries);
    }




    @Test
    @DisplayName("TC002 - Replay function validation, post win")
    public void testUserInputYAfterWinningGame() throws IOException {
        // Redirect System.out to capture output
        ByteArrayOutputStream systemOutContent = new ByteArrayOutputStream();
        PrintStream originalSystemOut = System.out;
        System.setOut(new PrintStream(systemOutContent));

        // Prepare a ByteArrayInputStream with the input "Y\n"
        ByteArrayInputStream systemInContent = new ByteArrayInputStream("apple\napple\nY\n".getBytes());
        InputStream originalSystemIn = System.in;
        System.setIn(systemInContent);

        try {
            // Prepare a list of words with a known target word
            List<String> wordList = Arrays.asList("apple");
            ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(outputBuffer);

            // Simulate the game being played and the user entering "Y" after winning
            Commandle.startGameWithSetTargetWord(systemInContent, out, wordList, "apple");

            // Capture the output
            String capturedOutput = outputBuffer.toString();

            // Check if the output contains the expected messages
          //  assertTrue(capturedOutput.contains("Please enter your guess:"));
            assertTrue(capturedOutput.contains("1: apple  1: apple"));
            assertTrue(capturedOutput.contains("Congratulations, you won!"));
            assertTrue(capturedOutput.contains("Play again? (Y/N)"));
            assertTrue(capturedOutput.contains("Please enter your guess:"));
        } finally {
            // Restore original System.in and System.out
            System.setIn(originalSystemIn);
            System.setOut(originalSystemOut);
        }
    }

    @Test
    @DisplayName("TC003 - Replay function validation, post loss")
    public void testUserInputYAfterLosingGame() throws IOException {
        // Redirect System.out to capture output
        ByteArrayOutputStream systemOutContent = new ByteArrayOutputStream();
        PrintStream originalSystemOut = System.out;
        System.setOut(new PrintStream(systemOutContent));

        // Prepare a list of words with known target words
        List<String> wordList = Arrays.asList("apple", "crown", "break", "night", "sight", "tight", "fight");

        // Prepare a ByteArrayInputStream with 6 unique 5-letter word guesses and replay ("crown\nbreak\nnight\nsight\ntight\nfight\nY\ncrown\nbreak\nnight\nsight\ntight\nfight\nN\n")
        String input = "crown\nbreak\nnight\nsight\ntight\nfight\nY\ncrown\nbreak\nnight\nsight\ntight\nfight\nN\n";
        ByteArrayInputStream systemInContent = new ByteArrayInputStream(input.getBytes());
        InputStream originalSystemIn = System.in;
        System.setIn(systemInContent);

        try {
            // Simulate the game being played
            Commandle.startGameWithSetTargetWord(systemInContent, System.out, wordList, "apple");

            // Capture the output
            String capturedOutput = systemOutContent.toString();

          //Check if a new game can be played.
            assertTrue(capturedOutput.contains("Sorry, you lost!"));
            assertTrue(capturedOutput.contains("Play again? (Y/N)"));

            assertTrue(capturedOutput.contains("Please enter your guess: "));
        } finally {
            // Restore original System.in and System.out
            System.setIn(originalSystemIn);
            System.setOut(originalSystemOut);
        }
    }




    private void provideInput(String guess) {
        System.setIn(new ByteArrayInputStream(guess.getBytes()));
    }
}