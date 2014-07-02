package util;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.ws.security.handler.WSHandlerConstants;

import javax.security.auth.callback.CallbackHandler;
import java.io.InputStream;

/**
 * Created by hasithad on 5/12/14.
 */
public class SecurityHandler {
    //private String user;
    //private CallbackHandler callbackHandler;
    //private ServiceClient serviceClient;
    //private InputStream policyStream;

    public static ServiceClient engage(String user, ServiceClient serviceClient, InputStream policy,CallbackHandler callbackHandler) {
        try {
            RampartConfig rc = new RampartConfig();
            rc.setUser(user);
            Policy policyContent = PolicyEngine.getPolicy(new StAXOMBuilder(policy).getDocumentElement());
            policyContent.addAssertion(rc);

            serviceClient.engageModule("rampart");

            serviceClient.getOptions().setProperty(WSHandlerConstants.PW_CALLBACK_REF, callbackHandler);
            serviceClient.getOptions().setProperty(RampartMessageData.KEY_RAMPART_POLICY, policyContent);
            //options.setProperty(WSHandlerConstants.PW_CALLBACK_REF, callbackHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return serviceClient;
    }
}
