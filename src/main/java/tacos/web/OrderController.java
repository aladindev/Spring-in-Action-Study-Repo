package tacos.web;

import javax.validation.Valid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import tacos.Order;
import tacos.User;
import tacos.data.OrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
/**
 * 스프링 자동 구성-속성을 사용할 수 있도록 하는 애노테이션
 * 자동 구성 속성은 application.yml 파일에 정의되어 있다.
 * 들여쓰기 단위로 간다. 
 */
//@ConfigurationProperties(prefix="taco.orders") 
public class OrderController {
	
//	private int pageSize = 20;
//	public void setPageSize(int pageSize) {
//		this.pageSize = pageSize;
//	}
	private OrderProps props;
	
	private OrderRepository orderRepo;
	
	public OrderController(OrderRepository orderRepo
			 			  //, @Valid OrderProps props
							, OrderProps props	
			 			  ) {
		this.orderRepo = orderRepo;
		this.props = props;
	}
	
	@GetMapping
	public String ordersForUser(@AuthenticationPrincipal User user, Model model) {
		
		//Pageable pageable = PageRequest.of(0,  pageSize);
		Pageable pageable = PageRequest.of(0,  props.getPageSize());
		model.addAttribute("orders", orderRepo.findByUserOrderByPlacedAtDesc(user, pageable));
		
		return "orderList";
	}
	
	@GetMapping("/current")
	public String orderForm(@AuthenticationPrincipal User user,
							@ModelAttribute Order order) {
		if (order.getDeliveryName() == null) {
			order.setDeliveryName(user.getFullname());
		}
		if (order.getDeliveryStreet() == null) {
			order.setDeliveryStreet(user.getStreet());
		}
		if (order.getDeliveryCity() == null) {
			order.setDeliveryCity(user.getCity());
		}
		if (order.getDeliveryState() == null) {
			order.setDeliveryState(user.getState());
		}
		if (order.getDeliveryZip() == null) {
			order.setDeliveryZip(user.getZip());
		}
		return "orderForm";
	}
	
	@PostMapping
	public String processOrder(@Valid Order order, Errors errors
			                 , SessionStatus sessionStatus
			                 , @AuthenticationPrincipal User user) {
		
		
		/* Security Context로 부터 Authentication객체를 얻은 후 
		   Principal 객체(인증된 사용자를 나타냄)를 요청하는 방법  */ 
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		User user2 = (User) authentication.getPrincipal();
		
		/**
		 * 사용자를 판단하는 방법
		 * 1. Principal 객체를 컨트롤러 메서드에 주입한다.
		 * 2. Authentication 객체를 컨트롤러 메서드에 주입한다.
		 * 3. SecurityContextHolder를 사용해서 보안 컨텍스트를 얻는다. 
		 * 4. @AuthenticationPrincipal 애노테이션을 메서드에 지정한다.
		 * */
		
		if (errors.hasErrors()) {
			return "orderForm";
		}
		order.setUser(user);

		orderRepo.save(order);
		sessionStatus.setComplete(); //세션 초기화  
		
		return "redirect:/";
	}
}