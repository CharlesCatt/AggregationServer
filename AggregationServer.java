// A Java program for a Server
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class AggregationServer {
    private ServerSocket server = null;
    public  ThreadPoolExecutor readWriteHandler = null;
    public  int eventNo;


    // initialise the server threads
    public AggregationServer(int port) {

        eventNo = 0;


        // start read/write request handler thread
        FileRequestComparator comparator = new FileRequestComparator();
        BlockingQueue<Runnable> threadQueue = new PriorityBlockingQueue<Runnable>(11, comparator);
        readWriteHandler = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, threadQueue);
        // readWriteHandler = new PriorityThreadPool();
        // readWriteHandler = Executors.newSingleThreadExecutor();


        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
        } catch(IOException e) {
            System.out.println(e);
        }

        
        // readWriteHandler.shutdown();
        // handleReadWriteRequests();

    }

    // public class compareThreads implements Comparator<ReadWriteRequest> {
    //     public int compare(ReadWriteRequest a, ReadWriteRequest b) {
    //         return a.eventNo - b.eventNo;
    //     }
    // }

    public void acceptConnections() {
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
    }

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
        AS.acceptConnections();


    }
}
