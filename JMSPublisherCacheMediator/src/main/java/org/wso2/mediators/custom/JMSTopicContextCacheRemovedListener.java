package org.wso2.mediators.custom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryRemovedListener;
import javax.jms.JMSException;

public class JMSTopicContextCacheRemovedListener implements CacheEntryRemovedListener{

    private static final Log log = LogFactory.getLog(JMSTopicContextCacheRemovedListener.class);

    @Override
    public void entryRemoved(CacheEntryEvent cacheEntryEvent) throws CacheEntryListenerException {

        if (cacheEntryEvent.getValue() instanceof JMSTopicContext) {
            try {
                log.info("About to clear Cache!");
                ((JMSTopicContext)cacheEntryEvent.getValue()).close();
                log.info("Cleared!");
            } catch (JMSException e) {
                log.error("Error while closing cached topic session", e);
            }
        }
    }
}
