package tacos.messagingJms;

import javax.jms.JMSException;

import org.springframework.jms.support.converter.MessageConversionException;

import tacos.Order;

public interface OrderReceiver {
	Order recevierOrder() throws MessageConversionException, JMSException;
}
