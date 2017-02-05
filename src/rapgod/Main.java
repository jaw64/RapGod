package rapgod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Arrays;

import org.json.JSONException;

import markov.Markov;
import datamuse.Datamuse;
import datamuse.JSONParse;
import hab.rapgod.util.WikipediaParser;

public class Main {
  public static void main(String[] args) throws IOException, JSONException {
    //String corpus = new String(Files.readAllBytes(Paths.get(args[0])));
    Markov chain = new Markov(WikipediaParser.parseArticle(args[0]));
    chain.generate(10);
  }
}
