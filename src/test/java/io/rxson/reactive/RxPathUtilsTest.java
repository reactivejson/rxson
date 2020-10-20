package io.rxson.reactive;

import io.reactivex.Flowable;
import io.rxson.examples.model.airline.Airport;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class RxPathUtilsTest {

    @Test
    public void createInstance_AndMapFieldsTest() throws InterruptedException {
        Model streamInstance = RxPathUtils.createInstance(Model.class);
        var streamableFields = RxPathUtils.mapFields(Model.class);
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
        emitter.onNext(airport);
        assertNotNull(emitter);
        emitter.onComplete();
    }

    @Test
    public void createInstance_AndMapMonoFieldsTest() throws InterruptedException {
        var jsonPath = "$.[*]";
        var streamableField = RxPathUtils.mapMonoFlowablePath(Airport.class, jsonPath);
        var streamInstance = new FlowablePath<Airport>(jsonPath);
        streamInstance.setResult(streamableField.getPublisher());

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
        emitter.onNext(airport);
        assertNotNull(emitter);
        emitter.onComplete();
    }
}
