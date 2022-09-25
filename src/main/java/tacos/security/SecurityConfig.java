package tacos.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		   .authorizeRequests()
		    .antMatchers("/design", "/orders") //Security 적용할 경로 매
		     .access("hasRole('ROLE_USER')")
		    .antMatchers("/", "/**")
		     .access("permitAll")
		   .and()
		    .httpBasic();
		
	}
	
	@Override
	/**
	 * 인증을 하기 위해 사용자를 찾는 방법을 지정하는 코드를 작성해야 한다.
	 * 이 때 인자로 전달된 AuthenticationManagerBuilder를 사용한다.
	 */
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//보안 구성 자체에 사용자 정보를 직접 지정하는 방
		auth.inMemoryAuthentication()
		      .withUser("user1") //사용자 구성 
		      .password("{noop}password1")
		      .authorities("ROLE_USER") //부여 권한 => .rols("USER")와 동
		      .and() // and 메소드를 이용해서 여러 유저를 동시에 관리 가
		      .withUser("user2")
		      /* 스프링5 부터는 반드시 비밀번호를 암호화해야한다.
		       * 암호화하지 않을 시 접근 거부(HTTP 403 || Internal Server Error(HTTP 500)에러 
		       * */
		      .password("{noop}password2") // {noop}은 테스트용으로 암호화 미진행 
		      .authorities("ROLE_USER");
	}

	

}
