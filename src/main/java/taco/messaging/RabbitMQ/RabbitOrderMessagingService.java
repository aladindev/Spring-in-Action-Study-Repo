package taco.messaging.RabbitMQ;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tacos.Order;
import tacos.messagingJms.OrderMessagingService;

/* RabbitMQ가 제공하는 메소드(전송)
 * 추가적인 파라미터가 존재하는 오버로딩 메소드 여러 존재
 * */

// destination 객체를 전달받는 JmsTemplate과 달리 거래소와 라우팅 키 설정이 필요하다.

// 원시 메시지 전송
//void send(Message message) 

// 객체로부터 변환된 메세지를 전송
//void convertAndSend(Object message)

// 객체로부터 변환되고 후처리(post-processing)되는 메세지를 전송한다.
//void convertAndSend(Object message, MessagePostProcessor mPP) throw AmqpException;

/*
 *  RabbitMQ가 제공하는 메소드(메세지 수신)
 *  (오버로딩 된 메소드 여러 존재)
 *  Message receive() throws AmqpException;
 *  
 *  메세지로부터 변환된 객체를 수신한다.
 *  Object receiveAndConvert() throws AmqpException
 *  
 *  메세지로부터 변환된 타입-안전(type-safe) 객체를 수신한다.
 *  <T> T receiveAndConvert(ParameterizedTypeReference<T> type) throw AmqpException
 * */



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
		
		/* 프로퍼티 헤더 설정 */
		props.setHeader("X_ORDER_SOURCE", "WEB");
		Message message = converter.toMessage(order, props);
		
		/*
		 * 프로퍼티에 기본 거래소를 지정할 수 있다.
		 * "" 빈 문자열 전송 시, 해당 속성의 기본 거래소로 찾아가게 된다
		 * (라우팅 키도 속성값으로 지정가능)
		 * */
		rabbit.send("tacocloud.order", message);
		
		/* convertAndSend()를 사용할 때는 MessageProperties 객체를 직접 
		* 사용할 수 없다. 다음과 같이 MessagePostProcessor에서 해야 한다.
		*/
		rabbit.convertAndSend("tacocloud.order.queue", order
				, new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				MessageProperties props = message.getMessageProperties();
				props.setHeader("X_ORDER_SOURCE", "WEB");
				
				return message;
			}
		});
		
	}

}
