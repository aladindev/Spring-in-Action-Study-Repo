package tacos.messagingJms;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import tacos.Order;

public class JmsOrderReceiver implements OrderReceiver {

	private JmsTemplate jms;
	private MessageConverter converter;
	
	@Autowired
	public JmsOrderReceiver(JmsTemplate jms, MessageConverter converter) {
		this.jms = jms;
		this.converter = converter;
	}
	
	public Order recevierOrder() throws MessageConversionException, JMSException {
		Message message = jms.receive("tacocloud.order.queue");
		
		return (Order)converter.fromMessage(message);
	}

}
