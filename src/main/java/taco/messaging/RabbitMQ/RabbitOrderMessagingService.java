package taco.messaging.RabbitMQ;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tacos.Order;
import tacos.messagingJms.OrderMessagingService;

/* RabbitMQ가 제공하는 메소드 
 * 추가적인 파라미터가 존재하는 오버로딩 메소드 여러 존재
 * */

// destination 객체를 전달받는 JmsTemplate과 달리 거래소와 라우팅 키 설정이 필요하다.

// 원시 메시지 전송
//void send(Message message) 

// 객체로부터 변환된 메세지를 전송
//void convertAndSend(Object message)

// 객체로부터 변환되고 후처리(post-processing)되는 메세지를 전송한다.
//void convertAndSend(Object message, MessagePostProcessor mPP) throw AmqpException;


@Service
public class RabbitOrderMessagingService implements OrderMessagingService {
	private RabbitTemplate rabbit;
	
	@Autowired
	public RabbitOrderMessagingService(RabbitTemplate rabbit) {
		this.rabbit = rabbit;
	}
	
	public void sendOrder(Order order) {
		// 객체를 Message객체로 변환해주는 RabbitMQ 내장 메소드 
		MessageConverter converter = rabbit.getMessageConverter();
		MessageProperties props = new MessageProperties();
		Message message = converter.toMessage(order, props);
		rabbit.send("tacocloud.order", message);
	}

}
