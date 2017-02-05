package rapgod;

<<<<<<< HEAD
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
	public static void main(String[] args) throws Exception {
		// String corpus = new String(Files.readAllBytes(Paths.get(args[0])));
		String corpus = WikipediaParser.parseArticle("Barack Obama");
		// Markov chain = new Markov(corpus.replace(".",
		// "\n").replaceAll("\\p{P}", ""));
		Markov chain = new Markov(corpus);
		chain.generate(8);
	}
}
