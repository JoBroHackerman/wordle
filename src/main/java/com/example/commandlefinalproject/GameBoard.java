package com.example.commandlefinalproject;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameBoard {
    public enum Status {
        correct, wrong, partial;
    }

    private String target;
    private final Set<String> usedWords = new HashSet<>();
    private final List<String> wordList;
    private final Random random = new Random();
    public GameBoard(List<String> wordList) {
        this.wordList = wordList;
    }


    public void setTargetWord(String targetWord)
    {
        int size = wordList.size();
        target = targetWord;

    }

    public String getTargetWord()
    {

      //  System.out.println("this is the target word: " + target);
        return target;
    }

    public void startGame() {
        int size = wordList.size();
        do{
            target = wordList.get(new Random().nextInt(size)).toLowerCase();
            setTargetWord(target);
            getTargetWord();
        } while(usedWords.contains(getTargetWord()));
        usedWords.add(getTargetWord());
        System.err.println("\ntarget w = " + getTargetWord());
        //selectNewTargetWord();
    }

    private void selectNewTargetWord() {
        int size = wordList.size();
        target = wordList.get(random.nextInt(size)).toLowerCase();
        System.err.println("\ntarget word = " + target);
    }

    //used to set a specific target word in tests
    public void setSpecificTargetWord(String targetWord){
        int size = wordList.size();
        target = targetWord.toLowerCase();
        System.err.println("\ntarget w = " + target);

    }

    public Status[] isInTarget(char[] word) {
        Status[] result = new Status[target.length()];

        for (int i = 0; i < target.length(); i++) {
            if (word[i] == target.charAt(i)) {
                result[i] = Status.correct;
            } else if (target.indexOf(word[i]) >= 0) {
                result[i] = Status.partial;
            } else {
                result[i] = Status.wrong;
            }
        }
        return result;
    }

    public boolean hasWon(Status[] current) {
        boolean result = true;
        for (Status st : current) {
            result &= (st == Status.correct);
        }
        return result;
    }

    public boolean containsWord(String word) {
        return wordList.contains(word);
    }
}