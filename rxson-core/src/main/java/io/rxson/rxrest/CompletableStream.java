package io.rxson.rxrest;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * @author Mohamed Aly Bou Hanane ©2020
 *
 * <p> This class provides method {@link CompletableStream#getAsyncResponse()} for accessing the response status code,
 * headers, the response body, and the {@code HttpRequest} corresponding
 * to this response.
 * <p> An {@code HttpResponse} is
 * made available when the response status code and headers have been received,
 * and typically after the response body has also been completely received.
 */
public class CompletableStream<T> {
    private CompletableFuture<HttpResponse<T>> asyncResponse;

    public CompletableFuture<HttpResponse<T>> getAsyncResponse() {
        return asyncResponse;
    }

    public void setAsyncResponse(final CompletableFuture<HttpResponse<T>> asyncResponse) {
        this.asyncResponse = asyncResponse;
    }
}
