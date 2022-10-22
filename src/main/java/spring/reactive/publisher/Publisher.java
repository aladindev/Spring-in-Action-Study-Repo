package spring.reactive.publisher;

import org.reactivestreams.Subscriber;

/*
 * 리액티브(반응형)스트림
 * : 동시에 여러 작업을 수행(병렬적)하여 더 큰 확장성을 얻게 해준다.
 *   백 프레셔는 데이터를 소비(읽는)하는 컨슈머가 처리할 수 있는 만큼으로
 *   데이터를 제한함으로써 지나치게 빠른 데이터 소스로부터의 데이터 전달 폭주를 피할 수 있는 수단이다.
 * */

/**
 *
 * Publisher(발행자)
 *
 */
public interface Publisher<T> {
	// subsribe(구독), subscriber(구독자)가 구독신청되면 publisher로부터 이벤트를 수신할 수 있다.
	// 이 이벤트들은 subscriber인터페이스의 메서드를 통해 전송된다. 
	void subscribe(Subscriber<? super T> subscriber);

}
