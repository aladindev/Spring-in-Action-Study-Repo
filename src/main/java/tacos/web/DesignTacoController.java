package tacos.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.TacoRepositoryByWebflux;
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
	private TacoRepositoryByWebflux tacoRepoWebflux;
	
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
	
//	@GetMapping("/{id}") //메서드의 경로에 플레이스홀더 변수 지정하여 해당 변수르 통해 ID를 인자로 받는다.
//	public ResponseEntity<Taco> tacoById(@PathVariable("id")Long id) {
//		Optional<Taco> optTaco = tacoRepo.findById(id);
//		
//		if(optTaco.isPresent()) {
//			return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
//		}
//		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//	}
	
	public Iterable<Taco> recentTacos() {
		PageRequest page = PageRequest.of(0,  12, Sort.by("createdAt").descending());
		
		return tacoRepo.findAll(page).getContent();
		
	}
	
	
	/* spring webflux 비동기적 이벤트 중심 처리 리액티브 방식 */
	@GetMapping("/recent")
	public Flux<Taco> recentTacosByReactive() {
		/* 이상적인 리액티브 컨트롤러에서는 리액티브 end-to-end 스택의 제일 끝에 위치하며 
		 * 이 스택에는 controller, repository, db, service가 포함된다. 
		 * 리액티브 웹 프레임워크의 장점을 극대화하려면 완전한 end-to-end 리액티브 스택의 일부가 되어야한다.*/
		return tacoRepoWebflux.findAll().take(12);
	}
	
	
	@GetMapping("/{id}")
	public Mono<Taco> tacoById(@PathVariable("id") Long id) {
		return tacoRepoWebflux.findById(id);
	}
	
	/* RxJava 타입 사용하기 
	 * Observable<Taco> / Single<Taco> 타입을 반환할 수도 있다. 
	 * */
	
	/*  
	 * 기존에는 postTaco() 호출 시 1번 블로킹되고 
	 * save() 메서드의 블로킹되는 호출이 끝나고 복귀할 수 있었지만
	 * 리액티브 코드를 적용하여 완전하게 블로킹되지 않는 요청 처리 메서드를 만들 수 있다. 
	 * */
	@PostMapping(consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Taco> postTaco(@RequestBody Mono<Taco> tacoMono) {
		/* 
		 * saveAll()의 반환형은 Flux<?>를 반환한다.(여기서는 Flux<Taco>)
		 * next()를 호출하여 Mono<Taco>(하나의 Taco 객체만을 포함하는)로 받을 수 있다.
		 * Flux : 0, 1, 또는 다수의(무한일 수 있는) 데이터를 갖는 파이프라인을 나타낸다.
		 * */
		return tacoRepoWebflux.saveAll(tacoMono).next(); 
	}
	
}