package io.rxson.examples.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Flowable;
import io.rxson.reactive.Reactive;

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
    private Flowable<JsonNode> result;

    public Flowable<JsonNode> getResult() {
        return result;
    }

    public void setResult(final Flowable<JsonNode> result) {
        this.result = result;
    }
}