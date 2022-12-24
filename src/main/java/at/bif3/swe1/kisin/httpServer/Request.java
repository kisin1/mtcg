package at.bif3.swe1.kisin.httpServer;

import java.util.Map;

public class Request {
    private Method method;
    private String path;
    private String httpVersion;

    private String parameters;
    private Map<String, String> headerMap;
    private String body;



    public void setMethod(String method) {
        this.method = Method.valueOf(method.toUpperCase());
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setHttpVersion(String httpVersion){
        this.httpVersion = httpVersion;
    }
    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getBody() {
        return this.body;
    }
    public int getContentLength() {
        if(this.headerMap.get("Content-Length") != null)
            return Integer.parseInt(this.headerMap.get("Content-Length"));
        return 0;
    }

    @Override
    public String toString(){
        return "Method: " + this.method + "\n" +
                "Path: " + this.path + "\n" +
                "Version: " + this.httpVersion + "\n" +
                "Headers: " + headerMap + "\n" +
                "Body: " + this.body;
    }

}
