package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.Ingredient;

/***
 * CrudRepository<Ingredient, String> CRUD연산을 위한 많은 메소드가 내장되어 있다.
 * 제너릭 첫 번째 인자는 레포지토리에 저장되는 Entity 개체 타입이며 
 * 두 번째 매개변수는 Entity의 ID(KEY) 속성의 타입이다.
 */
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
	
	

}
