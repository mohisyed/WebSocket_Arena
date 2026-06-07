package org.example.websocket;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WebSocket {
    public static void main(String[] args) throws IOException{

        // we creating/opening a server socket
        ServerSocket server = new ServerSocket(8080);

        // when we close the server it still waits for a little bit just in case other connections still connect
        server.setReuseAddress(true);

        try{
            System.out.println("Server has started on 127.0.0.1:8080.\r\nWaiting for a connection…");


            // create/open up a tcp server socket bound to 8080
            Socket client = server.accept();
            System.out.println("A client connected.");

            // the incoming input stream  (what the user types in)
            InputStream in = client.getInputStream();

            // the outgoing input stream the results we send back to the client
            OutputStream out = client.getOutputStream();

            // define scanner
            Scanner s = new Scanner(in);


            // input loop
            while(s.hasNextLine()){
                String line = s.nextLine();
                System.out.println("Received: " + line);
                out.write((line+ "\n").getBytes());
                out.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
