package tacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TacoCloudClient {

	@Autowired
	private RestTemplate rest;

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
		 *  getForEntity() : getForObject()와 같은 방식으로 동작하지만
		 *  응답 결과를 나타내는 도메인 객체를 포함하는 ResponseEntity 객체를 반환한다. 
		 *  header 정보와 같은 상세한 정보를 담고 있다.
		 * */
		ResponseEntity<Ingredient> responseEntity =
					rest.getForEntity("http://localhost:8080/ingredients/{id}"
							,Ingredient.class, ingredientId);
		
		log.info("Fetched time ::   " + responseEntity.getHeaders().getDate());
		return responseEntity.getBody(); // ingredient entity
	}
}
