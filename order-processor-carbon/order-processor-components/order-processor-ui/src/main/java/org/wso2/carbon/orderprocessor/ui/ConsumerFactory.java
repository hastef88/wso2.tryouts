package org.wso2.carbon.orderprocessor.ui;

import org.apache.axis2.AxisFault;
import org.wso2.carbon.orderprocessor.stub.CustomerServiceCustomSOAPExceptionException;
import org.wso2.carbon.orderprocessor.stub.CustomerServiceStub;

import java.rmi.RemoteException;

/**
 * Created by hasithad on 5/20/14.
 */
public class ConsumerFactory {

    public static String RegisterCustomer(CustomerServiceStub.Customer newCustomer) {

        String customerId = "";

        try {
            CustomerServiceStub css = new CustomerServiceStub();

            CustomerServiceStub.Register crRequest = new CustomerServiceStub.Register();

            crRequest.setCustomer(newCustomer);

            CustomerServiceStub.RegisterResponse crResponse = css.register(crRequest);

            customerId = crResponse.get_return();

        } catch (AxisFault axisFault) {
            //log error
            //TODO
        } catch (RemoteException e) {
            //TODO
        } catch (CustomerServiceCustomSOAPExceptionException e) {
            //TODO
        }

        return customerId;
    }
}
