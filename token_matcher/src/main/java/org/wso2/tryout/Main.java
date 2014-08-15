package org.wso2.tryout;/*
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

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Random;

public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    // To use for generating random datasets
    final static int SUBSCRIPTION_COUNT = 100;
    final static int DESTINATION_COUNT = 100;
    final static int MAX_LEVELS = 5;
    //--------------------------------------

    static String[] subscriptions = new String[]{"dest",
                                                    "dest.*",
                                                    "dest.#",
                                                    "dest.+.news",
                                                    "dest.sports.news",
                                                    "dest.sports.#",
                                                    "dest.sports.*",
                                                    "dest.+.+.today",
                                                    "dest2.+.news",
                                                    "dest2.+.news.+.hehe",
                                                    "dest2.+.news.today.*"};

    static String[] inputDestinations = new String[]{"dest.1",
                                                    "dest.sports.news",
                                                    "dest.2",
                                                    "dest",
                                                    "dest.sports",
                                                    "dest.weather.news",
                                                    "dest.sports.news.today",
                                                    "dest.sports.news.yesterday",
                                                    "dest2.weather.news",
                                                    "dest2.sports.news.today.hehe"};

    //This will store the subscriptions in a Tree structure
    static TreeNode subscriptionHierarchy = new TreeNode("subscriptions",null);

    public static void main(String[] args) throws IOException {

        // to generate unknown sets of data - comment to test with above fixed, more meaningful dataset
        subscriptions = generateRandomSubscriptions(SUBSCRIPTION_COUNT,MAX_LEVELS);
        inputDestinations = generateRandomDestinations(DESTINATION_COUNT,MAX_LEVELS);
        //---------------------------------------------------------------------------------------------

        for (String subscription : subscriptions) {
            addSubscription(subscription);
        }

        logger.info(new ObjectMapper().writeValueAsString(subscriptionHierarchy));

        for (String inputDestination : inputDestinations) {
            addDestinationToSubscription(inputDestination);
        }

        logger.info("Deleting subscriptions");

        for (int i=0;i<subscriptions.length;i++) {
                subscriptionHierarchy.deleteChildren(subscriptions[i]);
        }

        logger.info("Deletion successful");
        logger.info(new ObjectMapper().writeValueAsString(subscriptionHierarchy));

    }

    public static void addSubscription(String sub) {
        subscriptionHierarchy.addChildren(sub);
    }

    public static void addDestinationToSubscription(String dest) throws IOException {
        logger.info("==============================================================");
        logger.info("Matches for " + dest);
        logger.info(new ObjectMapper().writeValueAsString(subscriptionHierarchy.findMatches(dest)));
        logger.info("==============================================================");
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * To generate random unknown set of subscriptions spanning to given number of levels.
     * @param count
     * @param max_levels
     * @return
     */
    public static String[] generateRandomSubscriptions(int count, int max_levels) {
        String[] arr = new String[count];

        for (int i=0;i<arr.length;i++) {

            //generate random char
            Random r = new Random();
            char root = (char)(r.nextInt(26) + 'a');

            String sub = String.valueOf(root);

            int levels = randInt(0,max_levels);

            if (levels > 0) {

                sub += TreeNode.DELIM;

                for (int j=0;j<levels;j++) {

                    if (j < levels -1) {

                        int tokenType = randInt(0,1);

                        switch (tokenType) {
                            case 0:
                                sub += (char)(r.nextInt(26) + 'a') + TreeNode.DELIM;
                                break;
                            case 1:
                                sub += TreeNode.IMMEDIATE_CHILDREN_WITH_SUFFIX + TreeNode.DELIM;
                                break;
                            default:
                                break;
                        }
                    } else {

                        int tokenType = randInt(0,2);

                        switch (tokenType) {

                            case 0:
                                sub += TreeNode.ALL_CHILDREN;
                                break;
                            case 1:
                                sub += TreeNode.ALL_CHILDREN_AND_PARENT;
                                break;
                            case 2:
                                sub += (char)(r.nextInt(26) + 'a');
                            default:
                                break;
                        }
                    }
                }
            }

            arr[i] = sub;
        }

        return arr;
    }

    /**
     * To generate random unknown set of destinations to match with above subscriptions
     * @param count
     * @param max_levels
     * @return
     */
    public static String[]  generateRandomDestinations(int count, int max_levels) {
        String[] arr = new String[count];

        for (int i=0;i<arr.length;i++) {

            //generate random char
            Random r = new Random();
            char root = (char)(r.nextInt(26) + 'a');

            String sub = String.valueOf(root);

            int levels = randInt(0,max_levels);

            if (levels > 0) {

                sub += TreeNode.DELIM;

                for (int j=0;j<levels;j++) {

                    sub += (char)(r.nextInt(26) + 'a');

                    if (j < levels -1)
                        sub += TreeNode.DELIM;
                }
            }

            arr[i] = sub;
        }

        return arr;
    }
}
