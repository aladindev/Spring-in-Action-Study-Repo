package spring.intergration.intergrationflow;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
public class FileWriterIntegrationConfig {
	
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
