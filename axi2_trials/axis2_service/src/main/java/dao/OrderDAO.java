package dao;

import model.Order;

import java.io.IOException;
import java.util.List;

/**
 * Created by hasithad on 4/11/14.
 */
public interface OrderDAO {

    public String create(Order order) throws IOException;
    public String update(Order order);
    public Order get(String id);
    public List<Order> list(String customerId);

}
