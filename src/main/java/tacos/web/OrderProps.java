package tacos.web;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="taco.orders")
@Data
@Validated
/**
 * 구성 속성 홀더
 * 다른 Bean에서 속성값을 참조할 수 있도록 스프링 컨텍스트에 Bean객체로 등록한다.
 * 코드의 간결화, 여러 bean객체에서 해당 속성 재사용 용이 
 * PageSize 외에 여러 Application 구성 속성을 이곳에 정의하여
 * 공통부만 수정할 수 있도록 한다.
 * */
public class OrderProps {
	
	@Min(value=5, message="must be between 5 and 25")
	@Max(value=25, message="must be between 5 and 25")
	private int pageSize = 20;

}
