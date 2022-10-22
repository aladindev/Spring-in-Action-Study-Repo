package spring.integration.intergrationflow;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;

import spring.integration.filter.EvenNumberFilter;

@Configuration
public class FileWriterIntegrationConfigByDSL {
	
	@Autowired
	EvenNumberFilter evenNumberFilter;
	
	/*
	 * 자바 DSL : Domain Specific Language 구성 방법
	 * 통합 플로우의 각 컴포넌트를 별도의 빈으로 선언하지 않고 
	 * 전체 플로우를 하나의 빈으로 선언하는 방식.
	 * */
	@Bean
	public IntegrationFlow filewWriterFlow() {
		
		return IntegrationFlows
				.from(MessageChannels.direct("textInChannel")) //인바운드 채널
				.<String, String>transform(t -> t.toUpperCase()) //변환기를 선언한다.
				//변환기를 아웃바운드 채널 어댑터와 연결하는 채널을 명시한다.
				.channel(MessageChannels.direct("fileWriterChannel")) 
				.<Integer>filter((p) -> p % 2 == 0) // 메세지 전달 조건(필터)
				//route 지정(생략)
				.handle(Files //파일에 쓰는 것을 처리한다.
						.outboundAdapter(new File("/tmp/sia5/files"))
						.fileExistsMode(FileExistsMode.APPEND)
						.appendNewLine(true))
				.get();
	}

}
