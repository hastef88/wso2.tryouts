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
import org.wso2.mediators.custom.util.JMSPublisherCache;
import org.wso2.mediators.custom.util.JMSPublisherContext;

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
     * Connection Factory Name chosen for publishing the message. (Should be a pre-defined value in conf/jndi.properties)
     * This should be shared as a static parameter of the mediator at proxy configuration.
     */
    private String connectionFactoryName;

    /**
     * interval at which the cache should expire (in seconds).
     */
    private int cacheExpirationInterval;

    @Override
    public boolean mediate(MessageContext messageContext) {

        String topicName = messageContext.getProperty("topicName").toString();

        if (log.isDebugEnabled()) {
            log.debug("Processing message for topic : " + topicName);
        }

        PrivilegedCarbonContext.getCurrentContext().setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
        PrivilegedCarbonContext.getCurrentContext().setTenantId(MultitenantConstants.SUPER_TENANT_ID);

        if (StringUtils.isBlank(topicName)) {
            handleException("Could not find a valid topic name to publish the message.", messageContext);
        }

        JMSPublisherCache.setCacheExpirationInterval(cacheExpirationInterval);

        JMSPublisherContext publisherContext;

        synchronized (topicName.intern()) {
            publisherContext = JMSPublisherCache.getJMSPublisherCache().get(topicName);

            if (null == publisherContext) {
                log.info("JMS Publisher context cache miss for topic : " + topicName);
                try {
                    publisherContext = new JMSPublisherContext(topicName, this.connectionFactoryName);
                } catch (JMSException e) {
                    handleException("JMSException : ", e, messageContext);
                } catch (NamingException e) {
                    handleException("NamingException : ", e, messageContext);
                } catch (AxisFault e) {
                    handleException("AxisFault : ", e, messageContext);
                } catch (IOException e) {
                    handleException("IOException : " + e, messageContext);
                }
                JMSPublisherCache.getJMSPublisherCache().put(topicName, publisherContext);
            }
        }

        try {
            assert publisherContext != null;
            publisherContext.publishMessage(((Axis2MessageContext) messageContext).getAxis2MessageContext(), "text/xml");
            return true;

        } catch (JMSException e) {
            handleException("JMSException : ", e, messageContext);
        } catch (AxisFault e) {
            handleException("AxisFault : ", e, messageContext);
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

    public String getConnectionFactoryName() {
        return connectionFactoryName;
    }

    public void setConnectionFactoryName(String connectionFactoryName) {
        this.connectionFactoryName = connectionFactoryName;
    }

    public int getCacheExpirationInterval() {
        return cacheExpirationInterval;
    }

    public void setCacheExpirationInterval(int cacheExpirationInterval) {
        this.cacheExpirationInterval = cacheExpirationInterval;
    }
}
