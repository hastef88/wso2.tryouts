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

    public QueueConnection queueConn;
    public QueueSession queueSession;
    public MessageConsumer msgConsumer;

    public MyMessageListener(QueueConnection queueConn, QueueSession queueSession, MessageConsumer msgConsumer) {

        this.queueConn = queueConn;
        this.queueSession = queueSession;
        this.msgConsumer = msgConsumer;
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Received Message : " + ((TextMessage) message).getText());
            count++;

            if (count > 20000) {
                System.out.println("no of messages received : " + count);
                msgConsumer.close();
                queueSession.close();
                queueConn.stop();
                queueConn.close();
            }

            //Thread.sleep(1000);

        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
