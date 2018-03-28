/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.custom.tools.identity.claims;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

/**
 * Class mapped to yaml file.
 */
public class Configuration {

    private final static Logger logger = Logger.getLogger(Configuration.class);

    private String fromServiceURI;
    private String toServiceURI;
    private String claimDialectURI;
    private String[] claimDisplayTags;

    public String getFromServiceURI() {
        return fromServiceURI;
    }

    public void setFromServiceURI(String fromServiceURI) {
        this.fromServiceURI = fromServiceURI;
    }

    public String getToServiceURI() {
        return toServiceURI;
    }

    public void setToServiceURI(String toServiceURI) {
        this.toServiceURI = toServiceURI;
    }

    public String getClaimDialectURI() {
        return claimDialectURI;
    }

    public void setClaimDialectURI(String claimDialectURI) {
        this.claimDialectURI = claimDialectURI;
    }

    public String[] getClaimDisplayTags() {
        return claimDisplayTags;
    }

    public void setClaimDisplayTags(String[] claimDisplayTags) {
        this.claimDisplayTags = claimDisplayTags;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.error("Error while printing configuration", e);
            return "";
        }
    }
}
