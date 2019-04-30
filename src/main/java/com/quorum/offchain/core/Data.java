package com.quorum.offchain.core;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;

@Embeddable
public class Data implements Serializable {

    @Lob
    private byte[] dataBytes;

    public Data() {
    }

    public Data(final byte[] dataBytes) {
        this.dataBytes = Arrays.copyOf(dataBytes, dataBytes.length);
    }

    public void setDataBytes(final byte[] dataBytes) {
        this.dataBytes = Arrays.copyOf(this.dataBytes, this.dataBytes.length);
    }

    public byte[] getDataBytes() {
        return Arrays.copyOf(dataBytes, dataBytes.length);
    }

    @Override
    public boolean equals(final Object o) {
        return (o instanceof Data) &&
                Arrays.equals(dataBytes, ((Data) o).dataBytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getDataBytes());
    }

    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(dataBytes);
    }


}

