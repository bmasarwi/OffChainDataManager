package com.quorum.offchain.api;

import com.quorum.offchain.server.OffChainDataManager;
import com.quorum.offchain.server.RestEasyServer;
import com.quorum.tessera.node.PartyInfoService;
import io.restassured.http.Header;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class E2ETest {

    private RestEasyServer restEasyServer;
    private RestEasyServer restEasyServer2;
    private String server1Url = "http://localhost:8081";
    private String server2Url = "http://localhost:8082";
    private URI server1Uri;
    private URI server2Uri;
    private String server1StoreDataUrl = server1Url + "/storedata";
    private String server1GetDataUrl = server1Url + "/data/";


    @Before
    public void before() throws Exception {
        server1Uri = new URI(server1Url);

        server2Uri = new URI(server2Url);
        PartyInfoService partyInfoService = mock(PartyInfoService.class);
        when(partyInfoService.getURLFromRecipientKey(any())).thenReturn(server2Url);
        restEasyServer = new RestEasyServer(new OffChainDataManager(partyInfoService), server1Uri);
        restEasyServer2 = new RestEasyServer(new OffChainDataManager(partyInfoService), server2Uri);
        restEasyServer.start();
        restEasyServer2.start();
    }

    @After
    public void after() {
        restEasyServer.stop();
        restEasyServer2.stop();
    }

    @Test
    public void storeDataTest() throws URISyntaxException, UnsupportedEncodingException {

        byte[] data = {1, 2, 3};
        String dataHash = "DGCuBPuxf+NvToRjGluPPNbQzUboAFa9/sl/0wX3ZNqt74rhrcibIDBD1+KvH7NB3wzl9m3+MgTsOpgxUyqOTA==";
        JSONObject requestParams = new JSONObject();
        requestParams.put("data", Base64.getEncoder().encodeToString(data));
        requestParams.put("shareWith", new String[]{"xyz"});
        String jsonString = requestParams.toJSONString();

        given()
                .header(new Header("content-type", "application/json"))
                .when()
                .body(jsonString)
                .log().all()
                .post(new URI(server1StoreDataUrl))
                .then()
                .statusCode(equalTo(200))
                .body(equalTo(dataHash))
                .log().all();


        String encodedHash = URLEncoder.encode(dataHash, "UTF-8");

        given()
                .when()
                .get(new URI(server1GetDataUrl + encodedHash))
                .then()
                .statusCode(equalTo(200))
                .body("payload", equalTo("AQID"))
                .log().all();
    }

    @Test
    public void fetchNonExistingDataTest() throws URISyntaxException, UnsupportedEncodingException {

        given()
                .when()
                .get(new URI(server1GetDataUrl + "NonExistingHash"))
                .then()
                .statusCode(equalTo(500))
                .log().all();
    }

    @Test
    public void receive() {
    }
}