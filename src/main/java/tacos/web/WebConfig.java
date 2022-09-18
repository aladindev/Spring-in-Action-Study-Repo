package tacos.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	/**
	 *  뷰에 요청 전달만을 수행하는 컨트롤러 = ViewController
	 *  WebConfig는 ViewController 역할을 수행하는 구성 클래스이며,
	 *  중요한 것은 WebMvcConfigurer 인터페이스를 구현한다는 점이다.
	 * */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
	}

}
