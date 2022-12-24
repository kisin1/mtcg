package at.bif3.swe1.kisin.httpServer;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private Socket connection;

    public RequestHandler(Socket connection){ this.connection = connection; }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            //message reader
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            in = new BufferedReader(inputStreamReader);
            //message writer
            OutputStream outputStream = connection.getOutputStream();
            out = new PrintWriter(outputStream);

            //receive and parse request
            Request request = new RequestParser().parseRequest(in);
            System.out.println(request);

            out.write(new Response(StatusCode.OK, ContentType.APPLICATION_JSON, "hola" + request.getBody()).get());
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                    connection.close();
                }
            } catch (IOException e) {
               e.printStackTrace();
            }
        }

    }
}
