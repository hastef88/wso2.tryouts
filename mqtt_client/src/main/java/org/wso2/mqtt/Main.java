package org.wso2.mqtt;/*
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

import org.eclipse.paho.client.mqttv3.MqttException;
import org.wso2.mqtt.client.MQTTClientConnectionConfiguration;
import org.wso2.mqtt.client.Util;

import java.util.ArrayList;

public class Main {

    public static int publisherCount = 1 ;
    public static int subscriberCount = 1 ;
    public static int messagesPerPublisher = 10;
    public static String topicName = "TestTopic";
    public static int qosOption = -1; //-1 will mixup. 0


    public static void main(String[] args) throws MqttException {

        //Set from arguments
        if (args != null) {
            if (args.length > 0) {
                publisherCount = Integer.valueOf(args[0]);
            }
            if (args.length > 1) {
                subscriberCount = Integer.valueOf(args[1]);
            }
            if (args.length > 2) {
                messagesPerPublisher = Integer.valueOf(args[2]);
            }
            if (args.length > 3) {
                topicName = args[3];
            }
            if (args.length > 4) {
                qosOption = Integer.valueOf(args[4]);
            }
        }

        //------------------

        //Will create the initial configuration
        MQTTClientConnectionConfiguration configuration = new MQTTClientConnectionConfiguration();

        configuration.setBrokerHost(Util.MQTTClientConstants.BROKER_HOST);
        configuration.setBrokerProtocol(Util.MQTTClientConstants.BROKER_PROTOCOL);
        configuration.setBrokerPort(Util.MQTTClientConstants.BROKER_PORT);
        configuration.setBrokerPassword(Util.MQTTClientConstants.BROKER_PASSWORD);
        configuration.setBrokerUserName(Util.MQTTClientConstants.BROKER_USER_NAME);
        configuration.setCleanSession(true);

        //create the subscribers
        for(int subscribers=0;subscribers < subscriberCount;subscribers++){
            MQTTClientEngine.getInstance().createSubscriberConnection(configuration,topicName,qosOption);
        }

        // do publishing
        String symbol = null;
        String payload = null;

        for(int publishers=0;publishers < publisherCount;publishers++){

            String template = "hello";

            ArrayList<byte[]> payloads = new ArrayList<byte[]>();
            for (int i=0;i<messagesPerPublisher;i++) {
                payloads.add((template+i).getBytes());
            }

            MQTTClientEngine.getInstance().createPublisherConnection(configuration, topicName, qosOption, payloads);
        }

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                try {
                    MQTTClientEngine.shutdown();
                    System.out.println("Shutdown hook ran!");
                } catch (MqttException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        });

    }
}
