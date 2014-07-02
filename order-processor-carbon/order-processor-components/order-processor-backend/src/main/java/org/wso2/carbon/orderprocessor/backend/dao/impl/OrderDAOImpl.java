package org.wso2.carbon.orderprocessor.backend.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.wso2.carbon.orderprocessor.backend.dao.OrderDAO;
import org.wso2.carbon.orderprocessor.backend.dao.StorageFactory;
import org.wso2.carbon.orderprocessor.backend.model.Order;
import org.wso2.carbon.orderprocessor.backend.util.JSONConverter;

import java.io.IOException;
import java.util.List;

/**
 * Created by hasithad on 4/9/14.
 */
public class OrderDAOImpl extends BaseDAOImpl implements OrderDAO {

    public String create(Order order) throws IOException {
        DBCollection orders = StorageFactory.getInstance().getDatabase().getCollection("order");

        DBObject newOrder = (DBObject) JSON.parse(JSONConverter.convertToJSON(order));

        orders.insert(newOrder);

        return newOrder.get("_id").toString();
    }

    public String update(Order order) {
        DBCollection table = StorageFactory.getInstance().getDatabase().getCollection("order");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", order.get_id());

        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.put("createdDate", order.getCreatedDate());
        updateDocument.put("customerId", order.getCustomerId());
        updateDocument.put("deliveryDate", order.getDeliveryDate());
        updateDocument.put("fee", order.getFee());
        updateDocument.put("products",order.getProducts());

        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", updateDocument);

        table.update(query, updateObj);

        return order.get_id();
    }

    public Order get(String id) {
        DBCollection table = StorageFactory.getInstance().getDatabase().getCollection("order");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", id);

        DBCursor cursor = table.find(searchQuery);

        while (cursor.hasNext()) {
            try {
                return JSONConverter.convertToObject(reformatDBObject(cursor.next()), Order.class);
            } catch (IOException e) {
                break;
            }
        }

        return null;
    }

    public List<Order> list(String customerId) {
        return null;
    }
}
