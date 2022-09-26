package tacos.web;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
public class OrderController {
	
	private OrderRepository orderRepo;
	
	public OrderController(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
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