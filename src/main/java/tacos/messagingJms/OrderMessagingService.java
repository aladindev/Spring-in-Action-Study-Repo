package tacos.messagingJms;

import tacos.Order;

public interface OrderMessagingService {
	
	void sendOrder(Order order);

}
