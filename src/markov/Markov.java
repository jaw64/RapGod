package markov;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class Markov {
  private String corpus;
  private Map<String, Map<String, List<String>>> chain = new HashMap<>();
  private final int LINESIZE = 10;

  public Markov(String corpus) {
    this.corpus = corpus;
    triples();
    //System.out.println(chain);
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

  public String generateLine() {
    Random r = new Random();
    String word1 = "*STOP*";
    List<String> secondWords = new ArrayList<>(chain.get(word1).keySet());
    String word2 = secondWords.get(r.nextInt(secondWords.size()));
    StringBuilder genWords = new StringBuilder();
    for (int i = 0; i < LINESIZE; i++) {
      genWords.insert(0, word1);
      if (chain.get(word1) == null || chain.get(word1).get(word2) == null) {
        return generateLine();
      }
      List<String> thirdWords = chain.get(word1).get(word2);
      String word3 = thirdWords.get(r.nextInt(thirdWords.size()));
      word1 = word2;
      word2 = word3;
      genWords.insert(0," ");
    }
    genWords.insert(0, word2);
    return genWords.toString();
  }
}