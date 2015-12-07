package org.wso2.mediators.custom;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.CacheConfiguration;
import javax.cache.Caching;
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JMSPublisherCacheMediator extends AbstractMediator {

    private static final Log log = LogFactory.getLog(JMSPublisherCacheMediator.class);

    public static String cacheKey = "JMSTopicSessionCache";

    private String connectionFactoryName;
    private String topicName;

    @Override
    public boolean mediate(MessageContext messageContext) {

        log.info("Entered the mediator !!");

        log.warn("connectionFactory as read : " + this.connectionFactoryName);
        log.warn("topicName as read : " + this.topicName);

        try {
            JMSTopicContext context = retrieveTopicSessionFromCache(this.topicName,this.connectionFactoryName);
            context.publishMessage(((Axis2MessageContext)messageContext).getAxis2MessageContext(),"text/xml");
            return true;

        } catch (JMSException e) {
            log.error("JMSException : ", e);
        } catch (NamingException e) {
            log.error("NamingException : ", e);
        } catch (AxisFault axisFault) {
            log.error("AxisFault : ", axisFault);
        } finally {
            return false;
        }

//          log.info("Reteving simple cache  : " + retSimpleCache(this.topicName));
//          return true;



    }

    private JMSTopicContext retrieveTopicSessionFromCache(String topicName, String connectionFactoryName) throws JMSException, NamingException, IOException {

        CacheManager cacheManager = Caching.getCacheManagerFactory().getCacheManager(cacheKey + "Manager");

        Cache<String, JMSTopicContext> cache = cacheManager.getCache(cacheKey);

        if (null == cache) {
            // Initialize cache with custom validity duration
            cache = cacheManager.<String, JMSTopicContext>createCacheBuilder(cacheKey).setExpiry(CacheConfiguration
                    .ExpiryType.MODIFIED, new CacheConfiguration.Duration(TimeUnit.SECONDS,
                    2)).setStoreByValue(false).build();

            //cache.registerCacheEntryListener(new JMSTopicContextCacheRemovedListener());
        }

        JMSTopicContext topicContext = cache.get(topicName);

        if (null == topicContext) {
            log.info("Cache miss for topicContext for topic : " + topicName);
            topicContext = new JMSTopicContext(topicName, connectionFactoryName);

            cache.put(topicName,topicContext);
            return topicContext;
        } else {
            return topicContext;
        }
    }

    private String retSimpleCache(String topicName) {

        CacheManager cacheManager = Caching.getCacheManagerFactory().getCacheManager(cacheKey + "Manager");

        Cache<String, String> cache = cacheManager.getCache(cacheKey);

        if (null == cache) {
            // Initialize cache with custom validity duration
            cache = cacheManager.<String, String>createCacheBuilder(cacheKey).setExpiry(CacheConfiguration
                    .ExpiryType.MODIFIED, new CacheConfiguration.Duration(TimeUnit.SECONDS,
                    2)).setStoreByValue(false).build();

            //cache.registerCacheEntryListener(new JMSTopicContextCacheRemovedListener());
        }

        String topicContext = cache.get(topicName);

        if (null == topicContext) {
            log.info("Cache miss for topicContext for topic : " + topicName);
            topicContext = topicName + "Value";

            cache.put(topicName,topicContext);
            return topicContext;
        } else {
            return topicContext;
        }
    }

    public String getConnectionFactoryName() {
        return connectionFactoryName;
    }

    public void setConnectionFactoryName(String connectionFactoryName) {
        this.connectionFactoryName = connectionFactoryName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
