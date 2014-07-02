package dao.impl;

import com.mongodb.*;
import com.mongodb.util.JSON;
import com.sun.corba.se.spi.ior.ObjectId;
import dao.CustomerDAO;
import dao.StorageFactory;
import enumerations.SOAPStatusEnum;
import model.Customer;
import org.json.JSONException;
import org.json.JSONObject;
import util.JSONConverter;

import java.io.IOException;

/**
 * Created by hasithad on 4/9/14.
 */
public class CustomerDAOImpl extends BaseDAOImpl implements CustomerDAO {

    public String register(Customer customer) throws IOException {

        DBCollection customers = StorageFactory.getInstance().getDatabase().getCollection("customer");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("email", customer.getEmail());

        DBCursor cursor = customers.find(searchQuery);

        if (!cursor.hasNext()) {

            DBObject newCustomer = (DBObject) JSON.parse(JSONConverter.convertToJSON(customer));

            customers.insert(newCustomer);

            return newCustomer.get("_id").toString();
        }else {
            return ((Customer)JSONConverter.convertToObject(reformatDBObject(cursor.next()),Customer.class)).get_id();
            //return SOAPStatusEnum.DUPLICATE_NOT_ALLOWED.toString();
        }
    }

    public String update(Customer customer) throws IOException {

        DBCollection table = StorageFactory.getInstance().getDatabase().getCollection("customer");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", customer.get_id());

        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.put("name", customer.getName());
        updateDocument.put("email", customer.getEmail());
        updateDocument.put("phone", customer.getPhone());

        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", updateDocument);

        table.update(query, updateObj);

        return customer.get_id();
    }

    public Customer get(String id, String email) {

        DBCollection table = StorageFactory.getInstance().getDatabase().getCollection("customer");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("email", email);

        DBCursor cursor = table.find(searchQuery);

        while (cursor.hasNext()) {
            try {
                return JSONConverter.convertToObject(reformatDBObject(cursor.next()),Customer.class);
            } catch (IOException e) {
                break;
            }
        }

        return null;
    }

    public Customer authenticate(String userName, String password) {
        return null;
    }
}
