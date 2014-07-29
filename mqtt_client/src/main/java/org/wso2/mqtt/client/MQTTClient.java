package org.wso2.mqtt.client;/*
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

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.wso2.mqtt.client.callback.CallbackHandler;
import org.wso2.mqtt.client.callback.PublisherCallbackHandler;
import org.wso2.mqtt.client.callback.SubscriptionCallbackHandler;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MQTTClient implements Runnable {

    // Private instance variables
    private MqttClient mqttClient;
    private boolean clean;
    private Util.ClientOperation mqttOperation;

    private CallbackHandler callbackHandler;

    // mqtt parameters
    private String mqttClientID; // unique identifier for mqtt client - less than or equal to 23 characters
    private MqttConnectOptions connection_options;
    private String broker_url;
    private String password;
    private String userName;
    private String topic; //destination
    private int qos; // at-most-once(0), atleast-once(1), exactly-once(2)
    private ArrayList<byte[]> messagePayLoads;

    public MQTTClient(MQTTClientConnectionConfiguration configuration, String clientID, Util.ClientOperation operation, String topic, int qos, ArrayList<byte[]> payloads) {

        //Initializing the variables locally
        this.broker_url = configuration.getBrokerURL();
        this.mqttClientID = clientID;
        this.clean = configuration.isCleanSession();
        this.password = configuration.getBrokerPassword();
        this.userName = configuration.getBrokerUserName();
        this.mqttOperation = operation;
        this.topic = topic;
        this.qos = qos;
        this.messagePayLoads = payloads;

        //Store messages until server fetches them
        String temp_directory = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(temp_directory);


        try {
            // Construct the connection options object that contains connection parameters
            // such as cleanSession and LWT
            connection_options = new MqttConnectOptions();
            connection_options.setCleanSession(clean);

            if (password != null) {
                connection_options.setPassword(this.password.toCharArray());
            }
            if (userName != null) {
                connection_options.setUserName(this.userName);
            }

            // Construct MQTT client
            mqttClient = new MqttClient(this.broker_url, clientID, dataStore);

            // Set callback handler
            if (mqttOperation.equals(Util.ClientOperation.SUBSCRIBE))
                callbackHandler = new SubscriptionCallbackHandler();
            else {
                callbackHandler = new PublisherCallbackHandler();
            }
            mqttClient.setCallback(callbackHandler);

        } catch (MqttException e) {
            e.printStackTrace();
            Util.log("Unable to set up client: " + e.toString());
            System.exit(1);
        }
    }


    public void publish(String topicName, int qos, ArrayList<byte[]> payloads) throws MqttException {

        // Connect to the MQTT server
        Util.log("Connecting to " + broker_url + " with client ID " + mqttClient.getClientId());
        mqttClient.connect(connection_options);
        Util.log("Connected");

        String time = new Timestamp(System.currentTimeMillis()).toString();
        Util.log("Publishing at: " + time + " to topic \"" + topicName + "\" qos " + qos);

        if (payloads != null) {
            for (byte[] payload : payloads) {
                // Create and configure message
                MqttMessage message = new MqttMessage(payload);
                message.setQos(qos);

                // Send message to server, control is not returned until
                // it has been delivered to the server meeting the specified
                // quality of service.
                mqttClient.publish(topicName, message);
            }
        }
    }

    public void subscribe(String topicName, int qos) throws MqttException {

        // Connect to the MQTT server
        mqttClient.connect(connection_options);
        Util.log("Connected to " + broker_url + " with client ID " + mqttClient.getClientId());

        // Subscribe to the requested topic
        // The QoS specified is the maximum level that messages will be sent to the client at.
        // For instance if QoS 1 is specified, any messages originally published at QoS 2 will
        // be downgraded to 1 when delivering to the client but messages published at 1 and 0
        // will be received at the same level they were published at.
        Util.log("Subscribing to topic \"" + topicName + "\" qos " + qos);
        mqttClient.subscribe(topicName, qos);

        //Will need to wait to receive all messages - subscriber closes on shutdown
    }

    @Override
    public void run() {
        if(mqttOperation.equals(Util.ClientOperation.SUBSCRIBE)){
            try {
                subscribe(this.topic, qos);
            } catch (MqttException e) {
                Util.log(e.getMessage());
                e.printStackTrace();
            }
        }
        else if(mqttOperation.equals(Util.ClientOperation.PUBLISH)){
            try {
               publish(topic,qos,messagePayLoads);
            } catch (MqttException e) {
                Util.log(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void shutdown() throws MqttException {

        Util.log("--------" + mqttOperation.toString() + " THREAD with clientID : "+mqttClientID+"--------");

        if (callbackHandler != null) {
            Util.log("No of messages : " + callbackHandler.getMessageCount());
        }

        if (mqttOperation.equals(Util.ClientOperation.SUBSCRIBE)) {
            mqttClient.unsubscribe(topic);
            Util.log("Subscriber unsubscribed");
        }
        mqttClient.disconnect();
        Util.log("Client Disconnected");
        Util.log("----------------");
    }

}
