package at.bif3.swe1.kisin.httpServer;

import javax.swing.plaf.PanelUI;

public class Response {

    private final int STATUS_CODE;
    private final String STATUS_MESSAGE;

    private final ContentType contentType;

    private final String content;

    private final String version;

    private final String CRLF = "\r\n";


    public Response(StatusCode statusCode, ContentType contentType, String content){
        this.STATUS_CODE = statusCode.STATUS_CODE;
        this.STATUS_MESSAGE = statusCode.STATUS_MESSAGE;
        this.contentType = contentType;
        this.content = content;
        this.version = "HTTP/1.1";
    }

    public String get(){
        return this.version + " " + this.STATUS_CODE + " " + this.STATUS_MESSAGE + CRLF +
                "Content-Type: " + this.contentType + CRLF +
                "Content-Length: " + (this.content != null ? this.content.length() : 0) + CRLF +
                "Connection: close" + CRLF +
                CRLF +
                this.content;
    }
}
