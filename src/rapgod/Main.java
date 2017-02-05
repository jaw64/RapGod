package rapgod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import hab.rapgod.util.WikipediaParser;
import markov.Markov;

public class Main {
  public static void main(String[] args) throws IOException {
//    String corpus = new String(Files.readAllBytes(Paths.get(args[0])));
    String corpus = WikipediaParser.parseArticle("The Office (U.S. TV series)");
    Markov chain = new Markov(corpus);
    System.out.println(chain.generateLine());
  }
}
