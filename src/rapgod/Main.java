package rapgod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import hab.rapgod.util.WikipediaParser;
import java.util.Arrays;
import markov.Markov;
import datamuse.Datamuse;
import datamuse.JSONParse;

public class Main {
  public static void main(String[] args) throws IOException {
    String corpus = new String(Files.readAllBytes(Paths.get(args[0])));
    Markov chain = new Markov(corpus.replace(".", "\n").replaceAll("\\p{P}", ""));
    chain.generate(10);
  }
}
