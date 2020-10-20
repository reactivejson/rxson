package io.rxson.reactive;

import io.reactivex.Flowable;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class FlowablePath<T> {
    @ReactiveIgnore
    String path;

    public Flowable<T> result;

    public FlowablePath(String path) {
        this.path = path;
    }

    public Flowable<T> getResult() {
        return result;
    }

    public void setResult(final Flowable<T> result) {
        this.result = result;
    }

    public String getPath() {
        return path;
    }
}