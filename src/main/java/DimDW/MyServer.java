package DimDW;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class MyServer {
    public static void main(String[] args) {
        System.out.println("Launch");
        int port = 2024;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started!");
            while (true) {
                Socket socket = serverSocket.accept();
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter output = new PrintWriter(socket.getOutputStream())) {
                    StringBuilder request = new StringBuilder();
                    while (input.ready()) {
                        request.append(input.readLine());
                    }
                    System.out.println("Client connected!");
                    String requestClient = request.toString();
                    if (requestClient.contains("GET")) {
                        output.println("HTTP/1.1 200 OK");
                        output.println("Content-Type: text/html; charset=utf-8");
                        output.println();
                        File currentDir = new File(System.getProperty("user.dir"));
                        for (File file : Objects.requireNonNull(currentDir.listFiles())) {
                            if (file.isDirectory()) output.print("/");
                            output.print(file.getName());
                            output.println("<br>");
                        }
                        output.flush();
                    } else {
                        output.println("HTTP/1.1 404 Not Found");
                        output.println("Content-Type: text/html; charset=utf-8");
                        output.println();
                        output.flush();
                    }
                    System.out.println("Client disconnected!");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


