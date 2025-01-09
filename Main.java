import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// E - 11.1607%
// A - 8.4966%
// R - 7.5809%
// I - 7.5448%
// O - 7.1635%
// T - 6.9509%
// N - 6.6544%
// S - 5.7351%
// L - 5.4893%
// C - 4.5388%
// U - 3.6308%
// D - 3.3844%
// P - 3.1671%
// M - 3.0129%
// H - 3.0034%
// G - 2.4705%
// B - 2.0720%
// F - 1.8121%
// Y - 1.7779%
// W - 1.2899%
// K - 1.1016%
// V - 1.0074%
// X - 0.2902%
// Z - 0.2722%
// J - 0.1965%
// Q - 0.1962%

class WordleLetter {
    public int Index;
    public String Letter;
    public String Color;
};

public class Main {
    public static void main(String[] args) throws IOException{
        System.out.print("Loading words... ");
        Scanner sc;
        File words;
        ArrayList<String> wordList = new ArrayList<String>();
        ArrayList<String> allValidWords = new ArrayList<String>();
        try {
            words = new File("wordlewords.txt");
            sc = new Scanner(words);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                if (data.length() == 5) {
                    wordList.add(data);
                }
            }

            words = new File("valid guesses.txt");
            sc = new Scanner(words);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                if (data.length() == 5) {
                    //wordList.add(data);
                    allValidWords.add(data);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            // e.printStackTrace();
        }
        //Collections.sort(wordList);
        Collections.shuffle(wordList);
        System.out.println("Done.");

        sc = new Scanner(System.in);

        int wordsRemaining = wordList.size();
        System.out.println("Total words remaining: " + wordsRemaining);

        // Calculate first recommendation with available words.
        //CalculateRecommendationV1(wordList, allValidWords, 0);
        CalculateRecommendationV2(wordList, 0);

        // this loop will run for each attempt.
        for (int attemptNumber = 1; attemptNumber <= 6; attemptNumber++) {
            switch (attemptNumber) {
                case 1:
                    System.out.println("Enter your first guess:");
                    break;
                case 2:
                    System.out.println("Enter your second guess:");
                    break;
                case 3:
                    System.out.println("Enter your third guess:");
                    break;
                case 4:
                    System.out.println("Enter your fourth guess:");
                    break;
                case 5:
                    System.out.println("Enter your fifth guess:");
                    break;
                case 6:
                    System.out.println("Enter your sixth guess:");
                    break;
            }

            // get the guess word from input.
            String guessString = sc.next().toLowerCase();
            if (guessString.equals("exit")) {
                break;
            }

            // get the color for each letter from input.
            ArrayList<WordleLetter> guessWord = new ArrayList<WordleLetter>();
            for (int letterKey = 0; letterKey < 5; letterKey++) {
                System.out.print("Input color(black/yellow/green) for letter " + (letterKey + 1) + ": ");
                String color = sc.next().toLowerCase();
                if (color.equals("black") || color.equals("grey") || color.equals("gray") || color.equals("b")) {
                    color = "black";
                }
                if (color.equals("yellow") || color.equals("y")) {
                    color = "yellow";
                }
                if (color.equals("green") || color.equals("g")) {
                    color = "green";
                }
                WordleLetter wordleLetter = new WordleLetter();
                wordleLetter.Index = letterKey;
                wordleLetter.Letter = guessString.substring(letterKey, letterKey + 1);
                wordleLetter.Color = color;
                guessWord.add(wordleLetter);
            }

            // Eliminate words.
            EliminateWords(wordList, guessWord);

            System.out.println("Your guess eliminated " + (wordsRemaining - wordList.size()) + " words.");
            wordsRemaining = wordList.size();

            // Print out possible words and a recommendation.
            System.out.println("Possible words:\n" + wordList.toString());
            //CalculateRecommendationV1(wordList, allValidWords, attemptNumber);
            CalculateRecommendationV2(wordList, attemptNumber);
        }
        sc.close();
    }

