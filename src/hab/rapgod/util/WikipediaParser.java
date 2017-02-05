package hab.rapgod.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public final class WikipediaParser {

	private WikipediaParser() {
	}

	private static String sanitizeInput(String s) {
		s = s.replaceAll("[=]{2,}?.*[=]{2,}", "").replaceAll("[^a-zA-Z0-9'=.]", " ")
				.replaceAll("\\.+\\s*", System.lineSeparator()).replaceAll("\\s+'", " ").replaceAll("'\\s+", " ")
				.replaceAll("[ ]+", " ").toLowerCase();
		return s;
	}

	private static String getJSON(String url) {
		URL datamuse;
		URLConnection dc;
		StringBuilder s = null;
		try {
			datamuse = new URL(url);
			dc = datamuse.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(dc.getInputStream(), "UTF-8"));
			String inputLine;
			s = new StringBuilder();
			while ((inputLine = in.readLine()) != null)
				s.append(inputLine);
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s != null ? s.toString() : null;
	}

	public static String parseExtract(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		String ret = obj.getJSONObject("query").getJSONArray("pages").getJSONObject(0).getString("extract");
		return ret;
	}

	public static String parseArticle(String articleTitle) throws IOException, JSONException {
		articleTitle = articleTitle.replaceAll("\\s+", "_");
		String text = getJSON(
				"https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&formatversion=2&explaintext=1&titles="
						+ articleTitle);
		text = parseExtract(text);
		text = sanitizeInput(text);
		BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt"));
		writer.write(text);
		writer.close();
		return text;
	}
}