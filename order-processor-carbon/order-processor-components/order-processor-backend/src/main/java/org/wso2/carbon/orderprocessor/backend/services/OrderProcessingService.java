package org.wso2.carbon.orderprocessor.backend.services;

import org.wso2.carbon.orderprocessor.backend.dao.DAOFactory;
import org.wso2.carbon.orderprocessor.backend.enumerations.SOAPStatusEnum;
import org.wso2.carbon.orderprocessor.backend.model.Order;
import org.wso2.carbon.orderprocessor.backend.util.SOAPStatus;

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
        return null;
    }

    public Order removeProducts(String orderId, String[] productIDs) {
        SOAPStatus ss = new SOAPStatus();
        ss.setStatus(SOAPStatusEnum.FAILURE);

        return null;
    }

    public Order get(String id) {

        return DAOFactory.getInstance().getOrderDAO().get(id);
    }

    public List<Order> list(String customerId) {
        return DAOFactory.getInstance().getOrderDAO().list(customerId);
    }


}
