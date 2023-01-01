package at.bif3.swe1.kisin.httpServer;

public enum StatusCode {
    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content"),
    //client errors
    BAD_REQUEST(400, "Bad Request"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    //server errors
    INTERNAL_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    VERSION_NOT_SUPPORTED(505, "Version Not Supported");


    public final int STATUS_CODE;
    public final String STATUS_MESSAGE;
    StatusCode(int STATUS_CODE, String STATUS_MESSAGE) {
        this.STATUS_CODE = STATUS_CODE;
        this.STATUS_MESSAGE = STATUS_MESSAGE;
    }
}
