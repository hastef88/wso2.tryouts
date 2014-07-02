package util;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.Map;

/**
 * Created by hasithad on 5/12/14.
 */
public class ClientPWCBHandler implements CallbackHandler {
    private String password;
    private Map properties;
    public ClientPWCBHandler() {
    }
    public ClientPWCBHandler(String password, Map properties) {
        this.password = password;
        this.properties = properties;
    }
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[0];
        int usage = pwcb.getUsage();
        if (usage == WSPasswordCallback.USERNAME_TOKEN) {
            // user has provided the username and the password
            if (password != null) {
                pwcb.setPassword(password);
            } else {
                // user has provided only the username, get password from LDAP
                pwcb.setPassword("");
            }
        } else if (usage == WSPasswordCallback.SIGNATURE || usage == WSPasswordCallback.DECRYPT) {
            // handle signature and decrypt type
        }
    }
}
