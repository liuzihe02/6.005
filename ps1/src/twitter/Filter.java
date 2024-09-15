/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import java.time.Instant;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *                 a list of tweets with distinct ids, not modified by this
     *                 method.
     * @param username
     *                 Twitter username, required to be a valid Twitter username as
     *                 defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> out = new ArrayList<Tweet>();
        for (Tweet element : tweets) {
            // note that == compares object references, NOT the contents of the object!
            // use equals() to compare contents
            // ignore lower or upper case here
            if (element.getAuthor().equalsIgnoreCase(username)) {
                out.add(element);
            }
        }
        ;
        return out;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *                 a list of tweets with distinct ids, not modified by this
     *                 method.
     * @param timespan
     *                 timespan
     * @return all and only the tweets in the list that were sent during the
     *         timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> valid = new ArrayList<>();
        Instant start = timespan.getStart();
        Instant end = timespan.getEnd();

        for (Tweet element : tweets) {
            if (element.getTimestamp().isBefore(end) & element.getTimestamp().isAfter(start)) {
                valid.add(element);
            }
        }
        ;
        return valid;
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *               a list of tweets with distinct ids, not modified by this
     *               method.
     * @param words
     *               a list of words to search for in the tweets.
     *               A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when
     *         represented as a sequence of nonempty words bounded by space
     *         characters
     *         and the ends of the string) includes *at least one* of the words
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama". The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> out = new ArrayList<Tweet>();

        // for each tweet
        for (Tweet element : tweets) {
            /*
             * ^ is a negator
             * \\w matches any word character which is A-Z, a-z, digits 0-9 and _
             * \\s matches whitespace characters
             * this replaces all those characters that is not these characters with empty
             * string
             * 
             * only keep word characs and space characs
             */
            String text = element.getText().toLowerCase().replaceAll("[^\\w\\s]", "");
            List<String> tweetWords = new ArrayList<>(Arrays.asList(text.split(" ")));

            // one bug is when tweetWord is "sup?" but we want to check with "sup"
            // another bug is when tweetWord is "super" but we want to check with "sup" and
            // it mistakenly adds it

            // for each word we want to search over
            for (String checkWord : words) {

                if (tweetWords.contains(checkWord.toLowerCase())) {
                    out.add(element);
                    // break out of for loop
                    break;
                }
            }
        }
        return out;
    }
}
