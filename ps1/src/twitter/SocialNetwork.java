/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames.
 * 
 * Map<person, all those who person is following>
 * 
 * Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even
 * exist
 * as a key in the map; this is true even if A is followed by other people in
 * the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *               a list of tweets providing the evidence, not modified by this
     *               method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     * @-mentions Bert in a tweet. This must be implemented. Other kinds
     *            of evidence may be used at the implementor's discretion.
     *            All the Twitter usernames in the returned social network must be
     *            either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> graph = new HashMap<>();

        for (Tweet tweet : tweets) {
            // this creates an immutable list containing a single object
            List<Tweet> singleList = Collections.singletonList(tweet);
            Set<String> users = Extract.getMentionedUsers(singleList);

            Set<String> values = graph.get(tweet.getAuthor());
            // no key, create a key with empty value
            if (values == null) {
                // Key does not exist, handle accordingly
                values = new HashSet<>();
                graph.put(tweet.getAuthor(), values);
            }

            // for each person that is mentioned
            // the HashSet will be key-Person;value=following
            for (String username : users) {

                // add the follower to the list
                // we reference values directly here

                // we cannot add ourselves!
                if (!username.equalsIgnoreCase(tweet.getAuthor()))
                    values.add(username);
            }
        }
        ;
        return graph;

    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *                     a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        // first i need to inverse our graph
        Map<String, Set<String>> inverse = inverseGraph(followsGraph);

        // Get the list of names sorted by the count of strings in the respective lists
        // this does not include those with no followers
        List<String> sortedNames = sortNamesBySetCount(inverse);

        // this is in ascending order, we want ascending order
        Collections.reverse(sortedNames);

        // make a new set for comparison
        return sortedNames;
    }

    public static List<String> sortNamesBySetCount(Map<String, Set<String>> map) {
        // Create a list to hold the names
        List<String> names = new ArrayList<>(map.keySet());

        // Sort the names based on the count of strings in the respective lists
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String name1, String name2) {
                // Compare based on the size of the lists
                int count1 = map.get(name1).size();
                int count2 = map.get(name2).size();
                return Integer.compare(count1, count2);
            }
        });

        return names;
    }

    public static Map<String, Set<String>> inverseGraph(Map<String, Set<String>> followsGraph) {
        Map<String, Set<String>> inverse = new HashMap<String, Set<String>>();
        for (Entry<String, Set<String>> entry : followsGraph.entrySet()) {

            // for each following, add to inverse
            for (String following : entry.getValue()) {
                Set<String> followers = inverse.get(following);
                // create an empty dict for the person who's following
                if (followers == null) {
                    // Key does not exist, handle accordingly
                    followers = new HashSet<>();
                    inverse.put(following, followers);
                }
                // also add in an entry for the being followed
                if (inverse.get(entry.getKey()) == null) {
                    inverse.put(entry.getKey(), new HashSet<>());
                }
                followers.add(entry.getKey());
            }
        }
        return inverse;
    }

}
