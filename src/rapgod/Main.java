package rapgod;

import hab.rapgod.util.WikipediaParser;
import markov.Markov;

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
