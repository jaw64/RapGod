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
  private final int MINLINELENGTH = 10;
  private final int MAXLINELENGTH = 12;

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
    for (int i = 0; i < lines; i++) {
      String firstLine = null;
      while ((firstLine = generateLine("*STOP*")) == null) {
      }
      String[] rhymeWords = firstLine.split(" ");
      String toRhyme = rhymeWords[rhymeWords.length - 1];

      List<String> allRhymes = new ArrayList<>();
      List<String> rhymes = new ArrayList<>();
      List<String> almostRhymes = new ArrayList<>();
      try {
        rhymes = Arrays.asList(JSONParse.parseWords(Datamuse.rhymesWith(toRhyme)));
        allRhymes.addAll(rhymes.subList(0, rhymes.size()/2));
      } catch (JSONParsingException e){      
      } finally {
        try {
          almostRhymes = Arrays.asList(JSONParse.parseWords(Datamuse.almostRhymesWith(toRhyme)));
          allRhymes.addAll(almostRhymes.subList(0, almostRhymes.size()/2));
        } catch (JSONParsingException e) {}
      }
      allRhymes.addAll(rhymes.subList(rhymes.size()/2, rhymes.size()));
      allRhymes.addAll(almostRhymes.subList(almostRhymes.size()/2, almostRhymes.size()));
      String word1 = null;
      for (String rhyme : allRhymes) {
        if (chain.get(rhyme) != null) {
          word1 = rhyme;
          break;
        }
      }
      if (word1 == null) {
        i--;
        continue;
      }

      String secondLine = null;
      while ((secondLine = generateLine(word1)) == null) {
        allRhymes.remove(word1);
        word1 = null;
        for (String rhyme : allRhymes) {
          if (chain.get(rhyme) != null) {
            word1 = rhyme;
            break;
          }
        }
        if (word1 == null) {
          i--;
          break;
        }
      }

      if (secondLine != null) {
        System.out.println(firstLine);
        System.out.println(secondLine);
      }
    }
  }

  private String generateLine(String word1) {
    Random r = new Random();
    List<String> secondWords = new ArrayList<>(chain.get(word1).keySet());
    String word2 = secondWords.get(r.nextInt(secondWords.size()));
    StringBuilder genWords = new StringBuilder();
    while (getSyllables(genWords.toString()) < MINLINELENGTH) {
      if (!word1.equals("*START*") && !word1.equals("*STOP*")) {
        genWords.insert(0, word1);
      }
      if (chain.get(word1) == null || chain.get(word1).get(word2) == null) {
        int syls = getSyllables(genWords.toString());
        if (syls > MINLINELENGTH && syls < MAXLINELENGTH) {
          return genWords.toString();
        } else {
          return null;
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

    int syls = getSyllables(genWords.toString());
    if (syls > MINLINELENGTH && syls < MAXLINELENGTH) {
      return genWords.toString();
    } else {
      return null;
    }
  }

  private int getSyllables(String line) {
    int sum = 0;
    String[] lines = line.split(" ");
    for (int i = 0; i < lines.length; i++) {
      sum += Datamuse.getSyllables(lines[i]);
    }

    return sum;
  }
}