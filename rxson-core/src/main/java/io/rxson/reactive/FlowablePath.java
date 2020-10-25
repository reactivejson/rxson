package io.rxson.reactive;

import org.reactivestreams.Publisher;

/**
 * @author Mohamed Aly Bou Hanane © 2020
 */
public class FlowablePath<T> {
    @ReactiveIgnore
    String path;

    public Publisher<T> result;

    public FlowablePath(String path) {
        this.path = path;
    }

    public Publisher<T> getResult() {
        return result;
    }

    public void setResult(final Publisher<T> result) {
        this.result = result;
    }

    public String getPath() {
        return path;
    }
}