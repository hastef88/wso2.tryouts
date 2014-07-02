package org.wso2.training;

import javax.jms.JMSException;
import javax.naming.NamingException;

/**
 * Created with IntelliJ IDEA.
 * User: hasithad
 * Date: 6/13/14
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */

public class Main {

    public static void main(String[] args) throws JMSException, NamingException {

        String topicName = "testTopic";
        int messageCount = 1000;
        String op = ""; //RUN_RECEIVER, RUN_SENDER
        int waitInterval = 100;
        long expirationTTL = 50;

        if (args != null) {
            if (args.length > 0) {
                topicName = args[0];
            }
            if (args.length > 1) {
                messageCount = Integer.valueOf(args[1]);
            }
            if (args.length > 2) {
                op = args[2];
            }
            if (args.length > 3) {
                waitInterval = Integer.valueOf(args[3]);
            }
            if (args.length > 4) {
                expirationTTL = Long.valueOf(args[4]);
            }
        }

        if (op.equals("RUN_RECEIVER"))  {
            TopicReceiver.start(topicName);
        } else if (op.equals("RUN_SENDER")) {
            TopicPublisher.start(messageCount,topicName,waitInterval,expirationTTL);
        } else {
            TopicReceiver.start(topicName);
            TopicPublisher.start(messageCount,topicName,waitInterval,expirationTTL);
        }

        System.out.println("Complete");

    }
}
