package services;

import dao.DAOFactory;
import enumerations.SOAPStatusEnum;
import model.Customer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.CustomSOAPException;
import util.SOAPStatus;

import java.io.IOException;

/**
 * Created by hasithad on 4/9/14.
 */
public class CustomerService {

    public String register(Customer customer) throws CustomSOAPException {
        try {
            return DAOFactory.getInstance().getCustomerDAO().register(customer);
        }catch (Exception e) {
            throw new CustomSOAPException(SOAPStatusEnum.AUTHORIZATION_FAILED,e.getMessage());
        }
    }

    public String update(Customer customer) {
        try {
            return DAOFactory.getInstance().getCustomerDAO().update(customer);
        } catch (IOException e) {
            return SOAPStatusEnum.FAILURE.toString();
        }
    }

    public Customer get(String id,String email) {
        return DAOFactory.getInstance().getCustomerDAO().get(id,email);
    }

    //To be checked for a better method
    public Customer authenticate(String email,String password) {
        throw new NotImplementedException();
    }

    public SOAPStatus delete(String id) {
        throw new NotImplementedException();
    }
}