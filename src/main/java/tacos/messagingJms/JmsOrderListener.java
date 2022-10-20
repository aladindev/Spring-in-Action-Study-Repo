package tacos.messagingJms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import tacos.Order;


/**
 * receive() / receiveAndConvert()를 호출해야 하는 풀 모델과 달리,
 * 메시지 리스너는 메세지가 도착할 때까지 대기하는 수동적 컴포넌트다.
 * */
@Component
public class JmsOrderListener {
	
	private KitchenUI ui;
	
	@Autowired
	public JmsOrderListener(KitchenUI ui) {
		this.ui = ui;
	}

	/*
	 * 수동적 리스닝을 하는 리스너 
	 * 도착지의 메세지를 '리스닝'하며, JmsTemplate를 사용하지 않는다.
	 * 어플리케이션 코드에서도 호출되지 않으며 
	 * 스프링 프레임워크에 bean으로 등록되어 지정된 주소지에 메세지가 도착하면 동작을 수행한다.
	 * */
	@JmsListener(destination = "tacocloud.order.queue") 
	public void receiveOrder(Order order) {
		ui.displayOrder(order);
	}

}
