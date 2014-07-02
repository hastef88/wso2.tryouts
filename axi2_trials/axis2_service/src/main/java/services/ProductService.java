package services;

import dao.DAOFactory;
import edu.emory.mathcs.backport.java.util.Arrays;
import enumerations.SOAPStatusEnum;
import util.SOAPStatus;
import model.Product;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        throw new NotImplementedException();
    }

}
