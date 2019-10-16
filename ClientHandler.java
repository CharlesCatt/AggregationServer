// A Java program for a Client Handler
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ClientHandler implements Runnable {
    private Socket              socket          = null;
    private DataInputStream     dis             = null;
    private DataOutputStream    dos             = null;
    private AggregationServer   AS              = null;

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
                // update event time
                System.out.println(line);
                int givenTime = Integer.parseInt(line.substring(line.indexOf("<eventNo>")+9, line.indexOf("</eventNo>")));
                AS.eventNo = (givenTime > AS.eventNo) ? (givenTime + 1) : (AS.eventNo + 1);

                // Anything not in the form of a GET or PUT request is rejected.
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
                    AS.eventNo += 1;
                    feed = "<eventNo>" + Integer.toString(AS.eventNo) + "</eventNo>" + feed;
                    dos.writeUTF(feed);

                } else if (line.split(" ")[0].compareTo("PUT") == 0) {

                    // line in form: PUT name

                    // String[] lineList = line.split(" ");
                    System.out.println(line.split("[\n ]")[1] + " made a PUT request");
                    String contentServerName = line.split("[\n ]")[1];
                    String packet = line.split("\n",2)[1];
                    packet = packet.substring(0, packet.indexOf("<eventNo>")); // strip the eventNo tags
                    Future future = AS.readWriteHandler.submit(
                            (new WriteRequest(AS.file, contentServerName, packet)));

                    // calling this will cause the thread to wait for the response of the future
                    try {
                        status = future.get().toString();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    }

                    // write back to client
                    AS.eventNo += 1;
                    status += "\n<eventNo>" + Integer.toString(AS.eventNo) + "</eventNo>" ;

                    // write back to client
                    dos.writeUTF(status);
                } else {
                    System.out.println(line.split(" ")[0]);
                    dos.writeUTF("400 - Bad request\n" + "<eventNo>" + Integer.toString(AS.eventNo) + "</eventNo>");
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
