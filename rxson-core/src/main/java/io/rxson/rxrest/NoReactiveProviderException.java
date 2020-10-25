package io.rxson.rxrest;

/**
 * @author Mohamed Aly Bou Hanane
 *
 * An Exception to thro if no Reactive provider provided.
 */
public class NoReactiveProviderException extends RuntimeException {

    /**
     *
     * @param msg The error message
     * @param err The throwable error
     */
    public NoReactiveProviderException(String msg, Throwable err) {
        super(msg, err);
    }

    /**
     *
     * @param msg The error message
     */
    public NoReactiveProviderException(String msg) {
        super(msg);
    }
}
