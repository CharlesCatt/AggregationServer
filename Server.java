// A Java program for a Server
import java.net.*;
import java.io.*;

public class Server {
    //initialize socket
    private ServerSocket server = null;

    // constructor with port
    public Server(int port) { 
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
        } catch(IOException e) {
            System.out.println(e);
        }
        System.out.println("Server started\nWaiting for a clients ...");

        while (true) {
            Socket socket = null;
            try {

                socket = server.accept();
                System.out.println("New client: " + socket);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                Thread t = new Thread(new ClientThread(socket, in, out));
                t.start();
            } catch(IOException i) {
                server.close();
                System.out.println(i);
                System.exit(1);
            }
        }

    }

    public static void main(String args[]) {
        Server server = new Server(5000);
    }
}
