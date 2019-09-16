// A Java program for a Client Handler
import java.net.*;
import java.io.*;

public class ConnectContentServer implements Runnable {
    //initialise server socket
    private ServerSocket server = null;
    int port;

    public ConnectContentServer(int port) {
        this.port = port;
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
        } catch(IOException e) {
            System.out.println(e);
        }

    }

    public void run() {

        System.out.println("Content Server Connection started\nWaiting for a content servers ...");

        while (true) {
            Socket socket = null;
            try {

                socket = server.accept();
                System.out.println("New Content Server: " + socket);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                Thread t = new Thread(new ContentServerHandler(socket, dis, dos));
                t.start();

            } catch(IOException i) {

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
