package io.github.rinmalavi;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Channel;
import org.jboss.resteasy.reactive.RestSseElementType;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/prices-in-mem")
public class PriceInMemResource {

    @Inject
    @Channel("prices-in-mem")
    Multi<Double> prices;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestSseElementType("text/plain")
    public Multi<Double> stream() {
        return prices;
    }
}