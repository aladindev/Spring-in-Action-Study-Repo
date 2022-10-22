package ReactiveTest;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/* 리액티브 스트림의 변환과 필터링 테스트 */
public class FluxTransformingTests {

	/* Skip */
	@Test
	  public void skipAFew() {
	    Flux<String> countFlux = Flux.just(
	        "one", "two", "skip a few", "ninety nine", "one hundred")
	        .skip(3);
	   
	    StepVerifier.create(countFlux)
	        .expectNext("ninety nine", "one hundred")
	        .verifyComplete();
	  }
	
	/* 항목을 건너뛰는 것이 아닌 일정 시간이 경과할 때까지 처음의 여러 항목을 건너뛰는 경우 */
	@Test
	  public void skipAFewSeconds() {
	    Flux<String> countFlux = Flux.just(
	        "one", "two", "skip a few", "ninety nine", "one hundred")
	        .delayElements(Duration.ofSeconds(1))
	        .skip(Duration.ofSeconds(4));
	   
	    StepVerifier.create(countFlux)
	        .expectNext("ninety nine", "one hundred")
	        .verifyComplete();
	  }
	
	/* skip() operation과 반대의 경우 
	 * take()는 처음부터 지정된 수의 항목만을 방출한다. 
	 * 지정된 수의 메세지만 전달하고 구독을 취소시킨다.*/
	@Test
	  public void take() {
	    Flux<String> nationalParkFlux = Flux.just(
	        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Acadia")
	        .take(3);
	   
	    StepVerifier.create(nationalParkFlux)
	        .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
	        .verifyComplete();
	  }
	
	/* take()역시 항목 수가 아닌 경과 시간을 기준으로 하는 다른 형태도 갖는다.
	 * flux로부터 전달되는 항목이 일정 시간이 경과될 동안만 방출된다. */
	@Test
	  public void takeForAwhile() {
	    Flux<String> nationalParkFlux = Flux.just(
	        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
	        .delayElements(Duration.ofSeconds(1))
	        .take(Duration.ofMillis(3500));
	   
	    StepVerifier.create(nationalParkFlux)
	        .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
	        .verifyComplete();
	  }
	
	/* Flux 값의 더 범용적인 필터링을 할 때는 filter() 오퍼레이션이 유리하다. */
	@Test
	  public void filter() {
	    Flux<String> nationalParkFlux = Flux.just(
	        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
	        .filter(np -> !np.contains(" "));
	   
	    StepVerifier.create(nationalParkFlux)
	        .expectNext("Yellowstone", "Yosemite", "Zion")
	        .verifyComplete();
	  }
}
