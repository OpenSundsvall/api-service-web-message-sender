package se.sundsvall.webmessagesender.integration.oep;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
import se.sundsvall.webmessagesender.Application;

@SpringBootTest(classes = { Application.class, OepIntegrationConfiguration.class })
@ActiveProfiles("junit")
class OepIntegrationConfigurationTest {

	@Autowired
	private OepIntegrationConfiguration config;
	
	@Autowired
	private OepIntegrationProperties properties;
	
	@Test
	void testSettings() {
		assertThat(properties.getConnectTimeout()).isEqualTo(10);
		assertThat(properties.getPassword()).isEqualTo("oep.client.password");
		assertThat(properties.getReadTimeout()).isEqualTo(20);
		assertThat(properties.getUsername()).isEqualTo("oep.client.username");
		
		assertThat(config.basicAuthRequestInterceptor(properties)).isNotNull();
		assertThat(config.feignBuilderCustomizer(properties)).isNotNull();
		assertThat(config.feignSOAPDecoder()).isNotNull().isExactlyInstanceOf(SOAPDecoder.class);
		assertThat(config.feignSOAPEncoder()).isNotNull().isExactlyInstanceOf(SOAPEncoder.class);
	}
}
