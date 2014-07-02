package dao;

import model.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by hasithad on 4/11/14.
 */
public interface ProductDAO {

    public String create(Product product) throws IOException;
    public String update(Product product);
    public Product get(String id);
    public Product[] list(String[] tags);
}
