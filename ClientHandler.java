// A Java program for a Client Handler
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ClientHandler implements Runnable {
    private Socket              socket          = null;
    private DataInputStream     dis             = null;
    private DataOutputStream    dos             = null;
    private AggregationServer   AS              = null;
    private int                 eventNo;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, AggregationServer AS) {
        socket = s;
        this.dis = dis;
        this.dos = dos;
        this.AS  = AS;
        eventNo = 1;

    }
    public void run() {
        String line = "";
        // String packet = "";
        // String feedline = "";
        String feed = "";
        // String response = "";

        // reads message from client until "Over" is sent
        while (!line.equals("over")) {
            try {
                line = dis.readUTF();
                // Anything not in the form of a GET request is rejected.
                if (line.split(" ", 2)[0].compareTo("GET") == 0) {

                    Future future = AS.readWriteHandler.submit(new ReadRequest(AS.file));
                    // calling this will cause the thread to wait for the response of the future
                    try {
                        // System.out.println(future.get());
                        // dos.writeUTF(future.get().toString());
                        feed = future.get().toString();
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println(e);
                    }
                    // write back to client
                    dos.writeUTF(feed);

                } else {
                    dos.writeUTF("400 - Bad request");
                    Thread.currentThread().interrupt();
                }

            } catch(IOException i) {
                // i.printStackTrace();
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
