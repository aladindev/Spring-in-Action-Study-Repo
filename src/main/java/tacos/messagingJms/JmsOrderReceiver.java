package tacos.messagingJms;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import tacos.Order;


/*
 * 메시지의 원시 데이터 전부를 얻을 때는 receive()
 * 헤더, 속성 등의 정보도 담고 있다.
 * */
//public class JmsOrderReceiver implements OrderReceiver {
//
//	private JmsTemplate jms;
//	private MessageConverter converter;
//	
//	@Autowired
//	public JmsOrderReceiver(JmsTemplate jms, MessageConverter converter) {
//		this.jms = jms;
//		this.converter = converter;
//	}
//
//	public Order recevierOrder() throws MessageConversionException, JMSException {
//		Message message = jms.receive("tacocloud.order.queue");
//		
//		return (Order)converter.fromMessage(message);
//	}
//}


/*
 * 메세지의 페이로드(데이터 내에 순수한 데이터 객체, 여기서는 order)만 
 * 얻어내는 메소드 receiveAndConvert()
 * */
public class JmsOrderReceiver implements OrderReceiver {

	private JmsTemplate jms;
	
	@Autowired
	public JmsOrderReceiver(JmsTemplate jms, MessageConverter converter) {
		this.jms = jms;
	}

	public Order recevierOrder() throws MessageConversionException, JMSException {
		Message message = jms.receive("tacocloud.order.queue");
		
		return (Order)jms.receiveAndConvert("tacocloude.order.queue");
	}
}
