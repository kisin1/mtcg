package at.bif3.swe1.kisin.httpServer;

import at.bif3.swe1.kisin.httpServer.enums.Method;

import java.util.List;
import java.util.Map;

public class Request {
     private Method method;
    private String fullPath;
    private List<String> path;
    private String httpVersion;
    private Map<String, String> headerMap;
    private String body;



    public void setMethod(String method) {
        this.method = Method.valueOf(method.toUpperCase());
    }
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
    public String getFullPath() { return fullPath; }
    public List<String> getPath() { return path; }
    public void setPath(List<String> path) { this.path = path; }
    public void setHttpVersion(String httpVersion){
        this.httpVersion = httpVersion;
    }
    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }
    public Map<String, String> getHeaderMap(){ return this.headerMap; }
    public void setBody(String body) {
        this.body = body;
    }
    public Method getMethod() { return method; }
    public String getBody() {
        return this.body;
    }
    public int getContentLength() {
        if(this.headerMap.get("Content-Length") != null)
            return Integer.parseInt(this.headerMap.get("Content-Length"));
        return 0;
    }

    public String getAuthorizationToken(){
        if(this.headerMap.get("Authorization") != null)
            return this.headerMap.get("Authorization");
        return "Unknown";
    }
    @Override
    public String toString(){
        return "Method: " + this.method + "\n" +
                "Path: " + this.fullPath + "\n" +
                "Version: " + this.httpVersion + "\n" +
                "Headers: " + headerMap + "\n" +
                "Body: " + this.body;
    }


}
