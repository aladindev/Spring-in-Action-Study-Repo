package tacos;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
/***
 * access=AccessLevel.PRIVATE =>클래스 외부에서 사용하지 못하도록 캡슐화 
 * force=true => 초기화가 필요한 final 상수 최초 강제 null set
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Entity //JPA 의 개체(Entity)임을 선
public class Ingredient {
	
	@Id //Domain primaryKey 지정 
	private final String id;
	private final String name;
	private final Type type;
	
	public static enum Type {
		WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
	}
	 

}
