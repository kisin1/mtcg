package at.bif3.swe1.kisin.httpServer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestParserTest {

    @Test
    void parseRequest() {
        String path ="/users/kienboec";
        List<String> temp = List.of(path.split("/"));

        System.out.println(temp);
        if(temp.get(1).contains("?")){
            List<String> temp2 = List.of(temp.get(1).split("\\?"));
            System.out.println(temp2);
        }
    }
}