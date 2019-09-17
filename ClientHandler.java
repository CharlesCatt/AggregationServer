// A Java program for a Client Handler
import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable {
    private Socket           socket   = null;
    private DataInputStream  dis       = null;
    private DataOutputStream dos      = null;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        socket = s;
        this.dis = dis;
        this.dos = dos;

    }
    public void run() {
        String line = "";
        String packet = "";

        // reads message from client until "Over" is sent
        while (!line.equals("Over"))
        {
            try {
                line = dis.readUTF();
                System.out.println(line);
                packet += line + "\n";
                dos.writeUTF("server says: " + line.toUpperCase());

            } catch(IOException i) {
                // i.printStackTrace();
                System.out.println(i);
                return;

            }
            AS.addToQueue(socket, packet);
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
