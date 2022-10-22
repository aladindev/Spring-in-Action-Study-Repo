package spring.intergration.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

//메세지 게이트를 선언하는 어노테이션 
@MessagingGateway(defaultRequestChannel="textInChannel")
public interface FileWriterGateway {
	
	// 파라미터로 전달되는 파일에 쓴다.
	void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}
