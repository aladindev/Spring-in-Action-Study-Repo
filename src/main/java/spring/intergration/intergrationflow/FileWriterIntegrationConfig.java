package spring.intergration.intergrationflow;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.GenericTransformer;

@Configuration
public class FileWriterIntegrationConfig {
	
	@Bean
	@Transformer(inputChannel="textInChannel"  //변환기 빈을 선언한다.
				,outputChannel="fileWriterChannel")	
	public GenericTransformer<String, String> upperCaseTransformer() {
		return text -> text.toUpperCase();
	}
	
	@Bean
	@ServiceActivator(inputChannel="fileWriterChannel")
	public FileWritingMessageHandler fileWriter() { // 파일-쓰기 빈을 선언한다.
		FileWritingMessageHandler handler =
					new FileWritingMessageHandler(new File("/tmp/sia5/files"));
		
		handler.setExpectReply(false);
		handler.setFileExistsMode(FileExistsMode.APPEND);
		handler.setAppendNewLine(true);
		
		return handler;
	}

}
