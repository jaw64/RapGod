package hab.rapgod.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
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
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return s != null ? s.toString() : null;
	}

	private static String parseExtract(String json) throws Exception {
		JSONObject obj = new JSONObject(json);
		String ret = obj.getJSONObject("query").getJSONArray("pages").getJSONObject(0).getString("extract");
		return ret;
	}

	private static String parseLinks(String json) throws Exception {
		JSONObject obj = new JSONObject(json);
		JSONObject pages = obj.getJSONObject("query").getJSONObject("pages");
		String id = JSONObject.getNames(pages)[0];
		JSONArray links = pages.getJSONObject(id).getJSONArray("links");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < links.length(); i++) {
			if (Math.random() < 0.05) {
				String title = links.getJSONObject(i).getString("title");
				String text = getCleanArticle(title);
				if (text != null) {
					try {
						text = parseExtract(text);
					} catch (Exception e) {
						continue;
					}
					text = sanitizeInput(text);
					sb.append(text);
				}
			}
		}
		return sb.toString();
	}

	public static String parseArticle(String articleTitle) throws Exception {
		articleTitle = articleTitle.replaceAll("\\s+", "_");
		String text = getCleanArticle(articleTitle);
		String dirtyText = getDirtyArticle(articleTitle);
		text = parseExtract(text);
		text = sanitizeInput(text);
		dirtyText = parseLinks(dirtyText);
		BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt"));
		writer.write(text);
		writer.close();
		return text;
	}

	private static String getCleanArticle(String articleTitle) throws Exception {
		return getJSON(
				"https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&formatversion=2&explaintext=1&titles="
						+ articleTitle);
	}

	private static String getDirtyArticle(String articleTitle) throws Exception {
		return getJSON(
				"https://en.wikipedia.org/w/api.php?action=query&prop=info&prop=links&pllimit=max&format=json&titles="
						+ articleTitle);
	}
}
