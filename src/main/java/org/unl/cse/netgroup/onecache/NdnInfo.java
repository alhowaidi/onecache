package org.unl.cse.netgroup.onecache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NdnInfo {

    private String name;
    private String interestFileResource;
    private String interestSrc;
    private String interestDst;

    private Logger log = LoggerFactory.getLogger(NdnInfo.class);

    public NdnInfo(String name, String interestFileResource, String interestSrc, String interestDst) {
        this.name = name;
        this.interestFileResource = interestFileResource;
        this.interestSrc = interestSrc;
        this.interestDst = interestDst;
    }

    public String getName() {
        return name;
    }

    public String getInterestFileResource() {
        return interestFileResource;
    }

    public String getInterestSrc() {
        return interestSrc;
    }

    public String getInterestDst() {
        return interestDst;
    }

    public void logInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name:" ).append(this.name).append(", ")
                .append("Interest File: ").append(this.interestFileResource).append(", ")
                .append("Interest Src: ").append(this.interestSrc).append(", ")
                .append("Interest Dst: ").append(this.interestDst);
        log.info(sb.toString());
    }
}
