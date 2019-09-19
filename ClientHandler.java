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
        try {
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.print(e);
        }

        // this.dis = dis;
        // this.dos = dos;
        this.AS  = AS;
        eventNo = 1;

    }
    // the first line from the socket. Check if it is a GET or PUT request and submit read or write request accordingly
    public void run() {
        String line = "";
        String feed = "";
        String status = "";

        // reads message from client until "Over" is sent
        while (true) {
            try {
                line = dis.readUTF();
                // Anything not in the form of a GET request is rejected.
                if (line.split(" ")[0].compareTo("GET") == 0) {

                    Future future = AS.readWriteHandler.submit(new ReadRequest(AS.file));

                    // calling this will cause the thread to wait for the response of the future
                    try {
                        feed = future.get().toString();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    }

                    // write back to client
                    dos.writeUTF(feed);

                } else if (line.split(" ")[0].compareTo("PUT") == 0) {

                    // line in form: PUT name

                    // String[] lineList = line.split(" ");
                    System.out.println(line.split("[\n ]")[1] + " made a PUT request");
                    Future future = AS.readWriteHandler.submit(
                            (new WriteRequest(AS.file, line.split("[\n ]")[1], line.split("\n",2)[1])));

                    // calling this will cause the thread to wait for the response of the future
                    try {
                        status = future.get().toString();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    }
                    // write back to client
                    dos.writeUTF(status);
                } else {
                    dos.writeUTF("400 - Bad request");
                    break;
                }

            } catch(IOException i) {
                // i.printStackTrace();
                System.out.println(i);
                break;

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
