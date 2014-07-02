package services;

import dao.DAOFactory;
import enumerations.SOAPStatusEnum;
import model.Order;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.SOAPStatus;

import java.io.IOException;
import java.util.List;

/**
 * Created by hasithad on 4/8/14.
 */
public class OrderProcessingService {

    public String create(Order order) {
        try {
            return DAOFactory.getInstance().getOrderDAO().create(order);
        } catch (IOException e) {
            return SOAPStatusEnum.FAILURE.toString();
        }
    }

    public String update(Order order) {
        return DAOFactory.getInstance().getOrderDAO().update(order);
    }

    public Order addProducts(String orderId, String[] productIDs) {
        throw new NotImplementedException();
    }

    public Order removeProducts(String orderId, String[] productIDs) {
        throw new NotImplementedException();
    }

    public Order get(String id) {

        return DAOFactory.getInstance().getOrderDAO().get(id);
    }

    public List<Order> list(String customerId) {
        return DAOFactory.getInstance().getOrderDAO().list(customerId);
    }


}
