package apptest;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.webmessagesender.Application;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@WireMockAppTestSuite(
        files = "classpath:/GetWebMessage/",
        classes = Application.class
)
@Sql(scripts = {
        "/db/scripts/truncate.sql",
        "/db/scripts/WebMessageRepositoryTest.sql"
})
class GetWebMessageTest extends AbstractAppTest {

    @Test
    void test1_getWebmessageByIdSuccessful() throws Exception {
        final var id = "1e098e28-d9ba-459c-94c7-5508be826c08";
        setupCall()
                .withServicePath("/webmessages/" + id)
                .withHttpMethod(GET)
                .withExpectedResponse("response.json")
                .withExpectedResponseStatus(OK)
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test2_getWebmessagesByPartyIdSuccessful() throws Exception {
        final var partyId = "b7bd0e55-0811-4d3a-91d9-6bab7fd9ce5e";
        setupCall()
                .withServicePath("/webmessages/recipients/" + partyId)
                .withHttpMethod(GET)
                .withExpectedResponse("response.json")
                .withExpectedResponseStatus(OK)
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test3_getWebmessagesByExternalReferencesSuccessful() throws Exception {
        final var key = "key1";
        final var value = "value1";
        setupCall()
                .withServicePath("/webmessages/external-references/" + key + "/" + value)
                .withHttpMethod(GET)
                .withExpectedResponse("response.json")
                .withExpectedResponseStatus(OK)
                .sendRequestAndVerifyResponse();
    }

}
