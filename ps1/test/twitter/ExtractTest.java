/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment
     * looks like.
     * Make sure you have partitions.
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-18T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-18T13:00:00Z");
    private static final Instant d5 = Instant.parse("2016-02-18T14:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "@alyssa sup?", d3);
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "@bbitdiddle not much", d4);
    private static final Tweet tweet5 = new Tweet(5, "mike", "@Bbitdiddle @Alyssa message me", d5);
    private static final Tweet tweet6 = new Tweet(6, "bob", "I don't know what to say @mike!", d2);
    private static final Tweet tweet7 = new Tweet(7, "mike", "my e-mail is mike@gmail.com", d4);

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

    // tests for getTimespan

    // covers tweets.size() = 1
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    // covers unsorted tweets with multiple tweets
    @Test
    public void testGetTimespanMultipleTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet1, tweet2, tweet4));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d4, timespan.getEnd());
    }

    // covers tweets of length zero
    @Test(expected = IllegalArgumentException.class)
    public void testGetTimespanWithNullList() {
        Extract.getTimespan(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTimespanWithEmptyList() {
        Extract.getTimespan(new ArrayList<Tweet>());
    }

    // covers different tweets of the same time
    @Test
    public void testGetTimespanSameTimeTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet6));

        assertEquals("expected start", d2, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    // checks for mutation
    @Test
    public void testGetTimespanMutation() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
        Extract.getTimespan(tweets);

        assertEquals("expected unmodified list", Arrays.asList(tweet1, tweet2), tweets);

    }

    // tests for getMentioned users

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    // covers tweets.size() = 0
    @Test
    public void testGetMentionedUsersZeroTweets() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(new ArrayList<Tweet>());

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    // covers user name mentioned once
    // tweets.size() > 0
    @Test
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));

        assertTrue("expected set containing 'alyssa'", mentionedUsers.contains("alyssa"));
        assertTrue("expected set containing 'bbitdiddle'", mentionedUsers.contains("bbitdiddle"));
    }

    // covers user name mentioned several times
    // different cases
    @Test
    public void testGetMentionedUsersSeveralMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4, tweet5));

        assertTrue("expected set containing 'alyssa'", mentionedUsers.contains("alyssa"));
        assertTrue("expected set containing 'bbitdiddle'", mentionedUsers.contains("bbitdiddle"));
        assertFalse("expected set not containing 'Alyssa'", mentionedUsers.contains("Alyssa"));
        assertFalse("expected set not containing 'Bbitdiddle'", mentionedUsers.contains("Bbitdiddle"));
    }

    // checks for valid username
    @Test
    public void testGetMentionedUsersValidUsername() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6, tweet7));

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    // checks for mutation
    @Test
    public void estGetMentionedUsersMutation() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
        Extract.getMentionedUsers(tweets);

        assertEquals("expected unmodified list", Arrays.asList(tweet1, tweet2), tweets);

    }

    public static Set<String> toLowerCase(Set<String> strings) {
        Set<String> result = new HashSet<>();

        for (String string : strings) {
            result.add(string.toLowerCase());
        }

        return result;
    }

}
