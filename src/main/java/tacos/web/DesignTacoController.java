package tacos.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
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
@RestController
@RequestMapping(path="/design", produces="application/json")
@CrossOrigin(origins="*") // 서로 다른 도메인 간의 요청을 허용한다.
//@SessionAttributes("order") //세션에 order 객체를 담는다.
public class DesignTacoController {
	
	private final IngredientRepository ingredientRepo;
	private TacoRepository tacoRepo;
	private UserRepository userRepo;
	
	@Autowired
	EntityLinks eneityLinks;
	
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
	
	@GetMapping("/{id}") //메서드의 경로에 플레이스홀더 변수 지정하여 해당 변수르 통해 ID를 인자로 받는다.
	public ResponseEntity<Taco> tacoById(@PathVariable("id")Long id) {
		Optional<Taco> optTaco = tacoRepo.findById(id);
		
		if(optTaco.isPresent()) {
			return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	/* spring webflux 비동기적 이벤트 중심 처리 리액티브 방식 */
	@GetMapping("/recent")
	public Flux<Taco> recentTacos() {
		/* 기존의 iterable 객체를 Flux로 반환 */ 
		return Flux.fromIterable(tacoRepo.findAll()).take(12); // Flux의 페이징 메소드 take();
	}
//	public Iterable<Taco> recentTacos() {
//		PageRequest page = PageRequest.of(0,  12, Sort.by("createdAt").descending());
//		
//		return tacoRepo.findAll(page).getContent();
//		
//	}
}