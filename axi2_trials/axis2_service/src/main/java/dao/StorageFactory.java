package dao;

import com.hazelcast.core.HazelcastInstance;
import com.mongodb.*;
import edu.emory.mathcs.backport.java.util.Arrays;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by hasithad on 4/10/14.
 */

public class StorageFactory {

    private String status;
    private HazelcastInstance cache;
    private DB  database;
    private MongoClient dbClient;
    private boolean isAuthenticated;

    private static volatile StorageFactory instance;

    public StorageFactory() {

        try {
            status = "Ready";
            //cache = Hazelcast.newHazelcastInstance();

            Properties mongoConfig = new Properties();

            try {
                mongoConfig.load(StorageFactory.class.getClassLoader().getResourceAsStream("mongodb.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            MongoCredential dbUser = MongoCredential.createMongoCRCredential(mongoConfig.getProperty("userName"),mongoConfig.getProperty("db"),mongoConfig.getProperty("password").toCharArray());

            MongoCredential[] arr = new MongoCredential[]{dbUser};

            dbClient = new MongoClient(new ServerAddress(mongoConfig.getProperty("host"), Integer.parseInt(mongoConfig.getProperty("port"))), Arrays.asList(arr));
            database = dbClient.getDB(mongoConfig.getProperty("db"));


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
        cache.shutdown();
    }

    public HazelcastInstance getCache() {
        return cache;
    }

    public DB getDatabase() {
        return database;
    }

    public String getStatus() {
        return status;
    }

}
