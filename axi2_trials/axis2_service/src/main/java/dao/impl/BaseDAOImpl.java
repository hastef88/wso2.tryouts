package dao.impl;

import com.mongodb.DBObject;
import dao.BaseDAO;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hasithad on 4/9/14.
 */
public class BaseDAOImpl implements BaseDAO {

    @Override
    public String reformatDBObject(DBObject record) {

        JSONObject jsObj = null;
        try {
            jsObj = new JSONObject(record.toString());

            String recordId = ((JSONObject)jsObj.get("_id")).get("$oid").toString();

            jsObj.remove("_id");

            jsObj.put("_id",recordId);

            return jsObj.toString();
        } catch (JSONException e) {
            //log error
            return "";
        }
    }
}
