import client.CustomerServiceCustomSOAPExceptionException;
import client.CustomerServiceStub;
import client.OrderProcessingServiceStub;
import client.ProductServiceStub;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import sun.security.krb5.Config;
import util.ConfigFactory;
import util.SecurityHandler;
import util.ServiceContext;

import java.net.Authenticator;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hasithad on 4/11/14.
 */
public class ClientTester {

    public static void main(String[] args) {

        try {

            // initialize configurations for service consumption
            ConfigFactory.initialize();
            ServiceContext.initialize();

            CustomerServiceStub css = new CustomerServiceStub(ServiceContext.getContext());

            CustomerServiceStub.Register crRequest = new CustomerServiceStub.Register();

            CustomerServiceStub.Customer c = new CustomerServiceStub.Customer();
            c.setName("Lama");
            c.setEmail("lamatintin@gmail.com");
            c.setPhone("23984322");
            c.setShippingAddress(new CustomerServiceStub.Address());

            crRequest.setCustomer(c);

            css._setServiceClient(ServiceContext.prepareServiceClient(ConfigFactory.getCustomerEndpoint(), css._getServiceClient()));

            CustomerServiceStub.RegisterResponse crResponse = css.register(crRequest);

            String customerId = crResponse.get_return();

            System.out.println("Customer Registered : " + customerId);

            ProductServiceStub pss = new ProductServiceStub(ServiceContext.getContext());

            pss._setServiceClient(ServiceContext.prepareServiceClient(ConfigFactory.getProductEndpoint(),pss._getServiceClient()));

            for (int i=20;i<30;i++) {

                ProductServiceStub.Product product = new ProductServiceStub.Product();

                product.setTitle("Product " + (i+1));
                product.setQuantity(i+1);
                product.setTags(new String[]{"Tag" + (i+1),"Tag" + (i+2),"Tag" + (i+3)});
                product.setPrice(1000 * (i+2));
                product.setDescription("Sample Product");

                ProductServiceStub.Create pcRequest = new ProductServiceStub.Create();
                pcRequest.setProduct(product);

                ProductServiceStub.CreateResponse pcResponse = pss.create(pcRequest);

                System.out.println("Product Created : "+ pcResponse.get_return());
            }

            //Get Products
            ProductServiceStub.List plRequest = new ProductServiceStub.List();
            plRequest.setTags(new String[]{"Tag21","Tag22","Tag23"});
            plRequest.setTitle("sd");

            ProductServiceStub.ListResponse plResponse = pss.list(plRequest);

            ProductServiceStub.Product[] listedProducts = plResponse.get_return();
            System.out.println(listedProducts.toString());

            OrderProcessingServiceStub opss = new OrderProcessingServiceStub(ServiceContext.getContext());

            OrderProcessingServiceStub.Order order = new OrderProcessingServiceStub.Order();
            order.setCreatedDate(new Date().getTime());
            order.setDeliveryDate(new Date().getTime());
            order.setCustomerId(customerId);

            order.setProducts(new OrderProcessingServiceStub.ProductOrder[listedProducts.length]);

            for(int i=0;i<listedProducts.length;i++) {
                OrderProcessingServiceStub.ProductOrder po = new OrderProcessingServiceStub.ProductOrder();
                po.setProductId(listedProducts[i].get_id());
                po.setQuantity(i+1);
                order.getProducts()[i] = po;
            }

            OrderProcessingServiceStub.Fee fee = new OrderProcessingServiceStub.Fee();
            fee.setShippingFee(1300.00);
            order.setFee(fee);

            OrderProcessingServiceStub.Create opcRequest = new OrderProcessingServiceStub.Create();
            opcRequest.setOrder(order);

            opss._setServiceClient(ServiceContext.prepareServiceClient(ConfigFactory.getOrderProcessingEndpoint(),opss._getServiceClient()));
            OrderProcessingServiceStub.CreateResponse opcResponse = opss.create(opcRequest);

            System.out.println("Created Order : " + opcResponse.get_return());
            //order.setProducts();

            //order.setFee();
            //order.setProducts();

            ServiceContext.close();


        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (CustomerServiceCustomSOAPExceptionException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }
}
