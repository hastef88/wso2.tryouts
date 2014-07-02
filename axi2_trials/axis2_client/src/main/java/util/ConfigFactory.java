package util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by hasithad on 4/28/14.
 */
public class ConfigFactory {

    private static String orderProcessingEndpoint;
    private static String productEndpoint;
    private static String customerEndpoint;

    private static String endpointRequestNamespace;
    private static String transporturl;

    public static void initialize() {

        Properties config = new Properties();

        try {
            config.load(ConfigFactory.class.getClassLoader().getResourceAsStream("client.properties"));

            orderProcessingEndpoint = config.getProperty("endpoint.orderprocessing");
            productEndpoint = config.getProperty("endpoint.products");
            customerEndpoint = config.getProperty("endpoint.customers");

            if (config.containsKey("transport.url")) {
                transporturl = config.getProperty("transport.url");
            }
            endpointRequestNamespace = config.getProperty("endpoint.namespace");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getOrderProcessingEndpoint() {
        return orderProcessingEndpoint;
    }

    public static void setOrderProcessingEndpoint(String orderProcessingEndpoint) {
        ConfigFactory.orderProcessingEndpoint = orderProcessingEndpoint;
    }

    public static String getProductEndpoint() {
        return productEndpoint;
    }

    public static void setProductEndpoint(String productEndpoint) {
        ConfigFactory.productEndpoint = productEndpoint;
    }

    public static String getCustomerEndpoint() {
        return customerEndpoint;
    }

    public static void setCustomerEndpoint(String customerEndpoint) {
        ConfigFactory.customerEndpoint = customerEndpoint;
    }

    public static String getEndpointRequestNamespace() {
        return endpointRequestNamespace;
    }

    public static void setEndpointRequestNamespace(String endpointRequestNamespace) {
        ConfigFactory.endpointRequestNamespace = endpointRequestNamespace;
    }

    public static String getTransporturl() {
        return transporturl;
    }

    public static void setTransporturl(String transporturl) {
        ConfigFactory.transporturl = transporturl;
    }
}
