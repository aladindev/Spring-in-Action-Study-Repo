package ReactiveTest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

/* 리액티브 스트림의 데이터 버퍼링하기 */
public class FluxBufferingTests {
	/* Flux를 통해 전달되는 데이터를 처리하는 동안 데이터 스트림을 작은 덩어리로 분할하면
	 * 도움이 될 수 있다. 이때 Buffer() operation을 사용할 수 있다. */
	@Test
	  public void buffer() {
	    Flux<String> fruitFlux = Flux.just(
	        "apple", "orange", "banana", "kiwi", "strawberry");
	    
	    Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);
	    
	    StepVerifier
	        .create(bufferedFlux)
	        .expectNext(Arrays.asList("apple", "orange", "banana"))
	        .expectNext(Arrays.asList("kiwi", "strawberry"))
	        .verifyComplete();
	  }
	
	  @Test
	  public void bufferAndFlatMap() throws Exception {
	    Flux.just(
	        "apple", "orange", "banana", "kiwi", "strawberry")
	        .buffer(3)
	        .flatMap(x -> 
	          Flux.fromIterable(x)
	            .map(y -> y.toUpperCase())
	            .subscribeOn(Schedulers.parallel())   
	            .log()
	        ).subscribe();
	  }
	  
	  /* 리액티브 타입에 로직 오퍼레이션 수행하기 */
	  @Test
	  /* 모든 Flux 항목을 검사하는 Operation : all() */
	  public void all() {
	    Flux<String> animalFlux = Flux.just(
	        "aardvark", "elephant", "koala", "eagle", "kangaroo");
	    
	    Mono<Boolean> hasAMono = animalFlux.all(a -> a.contains("a"));
	    StepVerifier.create(hasAMono)
	      .expectNext(true)
	      .verifyComplete();
	    
	    Mono<Boolean> hasKMono = animalFlux.all(a -> a.contains("k"));
	    StepVerifier.create(hasKMono)
	      .expectNext(false)
	      .verifyComplete();
	  }
	  
	  @Test
	  /* all()과 반대로 최소한 하나의 항목이 일치하는지 검사하는 오퍼레이션 : any() */
	  public void any() {
	    Flux<String> animalFlux = Flux.just(
	        "aardvark", "elephant", "koala", "eagle", "kangaroo");
	    
	    Mono<Boolean> hasAMono = animalFlux.any(a -> a.contains("a"));
	    
	    StepVerifier.create(hasAMono)
	      .expectNext(true)
	      .verifyComplete();
	    
	    Mono<Boolean> hasZMono = animalFlux.any(a -> a.contains("z"));
	    StepVerifier.create(hasZMono)
	      .expectNext(false)
	      .verifyComplete();
	  }
}
