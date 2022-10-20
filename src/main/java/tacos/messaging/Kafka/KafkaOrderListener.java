package tacos.messaging.Kafka;

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
	
	@KafkaListener(topics="tacocloud.orders.topic")
	public void handle(Order order) {
		ui.displayOrder(order);
	}

}
