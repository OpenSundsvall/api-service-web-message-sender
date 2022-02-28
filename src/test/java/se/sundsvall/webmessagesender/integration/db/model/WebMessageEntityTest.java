package se.sundsvall.webmessagesender.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WebMessageEntityTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		assertThat(WebMessageEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSettersExcluding("externalReferences"),
			hasValidBeanHashCodeExcluding("externalReferences"),
			hasValidBeanEqualsExcluding("externalReferences"),
			hasValidBeanToStringExcluding("externalReferences")));
	}

	@Test
	void testBuilderMethods() {

		final var created = OffsetDateTime.now();
		final var id = UUID.randomUUID().toString();
		final var externalReferences = List.of(ExternalReferenceEntity.create());
		final var message = "message";
		final var partyId = UUID.randomUUID().toString();
		final var oepMessageId = Integer.MAX_VALUE;
		
		final var webMessage = WebMessageEntity.create()
			.withCreated(created)
			.withId(id)
			.withExternalReferences(externalReferences)
			.withMessage(message)
			.withPartyId(partyId)
			.withOepMessageId(oepMessageId);

		assertThat(webMessage).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(webMessage.getCreated()).isEqualTo(created);
		assertThat(webMessage.getId()).isEqualTo(id);
		assertThat(webMessage.getExternalReferences()).isEqualTo(externalReferences);
		assertThat(webMessage.getMessage()).isEqualTo(message);
		assertThat(webMessage.getPartyId()).isEqualTo(partyId);
		assertThat(webMessage.getOepMessageId()).isEqualTo(oepMessageId);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(WebMessageEntity.create()).hasAllNullFieldsOrProperties();
	}
}
