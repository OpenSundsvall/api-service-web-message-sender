package se.sundsvall.webmessagesender.integration.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

public interface WebMessageRepository extends CrudRepository<WebMessageEntity, String> {

	List<WebMessageEntity> findByPartyIdOrderByCreated(String partyId);

	List<WebMessageEntity> findByExternalReferencesKeyAndExternalReferencesValueOrderByCreated(String key, String value);
}
