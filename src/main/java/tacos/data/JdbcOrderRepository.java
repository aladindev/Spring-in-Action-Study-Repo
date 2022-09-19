//package tacos.data;
//
//import java.util.Date;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.stereotype.Repository;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.*;
//import tacos.Order;
//import tacos.Taco;
//
//@Repository
//public class JdbcOrderRepository implements OrderRepository {
//	
//	/**
//	 *  SimpleJdbcInsert는 데이터를 추가하는 두 개의 메소드 execute(), executeAndReturnKey()
//	 *  를 제공한다.
//	 *  두 메서드는 모두 Map<String, Object>를 인자로 전달 받는다.
//	 *  이 Map의 key는 데이터가 추가되는 테이블의 컬럼명과 대응되며 Map의 값은 해당 열에
//	 *  추가되는 값이다.
//	 * */
//	
//	private SimpleJdbcInsert orderInserter;
//	private SimpleJdbcInsert orderTacoInserter;
//	private ObjectMapper objectMapper; //Jackson library 
//	
//	@Autowired
//	public JdbcOrderRepository(JdbcTemplate jdbc) {
//		this.orderInserter = new SimpleJdbcInsert(jdbc)
//				.withTableName("Taco_Order")
//				.usingGeneratedKeyColumns("id");
//		
//		this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
//				.withTableName("Taco_Order_Tacos");
//		
//		this.objectMapper = new ObjectMapper();
//	}
//
//	@Override
//	public Order save(Order order) {
//		
//		order.setPlacedAt(new Date());
//		long orderId = saveOrderDetails(order);
//		order.setId(orderId);
//		List<Taco> tacos = order.getTacos();
//		
//		for(Taco taco : tacos) {
//			saveTacoToOrder(taco, orderId);
//		}
//		
//		return order;
//	}
//	
//	private long saveOrderDetails(Order order) {
//		@SuppressWarnings("unchecked")
//		Map<String, Object> values = objectMapper.convertValue(order, Map.class);
//		values.put("placeAt", order.getPlacedAt());
//		 
//		long orderId = orderInserter.executeAndReturnKey(values).longValue();
//		
//		return orderId;
//	}
//	
//	private void saveTacoToOrder(Taco taco, long orderId) {
//		Map<String, Object> values = new HashMap<>();
//		
//		values.put("tacoOrder", orderId);
//		values.put("taco", taco.getId());
//		orderTacoInserter.execute(values);
//	}
//
//}
