package at.bif3.swe1.kisin.httpServer;


import java.io.BufferedReader;
import java.io.IOException;

public class Request {
    private Method method;

    private String path;

    private String parameters;

    private String body;

    private int contentLength;

    private final String CRLF = "\r\n";

    public Request parseRequest(BufferedReader in) throws IOException {
        Request request = new Request();
        String firstLine = in.readLine();

        if(firstLine != null){
            //divide first line of the request on " "
            String[] splitFirstLine = firstLine.split(" ");
            //set method
            request.method = Method.valueOf(splitFirstLine[0].toUpperCase());
            setPath(request, request.path);
        }
        return request;
    }

    private void setPath(Request request, String path){

    }
}
