package ReactiveTest;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

/*
 * 리액티브 타입 조합하기 
 * : 두 개 이상의 리액티브 타입을 결합하여 하나의 Flux를 생성한다.
 * */
public class FluxMergingTests {
	
	// 결합하기
	@Test
	public void mergeFluxes() {
		
		/*
		 * Flux는 가능한 빨리 데이터를 방출한다. 따라서 생성되는 Flux 스트림 두 개 모두에 
		 * delayElements() 오퍼레이션을 적용하여 조금 느리게 방출되도록 ms를 지정한다.
		 * 또한 foodFlux가 characterFlux 다음에 스트리밍을 시작하도록 foodFlux에 
		 * delaySubscription() 오퍼레이션을 적용하여 250ms가 지난 후에 구독 및 데이터를
		 * 방출하도록 한다.
		 * */
		//캐릭터 이름을 갖는 Flux
		Flux<String> characterFlux = Flux
								.just("Garfield", "Kojak", "Barbossa")
								.delayElements(Duration.ofMillis(500));
		
		//위 캐릭터들이 즐겨 먹는 식품 이름을 갖는 Flux
		Flux<String> foodFlux = Flux
							.just("Lasagna", "Lollipops", "Apples")
							.delaySubscription(Duration.ofMillis(250))
							.delayElements(Duration.ofMillis(500));
		
		Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);
		
		StepVerifier.create(mergedFlux)
        .expectNext("Garfield")
        .expectNext("Lasagna")
        .expectNext("Kojak")
        .expectNext("Lollipops")
        .expectNext("Barbossa")
        .expectNext("Apples")
        .verifyComplete();
	}
	
	/*
	 *  위의 mergeWith는 하나의 Flux에 지연 시간이 변경된다면 한 Flux의 값이 두 번씩 
	 *  방출되는 것을 볼 수도 있다. 
	 *  따라서 zip() 오퍼레이션(정적인 생성 오퍼레이션)을 이용하여 완벽하게 조합할 수 있다.
	 * */
	@Test
	  public void zipFluxes() {
	    Flux<String> characterFlux = Flux
	        .just("Garfield", "Kojak", "Barbossa");
	    Flux<String> foodFlux = Flux
	        .just("Lasagna", "Lollipops", "Apples");
	    
	    Flux<Tuple2<String, String>> zippedFlux = 
	        Flux.zip(characterFlux, foodFlux);
	    
	    StepVerifier.create(zippedFlux)
	          .expectNextMatches(p -> 
	              p.getT1().equals("Garfield") && 
	              p.getT2().equals("Lasagna"))
	          .expectNextMatches(p -> 
	              p.getT1().equals("Kojak") && 
	              p.getT2().equals("Lollipops"))
	          .expectNextMatches(p -> 
	              p.getT1().equals("Barbossa") && 
	              p.getT2().equals("Apples"))
	          .verifyComplete();
	  }

}
