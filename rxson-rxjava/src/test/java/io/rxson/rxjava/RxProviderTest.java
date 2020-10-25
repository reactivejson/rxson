package io.rxson.rxjava;

import io.reactivex.Flowable;
import io.rxson.reactive.FlowablePath;
import io.rxson.reactive.ReactiveProvider;
import io.rxson.rxjava.model.Airport;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class RxProviderTest {

    ReactiveProvider provider = new RxProvider();

    @Test
    public void createInstance_AndMapFieldsTest() throws InterruptedException {
        RxModel streamInstance = provider.createInstance(RxModel.class);
        var streamableFields = provider.mapFields(RxModel.class);
        streamableFields.forEach(it -> it.invokeSetter(streamInstance));

        streamableFields.forEach(Assert::assertNotNull);

        var airport = new Airport();

        airport.setName("A1");
        airport.setCode("C1");

        var f = streamInstance.getAirports();

        new Thread(() -> f.doOnNext(it -> {
            Thread.sleep(30);
            assertEquals(airport.getName(), it.getName());
            assertEquals(airport.getCode(), it.getCode());
        }).subscribe()).start();
        Thread.sleep(20);
        var emitter = streamableFields.get(0).getFlowEmitter();
        assertTrue(emitter instanceof RxEmitter);
        emitter.next(airport);
        assertNotNull(emitter);
        emitter.complete();
    }

    @Test
    public void createInstance_AndMapMonoFieldsTest() throws InterruptedException {
        var jsonPath = "$.[*]";
        var streamableField = provider.mapMonoFlowablePath(Airport.class, jsonPath);
        var streamInstance = new FlowablePath<Airport>(jsonPath);
        var publisher = streamableField.getPublisher();
        assertTrue(publisher instanceof Flowable);
        streamInstance.setResult((Flowable<Airport>) publisher);

        assertNotNull(streamableField);

        var airport = new Airport();

        airport.setName("A1");
        airport.setCode("C1");

        var f = (Flowable<Airport>) streamInstance.getResult();

        new Thread(() -> f.doOnNext(it -> {
            Thread.sleep(30);
            assertEquals(airport.getName(), it.getName());
            assertEquals(airport.getCode(), it.getCode());
        }).subscribe()).start();
        Thread.sleep(20);
        var emitter = streamableField.getFlowEmitter();
        emitter.next(airport);
        assertNotNull(emitter);
        emitter.complete();
    }
}