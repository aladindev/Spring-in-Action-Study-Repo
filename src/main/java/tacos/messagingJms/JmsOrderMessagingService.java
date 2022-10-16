package tacos.messagingJms;

import java.io.Serializable;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import tacos.Order;

@Service
public class JmsOrderMessagingService implements OrderMessagingService {
	private JmsTemplate jms;
	private Destination orderQueue;
	
	@Autowired
	public JmsOrderMessagingService(JmsTemplate jms) {
		this.jms = jms;
	}
	@Autowired
	public JmsOrderMessagingService(JmsTemplate jms, Destination orderQueue) {
		this.jms = jms;
		this.orderQueue = orderQueue;
	}
	
	@Override
	public void sendOrder(Order order) {
//		jms.send(new MessageCreator() {
//			@Override
//			public Message createMessage(Session session) throws JMSException {
//				return session.createObjectMessage(order);
//			}
//		});
		jms.send(orderQueue
					, session -> session.createObjectMessage((Serializable) order));
	}
}
