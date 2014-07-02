package org.wso2.training;

import javax.jms.*;

/**
 * Created with IntelliJ IDEA.
 * User: hasithad
 * Date: 6/24/14
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyMessageListener implements MessageListener {

    public int count = 0;

    public TopicConnection topicConn;
    public TopicSession topicSession;
    public TopicSubscriber subscriber;

    public MyMessageListener(TopicConnection topicConn, TopicSession topicSession, TopicSubscriber subscriber) {

        this.topicConn = topicConn;
        this.topicSession = topicSession;
        this.subscriber = subscriber;
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Received Message : " + ((TextMessage) message).getText());
            count++;

            if (count > 20000) {
                subscriber.close();
                topicSession.close();
                topicConn.stop();
                topicConn.close();
            }

            /*try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }  */

        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
