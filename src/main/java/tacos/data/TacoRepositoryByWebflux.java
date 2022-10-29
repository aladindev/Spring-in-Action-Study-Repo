package tacos.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import tacos.Taco;

/* spring webflux : 비동기적 이벤트 처리 중심의 리액티브 프로그래밍 */
public interface TacoRepositoryByWebflux extends ReactiveCrudRepository<Taco, Long>{
	
}
