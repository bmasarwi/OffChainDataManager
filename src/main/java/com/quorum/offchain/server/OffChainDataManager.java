package com.quorum.offchain.server;

import com.quorum.offchain.GlobalFilter;
import com.quorum.offchain.api.EntanglementManager;
import com.quorum.offchain.api.OffChainGateway;
import com.quorum.offchain.core.datastore.DataStoreManager;
import com.quorum.tessera.node.PartyInfoService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * The main application that is submitted to the HTTP server
 * Contains all the service classes created by the service locator
 */
@GlobalFilter
@ApplicationPath("/")
public class OffChainDataManager extends Application {

    private PartyInfoService partyInfoService;

    public OffChainDataManager(PartyInfoService partyInfoService) {
        this.partyInfoService = partyInfoService;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> objects = new HashSet<>();
        objects.add(new EntanglementManager(new OffChainGateway(new DataStoreManager()), partyInfoService));

        return objects;
    }
}
