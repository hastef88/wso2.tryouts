package org.wso2.mqtt.client.callback;/*
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
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.wso2.mqtt.client.Util;

/**
    Common callback handler to provide basic information about the mqtt client.
    e:g: - number of messages published/subscribed
 */
public class CallbackHandler implements MqttCallback {

    private int messageCount;

    public int getMessageCount() {
        return messageCount;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Util.log("Connection Lost - Client Disconnected!!!");
        Util.log("No of messages : " + getMessageCount());
        Util.log("----------------");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        //must be implemented in a subscribing client callback handler
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //must be implemented in a publishing client callback handler
    }

    public void incrementMessageCount() {
        messageCount++;
        //System.out.println("Delivered Message Count : "+messageCount);
    }
}
