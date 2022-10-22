package spring.integration.intergrationflow;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import spring.integration.filter.EvenNumberFilter;

@Configuration
public class FileWriterIntegrationConfig {
	
	@Autowired
	EvenNumberFilter evenNumberFilter;
	
	
	//Router : 전달 조건을 기반으로 통합 플로우 내부를 분기(서로 다른 채널로 메세지를 전달)한다.
	@Bean
	@Router(inputChannel="numberChannel") // 정수값을 전달하는 채널
	public AbstractMessageRouter evenOddRouter() {
		return new AbstractMessageRouter() {
			@Override
			protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
				Integer number = (Integer)message.getPayload();
				
				if(number % 2 == 0) {
					return Collections.singleton(evenChannel());
				}
				return Collections.singleton(oddChannel());
			}
		};
	}
	
	@Bean
	public MessageChannel evenChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel oddChannel() {
		return new DirectChannel();
	}
	
	//Router : 전달 조건을 기반으로 통합 플로우 내부를 분기(서로 다른 채널로 메세지를 전달)한다.
	
	
	// 별도의 채널 지정
	@Bean
	public MessageChannel orderChannel() {
		return new QueueChannel();
	}
	
	@Bean
	@Transformer(inputChannel="textInChannel"  //변환기 빈을 선언한다.
				,outputChannel="fileWriterChannel")	
	public GenericTransformer<String, String> upperCaseTransformer() {
		return text -> text.toUpperCase();
	}
	
	@Bean
	/*
	 * @ServiceActivator : fileWriterChannel로부터 메세지를 받아서 
	 * FileWritingMessageHandler의 인스턴스로 정의된 서비스에 넘겨줌을 나타낸다.
	 * */
	
	//QueueChannel을 사용할 시에는 폴링 구성(도착한 메세지가 있는지 지속적으로 확인함)이 필요하다. ms
	@ServiceActivator(inputChannel="orderChannel", poller=@Poller(fixedRate="1000")) 
	public FileWritingMessageHandler fileWriter() { // 파일-쓰기 빈을 선언한다.
		FileWritingMessageHandler handler =
					new FileWritingMessageHandler(new File("/tmp/sia5/files"));
		
		handler.setExpectReply(false);//서비스에서 응답 채널을 사용하지 않는다.
		handler.setFileExistsMode(FileExistsMode.APPEND);
		handler.setAppendNewLine(true);
		
		return handler;
	}

}
