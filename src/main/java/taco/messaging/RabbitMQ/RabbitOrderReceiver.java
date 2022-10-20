package taco.messaging.RabbitMQ;

import java.util.Collection;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import tacos.Order;

@Component
public class RabbitOrderReceiver {
	private RabbitTemplate rabbit;
	private MessageConverter converter;
	
	@Autowired
	public RabbitOrderReceiver(RabbitTemplate rabbit) {
		this.rabbit = rabbit;
		this.converter = rabbit.getMessageConverter();
	}
	
//	public Order receiveOrder() {
//		// 메세지 객체에 타임아웃 시간을 인자로 전달할 수 있다. 프로퍼티에서 지정 가능.
//		Message message = rabbit.receive("tacocloud.orders");
//		
//		return message != null 
//					? (Order)converter.fromMessage(message)
//					: null;
//	}
	public Order receiveOrder() {
		//return (Order)rabbit.receiveAndConvert("tacocloud.order.queue");
		
		return rabbit.receiveAndConvert("tacocloud.order.queue"
				, new ParameterizedTypeReference<Order>() {});
	}

}
