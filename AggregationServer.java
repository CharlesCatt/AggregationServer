// A Java program for a Server
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class AggregationServer {
    // public  AggregationServer AS = null;
    private String fileName = null;
    public File file = null;
    // private BufferedReader reader = null;
    private ServerSocket server = null;
    public  ExecutorService readWriteHandler = null;
    public int eventNo;


    // initialise the server threads
    public AggregationServer(int port) {
        // consolidate file to read
        fileName = "temp.dat";
        try {
            // initialise the file to use
            file = new File(fileName);
            if (!(file.exists() || file.isFile())){
                file.createNewFile();
            }
            System.out.println("file is ready");
        } catch (IOException e) {
            System.out.println("file reading error");
            System.out.println(e);
            System.exit(-1);
        }
        eventNo = 0;


        // start read/write request handler thread
        readWriteHandler = Executors.newSingleThreadExecutor();


        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
        } catch(IOException e) {
            System.out.println(e);
        }

        while (true) {
            Socket socket = null;
            try {

                socket = server.accept();
                System.out.println("New client: " + socket);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                Thread t = new Thread(new ClientHandler(socket, dis, dos, this));
                t.start();

            } catch(IOException i) {
                i.printStackTrace();
                System.out.println(i);
                break;

            }

        }
        try {
            server.close();
        } catch (IOException e){
            System.out.println(e);
            System.exit(1);
        }
        readWriteHandler.shutdown();
        // handleReadWriteRequests();

    }

    // public class compareThreads implements Comparator<ReadWriteRequest> {
    //     public int compare(ReadWriteRequest a, ReadWriteRequest b) {
    //         return a.eventNo - b.eventNo;
    //     }
    // }

    public static void main(String args[]) {

        int port;
        // supplying port numbers are optional
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            // default port numbers
            port = 4567;
        }
        AggregationServer AS = new AggregationServer(port);



    }
}
