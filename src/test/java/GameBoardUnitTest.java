
import com.example.commandlefinalproject.Commandle;
import com.example.commandlefinalproject.GameBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static com.example.commandlefinalproject.GameBoard.Status.correct;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardUnitTest {
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        List<String> wordList;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void isInTarget() {
    }

    @Test
    void hasWon() {
        gameBoard = new GameBoard(new ArrayList<>());

        GameBoard.Status[] result = new GameBoard.Status[]{correct, correct, correct, correct, correct};
        assertTrue(gameBoard.hasWon(result), "game is won");
    }

    @Test
    void containsCorrectWord() {
        ArrayList<String> list = new ArrayList<>();
        String word = "prone";
        list.add(word);
        gameBoard = new GameBoard(list);

        assertTrue(gameBoard.containsWord(word), "contains word");
    }

    @Test
    @DisplayName("TC001(2) - Guess is completely correct")
    public void testIsInTargetCorrectGuess() {

        //create a word list
        List<String> wordList = new ArrayList<>();

        //Add words to the list
        wordList.add("apple");

        // Create a sample GameBoard with a known target word "apple" from wordList
        gameBoard = new GameBoard(wordList);

        char[] partiallyCorrectGuess = "apple".toCharArray();
        gameBoard.startGame();
        GameBoard.Status[] result = gameBoard.isInTarget(partiallyCorrectGuess);

        // Verify that all letters are correct
        GameBoard.Status[] expected = {
                GameBoard.Status.correct,
                GameBoard.Status.correct,
                GameBoard.Status.correct,
                GameBoard.Status.correct,
                GameBoard.Status.correct
        };

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("TC005 - Guess evaluation, incorrect letter placement")
    public void testIsPartiallyInTarget() {

        //create a word list
        List<String> wordList = new ArrayList<>();

        //Add words to the list
        wordList.add("crate");

        // Create a sample GameBoard with a known target word "apple" from wordList
        gameBoard = new GameBoard(wordList);

        char[] correctGuess = "trace".toCharArray();
        gameBoard.startGame();
        GameBoard.Status[] result = gameBoard.isInTarget(correctGuess);

        // Verify that all letters are correct
        GameBoard.Status[] expected = {
                GameBoard.Status.partial,
                GameBoard.Status.correct,
                GameBoard.Status.correct,
                GameBoard.Status.partial,
                GameBoard.Status.correct
        };

        assertArrayEquals(expected, result);
    }



    @Test
    @DisplayName("TC004 - Guess is partially incorrect")
    public void testIsPartiallyNotInTarget() {

        //create a word list
        List<String> wordList = new ArrayList<>();

        //Add words to the list
        wordList.add("sight");

        // Create a sample GameBoard with a known target word "apple" from wordList
        gameBoard = new GameBoard(wordList);

        char[] correctGuess = "fight".toCharArray();
        gameBoard.startGame();
        GameBoard.Status[] result = gameBoard.isInTarget(correctGuess);

        // Verify that all letters are correct
        GameBoard.Status[] expected = {
                GameBoard.Status.wrong,
                GameBoard.Status.correct,
                GameBoard.Status.correct,
                GameBoard.Status.correct,
                GameBoard.Status.correct
        };

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("TC011 - A valid 5 letter guess is partially correct and partially incorrect")
    public void testIsPartiallyIncorrectCorrectTarget() {

        //create a word list
        List<String> wordList = new ArrayList<>();

        //Add words to the list
        wordList.add("apple");

        // Create a sample GameBoard with a known target word "apple" from wordList
        gameBoard = new GameBoard(wordList);

        char[] correctGuess = "happy".toCharArray();
        gameBoard.startGame();
        GameBoard.Status[] result = gameBoard.isInTarget(correctGuess);

        // Verify that all letters are correct
        GameBoard.Status[] expected = {
                GameBoard.Status.wrong,
                GameBoard.Status.partial,
                GameBoard.Status.correct,
                GameBoard.Status.partial,
                GameBoard.Status.wrong
        };

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("TC016 - Verifying that the target word changes between rounds")
    public void testTargetWordIsNotTheSameBetweenRounds() {

        List<String> wordList = Arrays.asList("apple", "spice", "super");
        gameBoard = new GameBoard(wordList);

        // Start the game
        gameBoard.startGame();
        String targetWordRound1 = gameBoard.getTargetWord();

        // Start a new round
        gameBoard.startGame();
        String targetWordRound2 = gameBoard.getTargetWord();

        // Check that the target word does not remain the same between rounds
        assertNotEquals(targetWordRound1, targetWordRound2);
    }



    @Test
    @DisplayName("TC007 - Round reset after beginning a new session")
    public void roundResetBetweenSessions() throws IOException {
        // Prepare the input for the first session
        String input1 = "crown\nN\n";
        InputStream inputStream1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(inputStream1);

        // Redirect System.out to capture output
        ByteArrayOutputStream systemOutContent = new ByteArrayOutputStream();
        PrintStream originalSystemOut = System.out;
        System.setOut(new PrintStream(systemOutContent));

        // Start the first game session
        List<String> wordList1 = Arrays.asList("crown");
        Commandle.start(inputStream1, System.out, wordList1);

        // Reset the input stream
        System.setIn(System.in);

        // Prepare the input for the second session
        String input2 = "crown\nN\n";
        InputStream inputStream2 = new ByteArrayInputStream(input2.getBytes());
        System.setIn(inputStream2);

        // Start the second game session
        List<String> wordList2 = Arrays.asList("crown");
        Commandle.start(inputStream2, System.out, wordList2);

        // Capture the output of the second session
        String capturedOutput2 = systemOutContent .toString();

        // Check that the round number is reset to 1 at the beginning of the second session
        assertTrue(capturedOutput2.contains("round: 1"));
    }



}