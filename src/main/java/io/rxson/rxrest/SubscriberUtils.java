package io.rxson.rxrest;

import io.rxson.reactive.Streamable;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.SurfingConfiguration;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Flow;

/**
 * @author Mohamed Aly Bou Hanane
 * ©2020
 */
final class SubscriberUtils {
    static SurfingConfiguration bind(final Collection<Streamable> streamableFields,
                                     final JsonSurfer surfer,
                                     final Flow.Subscription subscription) {
        final var builder = surfer.configBuilder();
        streamableFields
            .forEach(jsonStreamField ->
                builder.bind(jsonStreamField.getJsonPath(), (value, context) -> {
                    final var o = context.cast(value, jsonStreamField.getClazz());
                    final var emitter = jsonStreamField.getFlowEmitter();
                    if (emitter.isCancelled()) {
                        context.stop();
                        subscription.cancel();
                        return;
                    }
                    emitter.onNext(o);

                }));
        return builder.build();
    }

    static <T> SurfingConfiguration bind(final Streamable<T> streamableField,
                                         final Class<T> clazz,
                                         final JsonSurfer surfer,
                                         final Flow.Subscription subscription) {
        final var builder = surfer.configBuilder();
        builder.bind(streamableField.getJsonPath(), (value, context) -> {
            final var o = context.cast(value, clazz);
            final var emitter = streamableField.getFlowEmitter();
            if (emitter.isCancelled()) {
                context.stop();
                subscription.cancel();
                return;
            }
            emitter.onNext(o);
        });

        return builder.build();
    }

    static byte[] toBytes(final List<ByteBuffer> buffers) {
        final int size = buffers.stream().mapToInt(ByteBuffer::remaining).sum();
        final byte[] bs = new byte[size];
        int offset = 0;
        for (ByteBuffer buffer : buffers) {
            final int remaining = buffer.remaining();
            buffer.get(bs, offset, remaining);
            offset += remaining;
        }
        return bs;
    }
}
