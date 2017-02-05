package hab.rapgod;

import hab.rapgod.util.WikipediaParser;

public final class Application {

	/**
	 * 
	 * @param argv
	 */
	public static void main(String[] argv) {
		try {
			WikipediaParser.parseArticle("Jesus");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
