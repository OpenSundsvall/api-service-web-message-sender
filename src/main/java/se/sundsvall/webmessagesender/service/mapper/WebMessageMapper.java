package se.sundsvall.webmessagesender.service.mapper;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

import java.util.List;
import java.util.Optional;

import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.api.model.WebMessage;
import se.sundsvall.webmessagesender.integration.db.model.ExternalReferenceEntity;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

public class WebMessageMapper {

	private WebMessageMapper() {}

	public static WebMessageEntity toWebMessageEntity(final CreateWebMessageRequest createWebMessageRequest, final Integer oepMessageId) {
		if (isNull(createWebMessageRequest)) {
			return null;
		}
		return WebMessageEntity.create()
			.withExternalReferences(toExternalReferenceEntities(createWebMessageRequest.getExternalReferences()))
			.withMessage(createWebMessageRequest.getMessage())
			.withPartyId(createWebMessageRequest.getPartyId())
			.withOepMessageId(oepMessageId);
	}

	public static WebMessage toWebMessage(final WebMessageEntity webMessageEntity) {
		if (isNull(webMessageEntity)) {
			return null;
		}
		return WebMessage.create()
			.withCreated(webMessageEntity.getCreated())
			.withExternalReferences(toExternalReferences(webMessageEntity.getExternalReferences()))
			.withId(webMessageEntity.getId())
			.withMessage(webMessageEntity.getMessage())
			.withPartyId(webMessageEntity.getPartyId());
	}

	public static List<WebMessage> toWebMessages(final List<WebMessageEntity> webMessageEntities) {
		return Optional.ofNullable(webMessageEntities).orElse(emptyList()).stream()
			.map(WebMessageMapper::toWebMessage)
			.toList();
	}

	private static List<ExternalReferenceEntity> toExternalReferenceEntities(List<ExternalReference> externalReferences) {
		return Optional.ofNullable(externalReferences).orElse(emptyList()).stream()
			.distinct() // Remove duplicates
			.map(WebMessageMapper::toExternalReferenceEntity)
			.toList();
	}

	private static ExternalReferenceEntity toExternalReferenceEntity(ExternalReference externalReference) {
		return ExternalReferenceEntity.create()
			.withKey(externalReference.getKey())
			.withValue(externalReference.getValue());
	}

	private static List<ExternalReference> toExternalReferences(List<ExternalReferenceEntity> externalReferenceEntities) {
		return Optional.ofNullable(externalReferenceEntities).orElse(emptyList()).stream()
			.map(WebMessageMapper::toExternalReference)
			.toList();
	}

	private static ExternalReference toExternalReference(ExternalReferenceEntity externalReferenceEntity) {
		return ExternalReference.create()
			.withKey(externalReferenceEntity.getKey())
			.withValue(externalReferenceEntity.getValue());
	}
}
