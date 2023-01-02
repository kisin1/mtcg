package at.bif3.swe1.kisin.httpServer;

import at.bif3.swe1.kisin.httpServer.model.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private static final int PORT = 10001;
    private int port;
    public HttpServer(int port) { this.port = PORT; }

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(PORT);
        server.start();
    }

    protected void start() throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Server started ...");
            while(true) {
                Socket clientConnection = serverSocket.accept();
                executorService.execute(new RequestHandler(clientConnection));
            }
        }
    }
}

