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
import org.wso2.carbon.caching.impl.CachingConstants;

import javax.cache.Cache;
import javax.cache.CacheConfiguration;
import javax.cache.CacheManager;
import javax.cache.Caching;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class to act as a single reference point for the Cache containing JMS Topic Sessions used in the class mediator
 *
 * @link org.wso2.mediators.custom.JMSPublisherCacheMediator.
 */
public class PublisherCache {

    /**
     * Is set to true if the global cache already initialized.
     */
    private static AtomicBoolean isCacheInitialized = new AtomicBoolean(false);

    private static final Log log = LogFactory.getLog(PublisherCache.class);

    /**
     * Cache Name
     */
    private static final String CACHE_KEY = "PublisherPoolCache";

    /**
     * Name of CacheManager holding the cache
     */
    private static final String CACHE_MANAGER_KEY = CACHE_KEY + "Manager";

    /**
     * Cache invalidation interval in seconds.
     */
    private static int cacheExpirationInterval;

    /**
     * Listener to handle removal/expiration of a cached entry
     */
    private final static PublisherPoolCacheExpiredListener<String, PublisherPool> entryExpiredListener = new PublisherPoolCacheExpiredListener<>();

    /**
     * Get the cache which holds all sessions created for publishing to topics using this mediator.
     *
     * @return Cache with key PublisherCache
     */
    public static Cache<String, PublisherPool> getJMSPublisherPoolCache() {

        if (isCacheInitialized.get()) {
            return Caching.getCacheManagerFactory().getCacheManager(CACHE_MANAGER_KEY).getCache(CachingConstants.LOCAL_CACHE_PREFIX +
                    CACHE_KEY);
        } else {

            String cacheName = CachingConstants.LOCAL_CACHE_PREFIX + CACHE_KEY;

            if (log.isDebugEnabled()) {
                log.debug("Using cacheName : " + cacheName);
            }

            CacheManager cacheManager = Caching.getCacheManagerFactory().getCacheManager(CACHE_MANAGER_KEY);
            isCacheInitialized.getAndSet(true);

            Cache<String, PublisherPool> cache = cacheManager.<String, PublisherPool>createCacheBuilder(cacheName)
                    .setExpiry(CacheConfiguration.ExpiryType.MODIFIED,
                            new CacheConfiguration.Duration(TimeUnit.SECONDS, cacheExpirationInterval))
                    .setExpiry(CacheConfiguration.ExpiryType.ACCESSED,
                            new CacheConfiguration.Duration(TimeUnit.SECONDS, cacheExpirationInterval))
                    .setStoreByValue(false).build();

            cache.registerCacheEntryListener(entryExpiredListener);

            return cache;
        }
    }

    /**
     * Set the interval at which the cached entries should expire based on last Accessed timestamp.
     * @param cacheExpirationInterval interval at which the cached entries should expire based on last Accessed timestamp.
     */
    public static void setCacheExpirationInterval(int cacheExpirationInterval) {
        PublisherCache.cacheExpirationInterval = cacheExpirationInterval;
    }
}
