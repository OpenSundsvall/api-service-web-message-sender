package apptest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.webmessagesender.Application;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@WireMockAppTestSuite(
        files = "classpath:/GetWebMessage/",
        classes = Application.class
)
@Sql(scripts = {
        "/db/scripts/truncate.sql",
        "/db/scripts/WebMessageRepositoryTest.sql"
})
class DeleteWebMessageTest extends AbstractAppTest {

    @Test
    void test1_deleteWebmessageByIdSuccessful() throws Exception {
        final var id = "e535c9f7-c473-44f2-81d5-a8fbfcc932ea";
        setupCall()
                .withServicePath("/webmessages/" + id)
                .withHttpMethod(DELETE)
                .withExpectedResponseStatus(NO_CONTENT)
                .sendRequestAndVerifyResponse();
    }
}
