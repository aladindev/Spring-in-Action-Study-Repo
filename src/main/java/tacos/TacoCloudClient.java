package tacos;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TacoCloudClient {

	
	/*
	 * 간단하게 구현할 수 있는 REST API RestTemplate
	 * 하지만 하이퍼링크를 필요로 하는 client 구현에서는 용이하지 않다.
	 * */
	@Autowired
	private RestTemplate rest;
	
	/**
	 * 스프링 JMS 통합 비동기 메세징 처리 라이브러리
	 * send() : 원시 메시지 전송  
	 * convertAndSend() : 객체로부터 변환된 메시지를 전송한다.
	 *   >전송에 앞서 후처리(post-processing)되는 메시지를 전송할 수 있다.
	 * */
	@Autowired
	private JmsTemplate jmsTemplate;

	// Resource Get
	public Ingredient getIngredientById(String ingredientId) {

		/*
		 * getForObject Method 두 번째 파라미터는 응답이 바인딩되는 타입이다. JSON형식인 응답 데이터가 객체로 역직렬화되어
		 * 반환된다.
		 * 
		 */
//		return rest.getForObject("http://localhost:8080/ingredients/{id}",
//							Ingredient.class, ingredientId);

		/*
		 * Map을 사용해서 URL변수 지정하는 방식
		 */
//		Map<String, String> urlVariables = new HashMap<>();
//		urlVariables.put("Id", ingredientId);
//		return rest.getForObject("http://localhost:8080/ingredients/{id}",
//							Ingredient.class, ingredientId);

		/*
		 * URI 매개변수 사용 방식
		 */
//		Map<String, String> urlVariables = new HashMap<>();
//		urlVariables.put("id", ingredientId);
//
//		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/ingredients/{id}").build(urlVariables);
//		return rest.getForObject(uri, Ingredient.class);

		/**
		 * getForEntity() : getForObject()와 같은 방식으로 동작하지만 응답 결과를 나타내는 도메인 객체를 포함하는
		 * ResponseEntity 객체를 반환한다. header 정보와 같은 상세한 정보를 담고 있다.
		 */
		ResponseEntity<Ingredient> responseEntity = rest.getForEntity("http://localhost:8080/ingredients/{id}",
				Ingredient.class, ingredientId);

		log.info("Fetched time ::   " + responseEntity.getHeaders().getDate());
		return responseEntity.getBody(); // ingredient entity
	}

	// Resource Put(리소스 쓰기)
	public void updateIngredient(Ingredient ingredient) {
		/*
		 *  URL placeHolder에 담긴 id로 ingredient 객체 수
		 * */
		rest.put("http://localhost:8080/ingredients/{id}"
				, ingredient, ingredient.getId());
	}
	
	// Resource Delete
	public void deleteIngredient(Ingredient ingredient) {
		rest.delete("http://localhost:8080/ingredients/{id}"
					, ingredient.getId());
		
	}
	
	// Resource Add
	public Ingredient createIngredient(Ingredient ingredient) {
		
		/*
		 *  POST 요청 수행 후 새로 생성된 리소스를 반환 받을 수 있는 메소드 
		 *  postForObject()
		 * */
		return rest.postForObject("http://localhost:8080/ingredients/{id}"
				, ingredient, Ingredient.class);

	}
	
	/*
	 *  새로 생성된 리소스의 위치를 반환하는 메소드
	 *  postForLocation() 
	 * */
	public URI createIngredientForURI(Ingredient ingredient) {
		return rest.postForLocation("http://localhost:8080/ingredients/{id}" 
				, ingredient);
	}
	
	/*
	 *  새로 생성된 리소스의 위치와 리소스 객체 모두를 반환하는 메소드 
	 * 	postForEntity()
	 * */
	public Ingredient createIngredientForEntity(Ingredient ingredient) {
		ResponseEntity<Ingredient> responseEntity =
				rest.postForEntity("http://localhost:8080/ingredients/{id}"
						, ingredient, Ingredient.class);
		
		log.info("New Resource created at : " + responseEntity.getHeaders().getLocation());
		
		return responseEntity.getBody();
	}
	
	/*
	 *  스프링 데이터 HATEOAS에서 같이 제공되는 Traverson REST API 
	 *  하이퍼 미디어 API를 사용할 수 있다.
	 * */
	public void traversonFunc() {
		
		//Traverson을 사용할 때는 API의 기본 URI를 갖는 객체를 생성해야 한다.
		Traverson traverson = new Traverson(URI.create("http://localhost:8080/api")
											, MediaTypes.HAL_JSON);
				
		// 하이퍼링크를 추적하며 API사용
		ParameterizedTypeReference<Resources> ingredientType =
				new ParameterizedTypeReference<Resources>() {}; 
				
	}
}
