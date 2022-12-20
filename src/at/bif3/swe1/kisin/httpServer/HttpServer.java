package at.bif3.swe1.kisin.httpServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private static final int PORT = 10001;
    private int port;
    public HttpServer(int port) { this.port = PORT; }

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(PORT);
        server.start();
    }

    protected void start() throws IOException {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server starting on port " + this.port);
        } catch (IOException e) {
            System.out.println("Server starting error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println("Waiting for connections...");

        while (true){
            try {
                Socket connection = serverSocket.accept();
                System.out.println("Connection established, sending data...");
/////////////////////////////////////////////////////////////////////
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                PrintWriter out = new PrintWriter(connection.getOutputStream());

                String str = ".";
                while (!str.equals("")){
                    str = in.readLine();
                }

                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html");
                out.println("Server: Bot");
                out.println("");
                out.println("<h3>Welcome to my first attempt server</h3>");
                out.flush();

                connection.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