    public static void EliminateWords(ArrayList<String> wordList, ArrayList<WordleLetter> guessWord) {
        for (WordleLetter wordleLetter : guessWord) {
            if (wordleLetter.Color.equals("black")) {
                //remove all words that have this letter at THIS index
                for (int wordIndex = 0; wordIndex < wordList.size(); wordIndex++) {
                    if (wordList.get(wordIndex).charAt(wordleLetter.Index) == wordleLetter.Letter.charAt(0)) {
                        wordList.remove(wordIndex);
                        wordIndex--;
                    }
                }
                boolean remove = true;
                for (WordleLetter wordleLetter2 : guessWord) {
                    if (wordleLetter2.Index != wordleLetter.Index
                            && wordleLetter2.Letter.equals(wordleLetter.Letter) && !wordleLetter2.Color.equals("black")) {
                        // if we have a black letter, check all other letters in the word. If any match,
                        // and are NOT black, do not remove the word.
                        remove = false;
                    }
                }
                if (remove) {
                    // remove all words containing this letter
                    for (int wordIndex = 0; wordIndex < wordList.size(); wordIndex++) {
                        if (wordList.get(wordIndex).contains(wordleLetter.Letter)) {
                            wordList.remove(wordIndex);
                            wordIndex--;
                        }
                    }
                }
            } else if (wordleLetter.Color.equals("yellow")) {
                for (int wordIndex = 0; wordIndex < wordList.size(); wordIndex++) {
                    // remove if letter not in word
                    if (!wordList.get(wordIndex).contains(wordleLetter.Letter)) {
                        wordList.remove(wordIndex);
                        wordIndex--;
                    }
                }
                // remove all words that have this letter at this index
                for (int wordIndex = 0; wordIndex < wordList.size(); wordIndex++) {
                    if (wordList.get(wordIndex).charAt(wordleLetter.Index) == wordleLetter.Letter.charAt(0)) {
                        wordList.remove(wordIndex);
                        wordIndex--;
                    }
                }
            } else if (wordleLetter.Color.equals("green")) {
                // remove all words that don't have this letter at this index
                for (int wordIndex = 0; wordIndex < wordList.size(); wordIndex++) {
                    if (wordList.get(wordIndex).charAt(wordleLetter.Index) != wordleLetter.Letter.charAt(0)) {
                        wordList.remove(wordIndex);
                        wordIndex--;
                    }
                }
            }
        }
    }

    public static String CalculateRecommendationV2(ArrayList<String> wordList, int attemptNumber) throws IOException
    {
        //The following will calculate the best word based on avg number of words eliminated when simulated with every possible answer.
        //Each word will be scored based on how many words it eliminates on average.
        double trueBestWordScore = 0;
        ArrayList<String> bestWordList = new ArrayList<>();
        // ArrayList<Integer> bestScores = new ArrayList<>();
        // Map<Integer, String> bestScoresMap = new HashMap<Integer, String>();
        if(attemptNumber == 0)
        {
            //Recommended starting word(s): [arise], score: 2259
            //pre-run algorithm for first attempt because it takes like 10 min. draft
            //taken from firstwordscores file
            bestWordList.add("arise");
        }
        else
        {
            System.out.println(6 - attemptNumber + " remaining guesses.");
            ArrayList<String> problemWords = new ArrayList<>();
            problemWords = FindProblemSubstrings(wordList, 6 - attemptNumber);
            for(String possibleGuess : wordList)
            {
                double avgScore = 0;
                for(String possibleAnswer : wordList)
                {
                    //Copy wordList
                    ArrayList<String> wordListCopy = new ArrayList<>();
                    for(String word : wordList)
                    {
                        wordListCopy.add(word);
                    }
                    ArrayList<WordleLetter> guessWord = new ArrayList<WordleLetter>();
                    for(int letterIndex = 0; letterIndex < 5; letterIndex++)
                    {
                        WordleLetter wordleLetter = new WordleLetter();
                        wordleLetter.Index = letterIndex;
                        wordleLetter.Letter = possibleGuess.substring(letterIndex, letterIndex + 1);
                        if(possibleAnswer.substring(letterIndex, letterIndex + 1).equals(wordleLetter.Letter))
                        {
                            wordleLetter.Color = "green";
                        }
                        else if(possibleAnswer.contains(wordleLetter.Letter))
                        {
                            wordleLetter.Color = "yellow";
                        }
                        else
                        {
                            wordleLetter.Color = "black";
                        }
                        guessWord.add(wordleLetter);
                    }
                    EliminateWords(wordListCopy, guessWord);
                    avgScore += wordList.size() - wordListCopy.size();
                }
                avgScore = avgScore / wordList.size();
                avgScore = problemWords.contains(possibleGuess) ? avgScore * .85 : avgScore;
                // bestScores.add((int)avgScore);
                // bestScoresMap.put((int)avgScore, possibleGuess);
                System.out.println("Word: " + possibleGuess + ", score: " + avgScore);
                if(avgScore > trueBestWordScore)
                {
                    bestWordList.clear();
                    trueBestWordScore = avgScore;
                    bestWordList.add(possibleGuess);
                }
                else if(avgScore == trueBestWordScore)
                {
                    bestWordList.add(possibleGuess);
                }
            }

            // Collections.sort(bestScores);
            // FileWriter writer = new FileWriter("firstwordscores.txt");
            // for(int score : bestScores)
            // {
            //     writer.write(bestScoresMap.get(score) + ", score: " + score + "\n");
            // }
            // writer.close();
        }

        System.out.println("Recommended word(s): " + bestWordList.toString() + ", score: " + trueBestWordScore);
        
        return bestWordList.get(0);
    }

