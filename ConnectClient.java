// A Java program for a Client Handler
import java.net.*;
import java.io.*;

public class ConnectClient implements Runnable {
    //initialise server socket
    private ServerSocket server = null;
    private int port;

    public ConnectClient(int port) {
        this.port = port;
        this.AS = AS;
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
        } catch(IOException e) {
            System.out.println(e);
        }

    }

    // constructor with port
    public void run() {

        System.out.println("Server started\nWaiting for a clients ...");

        while (true) {
            Socket socket = null;
            try {

                socket = server.accept();
                System.out.println("New client: " + socket);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                Thread t = new Thread(new ClientHandler(socket, dis, dos, AS));
                t.start();

            } catch(IOException i) {
                i.printStackTrace();
                System.out.println(i);
                try {
                    server.close();
                } catch (IOException e){
                    System.out.println(e);
                    System.exit(1);
                }
                System.exit(1);

            }

        }

    }

}
