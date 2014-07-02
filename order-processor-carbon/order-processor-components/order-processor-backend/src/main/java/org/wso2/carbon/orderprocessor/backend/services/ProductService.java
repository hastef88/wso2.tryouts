package org.wso2.carbon.orderprocessor.backend.services;

import org.wso2.carbon.orderprocessor.backend.dao.DAOFactory;
import org.wso2.carbon.orderprocessor.backend.enumerations.SOAPStatusEnum;
import org.wso2.carbon.orderprocessor.backend.model.Product;
import org.wso2.carbon.orderprocessor.backend.util.SOAPStatus;

import java.io.IOException;

/**
 * Created by hasithad on 4/9/14.
 */
public class ProductService {

    public String create(Product product) {
        try {
            return DAOFactory.getInstance().getProductDAO().create(product);
        } catch (IOException e) {
            return SOAPStatusEnum.FAILURE.toString();
        }
    }

    public String update(Product product) {
        return DAOFactory.getInstance().getProductDAO().update(product);
    }

    public Product get(String id) {
        return DAOFactory.getInstance().getProductDAO().get(id);
    }

    public Product[] list(String title,String[] tags) {
        return DAOFactory.getInstance().getProductDAO().list(tags);
    }

    public SOAPStatus delete() {

        SOAPStatus ss = new SOAPStatus();
        ss.setStatus(SOAPStatusEnum.FAILURE);

        return ss;
    }

}
