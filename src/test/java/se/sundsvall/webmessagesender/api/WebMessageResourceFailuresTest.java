package se.sundsvall.webmessagesender.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;

import io.restassured.RestAssured;
import se.sundsvall.webmessagesender.Application;
import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.service.WebMessageService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class WebMessageResourceFailuresTest {

	@MockBean
	private WebMessageService webMessageService;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	void createWebMessageMissingPartyId() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withPartyId(null); // Missing partyId

		final var response = given()
			.contentType(JSON)
			.body(createWebMessageRequest)
			.when()
			.post("/webmessages")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("partyId");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageInvalidPartyId() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withPartyId("invalid"); // Invalid partyId

		final var response = given()
			.contentType(JSON)
			.body(createWebMessageRequest)
			.when()
			.post("/webmessages")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("partyId");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageMissingExternalReferences() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(null) // Missing externalReferences
			.withPartyId(UUID.randomUUID().toString());

		final var response = given()
			.contentType(JSON)
			.body(createWebMessageRequest)
			.when()
			.post("/webmessages")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("externalReferences");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("can not be empty or contain elements with empty keys or values");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageInvalidExternalReferences() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue(" "))) // Invalid externalReferences
			.withPartyId(UUID.randomUUID().toString());

		final var response = given()
			.contentType(JSON)
			.body(createWebMessageRequest)
			.when()
			.post("/webmessages")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("externalReferences");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("can not be empty or contain elements with empty keys or values");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageEmptyJsonBody() {

		// Parameter values
		final var createWebMessageRequest = "{}";

		final var response = given()
			.contentType(JSON)
			.body(createWebMessageRequest)
			.when()
			.post("/webmessages")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("externalReferences", "partyId");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("can not be empty or contain elements with empty keys or values", "not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void getWebMessageByIdInvalidId() {

		// Parameter values
		final var id = "invalid";

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.get("/webmessages/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("getWebMessageById.id");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void getWebMessagesByPartyIdInvalidPartyId() {

		// Parameter values
		final var partyId = "invalid";

		final var response = given()
			.contentType(JSON)
			.pathParam("partyId", partyId)
			.when()
			.get("/webmessages/recipients/{partyId}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("getWebMessagesByPartyId.partyId");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void getWebMessagesByExternalReferenceInvalidKey() {

		// Parameter values
		final var key = "x";
		final var value = "value";

		final var response = given()
			.contentType(JSON)
			.pathParam("key", key)
			.pathParam("value", value)
			.when()
			.get("/webmessages/external-references/{key}/{value}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("getWebMessagesByExternalReference.key");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("size must be between 3 and 128");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void getWebMessagesByExternalReferenceInvalidValue() {

		// Parameter values
		final var key = "flowInstanceId";
		final var value = "x";

		final var response = given()
			.contentType(JSON)
			.pathParam("key", key)
			.pathParam("value", value)
			.when()
			.get("/webmessages/external-references/{key}/{value}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("getWebMessagesByExternalReference.value");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("size must be between 3 and 128");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void deleteWebMessageByIdInvalidId() {

		// Parameter values
		final var id = "Not valid";

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.delete("/webmessages/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("deleteWebMessageById.id");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void deleteWebMessageByIdEmptyId() {

		// Parameter values
		final var id = "";

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.delete("/webmessages/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
		assertThat(response.getViolations()).isEmpty();

		// Verification
		verifyNoInteractions(webMessageService);
	}
}
