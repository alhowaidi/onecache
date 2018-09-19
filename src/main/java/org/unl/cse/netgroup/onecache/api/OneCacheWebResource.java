/*
 * Copyright 2018-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unl.cse.netgroup.onecache.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.rest.AbstractWebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unl.cse.netgroup.onecache.NdnInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;

import static org.onlab.util.Tools.nullIsNotFound;

/**
 * OneCache web resource.
 */
@Path("")
public class OneCacheWebResource extends AbstractWebResource {

    private Logger log = LoggerFactory.getLogger(OneCacheWebResource.class);
    private static NdnInfo ndnInfo;

    /**
     * OneCache application info.
     *
     * @return 200 OK
     */
    @GET
    @Path("info")
    public Response getGreeting() {
        ObjectNode node = mapper().createObjectNode().put("Application", "OneCache");
        return ok(node).build();
    }

    @GET
    @Path("interest")
    public Response getInterest() {
        ObjectNode root = mapper().createObjectNode();

        try {
            root.put("Name", ndnInfo.getName());
            root.put("FileResource", ndnInfo.getInterestFileResource());
            root.put("SrcIP", ndnInfo.getInterestSrc());
            root.put("DstIP", ndnInfo.getInterestDst());
        }catch (NullPointerException e) {
            throw new NullPointerException("Empty get() methods");
        }

        return ok(root).build();
    }


    @POST
    @Path("interest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postInterest(InputStream stream) {
        ObjectNode root = mapper().createObjectNode();

        ndnInfo = JsonToNdnInfo(stream);
        ndnInfo.logInfo();

        root.put("Status", "Success");
        return ok(root).build();
    }


    private NdnInfo JsonToNdnInfo(InputStream stream) {
        JsonNode jsonNode;
        try {
            jsonNode = mapper().readTree(stream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse the OneCache NDN POST.");
        }

        String name = jsonNode.path("name").asText(null);
        String interestFileResource = jsonNode.path("interestFileResource").asText(null);
        String interestSrc = jsonNode.path("interestSrc").asText(null);
        String interestDst = jsonNode.path("interestDst").asText(null);

        log.info("Received an Interest.");
        if (name != null && interestFileResource != null && interestSrc != null && interestDst != null) {
            return new NdnInfo(name, interestFileResource, interestSrc, interestDst);
        } else {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

    }

}
