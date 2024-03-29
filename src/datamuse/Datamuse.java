/*
* Copyright © 2015 S.J. Blair <https://github.com/sjblair>
* This work is free. You can redistribute it and/or modify it under the
* terms of the Do What The Fuck You Want To Public License, Version 2,
* as published by Sam Hocevar. See the COPYING file for more details.
*/

package datamuse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * A handler for making calls to the Datamuse RESTful API.
 *
 * @author sjblair
 * @since 21/02/15
 */
public final class Datamuse {

    private Datamuse() {}

    /**
     * Returns a list of similar words to the word/phrase supplied.
     * @param word A word of phrase.
     * @return A list of similar words.
     */
    public static String findSimilar(String word) {
        String s = word.replaceAll(" ", "+");
        return getJSON("http://api.datamuse.com/words?rd="+s);
    }

    /**
     * Returns a list of similar words to the word/phrase supplied beginning with the specified letter(s).
     * @param word A word or phrase.
     * @param startLetter The letter(s) the similar words should begin with.
     * @return A list of similar words.
     */
    public static String findSimilarStartsWith(String word, String startLetter) {
        String s = word.replaceAll(" ", "+");
        return getJSON("http://api.datamuse.com/words?rd="+s+"&sp="+startLetter+"*");
    }

    /**
     * Returns a list of similar words to the word/phrase supplied ending with the specified letter(s).
     * @param word A word or phrase.
     * @param endLetter The letter(s) the similar words should end with.
     * @return A list of similar words.
     */
    public static String findSimilarEndsWith(String word, String endLetter) {
        String s = word.replaceAll(" ", "+");
        return getJSON("http://api.datamuse.com/words?rd="+s+"&sp=*"+endLetter);
    }

    /**
     * Returns a list of words beginning and ending with the specified letters and with the specified number of letters
     * in between.
     * @param startLetter The letter(s) the similar words should start with.
     * @param endLetter The letter(s) the similar words should end with.
     * @param numberMissing The number of letters between the start and end letters
     * @return A list of matching words.
     */
    public static String wordsStartingWithEndingWith(String startLetter, String endLetter, int numberMissing) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberMissing; i++) {
            sb.append("?");
        }
        return getJSON("http://api.datamuse.com/words?sp=" + startLetter + sb + endLetter);
    }

    /**
     * Returns a list of words beginning and ending with the specified letters and with an unspecified number of letters
     * in between.
     * @param startLetter The letter(s) the similar words should start with.
     * @param endLetter The letter(s) the similar words should end with.
     * @return A list of matching words.
     */
    public static String wordsStartingWithEndingWith(String startLetter, String endLetter) {
        return getJSON("http://api.datamuse.com/words?sp=" + startLetter + "*" + endLetter);
    }

    /**
     * Find words which sound the same as the specified word/phrase when spoken.
     * @param word A word or phrase.
     * @return A list of words/phrases which sound similiar when spoken.
     */
    public static String soundsSimilar(String word) {
        String s = word.replaceAll(" ", "+");
        return getJSON("http://api.datamuse.com/words?sl=" + s);
    }

    /**
     * Find words which are spelt the same as the specified word/phrase.
     * @param word A word or phrase.
     * @return A list of words/phrases which are spelt similar.
     */
    public static String speltSimilar(String word) {
        String s = word.replaceAll(" ", "+");
        return getJSON("http://api.datamuse.com/words?sp=" + s);
    }

    /**
     * Returns suggestions for what the user may be typing based on what they have typed so far. Useful for
     * autocomplete on forms.
     * @param word The current word or phrase.
     * @return Suggestions of what the user may be typing.
     */
    public static String prefixHintSuggestions(String word) {
        String s = word.replaceAll(" ", "+");
        return getJSON("http://api.datamuse.com/sug?s=" + s);
    }
    
    /**
     * 
     * @param word
     * @return
     */
    public static String rhymesWith(String word) {
    	String s = word.replaceAll(" ", "+");
    	return getJSON("http://api.datamuse.com/words?rel_rhy=" + s);
    }
    
    /**
     * 
     * @param word
     * @return
     */
    public static String almostRhymesWith(String word) {
    	String s = word.replaceAll(" ", "+");
    	return getJSON("http://api.datamuse.com/words?rel_nry=" + s);
    }
    
    /**
     * 
     * @param word
     * @return
     */
    public static String frequentFollowers(String word) {
    	String s = word.replaceAll(" ", "+");
    	return getJSON("http://api.datamuse.com/words?rel_bga=" + s);
    }
    
    /**
     * 
     * @param word
     * @return
     */
    public static String frequentPredecessors(String word) {
    	String s = word.replaceAll(" ", "+");
    	return getJSON("http://api.datamuse.com/words?rel_bgb=" + s);
    }
    

    /**
     * Query a URL for their source code.
     * @param url The page's URL.
     * @return The source code.
     */
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

    public static int getSyllables(String word) {
        Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y'));
        if (word == null) {
            throw new NullPointerException("the word parameter was null.");
        } else if (word.length() == 0) {
            return 0;
        } else if (word.length() == 1) {
            return 1;
        }

        final String lowerCase = word.toLowerCase();

        /*if (exceptions.containsKey(lowerCase)) {
            return exceptions.get(lowerCase);
        }*/

        final String prunned;
        if (lowerCase.charAt(lowerCase.length() - 1) == 'e') {
            prunned = lowerCase.substring(0, lowerCase.length() - 1);
        } else {
            prunned = lowerCase;
        }

        int count = 0;
        boolean prevIsVowel = false;
        for (char c : prunned.toCharArray()) {
            final boolean isVowel = vowels.contains(c);
            if (isVowel && !prevIsVowel) {
                ++count;
            }
            prevIsVowel = isVowel;
        }
        /*count += addSyls.stream()
                .filter(pattern -> pattern.matcher(prunned).find())
                .count();
        count -= subSyls.stream()
                .filter(pattern -> pattern.matcher(prunned).find())
                .count();*/

        return count > 0 ? count : 1;
    }
}