    public static ArrayList<String> FindProblemSubstrings(ArrayList<String> remainingWords, int remainingGuesses)
    {
        ArrayList<String> problemWords = new ArrayList<>();
        if(remainingGuesses == 6)
        {
            return problemWords;
        }
        Map<String, Integer> allLetterPairsWithFrequencyAtIndex_0_2 = new HashMap<String, Integer>();
        Map<String, Integer> allLetterPairsWithFrequencyAtIndex_1_3 = new HashMap<String, Integer>();
        Map<String, Integer> allLetterPairsWithFrequencyAtIndex_2_4 = new HashMap<String, Integer>();
        Map<String, Integer> allLetterPairsWithFrequencyAtIndex_3_5 = new HashMap<String, Integer>();
        for(String word : remainingWords)
        {
            String substring_0_2 = word.substring(0,2);
            String substring_1_3 = word.substring(1,3);
            String substring_2_4 = word.substring(2,4);
            String substring_3_5 = word.substring(3,5);
            if(!allLetterPairsWithFrequencyAtIndex_0_2.containsKey(substring_0_2))
            {
                allLetterPairsWithFrequencyAtIndex_0_2.put(substring_0_2, 1);
            }
            else
            {
                int value = allLetterPairsWithFrequencyAtIndex_0_2.get(substring_0_2);
                allLetterPairsWithFrequencyAtIndex_0_2.put(substring_0_2, value + 1);
            }

            if(!allLetterPairsWithFrequencyAtIndex_1_3.containsKey(substring_1_3))
            {
                allLetterPairsWithFrequencyAtIndex_1_3.put(substring_1_3, 1);
            }
            else
            {
                int value = allLetterPairsWithFrequencyAtIndex_1_3.get(substring_1_3);
                allLetterPairsWithFrequencyAtIndex_1_3.put(substring_1_3, value + 1);
            }

            if(!allLetterPairsWithFrequencyAtIndex_2_4.containsKey(substring_2_4))
            {
                allLetterPairsWithFrequencyAtIndex_2_4.put(substring_2_4, 1);
            }
            else
            {
                int value = allLetterPairsWithFrequencyAtIndex_2_4.get(substring_2_4);
                allLetterPairsWithFrequencyAtIndex_2_4.put(substring_2_4, value + 1);
            }

            if(!allLetterPairsWithFrequencyAtIndex_3_5.containsKey(substring_3_5))
            {
                allLetterPairsWithFrequencyAtIndex_3_5.put(substring_3_5, 1);
            }
            else
            {
                int value = allLetterPairsWithFrequencyAtIndex_3_5.get(substring_3_5);
                allLetterPairsWithFrequencyAtIndex_3_5.put(substring_3_5, value + 1);
            }
        }

        for(String key : allLetterPairsWithFrequencyAtIndex_0_2.keySet())
        {
            int value = allLetterPairsWithFrequencyAtIndex_0_2.get(key);
            if(value > remainingGuesses)
            {
                System.out.println(value + " <" + key + "> strings at index 0-2");
                for(String word : remainingWords)
                {
                    if(word.substring(0, 2).equals(key))
                    {
                        problemWords.add(word);
                    }
                }
            }
        }

        for(String key : allLetterPairsWithFrequencyAtIndex_1_3.keySet())
        {
            int value = allLetterPairsWithFrequencyAtIndex_1_3.get(key);
            if(value > remainingGuesses)
            {
                System.out.println(value + " <" + key + "> strings at index 1-3");
                for(String word : remainingWords)
                {
                    if(word.substring(1, 3).equals(key))
                    {
                        problemWords.add(word);
                    }
                }
            }
        }

        for(String key : allLetterPairsWithFrequencyAtIndex_2_4.keySet())
        {
            int value = allLetterPairsWithFrequencyAtIndex_2_4.get(key);
            if(value > remainingGuesses)
            {
                System.out.println(value + " <" + key + "> strings at index 2-4");
                for(String word : remainingWords)
                {
                    if(word.substring(2, 4).equals(key))
                    {
                        problemWords.add(word);
                    }
                }
            }
        }

        for(String key : allLetterPairsWithFrequencyAtIndex_3_5.keySet())
        {
            int value = allLetterPairsWithFrequencyAtIndex_3_5.get(key);
            if(value > remainingGuesses)
            {
                System.out.println(value + " <" + key + "> strings at index 3-5");
                for(String word : remainingWords)
                {
                    if(word.substring(3, 5).equals(key))
                    {
                        problemWords.add(word);
                    }
                }
            }
        }
        return problemWords;
    }

