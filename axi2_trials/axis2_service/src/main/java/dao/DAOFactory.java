package dao;

import dao.impl.CustomerDAOImpl;
import dao.impl.OrderDAOImpl;
import dao.impl.ProductDAOImpl;

/**
 * Created by hasithad on 4/11/14.
 */
public class DAOFactory {

    private static volatile DAOFactory instance;

    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;

    private DAOFactory() {
        customerDAO = new CustomerDAOImpl();
        productDAO = new ProductDAOImpl();
        orderDAO = new OrderDAOImpl();
    }

    public static DAOFactory getInstance() {
        if (instance == null) {
            synchronized (DAOFactory.class) {
                instance = new DAOFactory();
            }
        }

        return instance;
    }

    public static void setInstance(DAOFactory instance) {
        DAOFactory.instance = instance;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public void setOrderDAO(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }
}
