package org.wso2.mqtt.client;

/**
 * Created by pamod on 3/4/14.
 * Updated by HasithaD on 28/07/2014
 */
public class MQTTClientConnectionConfiguration {
    private String brokerProtocol = null;
    private String brokerHost = null;
    private String brokerPort = null;
    private String brokerPassword = null;
    private String brokerUserName = null;
    private boolean cleanSession = false;

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public String getBrokerProtocol() {
        return brokerProtocol;
    }

    public void setBrokerProtocol(String brokerProtocol) {
        this.brokerProtocol = brokerProtocol;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

    public void setBrokerHost(String brokerHost) {
        this.brokerHost = brokerHost;
    }

    public String getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(String brokerPort) {
        this.brokerPort = brokerPort;
    }

    public String getBrokerPassword() {
        return brokerPassword;
    }

    public void setBrokerPassword(String brokerPassword) {
        this.brokerPassword = brokerPassword;
    }

    public String getBrokerUserName() {
        return brokerUserName;
    }

    public void setBrokerUserName(String brokerUserName) {
        this.brokerUserName = brokerUserName;
    }

    public String getBrokerURL(){
        return new String(brokerProtocol + "://" + brokerHost +
                ":" + brokerPort);
    }
}
