package at.bif3.swe1.kisin.httpServer.model;


import at.bif3.swe1.kisin.httpServer.enums.ContentType;
import at.bif3.swe1.kisin.httpServer.enums.StatusCode;

public class Response {

    private int STATUS_CODE;
    private String STATUS_MESSAGE;
    private final ContentType contentType;
    private String content;
    private final String version;
    private final String CRLF = "\r\n";


    public Response(StatusCode statusCode, ContentType contentType, String content){
        this.STATUS_CODE = statusCode.STATUS_CODE;
        this.STATUS_MESSAGE = statusCode.STATUS_MESSAGE;
        this.contentType = contentType;
        this.content = content;
        this.version = "HTTP/1.1";
    }

    public void setStatusCode(StatusCode statusCode){
        this.STATUS_CODE = statusCode.STATUS_CODE;
        this.STATUS_MESSAGE = statusCode.STATUS_MESSAGE;
    }

    public int getStatusCode(){
        return this.STATUS_CODE;
    }

    public void setContent(String content){ this.content = content; }

    public String get(){
        return this.version + " " + this.STATUS_CODE + " " + this.STATUS_MESSAGE + CRLF +
                "Content-Type: " + this.contentType + CRLF +
                "Content-Length: " + (this.content != null ? this.content.length() : 0) + CRLF +
                "Connection: close" + CRLF +
                CRLF +
                this.content;
    }

    @Override
    public String toString() {
        return "Response{" +
                "STATUS_CODE=" + STATUS_CODE +
                ", STATUS_MESSAGE='" + STATUS_MESSAGE + '\'' +
                ", contentType=" + contentType +
                ", content='" + content + '\'' +
                ", version='" + version + '\'' +
                ", CRLF='" + CRLF + '\'' +
                '}';
    }
}
