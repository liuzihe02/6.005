/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.sql.Time;
import java.time.Instant;

import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *               list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        // check for preconditions
        if (tweets == null || tweets.isEmpty()) {
            throw new IllegalArgumentException("List of Tweets is empty!");
        }

        // move on, we know there's at least one element
        Instant minimum = tweets.get(0).getTimestamp();
        Instant maximum = tweets.get(0).getTimestamp();

        // some repetitive code here iterating over first element, but its okay
        // if we were to start from second elem, need to do some more work
        for (Tweet element : tweets) {
            Instant cur = element.getTimestamp();
            // current time is before
            if (cur.isBefore(minimum)) {
                minimum = cur;
                // if current time is not before, then check if maximum
            } else {
                if (cur.isAfter(maximum)) {
                    maximum = cur;
                }
            }
        }
        ;
        // call the constructor
        return new Timespan(minimum, maximum);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * static means this is a class method
     * 
     * @param tweets
     *               list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by
     *         any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> out = new HashSet<>();

        for (Tweet element : tweets) {
            // only if the username doesnt already exist;
            /*
             * in regex \s means any whitespace charac like newline \n or tab \t
             * we use \\s in java becasue backlashes need to be escaped in java
             * \\s+ means any number of this
             */
            String[] words = element.getText().split("\\s+");
            for (String word : words) {
                if (Pattern.matches("^@[A-Za-z0-9_-]+$", word)) {
                    out.add(word.substring(1).toLowerCase());
                }
            }
        }
        return out;
    }
}
