package com.quorum.offchain.server;

import com.sun.net.httpserver.HttpServer;
import org.jboss.resteasy.plugins.server.sun.http.HttpContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * A RestEasy and Sun HTTP server implementation
 */
public class RestEasyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestEasyServer.class);

    private HttpServer server;

    private URI uri;

    private final Application application;

    public RestEasyServer(final Application application, URI uri) {
        this.uri = uri;
        this.application = application;
    }

    public void start() throws Exception {
        this.server = HttpServer.create(new InetSocketAddress(this.uri.getPort()), 1);

        final HttpContextBuilder contextBuilder = new HttpContextBuilder();
        contextBuilder.getDeployment().setApplication(this.application);
        contextBuilder.bind(this.server);
        LOGGER.info("Starting server...");
        this.server.start();
    }

    public void stop() {
        LOGGER.info("Stopping Jersey server at {}", uri);

        if (Objects.nonNull(this.server)) {
            this.server.stop(0);
        }

        LOGGER.info("Stopped Jersey server at {}", uri);
    }


}