    public static String CalculateRecommendationV1(ArrayList<String> wordList, ArrayList<String> allValidWords, int attemptNumber) {
        boolean isHardModeEnabled = true;
        Map<Character, Integer> firstPlaceLetterMap = new HashMap<Character, Integer>();
        Map<Character, Integer> secondPlaceLetterMap = new HashMap<Character, Integer>();
        Map<Character, Integer> thirdPlaceLetterMap = new HashMap<Character, Integer>();
        Map<Character, Integer> fourthPlaceLetterMap = new HashMap<Character, Integer>();
        Map<Character, Integer> fifthPlaceLetterMap = new HashMap<Character, Integer>();
        Map<Character, Integer> allLetterMap = new HashMap<Character, Integer>();
        Map<Character, Integer> wordsWithLetterMap = new HashMap<Character, Integer>();
        PopulateLetterMap(firstPlaceLetterMap);
        PopulateLetterMap(secondPlaceLetterMap);
        PopulateLetterMap(thirdPlaceLetterMap);
        PopulateLetterMap(fourthPlaceLetterMap);
        PopulateLetterMap(fifthPlaceLetterMap);
        PopulateLetterMap(allLetterMap);
        PopulateLetterMap(wordsWithLetterMap);

        for (String word : wordList) {
            for (char letter : wordsWithLetterMap.keySet()) {
                String letterString = Character.toString(letter);
                if (word.contains(letterString)) {
                    wordsWithLetterMap.put(letter, wordsWithLetterMap.get(letter) + 1);
                }
            }
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                if (allLetterMap.containsKey(letter)) {
                    int value = allLetterMap.get(letter);
                    allLetterMap.put(letter, value + 1);
                }
            }
            char letter = word.charAt(0);
            if (firstPlaceLetterMap.containsKey(letter)) {
                int value = firstPlaceLetterMap.get(letter);
                firstPlaceLetterMap.put(letter, value + 1);
            }
            letter = word.charAt(1);
            if (secondPlaceLetterMap.containsKey(letter)) {
                int value = secondPlaceLetterMap.get(letter);
                secondPlaceLetterMap.put(letter, value + 1);
            }
            letter = word.charAt(2);
            if (thirdPlaceLetterMap.containsKey(letter)) {
                int value = thirdPlaceLetterMap.get(letter);
                thirdPlaceLetterMap.put(letter, value + 1);
            }
            letter = word.charAt(3);
            if (fourthPlaceLetterMap.containsKey(letter)) {
                int value = fourthPlaceLetterMap.get(letter);
                fourthPlaceLetterMap.put(letter, value + 1);
            }
            letter = word.charAt(4);
            if (fifthPlaceLetterMap.containsKey(letter)) {
                int value = fifthPlaceLetterMap.get(letter);
                fifthPlaceLetterMap.put(letter, value + 1);
            }
        }
        System.out.println("First place letter map:\n" + firstPlaceLetterMap.toString());
        System.out.println("Second place letter map:\n" + secondPlaceLetterMap.toString());
        System.out.println("Third place letter map:\n" + thirdPlaceLetterMap.toString());
        System.out.println("Fourth place letter map:\n" + fourthPlaceLetterMap.toString());
        System.out.println("Fifth place letter map:\n" + fifthPlaceLetterMap.toString());
        System.out.println("Letter totals map:\n" + allLetterMap.toString());
        System.out.println("Number of words containing each letter:\n" + wordsWithLetterMap.toString());

