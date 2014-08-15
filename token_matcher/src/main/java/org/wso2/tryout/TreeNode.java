package org.wso2.tryout;

/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the tree data structure used to tokenize and store strings for later matching.
 *
 * Problem :
 *
 * A message broker needs a way to find all subscribers listening to a destination (including hierarchical subscriptions).
 * A client should be able to subscribe to multiple topics relevant to a given context with a single subscription using this pattern.
 * e.g. To capture all news for all categories for today - can subscribe like "news.+.today".
 *
 * This class provides an efficient, abstract way to manage such subscriptions. (inspired by moquette subscription logic)
 *
 * Hierarchy definitions :
 *  dest.* - captures all immediate child elements to "dest".
 *  dest.# - captures all immediate child elements + parent (e:g: "dest" and "dest.news")
 *  dest.news - captures exactly "dest.news" destination
 *  dest.+.news - captures "dest.sports.news" and "dest.crisis.news"
 */
public class TreeNode {

    private String key;
    private HashMap<String,TreeNode> children;
    private TreeNode parent; // reference to parent is needed to handle recursive deletions

    public String matchingString;

    public final static String DELIM = ".";
    public final static String ALL_CHILDREN = "*";
    public final static String ALL_CHILDREN_AND_PARENT = "#";
    public final static String IMMEDIATE_CHILDREN_WITH_SUFFIX = "+";

    // a static arraylist used to maintain matching strings on a given request.
    private static ArrayList<String> matchingStrings;

    public TreeNode(String key, TreeNode parent) {
         this.key = key;
         this.children = new HashMap<String, TreeNode>();
         this.parent = parent;
    }

    public HashMap<String,TreeNode> getChildren() {
        return children;
    }

    /**
     * recursive function to split a string and add its components to relevant locations in the tree
     * @param rawInput
     */
    public void addChildren(String rawInput) {

        // there are no more branches/leaves to add
        if (rawInput.isEmpty()) {
            return;
        }

        String token;
        String tail = null;

        if (rawInput.contains(DELIM)) {
            token = rawInput.substring(0,rawInput.indexOf(DELIM));
            tail = rawInput.substring(rawInput.indexOf(DELIM) + 1,rawInput.length());
        } else {
            token = rawInput;
        }

        TreeNode child;

        //last leaf in the tree
        if (tail == null) {
            if (!this.children.containsKey(token)) {
                child = new TreeNode(token,this);
                this.children.put(token,child);
            }
            return;
        }

        // any middle branch of the tree
        if (this.children.containsKey(token))  {
            this.children.get(token).addChildren(tail);
        } else {
            child = new TreeNode(token,this);
            child.addChildren(tail);
            this.children.put(token,child);
        }

    }

    /**
     * recursive function to delete references of a given subscription string. does not delete nodes if they have children.
     * e.g. if dest.sports.+ is to be deleted, and there is another subscription for dest.sports.+.today,
     * deletion wont be done to save the second subscription.
     * @param rawInput
     */
    public void deleteChildren(String rawInput) {
        // there are no more branches/leaves to add
        if (rawInput.isEmpty()) {
            return;
        }

        String token;
        String tail = null;

        if (rawInput.contains(DELIM)) {
            token = rawInput.substring(0,rawInput.indexOf(DELIM));
            tail = rawInput.substring(rawInput.indexOf(DELIM) + 1,rawInput.length());
        } else {
            token = rawInput;
        }

        //last leaf in the tree
        if (tail == null) {
            if (this.children.containsKey(token) && this.children.get(token).getChildren().size() == 0) {
                this.children.remove(token);

                if (this.getChildren().size() == 0 && this.parent != null) {
                    this.parent.children.remove(this.key);
                }
            }
        } else {

            // any middle branch of the tree
            if (this.children.containsKey(token)) {
                if (this.children.get(token).getChildren().size() == 0)  {
                    this.children.remove(token);
                } else {
                    this.children.get(token).deleteChildren(tail);

                    if (this.getChildren().size() == 0 && this.parent != null) {
                        this.parent.children.remove(this.key);
                    }
                }
            }
        }
    }

