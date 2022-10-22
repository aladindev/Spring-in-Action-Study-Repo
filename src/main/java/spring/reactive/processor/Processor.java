package spring.reactive.processor;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public interface Processor<T, R> extends Subscriber<T>, Publisher<R>{}
