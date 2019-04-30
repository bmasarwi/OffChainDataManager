package com.quorum.offchain.api.model;

import com.sun.istack.Nullable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlMimeType;

@ApiModel
public class StoreDataRequest {

    @XmlMimeType("base64Binary")
    @Size(min = 1)
    @NotNull
    @ApiModelProperty("Encrypted payload to send to other parties.")
    private byte[] data;

    @ApiModelProperty("Recipients public keys")
    @Nullable
    private String[] shareWith;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String[] getShareWith() {
        return shareWith;
    }

    public void setShareWith(final String... shareWith) {
        this.shareWith = shareWith;
    }
}
