package dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import dao.ProductDAO;
import dao.StorageFactory;
import enumerations.SOAPStatusEnum;
import model.Customer;
import model.Product;
import util.JSONConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasithad on 4/9/14.
 */
public class ProductDAOImpl extends BaseDAOImpl implements ProductDAO{


    public String create(Product product) throws IOException {
        DBCollection products = StorageFactory.getInstance().getDatabase().getCollection("product");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("title", product.getTitle());

        DBCursor cursor = products.find(searchQuery);

        if (!cursor.hasNext()) {

            DBObject newProduct = (DBObject) JSON.parse(JSONConverter.convertToJSON(product));

            products.insert(newProduct);

            return newProduct.get("_id").toString();
        }else {
            return ((Product)JSONConverter.convertToObject(reformatDBObject(cursor.next()),Product.class)).get_id();
            //return SOAPStatusEnum.DUPLICATE_NOT_ALLOWED.toString();
        }
    }

    public String update(Product product) {

        DBCollection table = StorageFactory.getInstance().getDatabase().getCollection("product");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", product.get_id());

        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.put("title", product.getTitle());
        updateDocument.put("description", product.getDescription());
        updateDocument.put("price", product.getPrice());
        updateDocument.put("quantity",product.getQuantity());
        updateDocument.put("tags",product.getTags());

        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", updateDocument);

        table.update(query, updateObj);

        return product.get_id();
    }

    public Product get(String id) {
        DBCollection table = StorageFactory.getInstance().getDatabase().getCollection("product");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", id);

        DBCursor cursor = table.find(searchQuery);

        while (cursor.hasNext()) {
            try {
                return JSONConverter.convertToObject(reformatDBObject(cursor.next()), Product.class);
            } catch (IOException e) {
                break;
            }
        }

        return null;
    }

    public Product[] list(String[] tags) {

        ArrayList<Product> returnList = new ArrayList<Product>();

        DBCollection table = StorageFactory.getInstance().getDatabase().getCollection("product");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("tags", tags);

        DBCursor cursor = table.find(searchQuery);

        while (cursor.hasNext()) {
            try {
                returnList.add((Product) JSONConverter.convertToObject(reformatDBObject(cursor.next()), Product.class));
            } catch (IOException e) {
                break;
            }
        }

        return returnList.toArray(new Product[returnList.size()]);
    }
}
