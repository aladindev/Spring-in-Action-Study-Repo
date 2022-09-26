package tacos.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;


/**
 *  @Controller : 뷰로 보여줄 값을 Model객체에 담아 객체를 반환하는 컨트롤러 
 *  @RestController : 컨트롤러의 모든 HTTP요청 처리 메서드에서 HTTP 응답 몸체에
 *                    직접 쓰는 값을 반환한다.
 * */

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order") //세션에 order 객체를 담는다.
public class DesignTacoController {
	
	private final IngredientRepository ingredientRepo;
	private TacoRepository tacoRepo;
	private UserRepository userRepo;
	
	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo
							  , TacoRepository tacoRepo
							  , UserRepository userRepo) {
		this.ingredientRepo = ingredientRepo;
		this.tacoRepo = tacoRepo;
		this.userRepo = userRepo;
	}
	
	@GetMapping
	public String showDesignForm(Model model, Principal principal) {

		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));
		
		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(),
					filterByType(ingredients, type));
		}
		
		//model.addAttribute("taco", new Taco());
		String username = principal.getName();
		User user = userRepo.findByUsername(username);
		model.addAttribute("user", user);
		
		return "design";
	}
	
	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients
				.stream()
				.filter(x -> x.getType().equals(type))
				.collect(Collectors.toList());
	}
	
	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}
	
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}
	
	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors
			, @ModelAttribute Order order) { // Model에 세션객체에 담긴 Order객체를 담는다.
		if(errors.hasErrors()) {
			return "design";
		}
		
		Taco saved = tacoRepo.save(design);
		order.addDesign(saved);
		
		return "redirect:/orders/current";
	}
	
}