        double bestWordScore = 0;
        String bestWord = "";
        ArrayList<String> bestWordList = new ArrayList<>();
        ArrayList<String> problemWords = new ArrayList<>();
        problemWords = FindProblemSubstrings(wordList, 6 - attemptNumber);

        for (String word : wordList) {
            double wordScore = 0;
            int letterHeatmapScore = 0;
            int letterTotalsScore = 0;
            int wordsWithLetterScore = 0;
            boolean hasRepeats = false;
            letterHeatmapScore += firstPlaceLetterMap.get(word.charAt(0));
            letterHeatmapScore += secondPlaceLetterMap.get(word.charAt(1));
            letterHeatmapScore += thirdPlaceLetterMap.get(word.charAt(2));
            letterHeatmapScore += fourthPlaceLetterMap.get(word.charAt(3));
            letterHeatmapScore += fifthPlaceLetterMap.get(word.charAt(4));
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                letterTotalsScore += allLetterMap.get(letter);
                if (!word.substring(0, i).contains(Character.toString(letter))) {
                    wordsWithLetterScore += wordsWithLetterMap.get(letter);
                } else {
                    hasRepeats = true;
                }
            }
            wordScore = letterHeatmapScore + letterTotalsScore + wordsWithLetterScore;
            if (attemptNumber > 0) {
                System.out.println("Word: " + word + ", " + wordScore);
            }
            wordScore = hasRepeats && attemptNumber < 2 ? wordScore * .85 : wordScore;
            wordScore = problemWords.contains(word) ? wordScore * .85 : wordScore;
            if (wordScore > bestWordScore && !word.equals("stare")) {
                bestWordList.clear();
                bestWordList.add(word);
                bestWord = word;
                bestWordScore = wordScore;
            } else if (wordScore == bestWordScore) {
                bestWordList.add(word);
            }
        }

        //For non-hard mode only. Finds the best word for eliminating possibilities even if that word is already impossible.
        if(bestWordList.size() > 2 && !isHardModeEnabled) {
            Map<Character, Integer> bestLetterMap = new HashMap<Character, Integer>();
            PopulateLetterMap(bestLetterMap);
            for(String word : bestWordList) {
                for (char letter : word.toCharArray()) {
                    if (bestLetterMap.containsKey(letter)) {
                        int value = bestLetterMap.get(letter);
                        bestLetterMap.put(letter, value + 1);
                    }
                }
            }
            String uniqueLetters = "";
            for(char letter : bestLetterMap.keySet()) {
                if(bestLetterMap.get(letter) == 1)
                uniqueLetters += letter;
            }
            int bestWordEliminationScore = 0;
            for(String word : allValidWords)
            {
                int wordEliminationScore = 0;
                for(Character letter : uniqueLetters.toCharArray())
                {
                    if(word.contains(letter.toString()))
                    {
                        wordEliminationScore++;
                    }
                }
                if(wordEliminationScore > bestWordEliminationScore) {
                    bestWord = word;
                    bestWordEliminationScore = wordEliminationScore;
                }
            }
            System.out.println("Recommended word: " + bestWord + ", " + bestWordEliminationScore);
            return bestWord;
        }
        System.out.println("Recommended word(s): " + bestWordList.toString() + ", " + bestWordScore);
        return bestWordList.get(0);
    }
    
    public static void PopulateLetterMap(Map<Character, Integer> letterMap) {
        letterMap.put('a', 0);
        letterMap.put('b', 0);
        letterMap.put('c', 0);
        letterMap.put('d', 0);
        letterMap.put('e', 0);
        letterMap.put('f', 0);
        letterMap.put('g', 0);
        letterMap.put('h', 0);
        letterMap.put('i', 0);
        letterMap.put('j', 0);
        letterMap.put('k', 0);
        letterMap.put('l', 0);
        letterMap.put('m', 0);
        letterMap.put('n', 0);
        letterMap.put('o', 0);
        letterMap.put('p', 0);
        letterMap.put('q', 0);
        letterMap.put('r', 0);
        letterMap.put('s', 0);
        letterMap.put('t', 0);
        letterMap.put('u', 0);
        letterMap.put('v', 0);
        letterMap.put('w', 0);
        letterMap.put('x', 0);
        letterMap.put('y', 0);
        letterMap.put('z', 0);
    }
}
