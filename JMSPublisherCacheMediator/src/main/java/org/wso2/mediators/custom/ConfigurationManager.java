package org.wso2.mediators.custom;

import java.io.IOException;

public class ConfigurationManager {

    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    public static final String CF_NAME_PREFIX = "connectionfactory.";
    public static final String CF_NAME = "ConnectionFactory";
    public static final String TOPIC_NAME_PREFIX = "topic.";

    public static final String CARBON_CLIENT_ID = "clientID";
    public static final String CARBON_VIRTUAL_HOST_NAME = "carbon";
    public static final String CARBON_DEFAULT_HOSTNAME = "localhost";
    public static final String CARBON_DEFAULT_PORT = "5672";


    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "admin";

    public static String getTCPConnectionURL() {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return new StringBuffer()
                .append("amqp://").append(ConfigurationManager.USER_NAME).append(":").append(ConfigurationManager.PASSWORD)
                .append("@").append(ConfigurationManager.CARBON_CLIENT_ID)
                .append("/").append(ConfigurationManager.CARBON_VIRTUAL_HOST_NAME)
                .append("?brokerlist='tcp://").append(ConfigurationManager.CARBON_DEFAULT_HOSTNAME).append(":").append(ConfigurationManager.CARBON_DEFAULT_PORT).append("'")
                .toString();
    }

}
