/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.mediators.custom;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.mediators.custom.util.jms.PublisherCache;
import org.wso2.mediators.custom.util.jms.PublisherContext;
import org.wso2.mediators.custom.util.jms.PublisherNotAvailableException;
import org.wso2.mediators.custom.util.jms.PublisherPool;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;

/**
 * This mediator is implemented to provide session caching for dynamic JMS topics.
 * <p/>
 * As per the default ESB JMS transport, once we publish to a destination using a JMS session,
 * that session cannot be re-used in a subsequent publish to the same destination.
 * <p/>
 * This mediator uses the javax cache management and plain JMS libraries to maintain a JMS topic's publisher session and other context
 * for a pre-defined time period, thereby reducing the need to initialize a JMS session per each publish event.
 */
public class JMSPublisherCacheMediator extends AbstractMediator {

    private static final Log log = LogFactory.getLog(JMSPublisherCacheMediator.class);

    /**
     * interval at which the cache should expire (in seconds).
     */
    private int cacheExpirationInterval;

    /**
     * Connection factory name as configured in jndi.properties file. Could use different connection factories for
     * topics and queues.
     */
    private String connectionFactoryName;

    /**
     * Type of destination. "queue" or "topic".
     */
    private String destinationType;

    /** Maximum number of connections allowed for a single destination+destinationType combination.
     *
     */
    private String connectionPoolSize;

    @Override
    public boolean mediate(MessageContext messageContext) {

        String destinationName = messageContext.getProperty("destinationName").toString();

        if (StringUtils.isBlank(destinationName)) {
            handleException("Could not find a valid topic name to publish the message.", messageContext);
        }

        if ((!PublisherContext.QUEUE_NAME_PREFIX.equals(destinationType)) &&
                (!PublisherContext.TOPIC_NAME_PREFIX.equals(destinationType))) {
            handleException("Invalid destination type. It must be a queue or a topic. Current value : " +
                    destinationType, messageContext);
        }

        if (log.isDebugEnabled()) {
            log.debug("Processing message for destination : " + destinationType + " : " + destinationName + " with "
                    + "connection factory : " + connectionFactoryName);
        }

        PrivilegedCarbonContext.getCurrentContext().setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
        PrivilegedCarbonContext.getCurrentContext().setTenantId(MultitenantConstants.SUPER_TENANT_ID);

        PublisherCache.setCacheExpirationInterval(cacheExpirationInterval);

        PublisherPool publisherPool;
        PublisherContext publisherContext = null;

        String publisherContextKey = destinationType+":/"+destinationName; //queue:/queueA

        synchronized (publisherContextKey.intern()) {
            publisherPool = PublisherCache.getJMSPublisherPoolCache().get(publisherContextKey);

            if (null == publisherPool) {
                log.info("JMS Publisher pool cache miss for destination : " + destinationName);
                publisherPool = new PublisherPool(destinationName, destinationType,
                        connectionFactoryName, java.lang.Integer.parseInt(connectionPoolSize));
                PublisherCache.getJMSPublisherPoolCache().put(publisherContextKey, publisherPool);
            }
        }

        try {

            publisherContext = publisherPool.getPublisher();
            assert publisherContext != null;
            publisherContext.publishMessage(((Axis2MessageContext) messageContext).getAxis2MessageContext());
            return true;

        } catch (IllegalStateException e ) {
            publisherPool.close();
            handleException("IllegalStateException when publishing through JMS connection : ", e, messageContext);

        } catch (JMSException e) {

            publisherPool.close();
            handleException("JMSException : ", e, messageContext);

        } catch (AxisFault e) {
            handleException("AxisFault : ", e, messageContext);
        } catch (IOException e) {
            handleException("IOException : " + e, messageContext);
        } catch (NamingException e) {
            handleException("NamingException : ", e, messageContext);
        } catch (PublisherNotAvailableException e) {
            handleException("PublisherNotAvailableException : ", e, messageContext);
        } finally {
            if (null != publisherContext) {
                try {
                    publisherPool.releasePublisher(publisherContext);
                } catch (JMSException e) {
                    handleException("Error while releasing publisher after sending message : ", e, messageContext);
                }
            }
        }

        return false;

    }

    @Override
    protected void handleException(String message, MessageContext messageContext) {
        log.error(message);
        super.handleException(message, messageContext);
    }

    @Override
    protected void handleException(String message, Exception e, MessageContext messageContext) {
        log.error(message, e);
        super.handleException(message, e, messageContext);
    }

    public int getCacheExpirationInterval() {
        return cacheExpirationInterval;
    }

    public void setCacheExpirationInterval(int cacheExpirationInterval) {
        this.cacheExpirationInterval = cacheExpirationInterval;
    }

    public String getConnectionFactoryName() {
        return connectionFactoryName;
    }

    public void setConnectionFactoryName(String connectionFactoryName) {
        this.connectionFactoryName = connectionFactoryName;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public String getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(String connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }
}
