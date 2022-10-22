package ReactiveTest;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxCreationTests {
	
	//Flux 생성 
	@Test
	public void createAFlux_just() {
		//다섯 개의 String 객체로부터 Flux를 생성한다.
		Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");
		
		/*
		 * 구독자 추가
		 * subscribe()를 호출하는 즉시 데이터가 구독자에게 전달되기 시작한다.
		 * 이 예에서는 데이터는 곧바로 Flux로부터 Subscriber로 전달된다.
		 */
		fruitFlux.subscribe(f -> System.out.println("Here's some fruit : " + f));
		
		/* 
		 * StepVerifier : Flux나 Mono가 지정되면 StepVerifier는 해당 리액티브 타입을 
		 * 구독한 다음에 스트림을 통해 전달되는 데이터에 대해 assertion(어서션:검증)을 적용한다.
		 * 그리고 해당 스트림이 기대한 대로 완전하게 작동하는지 검사한다. 
		 * */
//		StepVerifier.create(fruitFlux) //Flux 생성
//				.expectNext("Apple")
//				.expectNext("Orange")
//				.expectNext("Grape")
//				.expectNext("Banana")
//				.expectNext("Strawberry")
//				.verifyComplete();
	}
	
	@Test
	public void createAFlux_fromArray() {
		String[] fruits = new String[] {
				"Apple", "Orange", "Grape", "Banana", "Strawberry" 
		};
		
		Flux<String> fruitFlux = Flux.fromArray(fruits);
		
		StepVerifier.create(fruitFlux) //Flux 생성
		.expectNext("Apple")
		.expectNext("Orange")
		.expectNext("Grape")
		.expectNext("Banana")
		.expectNext("Strawberry")
		.verifyComplete();
	}
	
	@Test
	/*
	 * 데이터 없이 매번 새 값으로 증가하는 숫자를 방출하는 카운터 역할의 Flux
	 * 이와 같은 카운터 Flux를 생성할 때는 static method인 range()를 사용할 수 있다.
	 * */
	public void createAFlux_range() {
		Flux<Integer> intervalFlux = Flux.range(1, 5);
		
		StepVerifier.create(intervalFlux)
					.expectNext(1)
					.expectNext(2)
					.expectNext(3)
					.expectNext(4)
					.expectNext(5)
					;
			
	}

}
