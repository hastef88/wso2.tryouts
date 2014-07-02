package org.wso2.carbon.orderprocessor.backend.dao;

import org.wso2.carbon.orderprocessor.backend.model.Customer;

import java.io.IOException;

/**
 * Created by hasithad on 4/11/14.
 */
public interface CustomerDAO {

    public String register(Customer customer) throws IOException;
    public String update(Customer customer) throws IOException;
    public Customer get(String id, String email);
    public Customer authenticate(String userName, String password);

}
