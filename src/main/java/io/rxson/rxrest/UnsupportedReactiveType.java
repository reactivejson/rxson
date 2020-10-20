package io.rxson.rxrest;

/**
 * @author Mohamed Aly Bou Hanane
 * ©2020
 */
public class UnsupportedReactiveType extends RuntimeException {
    public UnsupportedReactiveType(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public UnsupportedReactiveType(String errorMessage) {
        super(errorMessage);
    }
}
