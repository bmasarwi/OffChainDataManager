package com.quorum.offchain.core.datastore;

import com.quorum.offchain.core.Data;
import com.quorum.offchain.core.DataHash;
import com.quorum.offchain.core.HashCalculator;

public class DataStoreManager {

    //TODO: Store data on desk throughDAO then make the class static and stateless
    //TODO: Public and API callable from Quorum that invokes the functions below
    private DataStore<DataHash, Data> dataStore;

    public DataStoreManager(){
        this.dataStore = new InMemoryDataStore<>();//TODO: Spring inject this
    }

    public DataHash storeData(final Data data) {
        DataHash dataHash = HashCalculator.calculateHash(data.getDataBytes());
        dataStore.put(dataHash, data);

        return dataHash;
    }

    public Data getData(final DataHash dataHash) {
        if(!dataStore.containsKey(dataHash)){
            throw new IllegalArgumentException("Provided data hash not found: " + dataHash.toString());
        }

        return dataStore.get(dataHash);
    }

}
