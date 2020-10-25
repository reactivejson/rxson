package io.rxson.reactor;

import io.rxson.reactive.ReflectionUtils;
import io.rxson.reactor.model.Airport;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class ReactorPathTest {

    static ReactorPath<Airport> reactorPathFlux;
    static ReactorPath<String> reactorPathFlatFlux;
    static ReactorPath<String> reactorPathReactive;
    static ReactorPath<Map<String, String>> reactorPathMap;

    @BeforeClass
    public static void setUp() {
        List<Field> fields = ReflectionUtils.getClassFields(ReactorModel.class);
        reactorPathFlux = new ReactorPath<>(fields.get(0));
        reactorPathFlatFlux = new ReactorPath<>(fields.get(1));
        reactorPathReactive = new ReactorPath<>(fields.get(2));
        reactorPathMap = new ReactorPath<>(fields.get(3));
    }

    @Test
    public void getPublisherTest() throws InterruptedException {
        var airport = new Airport();

        airport.setName("A1");
        airport.setCode("C1");
        var flow = reactorPathFlux.getPublisher();
        Assert.assertNotNull(flow);

        new Thread(() -> flow.doOnNext(it -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(airport.getName(), it.getName());
            assertEquals(airport.getCode(), it.getCode());
        }).subscribe()).start();
        Thread.sleep(200);
        var emitter = reactorPathFlux.getFlowEmitter();
        emitter.next(airport);
        Assert.assertNotNull(emitter);
        emitter.complete();
    }

    @Test
    public void isPublisherTest() {
        Assert.assertTrue(reactorPathFlux.isPublisher());
        Assert.assertTrue(reactorPathReactive.isPublisher());
        Assert.assertTrue(reactorPathMap.isPublisher());
    }

    @Test
    public void getJsonPathTest() {
        assertEquals("$.airports[*]", reactorPathFlux.getJsonPath());
        assertEquals("$.airlines[*]", reactorPathReactive.getJsonPath());
        assertEquals("$[*]", reactorPathFlatFlux.getJsonPath());
        assertEquals("$.map[*]", reactorPathMap.getJsonPath());
    }

    @Test
    public void getGenericTypeTest() {
        assertEquals(Airport.class, reactorPathFlux.getGenericType());
        assertEquals(String.class, reactorPathFlatFlux.getGenericType());
        assertEquals(Long.class, reactorPathReactive.getGenericType());
        assertEquals(Map.class, reactorPathMap.getGenericType());
    }

}