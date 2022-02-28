package se.sundsvall.webmessagesender.integration.oep;

import static java.time.Duration.ofSeconds;

import java.nio.charset.StandardCharsets;

import javax.xml.soap.SOAPConstants;

import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder.Builder;
import feign.soap.SOAPErrorDecoder;
import se.sundsvall.dept44.configuration.feign.FeignHelper;

@Configuration
@Import(FeignClientsConfiguration.class)
public class OepIntegrationConfiguration {
	private static final JAXBContextFactory JAXB_FACTORY = new JAXBContextFactory.Builder()
			.withMarshallerJAXBEncoding(StandardCharsets.ISO_8859_1.toString())
			.build();

	private static final Builder ENCODER_BUILDER = new Builder()
			.withCharsetEncoding(StandardCharsets.ISO_8859_1)
			.withFormattedOutput(false)
			.withJAXBContextFactory(JAXB_FACTORY)
			.withSOAPProtocol(SOAPConstants.SOAP_1_1_PROTOCOL)
			.withWriteXmlDeclaration(true);
	
	@Bean
	Encoder feignSOAPEncoder() {
		return ENCODER_BUILDER.build();
	}

	@Bean
	Decoder feignSOAPDecoder() {
		return new SOAPDecoder(JAXB_FACTORY);
	}

	@Bean
	BasicAuthRequestInterceptor basicAuthRequestInterceptor(OepIntegrationProperties properties) {
		return new BasicAuthRequestInterceptor(properties.getUsername(), properties.getPassword());
	}
    
	@Bean
	ErrorDecoder errorDecoder() {
		return new SOAPErrorDecoder();
	}
	
	@Bean
	FeignBuilderCustomizer feignBuilderCustomizer(OepIntegrationProperties properties) {                           
		return FeignHelper.customizeRequestOptions()
				.withConnectTimeout(ofSeconds(properties.getConnectTimeout()))
				.withReadTimeout(ofSeconds(properties.getReadTimeout()))
				.build();
	}
}
