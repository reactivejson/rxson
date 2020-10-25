package io.rxson.rxrest;

/**
 * @author Mohamed Aly Bou Hanane
 * ©2020
 *
 * An Exception to thro if no Reactive publisher provided.
 */
public class UnsupportedReactiveType extends RuntimeException {

    /**
     *
     * @param msg The error message
     * @param err The throwable error
     */
    public UnsupportedReactiveType(String msg, Throwable err) {
        super(msg, err);
    }

    /**
     *
     * @param msg The error message
     */
    public UnsupportedReactiveType(String msg) {
        super(msg);
    }
}