    /**
     * wrapper method that calls the recursive findMatches function
     * @param strToMatch
     * @return
     */
    public ArrayList<String> findMatches(String strToMatch) {
        TreeNode.flushMatches();
        //matchingString is null in the initial call
        this.findMatches(strToMatch,null);
        ArrayList<String> matches = TreeNode.matchingStrings;
        TreeNode.flushMatches();

        return matches;
    }

    /**
     * recursive method to find and build matching subscriptions for a given string.
     * @param strToMatch
     * @param matchingString
     */
    private void findMatches(String strToMatch, String matchingString) {

        if (matchingString == null) {
            matchingString = "";
        }

        this.matchingString = matchingString;

        // there are no more branches/leaves to add
        if (strToMatch.isEmpty()) {
            return;
        }

        String token;
        String tail = null;

        if (strToMatch.contains(DELIM)) {
            token = strToMatch.substring(0,strToMatch.indexOf(DELIM));
            tail = strToMatch.substring(strToMatch.indexOf(DELIM) + 1,strToMatch.length());
        } else {
            token = strToMatch;
        }

        //last leaf in the tree
        if (tail == null) {

            if (this.children.size() > 0) {
                for (Map.Entry<String,TreeNode> child : this.children.entrySet()) {

                    String cKey = child.getKey();
                    TreeNode cNode = child.getValue();

                    cNode.matchingString = this.matchingString;

                    if (cKey.equals(token) || cKey.equals(ALL_CHILDREN) || cKey.equals(ALL_CHILDREN_AND_PARENT)) {

                        if (cKey.equals(token)) {
                            cNode.matchingString += token;
                            //if (child.getValue().getChildren().size() == 0)
                        } else if (cKey.equals(ALL_CHILDREN)) {
                            cNode.matchingString += ALL_CHILDREN;
                            //if (cNode.getChildren().size() == 0)
                        } else if (cKey.equals(ALL_CHILDREN_AND_PARENT)) {
                            cNode.matchingString += ALL_CHILDREN_AND_PARENT;
                            //if (cNode.getChildren().size() == 0)
                        }

                        addMatchingString(cNode.matchingString);
                    }

                    //Special Scenario - # Should catch immediate parent too.
                    if (cNode.getChildren().containsKey(ALL_CHILDREN_AND_PARENT)) {

                        if (!cNode.matchingString.endsWith(DELIM))
                            cNode.matchingString += DELIM;

                        cNode.matchingString += ALL_CHILDREN_AND_PARENT;
                        addMatchingString(cNode.matchingString);
                    }

                }
            } else {
                if (!this.matchingString.endsWith(DELIM))
                    addMatchingString(this.matchingString);
            }

            return;
        }

        // Anywhere other than last leaf
        if (this.children.size() > 0) {

            for (Map.Entry<String,TreeNode> child : this.children.entrySet()) {

                String cKey = child.getKey();
                TreeNode cNode = child.getValue();

                cNode.matchingString = this.matchingString;

                if (cKey.equals(token) || cKey.equals(IMMEDIATE_CHILDREN_WITH_SUFFIX)) {

                    if (cKey.equals(token)) {
                        cNode.matchingString += token + DELIM;
                    } else if (cKey.equals(IMMEDIATE_CHILDREN_WITH_SUFFIX)) {
                        cNode.matchingString += IMMEDIATE_CHILDREN_WITH_SUFFIX + DELIM;
                    }
                    cNode.findMatches(tail,cNode.matchingString);
                }
            }
        } else {
            if (!this.matchingString.endsWith(DELIM))
                addMatchingString(this.matchingString);
        }

    }

    /**
     * this flushes any previous matches before starting match lookup for a new string
     */
    public static void flushMatches() {
       matchingStrings = new ArrayList<String>();
    }

    /**
     * method to add matching strings to static collection without duplications.
     * @param str
     */
    private void addMatchingString(String str) {
        if (!matchingStrings.contains(str)) {
            matchingStrings.add(str);
        }
    }
}
