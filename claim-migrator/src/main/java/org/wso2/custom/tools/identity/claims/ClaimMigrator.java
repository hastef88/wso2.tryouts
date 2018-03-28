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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.Header;
import org.apache.log4j.Logger;
import org.wso2.carbon.claim.mgt.stub.ClaimManagementServiceException;
import org.wso2.carbon.claim.mgt.stub.ClaimManagementServiceStub;
import org.wso2.carbon.claim.mgt.stub.dto.ClaimDialectDTO;
import org.wso2.carbon.claim.mgt.stub.dto.ClaimMappingDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main Class.
 */
public class ClaimMigrator {

    private final static Logger logger = Logger.getLogger(ClaimMigrator.class);

    public static void main(String[] args) throws IOException, ClaimManagementServiceException {

        Configuration configuration = parseConfigurations();

        if (args.length < 1) {
            logger.error("Credentials to invoke fromServiceURI and toServiceURI are required in Base64 encoded format.");
            return;
        } else if (args.length < 2) {
            logger.error("Credentials to invoke toServiceURI is required in Base64 encoded format.");
            return;
        }

        String fromServiceURICredentials = args[0];
        String toServiceURICredentials = args[1];

        Header fromServiceAuthHeader = new Header("Authorization", "Basic " + fromServiceURICredentials);
        Header toServiceAuthHeader = new Header("Authorization", "Basic " + toServiceURICredentials);

        ClaimManagementServiceStub importStub = new ClaimManagementServiceStub(configuration.getFromServiceURI());
        importStub._getServiceClient().getOptions().setProperty(HTTPConstants.HTTP_HEADERS, new ArrayList<Header>()
        {{ add(fromServiceAuthHeader);}});

        ClaimManagementServiceStub exportStub = new ClaimManagementServiceStub(configuration.getToServiceURI());
        exportStub._getServiceClient().getOptions().setProperty(HTTPConstants.HTTP_HEADERS, new ArrayList<Header>()
        {{ add(toServiceAuthHeader);}});

        // Fetch existing claims from old IS
        ClaimDialectDTO claimDialectDTO = importStub.getClaimMappingByDialect(configuration.getClaimDialectURI());

        List claimTags = Arrays.asList(configuration.getClaimDisplayTags());

        for (ClaimMappingDTO claimMappingDTO : claimDialectDTO.getClaimMappings()) {

            String displayTag = claimMappingDTO.getClaim().getDisplayTag();

            logger.info("Processing Claim : " + displayTag);

            if (claimTags.contains(displayTag)) {
                logger.info("Adding claim to new Identity Server : " + displayTag);
                try {
                    exportStub.addNewClaimMapping(claimMappingDTO);
                } catch (Exception e) {
                    logger.error("Error while adding claim : " + displayTag, e);
                }
            }
        }
    }

    private static Configuration parseConfigurations() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        Configuration configuration = mapper.readValue(new File("config.yaml"), Configuration.class);

        logger.info("Printing configuration : ");
        logger.info(configuration.toString());
        logger.info("=========================");

        return configuration;
    }
}
