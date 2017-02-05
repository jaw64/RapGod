package hab.rapgod;

import java.io.IOException;

import hab.rapgod.util.WikipediaParser;

public final class Application {

	/**
	 * 
	 * @param argv
	 */
	public static void main(String[] argv) {
		try {
			WikipediaParser.parseArticle("Jesus");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
