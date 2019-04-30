package com.quorum.offchain.api;

import com.quorum.offchain.core.Data;
import com.quorum.offchain.core.DataHash;
import com.quorum.offchain.core.datastore.DataStoreManager;

/**
 * An intermediate layer between the API and the DataStore, potentially for:
 * <p>
 * - encrypting before storing data?
 * - validations on data
 * - any kind of intermediate action before storing data off-chain
 */
public class OffChainGateway {

    private DataStoreManager dataStoreManager;

    public OffChainGateway(DataStoreManager dataStoreManager) {

        this.dataStoreManager = dataStoreManager;
    }

    public DataHash storeData(byte[] input) {
        return dataStoreManager.storeData(new Data(input));
    }

    public byte[] getData(DataHash dataHash) {
        return dataStoreManager.getData(dataHash).getDataBytes();
    }
}
