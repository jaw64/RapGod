package rapgod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import markov.Markov;

public class Main {
  public static void main(String[] args) throws IOException {
    String corpus = new String(Files.readAllBytes(Paths.get(args[0])));
    Markov chain = new Markov(corpus.replace(".", "\n"));
    System.out.println(chain.generateLine());
  }
}
