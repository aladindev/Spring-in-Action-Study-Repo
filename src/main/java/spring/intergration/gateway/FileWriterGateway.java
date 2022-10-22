package spring.intergration.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * 메세지 게이트를 선언하는 어노테이션 
 * FileWriterGateway 인터페이스의 구현체(클래스)를 런타임 시에 생성하라고 
 * 스프링 통합에 알려준다.
 * defaultRequestChannel 속성 : 해당 인터페이스의 메서드 호출로 생성된 메세지가 
 * 							   이 속성에 지정된 메세지 채널로 전송된다는 것을 나타낸다.
 * 							   여기서는 writeToFile()의 호출로 생긴 메세지가 
 * 							   'textInChannel'이라는 이름의 채널로 전송된다.
 */
@MessagingGateway(defaultRequestChannel="textInChannel")
public interface FileWriterGateway {
	
	/*
	 *  @Param : filename
	 *  @Param : write data
	 *  @Header : filename에 전달되는 값이 메세지 페이로드가 아닌 메세지 헤더에 있다는 것을 
	 *  		 나타낸다.(FileHeaders.FILENAME 상수의 실제 값은 file_name이다)
	 * */
	void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}
