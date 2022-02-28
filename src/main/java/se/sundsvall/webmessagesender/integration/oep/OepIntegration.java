package se.sundsvall.webmessagesender.integration.oep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import se.sundsvall.webmessagesender.generatedsources.oep.AddMessage;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessageResponse;

@FeignClient(
		name = "oep.integration",
		url = "${integration.oep.url}",
		configuration = OepIntegrationConfiguration.class)

public interface OepIntegration {
	static final String TEXT_XML_ISO_8859_1 = "text/xml; charset=ISO-8859-1";

	@PostMapping(consumes = TEXT_XML_ISO_8859_1, produces = TEXT_XML_ISO_8859_1)
	AddMessageResponse addMessage(@RequestBody AddMessage addMessage);
}
