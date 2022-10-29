package tacos.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import tacos.Taco;
import tacos.data.TacoRepositoryByWebflux;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

/* 함수형 웹 프로그래밍 방식을 사용한 Reactive Api */
@Configuration
public class RouterFunctionConfig {

	@Autowired
	private TacoRepositoryByWebflux tacoRepoWebflux;
	
	@Bean
	public RouterFunction<?> routerFunction() {
		return route(GET("/design/taco") // RequestPredicate : 처리될 요청의 종류 
				, this::recents); 		 // RouterFunction : 일치하는 요청이 어떻게 핸들러에게 전달되어야 하는 지 정
				
	}
	
	public Mono<ServerResponse> recents(ServerRequest request) {
		return ServerResponse.ok()
				.body(tacoRepoWebflux.findAll().take(12), Taco.class);
	}
}
