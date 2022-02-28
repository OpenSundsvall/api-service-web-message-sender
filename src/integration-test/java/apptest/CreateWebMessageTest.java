package apptest;

import org.junit.jupiter.api.Test;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.webmessagesender.Application;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@WireMockAppTestSuite(
        files = "classpath:/CreateWebMessage/",
        classes = Application.class
)

class CreateWebMessageTest extends AbstractAppTest {

    @Test
    void test1_createSuccessful() throws Exception {

        setupCall()
                .withServicePath("/webmessages")
                .withHttpMethod(POST)
                .withRequest("request.json")
                .withExpectedResponseStatus(CREATED)
                .withExpectedResponseHeader(LOCATION, List.of("^http://(.*)/webmessages/(.*)$"))
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test2_timeoutFromOep() throws Exception {

        setupCall()
                .withServicePath("/webmessages")
                .withHttpMethod(POST)
                .withRequest("request.json")
                .withExpectedResponseStatus(INTERNAL_SERVER_ERROR)
                .withExpectedResponse("response.json")
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test3_instanceIdNotFound() throws Exception {

        setupCall()
                .withServicePath("/webmessages")
                .withHttpMethod(POST)
                .withRequest("request.json")
                .withExpectedResponseStatus(NOT_FOUND)
                .withExpectedResponse("response.json")
                .sendRequestAndVerifyResponse();
    }

}
