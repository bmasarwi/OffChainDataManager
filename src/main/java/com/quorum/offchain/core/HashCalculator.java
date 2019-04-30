package com.quorum.offchain.core;

import org.bouncycastle.jcajce.provider.digest.SHA3;

public class HashCalculator {

    public static DataHash calculateHash(byte[] dataBytes){
        final SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        final byte[] digest = digestSHA3.digest(dataBytes);
        return new DataHash(digest);
    }
}
