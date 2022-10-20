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
	
	/*
	 * 메세지가 큐에 도착할 때 메서드가 자동 호출되도록 지정하는 어노테이
	 * */
	@RabbitListener(queues = "tacocloud.order.queue")
	public void receiveOrder(Order order) {
		ui.displayOrder(order);
	}
}
