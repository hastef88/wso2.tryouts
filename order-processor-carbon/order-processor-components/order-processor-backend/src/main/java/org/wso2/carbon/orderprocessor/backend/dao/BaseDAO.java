package org.wso2.carbon.orderprocessor.backend.dao;

import com.mongodb.DBObject;

/**
 * Created by hasithad on 4/11/14.
 */
public interface BaseDAO {

    // utility method to remove the custom ID data structure in mongo db record result and restore ID Data structure to match model
    public String reformatDBObject(DBObject record);

}
