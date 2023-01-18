package at.bif3.swe1.kisin.httpServer.model;

import at.bif3.swe1.kisin.httpServer.enums.ContentType;
import at.bif3.swe1.kisin.httpServer.enums.StatusCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void responseBuilding() {
        Response response = new Response(StatusCode.BAD_REQUEST, ContentType.APPLICATION_JSON, "Test Response");
        assertEquals(ContentType.APPLICATION_JSON, response.getContentType());
    }
}