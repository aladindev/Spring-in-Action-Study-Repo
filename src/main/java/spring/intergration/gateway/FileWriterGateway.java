package spring.intergration.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * 메세지 게이트를 선언하는 어노테이션 
 * FileWriterGateway 인터페이스의 구현체(클래스)를 런타임 시에 생성하라고 
 * 스프링 통합에 알려준다.
 */
@MessagingGateway(defaultRequestChannel="textInChannel")
public interface FileWriterGateway {
	
	// 파라미터로 전달되는 파일에 쓴다.
	void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}
