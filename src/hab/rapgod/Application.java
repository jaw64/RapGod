package hab.rapgod;

import datamuse.Datamuse;
import datamuse.JSONParse;

public final class Application {
	
	public static String generatePhrase(String word) {
		int length = 8;
		StringBuilder sb = new StringBuilder();
		sb.append(word);
		String currWord = word;
		for (int i=0; i < length; i++) {
			String ret = Datamuse.frequentPredecessors(currWord);
			String[] pre = JSONParse.parseWords(ret);
			int[] scores = JSONParse.parseScores(ret);
			int rand = (int) (Math.random() * 20);
			sb.insert(0, pre[rand] + " ");
			currWord = pre[rand];
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param argv
	 */
	public static void main(String[] argv) {
		System.out.println(generatePhrase("day"));
	}
}
