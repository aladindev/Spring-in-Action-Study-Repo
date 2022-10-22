package spring.integration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="tacocloud.email")
@Component
public class EmailProperties {
	
	private String username;
	private String password;
	private String host;
	private String mailbox;
	private long pollRate = 30000; // 폴링 30000ms(30초 마다 메세지 수신 여부 확인)
	
	public String getImapUrl() {
		return String.format("imaps://%s:%s@%s/%s"
				, this.username, this.password, this.host, this.mailbox);
	}

}
