package at.bif3.swe1.kisin.httpServer;

public enum ContentType {
    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain");

    public final String contentType;
    ContentType(String contentType) {
        this.contentType = contentType;
    }
}
