package util;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import java.io.File;

/**
 * Created by hasithad on 5/8/14.
 */
public class ServiceContext {

    private static volatile ServiceContext instance;

    private static ConfigurationContext context;

    private MultiThreadedHttpConnectionManager httpConnectionManager;
    private HttpClient httpClient;

    public static void initialize() throws AxisFault {
        if (instance == null) {
            synchronized (ServiceContext.class) {
                instance = new ServiceContext();
            }
        }
    }

    public ServiceContext() throws AxisFault {
        context = ConfigurationContextFactory.createConfigurationContextFromFileSystem("client_repo",
                "client_repo" + File.separator + "axis2.xml");

        httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpClient = new HttpClient(httpConnectionManager);

    }

    public static ServiceClient prepareServiceClient(String serviceURI,ServiceClient serviceClient) {

        try {
            //serviceClient.getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,ConfigFactory.getTransporturl());
            //consumePattern.setUseSeparateListener(true);

            //serviceClient.getOptions().setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Constants.VALUE_TRUE);
            //serviceClient.getOptions().setProperty(HTTPConstants.CACHED_HTTP_CLIENT, instance.getHttpClient());
            //serviceClient.engageModule("rampart");

            //serviceClient.engageModule("addressing");

            serviceClient.getOptions().setTo(new EndpointReference(serviceURI));
            //HttpTransportProperties.Authenticator authenticator = new HttpTransportProperties.Authenticator();
            //authenticator.setUsername("admin");
            //authenticator.setPassword("admin");
            //authenticator.setPreemptiveAuthentication(true);

            //serviceClient = SecurityHandler.engage("admin", serviceClient, SecurityHandler.class.getClassLoader().getResourceAsStream("sample_policy.xml"), new ClientPWCBHandler("admin",null));

            //serviceClient.getOptions().setProperty(HTTPConstants.AUTHENTICATE,authenticator);

            return serviceClient;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ConfigurationContext getContext() {
        return context;
    }

    public static void setContext(ConfigurationContext context) {
        ServiceContext.context = context;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public MultiThreadedHttpConnectionManager getHttpConnectionManager() {
        return httpConnectionManager;
    }

    public void setHttpConnectionManager(MultiThreadedHttpConnectionManager httpConnectionManager) {
        this.httpConnectionManager = httpConnectionManager;
    }

    public static void close() throws Throwable {
        instance.finalize();
    }


    @Override
    protected void finalize() throws Throwable {
        httpConnectionManager.closeIdleConnections(0);
        httpConnectionManager.shutdown();
        super.finalize();
    }
}
