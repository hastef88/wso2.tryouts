package org.wso2.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.wso2.mqtt.client.MQTTClient;
import org.wso2.mqtt.client.MQTTClientConnectionConfiguration;
import org.wso2.mqtt.client.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pamod on 3/3/14.
 * updated hasithad on 29/07/2014.
 */
public class MQTTClientEngine  {

    private static MQTTClientEngine instance;

    private static ArrayList<MQTTClient> publisherList;
    private static ArrayList<MQTTClient> subscriberList;

    private static ExecutorService clientControlSubscriptionThreads = Executors.newFixedThreadPool(10);
    private static ExecutorService clientControlPublisherThreads = Executors.newFixedThreadPool(10);

    public MQTTClientEngine() {
        publisherList = new ArrayList<MQTTClient>();
        subscriberList = new ArrayList<MQTTClient>();
    }

    public static synchronized MQTTClientEngine getInstance(){
        if(instance == null){
            instance = new MQTTClientEngine();
        }
        return instance;
    }

    public static String generateClientID(){
        String retVal = Util.generateRandomString(new Random(),"abcdefghijklmnopqrstuvwxyz0123456789",23);
        Util.log(retVal);
        return retVal;
    }

    public synchronized static void createSubscriberConnection(MQTTClientConnectionConfiguration configuration,String topicName, int qos){

        MQTTClient mqttClient = new MQTTClient(configuration, generateClientID(), Util.ClientOperation.SUBSCRIBE, topicName, qos, null);
        subscriberList.add(mqttClient);
        clientControlSubscriptionThreads.execute(mqttClient);
    }

    public synchronized static void createPublisherConnection(MQTTClientConnectionConfiguration configuration,String topicName, int qos, ArrayList<byte[]> payloads){

        MQTTClient mqttClient = new MQTTClient(configuration, generateClientID(), Util.ClientOperation.PUBLISH, topicName, qos, payloads);
        publisherList.add(mqttClient);
        clientControlPublisherThreads.execute(mqttClient);
    }

    public static void shutdown() throws MqttException {

        int subCount = 1;

        for (MQTTClient mc : subscriberList) {
            Util.log("--------shutting down subscriber "+subCount+"--------");
            mc.shutdown();
            subCount++;
        }

        int pubCount = 1;

        for (MQTTClient mc : publisherList) {
            Util.log("--------shutting down publisher "+pubCount+"--------");
            mc.shutdown();
            pubCount++;
        }
    }


}
