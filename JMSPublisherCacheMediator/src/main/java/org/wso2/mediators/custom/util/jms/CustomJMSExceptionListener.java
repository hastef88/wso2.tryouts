/*
 *
 *  * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *  *
 *  * WSO2 Inc. licenses this file to you under the Apache License,
 *  * Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package org.wso2.mediators.custom.util.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * Custom exception handler to react to JMS exceptions during mediation.
 */
public class CustomJMSExceptionListener implements ExceptionListener {

    private static final Log log = LogFactory.getLog(CustomJMSExceptionListener.class);

    private String publisherContextKey;

    public CustomJMSExceptionListener(String publisherContextKey) {

        this.publisherContextKey = publisherContextKey;
    }

    @Override
    public void onException(JMSException e) {

        synchronized (publisherContextKey.intern()) {
            log.error("Cache will be cleared due to JMSException for destination : " + publisherContextKey, e);

            // tenant information must be populated before using the cache.
            PrivilegedCarbonContext.getCurrentContext().setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
            PrivilegedCarbonContext.getCurrentContext().setTenantId(MultitenantConstants.SUPER_TENANT_ID);

            PublisherPool publisherPool = PublisherCache.getJMSPublisherPoolCache().getAndRemove(publisherContextKey);

            try {
                publisherPool.close();
            } catch (JMSException e1) {
                log.error("Error while trying to remove obsolete publisher pool for : " + publisherContextKey, e1);
            }

            log.error("Cache has been cleared to a JMSException for destination : " + publisherContextKey, e);
        }
    }
}
