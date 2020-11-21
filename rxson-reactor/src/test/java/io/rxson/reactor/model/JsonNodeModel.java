package io.rxson.reactor.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.rxson.reactive.Reactive;
import reactor.core.publisher.Flux;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class JsonNodeModel {
    /*
       [
            {
              "Airport": {
                "Code": "ATL",
                "Name": "Atlanta, GA: Hartsfield-Jackson Atlanta International"
              },
              ....
            }
            ...
       ]
     */
    @Reactive(path = "$[*].Airport")
    private Flux<JsonNode> result;

    public Flux<JsonNode> getResult() {
        return result;
    }

    public void setResult(final Flux<JsonNode> result) {
        this.result = result;
    }
}