package se.sundsvall.webmessagesender.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import se.sundsvall.webmessagesender.Application;
import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.api.model.WebMessage;
import se.sundsvall.webmessagesender.service.WebMessageService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class WebMessageResourceTest {

	@MockBean
	private WebMessageService webMessageService;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	void createWebMessage() {

		// Mock
		final var id = UUID.randomUUID().toString();
		when(webMessageService.createWebMessage(any())).thenReturn(WebMessage.create().withId(id));

		final var expectedLocationURL = RestAssured.baseURI.concat(":").concat(String.valueOf(RestAssured.port)).concat("/webmessages/").concat(id);

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withPartyId(UUID.randomUUID().toString());

		given()
			.contentType(JSON)
			.body(createWebMessageRequest)
			.when()
			.post("/webmessages")
			.then().assertThat()
			.statusCode(HttpStatus.CREATED.value())
			.contentType(equalTo(ALL_VALUE))
			.header(LOCATION, is(expectedLocationURL));

		// Verification
		verify(webMessageService).createWebMessage(createWebMessageRequest);
	}

	@Test
	void deleteWebMessageById() {

		// Parameter values
		final var id = "81471222-5798-11e9-ae24-57fa13b361e1";

		given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.delete("/webmessages/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.NO_CONTENT.value())
			.contentType(is(emptyString()));

		// Verification
		verify(webMessageService).deleteWebMessageById(id);
	}

	@Test
	void getWebMessageById() {

		// Mock
		when(webMessageService.getWebMessageById(any())).thenReturn(WebMessage.create());

		// Parameter values
		final var id = UUID.randomUUID().toString();

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.get("/webmessages/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.OK.value())
			.contentType(equalTo(APPLICATION_JSON_VALUE))
			.extract().as(WebMessage.class);

		// Verification
		assertThat(response).isNotNull();
		verify(webMessageService).getWebMessageById(id);
	}

	@Test
	void getWebMessagesByPartyId() {

		// Mock
		when(webMessageService.getWebMessagesByPartyId(any())).thenReturn(List.of(WebMessage.create()));

		// Parameter values
		final var partyId = UUID.randomUUID().toString();

		final var response = given()
			.contentType(JSON)
			.pathParam("partyId", partyId)
			.when()
			.get("/webmessages/recipients/{partyId}")
			.then().assertThat()
			.statusCode(HttpStatus.OK.value())
			.contentType(equalTo(APPLICATION_JSON_VALUE))
			.extract().as(WebMessage[].class);

		// Verification
		assertThat(response).isNotNull().hasSize(1);
		verify(webMessageService).getWebMessagesByPartyId(partyId);
	}

	@Test
	void getWebMessagesByExternalReference() {

		// Mock
		when(webMessageService.getWebMessagesByExternalReference(any(), any())).thenReturn(List.of(WebMessage.create()));

		// Parameter values
		final var key = "key";
		final var value = "value";

		final var response = given()
			.contentType(JSON)
			.pathParam("key", key)
			.pathParam("value", value)
			.when()
			.get("/webmessages/external-references/{key}/{value}")
			.then().assertThat()
			.statusCode(HttpStatus.OK.value())
			.contentType(equalTo(APPLICATION_JSON_VALUE))
			.extract().as(WebMessage[].class);

		// Verification
		assertThat(response).isNotNull().hasSize(1);
		verify(webMessageService).getWebMessagesByExternalReference(key, value);
	}
}
