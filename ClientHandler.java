// A Java program for a Client Handler
import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable {
    private Socket              socket          = null;
    private DataInputStream     dis             = null;
    private DataOutputStream    dos             = null;
    private AggregationServer   AS              = null;
    private DataOutputStream    fileRequest     = null;
    private DataInputStream     fileResponse    = null;
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
        String packet = "";
        String feedline = "";
        String feed = "";
        String response = "";

        // reads message from client until "Over" is sent
        while (!line.equals("over")) {
            try {
                line = dis.readUTF();
                // Anything not in the form of a GET request is rejected.
                if (line.split(" ", 2)[0].compareTo("GET") == 0) {
                    System.out.println(line);
                    packet = line;

                    // make request to retrieve file contents
                    Socket s = new Socket("127.0.0.1", 9005);

                    System.out.println("Connected to server, awaiting service...");
                    dos.writeUTF("Request is being processed...");

                    // takes input from server
                    fileResponse = new DataInputStream(socket.getInputStream());
                    // sends output to the socket
                    fileRequest  = new DataOutputStream(socket.getOutputStream());
                    // fileRequest.writeInt(eventNo);

                    // waiting for response to tell us to send message
                    // TODO: maybe send packet straight away and wait for response.
                    if (fileResponse.readBoolean()) {
                        System.out.println("read access enabled");
                        fileRequest.writeUTF(packet);
                        while(!feedline.equals("</feed>")) {
                            feedline = fileResponse.readUTF();
                            feed += "\n";
                            feed += feedline;
                        }
                    } else {
                        dos.writeUTF("500 - Error processing request");
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
