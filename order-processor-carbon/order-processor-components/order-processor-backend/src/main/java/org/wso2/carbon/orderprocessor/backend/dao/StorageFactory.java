package org.wso2.carbon.orderprocessor.backend.dao;

import com.mongodb.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by hasithad on 4/10/14.
 */

public class StorageFactory {

    private String status;
    private DB  database;
    private MongoClient dbClient;
    private boolean isAuthenticated;

    private static volatile StorageFactory instance;

    public StorageFactory() {

        try {
            status = "Ready";

            Properties mongoConfig = new Properties();

            /*try {
                mongoConfig.load(StorageFactory.class.getClassLoader().getResourceAsStream("mongodb.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            String userName = "hasd"; //mongoConfig.getProperty("userName")
            String password = "hasd"; //mongoConfig.getProperty("password")
            String hostName = "localhost"; //mongoConfig.getProperty("host")
            String port = "27017"; // mongoConfig.getProperty("port")
            String db = "orderdb"; // mongoConfig.getProperty("db")

            MongoCredential dbUser = MongoCredential.createMongoCRCredential(userName,db,password.toCharArray());

            MongoCredential[] arr = new MongoCredential[]{dbUser};

            dbClient = new MongoClient(new ServerAddress(hostName, Integer.parseInt(port)), Arrays.asList(arr));
            database = dbClient.getDB(db);


            //isAuthenticated = database.authenticate(mongoConfig.getProperty("userName"), mongoConfig.getProperty("password").toCharArray());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static StorageFactory getInstance() {
        if (instance == null) {
            synchronized (StorageFactory.class) {
                instance = new StorageFactory();
            }
        }

        return instance;
    }

    protected void finalize() {
        dbClient.close();
    }


    public DB getDatabase() {
        return database;
    }

    public String getStatus() {
        return status;
    }

}
