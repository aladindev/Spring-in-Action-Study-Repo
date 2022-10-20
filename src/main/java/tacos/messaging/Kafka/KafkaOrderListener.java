package tacos.messaging.Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import tacos.Order;
import tacos.messagingJms.KitchenUI;

@Component
public class KafkaOrderListener {
	
	private KitchenUI ui;
	
	@Autowired
	public KafkaOrderListener(KitchenUI ui) {
		this.ui = ui;
	}
	
	/*
	 *  tacocloud.orders.topic 라는 이름의 토픽에 메시지가 도착할 때 
	 *  자동 호출되어야 한다는 것을 알리는 애노테이션 지정.
	 *  스프링 컨택스트에 등록되어 요청이 올 시에 동작한다..
	 *  
	 *  페이로드인 Order(헤더, 속성 등을 제외한 순수한 데이터 객체만) 객체만 
	 *  Handle()의 인자로 받는다.(추가적으로 파라미터 전달을 할 수 있음)
	 * */
	@KafkaListener(topics="tacocloud.orders.topic")
	public void handle(Order order, ConsumerRecord<Object, Order> record) {
		ui.displayOrder(order);
	}

}
