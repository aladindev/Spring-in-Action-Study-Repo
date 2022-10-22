package spring.reactive.subscriber;

import org.reactivestreams.Subscription;

public interface Subscriber<T> {

	/*
	 * Subscriber가 수신할 첫 번째 이벤트는 onSubscriber()의 호출을 통해 이뤄진다.
	 * Publisher가 onSubscribe()를 호출할 때 이 메서드의 인자로 Subscription 객체를
	 * Subscriber에 전달한다. 
	 * Subscriber는 Subscription 객체를 통해서 구독을 관리할 수 있다.
	 * */
	void onSubscribe(Subscription sub);
	void onNext(T item);
	void onError(Throwable ex);
	void onComplete();
}
