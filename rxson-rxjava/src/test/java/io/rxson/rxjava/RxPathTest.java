package io.rxson.rxjava;

import io.rxson.rxjava.model.Airport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static io.rxson.reactive.ReflectionUtils.getClassFields;
import static org.junit.Assert.*;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class RxPathTest {

    static RxPath<Airport> rxPathFlowable;
    static RxPath<String> rxPathFlatFlowable;
    static RxPath<String> rxPathReactive;
    static RxPath<Map<String, String>> rxPathMap;

    @BeforeClass
    public static void setUp() {
        List<Field> fields = getClassFields(RxModel.class);
        rxPathFlowable = new RxPath<>(fields.get(0));
        rxPathFlatFlowable = new RxPath<>(fields.get(1));
        rxPathReactive = new RxPath<>(fields.get(2));
        rxPathMap = new RxPath<>(fields.get(3));
    }

    @Test
    public void getPublisherTest() throws InterruptedException {
        var airport = new Airport();

        airport.setName("A1");
        airport.setCode("C1");
        var flow = rxPathFlowable.getPublisher();
        assertNotNull(flow);

        new Thread(() -> flow.doOnNext(it -> {
            Thread.sleep(300);
            assertEquals(airport.getName(), it.getName());
            assertEquals(airport.getCode(), it.getCode());
        }).subscribe()).start();
        Thread.sleep(200);
        var emitter = rxPathFlowable.getFlowEmitter();
        emitter.next(airport);
        assertNotNull(emitter);
        emitter.complete();
    }

    @Test
    public void isPublisherTest() {
        assertTrue(rxPathFlowable.isPublisher());
        assertTrue(rxPathReactive.isPublisher());
        assertTrue(rxPathMap.isPublisher());
    }

    @Test
    public void getJsonPathTest() {
        assertEquals("$.airports[*]", rxPathFlowable.getJsonPath());
        assertEquals("$.airlines[*]", rxPathReactive.getJsonPath());
        assertEquals("$[*]", rxPathFlatFlowable.getJsonPath());
        assertEquals("$.map[*]", rxPathMap.getJsonPath());
    }

    @Test
    public void getGenericTypeTest() {
        assertEquals(Airport.class, rxPathFlowable.getGenericType());
        assertEquals(String.class, rxPathFlatFlowable.getGenericType());
        assertEquals(Long.class, rxPathReactive.getGenericType());
        assertEquals(Map.class, rxPathMap.getGenericType());
    }

}