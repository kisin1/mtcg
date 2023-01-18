package at.bif3.swe1.kisin.httpServer.model;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParser {

    private Request request;

    public RequestParser(){
        this.request = new Request();
    }

    public Request parseRequest(BufferedReader in) throws IOException {
        String firstLine = in.readLine();

        if(firstLine != null){
            //divide first line of the request on " "
            String[] splitFirstLine = firstLine.split(" ");
            //set method, path and version
            request.setMethod(splitFirstLine[0]);
            request.setFullPath(splitFirstLine[1]);
            request.setHttpVersion(splitFirstLine[2]);
        }

        String line = in.readLine();
        Map<String, String> headerMap = new HashMap<>();

        //save headers in a hashmap
        while (!line.isEmpty()){
            String[] headerRow = line.split(":", 2);
            headerMap.put(headerRow[0], headerRow[1].trim());
            line = in.readLine();
        }

        request.setHeaderMap(headerMap);
        request.setPath(splitPath());

        //receive body
        if(request.getContentLength() > 0){
            char[] charBuffer = new char[request.getContentLength()];
            in.read(charBuffer, 0, request.getContentLength());
            request.setBody(new String(charBuffer));
        }
        return request;
    }
    public List<String> splitPath(){
        String fullPath = request.getFullPath().substring(1);
        List<String> temp = List.of(fullPath.split("/"));

        if(temp.get(0).contains("?")){
            temp = List.of(temp.get(0).split("\\?"));
        }
        return temp;
    }

}
