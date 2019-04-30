package com.quorum.offchain.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlMimeType;

/**
 * Model representation of a JSON body on outgoing HTTP requests
 * <p>
 * Contains a Base64 encoded string that is the decrypting payload of a transaction
 */
@ApiModel
public class BytesResponse {

    @XmlMimeType("base64Binary")
    @ApiModelProperty("Raw data")
    private byte[] payload;

    public BytesResponse(final byte[] payload) {
        this.payload = payload;
    }

    public BytesResponse() {
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(final byte[] payload) {
        this.payload = payload;
    }
}

