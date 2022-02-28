package se.sundsvall.webmessagesender.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static se.sundsvall.webmessagesender.service.mapper.OepMapper.toAddMessage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class OepmapperTest {

	@Test
	void testToAddMessage() throws Exception {
		final var message = "A message";
		final var flowInstanceId = 1337;

		final var addMessage = toAddMessage(message, flowInstanceId);

		assertThat(addMessage.getExternalID()).isNull();
		assertThat(addMessage.getPrincipal()).isNull();
		assertThat(addMessage.getFlowInstanceID()).isEqualTo(flowInstanceId);
		assertThat(addMessage.getMessage()).isNotNull();
		assertThat(addMessage.getMessage().getAdded()).isNotNull();
		assertThat(addMessage.getMessage().getAdded().toGregorianCalendar().toZonedDateTime().toLocalDateTime()).isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS));
		assertThat(addMessage.getMessage().getAttachments()).isNullOrEmpty();
		assertThat(addMessage.getMessage().getMessage()).isEqualTo(message);
		assertThat(addMessage.getMessage().getUserID()).isNull();
	}
}
