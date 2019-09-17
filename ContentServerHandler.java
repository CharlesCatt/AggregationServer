// A Java program for a Client Handler
import java.net.*;
import java.io.*;

public class ContentServerHandler implements Runnable {
    private Socket           socket   = null;
    private DataInputStream  dis      = null;
    private DataOutputStream dos      = null;

    public ContentServerHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        socket = s;
        this.dis = dis;
        this.dos = dos;

    }

    public void run() {
        String line = "";
        String response = "";

        // reads message from client until "Over" is sent
        while (!line.equals("Over")) {
            try {
                line = dis.readUTF();
                if (line.split(" ", 2)[0].compareTo("PUT") == 0) {
                    System.out.println(line);
                    dos.writeUTF(line.toUpperCase());
                } else {
                    System.out.println("invalid request, terminating connection");
                    dos.writeUTF("invalid request, terminating connection");
                    break;
                }

            } catch(IOException i) {
                System.out.println(i);
                return;
            }
        }

        try {
            dis.close();
            dos.close();
            socket.close();

        } catch(IOException i) {
            System.out.println(i);
        }
    }
}
