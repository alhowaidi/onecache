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
package org.unl.cse.netgroup.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.rest.AbstractWebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unl.cse.netgroup.NdnInfo;

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

    private static Logger log = LoggerFactory.getLogger(OneCacheWebResource.class);
    private static NdnInfo ndnInfo;
    private final ObjectNode root = mapper().createObjectNode();

    /**
     * Get hello world greeting.
     *
     * @return 200 OK
     */
    @GET
    @Path("info")
    public Response getAppInfo() {
        ObjectNode node = mapper().createObjectNode().put("Application", "OneCache");
        return ok(node).build();
    }

    @POST
    @Path("ndn")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ndnPostInfo(InputStream stream) throws IOException {
        ndnInfo = JsonToNdnInfo(stream);
        ndnInfo.logInfo();

        String msg = "Message";
        return Response.ok(root).build();
    }

    @GET
    @Path("ndn")
    public Response getNdnInfo() {

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



    private NdnInfo JsonToNdnInfo(InputStream stream) {
        JsonNode jsonNode;
        try {
            jsonNode = mapper().readTree(stream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse the Mobius NDN POST.");
        }

        String name = jsonNode.path("name").asText(null);
        String interestFileResource = jsonNode.path("interestFileResource").asText(null);
        String interestSrc = jsonNode.path("interestSrc").asText(null);
        String interestDst = jsonNode.path("interestDst").asText(null);

        log.info("Pinged!");
        if (name != null && interestFileResource != null && interestSrc != null && interestDst != null) {
            return new NdnInfo(name, interestFileResource, interestSrc, interestDst);
        } else {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

    }

}
