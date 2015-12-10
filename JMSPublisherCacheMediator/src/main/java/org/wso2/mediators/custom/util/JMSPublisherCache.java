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

package org.wso2.mediators.custom.util;

import javax.cache.Cache;
import javax.cache.CacheConfiguration;
import javax.cache.CacheManager;
import javax.cache.Caching;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class to act as a single reference point for the Cache containing JMS Topic Sessions used in the class mediator
 * @link org.wso2.mediators.custom.JMSPublisherCacheMediator.
 */
public class JMSPublisherCache {

    /**
     * Is set to true if the global cache already initialized.
     */
    private static AtomicBoolean isCacheInitialized = new AtomicBoolean(false);

    /**
     * Cache Name
     */
    private static final String CACHE_KEY = "JMSPublisherCache";

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
    private final static JMSPublisherContextCacheRemovedListener<String, JMSPublisherContext> entryRemovedListener = new
            JMSPublisherContextCacheRemovedListener<>();

    /**
     * Get the cache which holds all sessions created for publishing to topics using this mediator.
     *
     * @return Cache with key JMSPublisherCache
     */
    public static Cache<String, JMSPublisherContext> getJMSPublisherCache() {

        if (isCacheInitialized.get()) {
            return Caching.getCacheManagerFactory().getCacheManager(CACHE_MANAGER_KEY).getCache(CACHE_KEY);
        } else {
            CacheManager cacheManager = Caching.getCacheManagerFactory().getCacheManager(CACHE_MANAGER_KEY);
            isCacheInitialized.getAndSet(true);

            return cacheManager.<String, JMSPublisherContext>createCacheBuilder(CACHE_KEY)
                    .setExpiry(CacheConfiguration.ExpiryType.MODIFIED,
                            new CacheConfiguration.Duration(TimeUnit.SECONDS, cacheExpirationInterval))
                    .setExpiry(CacheConfiguration.ExpiryType.ACCESSED,
                            new CacheConfiguration.Duration(TimeUnit.SECONDS, cacheExpirationInterval))
                    //.registerCacheEntryListener(entryRemovedListener)
                    .setStoreByValue(false).build();
        }
    }

    public static Cache<String, String> getTestCache() {

        if (isCacheInitialized.get()) {
            return Caching.getCacheManagerFactory().getCacheManager(CACHE_MANAGER_KEY).getCache(CACHE_KEY);
        } else {
            CacheManager cacheManager =
                    Caching.getCacheManagerFactory().getCacheManager(CACHE_MANAGER_KEY);
            isCacheInitialized.getAndSet(true);

            return cacheManager.<String, String>createCacheBuilder(CACHE_KEY)
                    .setExpiry(CacheConfiguration.ExpiryType.MODIFIED,
                            new CacheConfiguration.Duration(TimeUnit.SECONDS, cacheExpirationInterval))
                    .setExpiry(CacheConfiguration.ExpiryType.ACCESSED,
                            new CacheConfiguration.Duration(TimeUnit.SECONDS, cacheExpirationInterval))
                    .setStoreByValue(false).build();
        }
    }


    public static int getCacheExpirationInterval() {
        return cacheExpirationInterval;
    }

    public static void setCacheExpirationInterval(int cacheExpirationInterval) {
        JMSPublisherCache.cacheExpirationInterval = cacheExpirationInterval;
    }
}
