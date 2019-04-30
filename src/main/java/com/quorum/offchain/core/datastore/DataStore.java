package com.quorum.offchain.core.datastore;

public interface DataStore<K, V> {

    public void put(K dataHash, V data);

    public boolean containsKey(K dataHash);

    public V get(K dataHash);
}
