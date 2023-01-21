package readability;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static int[] textToChars(String text) {
        return text.chars().toArray();
    }

    private static int countNumberOfSymbols(String text) {
        int[] chars = textToChars(text);
        return chars.length;
    }

    private static int calcAvgWordsPerSentence(String text) {
        String[] sentences = text.split("[?!.]");
        String[] words = text.split("\\s+");
        return words.length / sentences.length;
    }

    public static void printDifficulty(int count, int threshold) {
        System.out.println(count <= threshold ? "EASY": "HARD");
    }

    public static void estimateDifficultyUsingSymbols(String text) {
        int symbolCount = countNumberOfSymbols(text);
        printDifficulty(symbolCount, 100);
    }

    public static void estimateDifficultyUsingWords(String text) {
        // int wordCount = countNumberOfWords(text);
        int avgWordCount = calcAvgWordsPerSentence(text);
        printDifficulty(avgWordCount, 10);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();

        estimateDifficultyUsingWords(text);
    }
}
