package markov;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import datamuse.Datamuse;
import datamuse.JSONParse;

import com.json.exceptions.JSONParsingException;

public class Markov {
  private String corpus;
  private Map<String, Map<String, List<String>>> chain = new HashMap<>();
  private final int LINESIZE = 8;
  private int lineCount = 0;

  public Markov(String corpus) {
    this.corpus = corpus;
    triples();
  }

  private void triples() {
    List<String> lines = Arrays.asList(corpus.split("\n"));
    for (String line : lines) {
      List<String> words = new ArrayList<>(Arrays.asList(line.split(" ")));
      words.add("*STOP*");
      words.add(0, "*START*");
      for (int i = words.size()-1; i > 1; i--) {
        String word1 = words.get(i);
        String word2 = words.get(i-1);
        String word3 = words.get(i-2);
        if (chain.get(word1) == null) {
          chain.put(word1, new HashMap<>());
        }
        if (chain.get(word1).get(word2) == null) {
          chain.get(word1).put(word2, new ArrayList<>());
        }
        chain.get(word1).get(word2).add(word3);
      }
    }
  }

  public void generate(int lines) {
    while (lineCount < lines) {
      generateLine();
    }
  }

  private void generateLine() {
    Random r = new Random();
    String word1 = "*STOP*";
    List<String> secondWords = new ArrayList<>(chain.get(word1).keySet());
    String word2 = secondWords.get(r.nextInt(secondWords.size()));
    String toRhyme = word2;
    System.out.println(generateWords(word1, word2));
    generateRhymeLine(toRhyme);
    lineCount++;
  }

  private void generateRhymeLine(String word) {
    Random r = new Random();
    List<String> allRhymes = new ArrayList<>();
    List<String> rhymes = new ArrayList<>();
    List<String> almostRhymes = new ArrayList<>();
    try {
      rhymes = Arrays.asList(JSONParse.parseWords(Datamuse.rhymesWith(word)));
      allRhymes.addAll(rhymes.subList(0, rhymes.size()/2));
    } catch (JSONParsingException e){      
    } finally {
      try {
        almostRhymes = Arrays.asList(JSONParse.parseWords(Datamuse.almostRhymesWith(word)));
        allRhymes.addAll(almostRhymes.subList(0, almostRhymes.size()/2));
      } catch (JSONParsingException e) {}
    }
    allRhymes.addAll(rhymes.subList(rhymes.size()/2, rhymes.size()));
    allRhymes.addAll(almostRhymes.subList(almostRhymes.size()/2, almostRhymes.size()));
    String word1 = "*STOP*";
    for (String rhyme : allRhymes) {
      if (chain.get(rhyme) != null) {
        word1 = rhyme;
        break;
      }
    }
    List<String> secondWords = new ArrayList<>(chain.get(word1).keySet());
    String word2 = secondWords.get(r.nextInt(secondWords.size()));
    System.out.println(generateWords(word1, word2));
  }

  private String generateWords(String word1, String word2) {
    Random r = new Random();
    StringBuilder genWords = new StringBuilder();
    for (int i = 0; i < LINESIZE; i++) {
      if (!word1.equals("*START*") && !word1.equals("*STOP*")) {
        genWords.insert(0, word1);
      }
      if (chain.get(word1) == null || chain.get(word1).get(word2) == null) {
        if (i > 4) {
          return genWords.toString();
        } else {
          //lineCount--;
          generateLine();
          return "";
        }
      }
      List<String> thirdWords = chain.get(word1).get(word2);
      String word3 = thirdWords.get(r.nextInt(thirdWords.size()));
      word1 = word2;
      word2 = word3;
      genWords.insert(0," ");
    }
    if (!word2.equals("*START*") && !word2.equals("*STOP*")) {
        genWords.insert(0, word2);
      }
    return genWords.toString();
  }
}