package spring.integration.filter;

import org.springframework.integration.annotation.Filter;
import org.springframework.stereotype.Component;

@Component
public class EvenNumberFilter {
	
	@Filter(inputChannel="numberChannel"
		   ,outputChannel="evenNumberChannel")
	public boolean evenNumberFilter(Integer number) {
		return number % 2 == 0;
	}

}
