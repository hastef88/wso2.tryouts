package org.wso2.carbon.orderprocessor.backend.dao;

import org.wso2.carbon.orderprocessor.backend.model.Product;

import java.io.IOException;

/**
 * Created by hasithad on 4/11/14.
 */
public interface ProductDAO {

    public String create(Product product) throws IOException;
    public String update(Product product);
    public Product get(String id);
    public Product[] list(String[] tags);
}
