package io.rxson.reactor;

import io.rxson.reactive.FlowablePath;
import io.rxson.reactive.ReactiveProvider;
import io.rxson.reactor.model.Airport;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;

import static org.junit.Assert.assertEquals;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class ReactorProviderTest {

    ReactiveProvider provider = new ReactorProvider();

    @Test
    public void createInstance_AndMapFieldsTest() throws InterruptedException {
        ReactorModel streamInstance = provider.createInstance(ReactorModel.class);
        var streamableFields = provider.mapFields(ReactorModel.class);
        streamableFields.forEach(it -> it.invokeSetter(streamInstance));

        streamableFields.forEach(Assert::assertNotNull);

        var airport = new Airport();

        airport.setName("A1");
        airport.setCode("C1");

        var f = streamInstance.getAirports();

        new Thread(() -> f.doOnNext(it -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(airport.getName(), it.getName());
            assertEquals(airport.getCode(), it.getCode());
        }).subscribe()).start();
        Thread.sleep(20);
        var emitter = streamableFields.get(0).getFlowEmitter();
        Assert.assertTrue(emitter instanceof ReactorEmitter);
        emitter.next(airport);
        Assert.assertNotNull(emitter);
        emitter.complete();
    }

    @Test
    public void createInstance_AndMapMonoFieldsTest() throws InterruptedException {
        var jsonPath = "$.[*]";
        var streamableField = provider.mapMonoFlowablePath(Airport.class, jsonPath);
        var streamInstance = new FlowablePath<Airport>(jsonPath);
        var publisher = streamableField.getPublisher();
        Assert.assertTrue(publisher instanceof Flux);
        streamInstance.setResult(publisher);

        Assert.assertNotNull(streamableField);

        var airport = new Airport();

        airport.setName("A1");
        airport.setCode("C1");

        var f = (Flux<Airport>) streamInstance.getResult();

        new Thread(() -> f.doOnNext(it -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(airport.getName(), it.getName());
            assertEquals(airport.getCode(), it.getCode());
        }).subscribe()).start();
        Thread.sleep(20);
        var emitter = streamableField.getFlowEmitter();
        emitter.next(airport);
        Assert.assertNotNull(emitter);
        emitter.complete();
    }
}