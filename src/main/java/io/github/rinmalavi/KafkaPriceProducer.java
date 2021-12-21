package io.github.rinmalavi;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Random;

@ApplicationScoped
public class KafkaPriceProducer {

    private final Random random = new Random();

    @Outgoing("prices-in-mem")
    public Multi<Double> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .map(x -> random.nextDouble());
    }

}
