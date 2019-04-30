package com.quorum.offchain.core;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;

import static java.util.Arrays.copyOf;

@Embeddable
public class DataHash implements Serializable {

    @Lob
    private byte[] hashBytes;

    public DataHash() {
    }

    public DataHash(final byte[] hashBytes) {
        this.hashBytes = Arrays.copyOf(hashBytes, hashBytes.length);
    }

    public DataHash(final String hashString) {
       this.hashBytes =  Base64.getDecoder().decode(hashString);
    }

    public void setHashBytes(final byte[] hashBytes) {
        this.hashBytes = copyOf(hashBytes, hashBytes.length);
    }

    public byte[] getHashBytes() {
        return copyOf(hashBytes, hashBytes.length);
    }

    @Override
    public boolean equals(final Object o) {
        return (o instanceof DataHash) &&
            Arrays.equals(hashBytes, ((DataHash) o).hashBytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getHashBytes());
    }

    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}

