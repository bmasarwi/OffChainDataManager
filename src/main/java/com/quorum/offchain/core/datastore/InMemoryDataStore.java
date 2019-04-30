package com.quorum.offchain.core.datastore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class InMemoryDataStore<DataHash, Data> implements DataStore<DataHash, Data> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryDataStore.class);

    private HashMap<DataHash, Data> hashMap;

    public InMemoryDataStore() {
        this.hashMap = new HashMap<>();
    }

    public void put(DataHash dataHash, Data data) {

        hashMap.put(dataHash, data);

        printDataStore();
    }

    public boolean containsKey(DataHash dataHash) {
        return this.hashMap.containsKey(dataHash);
    }

    public Data get(DataHash dataHash) {
        return this.hashMap.get(dataHash);
    }

    private void printDataStore() {
        for (DataHash hash : hashMap.keySet()) {
            Data data = hashMap.get(hash);
            LOGGER.info(hash.toString() + " : " + data.toString());
        }
    }
}
