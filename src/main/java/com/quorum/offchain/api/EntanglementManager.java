package com.quorum.offchain.api;

import com.quorum.offchain.Logged;
import com.quorum.offchain.PrivateApi;
import com.quorum.offchain.api.model.BytesResponse;
import com.quorum.offchain.api.model.StoreDataRequest;
import com.quorum.offchain.core.Data;
import com.quorum.offchain.core.DataHash;
import com.quorum.tessera.encryption.PublicKey;
import com.quorum.tessera.node.PartyInfoService;
import groovy.lang.Tuple2;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;


/**
 * Provides endpoints for dealing with off-chain data, including:
 * <p>
 * - Storing data off-chain
 * - Retrieving off-chain data
 */
@Logged
@Path("/")
public class EntanglementManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntanglementManager.class);

    private final OffChainGateway offChainGateway;
    private PartyInfoService partyInfoService;

    public EntanglementManager(OffChainGateway offChainGateway, PartyInfoService partyInfoService) {
        this.offChainGateway = offChainGateway;
        this.partyInfoService = partyInfoService;
    }


    @ApiOperation(value = "Store data through off-chain", produces = "Hash of input data")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Data Hash", response = String.class),
            @ApiResponse(code = 500, message = "Unknown server error")
    })
    @POST
    @PrivateApi
    @Path("storedata")
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    public Response storeData(StoreDataRequest request) throws ShareDataFailedException {

        //TODO: delete debug prints
        LOGGER.debug("Received data: " + new Data(request.getData()).toString());
        LOGGER.debug("Received shareWith: " + Arrays.toString(request.getShareWith()));

        //Store data locally
        DataHash dataHash = offChainGateway.storeData(request.getData());

        if(request.getShareWith() != null && request.getShareWith().length > 0) shareData(request);

        final String encodedKey = dataHash.toString();
        LOGGER.debug("Encoded key: {}", encodedKey);

        return Response.status(Response.Status.OK)
                .entity(encodedKey)
                .build();
    }

    private void shareData(StoreDataRequest request) throws ShareDataFailedException {
        final byte[][] recipientsPublicKeys = Stream.of(request.getShareWith())
                .map(data -> Base64.getDecoder().decode(data))
                .toArray(byte[][]::new);

        final Stream<PublicKey> recipientList = Stream
                .of(recipientsPublicKeys)
                .map(PublicKey::from);

        Client client = ClientBuilder.newClient();
        StoreDataRequest storeDataRequest = new StoreDataRequest();
        storeDataRequest.setData(request.getData());

        Stream<Tuple2<String, Response>> urlsAndResponses = recipientList
                .map(partyInfoService::getURLFromRecipientKey)
                .map(url -> {
                    String path = "/storedata";
                    Response response = client
                            .target(url)
                            .path(path)
                            .request()
                            .post(Entity.entity(storeDataRequest, MediaType.APPLICATION_JSON));

                    return new Tuple2<>(url + path, response);
                });

        Stream<Tuple2<String, Response>> failedStatuses = urlsAndResponses.filter(urlAndResponse ->
                urlAndResponse.getSecond().getStatus() != Response.Status.OK.getStatusCode());


        Optional<String> reduced = failedStatuses.map(urlAndResponse ->
                "URL: " + urlAndResponse.getFirst() + " , Response: " + urlAndResponse.getSecond().toString()).reduce(String::concat);

        if (reduced.isPresent()) throw new ShareDataFailedException(reduced.get());
    }

    /**
     * The hash parameter must be encoded before calling this endpoint:
     * <p>
     * String encodedHash = URLEncoder.encode(hash, "UTF-8");
     * <p>
     * Example in: com.quorum.offchain.api.E2ETest
     */

    @ApiOperation(value = "Returns data associated with provided hash")
    @ApiResponses({
            @ApiResponse(code = 200, response = BytesResponse.class, message = "Bytes Response object")
    })
    @GET
    @PrivateApi
    @Path("data/{hash}")
    @Produces(APPLICATION_JSON)
    public Response retrieveData(
            @ApiParam("Encoded hash used used to fetch the data")
            @NotNull @Valid @PathParam("hash") final String hash) throws UnsupportedEncodingException {

        String decodeHash = URLDecoder.decode(hash, "UTF-8");
        LOGGER.info("Received hash: " + decodeHash);

        byte[] dataBytes = offChainGateway.getData(new DataHash(decodeHash));
        BytesResponse bytesResponse = new BytesResponse(dataBytes);

        return Response.status(Response.Status.OK)
                .type(APPLICATION_JSON)
                .entity(bytesResponse)
                .build();


    }

    class ShareDataFailedException extends Exception {
        ShareDataFailedException(String msg) {
            super(msg);
        }
    }
}
