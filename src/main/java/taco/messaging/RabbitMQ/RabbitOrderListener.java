package taco.messaging.RabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tacos.Order;
import tacos.messagingJms.KitchenUI;

@Component
public class RabbitOrderListener {
	private KitchenUI ui;
	
	@Autowired
	public RabbitOrderListener(KitchenUI ui) {
		this.ui = ui;
	}
	
	@RabbitListener(queues = "tacocloud.order.queue")
	public void receiveOrder(Order order) {
		ui.displayOrder(order);
	}
}
