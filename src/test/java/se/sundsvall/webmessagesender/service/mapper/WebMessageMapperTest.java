package se.sundsvall.webmessagesender.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.integration.db.model.ExternalReferenceEntity;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

class WebMessageMapperTest {

	@Test
	void toWebMessageEntityFromCreateWebMessageRequestWithoutOepMessageId() {

		// Setup
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(
				ExternalReference.create().withKey("key-1").withValue("value-1"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-3").withValue("value-3")))
			.withMessage("A message")
			.withPartyId(UUID.randomUUID().toString());

		// Call
		final var webMessageEntity = WebMessageMapper.toWebMessageEntity(createWebMessageRequest, null);

		// Verification
		assertThat(webMessageEntity.getExternalReferences()).hasSize(3); // Duplicates removed.
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getKey).containsExactly("key-1", "key-2", "key-3");
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getValue).containsExactly("value-1", "value-2", "value-3");
		assertThat(webMessageEntity.getMessage()).isEqualTo(createWebMessageRequest.getMessage());
		assertThat(webMessageEntity.getPartyId()).isEqualTo(createWebMessageRequest.getPartyId());
		assertThat(webMessageEntity.getOepMessageId()).isNull();
	}

	@Test
	void toWebMessageEntityFromCreateWebMessageRequestWithOepMessageId() {

		// Setup
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(
				ExternalReference.create().withKey("key-1").withValue("value-1"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-3").withValue("value-3")))
			.withMessage("A message")
			.withPartyId(UUID.randomUUID().toString());

		// Call
		final var webMessageEntity = WebMessageMapper.toWebMessageEntity(createWebMessageRequest, Integer.MAX_VALUE);

		// Verification
		assertThat(webMessageEntity.getExternalReferences()).hasSize(3); // Duplicates removed.
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getKey).containsExactly("key-1", "key-2", "key-3");
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getValue).containsExactly("value-1", "value-2", "value-3");
		assertThat(webMessageEntity.getMessage()).isEqualTo(createWebMessageRequest.getMessage());
		assertThat(webMessageEntity.getPartyId()).isEqualTo(createWebMessageRequest.getPartyId());
		assertThat(webMessageEntity.getOepMessageId()).isEqualTo(Integer.MAX_VALUE);
	}
	
	@Test
	void toWebMessageEntityFromNull() {

		// Call
		final var webMessageEntity = WebMessageMapper.toWebMessageEntity(null, null);

		// Verification
		assertThat(webMessageEntity).isNull();
	}

	@Test
	void toWebMessageFromWebMessageEntity() {

		// Setup
		final var webMessageEntity = WebMessageEntity.create()
			.withCreated(OffsetDateTime.now())
			.withExternalReferences(List.of(
				ExternalReferenceEntity.create().withKey("key-1").withValue("value-1"),
				ExternalReferenceEntity.create().withKey("key-2").withValue("value-2"),
				ExternalReferenceEntity.create().withKey("key-2").withValue("value-2"),
				ExternalReferenceEntity.create().withKey("key-3").withValue("value-3")))
			.withId(UUID.randomUUID().toString())
			.withMessage("A message")
			.withPartyId(UUID.randomUUID().toString());

		// Call
		final var webMessage = WebMessageMapper.toWebMessage(webMessageEntity);

		// Verification
		assertThat(webMessage.getCreated()).isEqualTo(webMessageEntity.getCreated());
		assertThat(webMessage.getExternalReferences()).hasSize(4);
		assertThat(webMessage.getExternalReferences()).extracting(ExternalReference::getKey).containsExactly("key-1", "key-2", "key-2", "key-3");
		assertThat(webMessage.getExternalReferences()).extracting(ExternalReference::getValue).containsExactly("value-1", "value-2", "value-2", "value-3");
		assertThat(webMessage.getId()).isEqualTo(webMessageEntity.getId());
		assertThat(webMessage.getMessage()).isEqualTo(webMessageEntity.getMessage());
		assertThat(webMessage.getPartyId()).isEqualTo(webMessageEntity.getPartyId());
	}

	@Test
	void toWebMessageFromNull() {

		// Call
		final var webMessageEntity = WebMessageMapper.toWebMessage(null);

		// Verification
		assertThat(webMessageEntity).isNull();
	}
}
