// A Java program for a Server
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class AggregationServer {
    public  static AggregationServer AS = null;
    private static String fileName = null;
    private static File file = null;
    private static BufferedReader reader = null;
    private ServerSocket fileReadWriteRequests = null;
    public  PriorityBlockingQueue<Thread> PQ = null;


    // initialise the server threads
    public AggregationServer(int clientPort, int contentServerPort) {

        Thread connectCServer = new Thread(new ConnectContentServer(contentServerPort, this));
        connectCServer.start();
        Thread connectC = new Thread(new ConnectClient(clientPort, this));
        connectC.start();

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

        handleReadWriteRequests();

    }

    public class compareThreads implements Comparator<ReadWriteRequest> {
        public int compare(ReadWriteRequest a, ReadWriteRequest b) {
            return a.eventNo - b.eventNo;
        }
    }

    public void handleReadWriteRequests() {

        PQ = new PriorityBlockingQueue<Thread>();
        Thread readWriteHandler = new Thread(new ReadWriteHandler(this));
        readWriteHandler.start();


        // starts server and waits for a connection
        try {
            fileReadWriteRequests = new ServerSocket(9005);
        } catch(IOException e) {
            System.out.println(e);
        }

        // listen for handlers sending read or write requests
        int i = 0;
        while (i < 3) {
            i++;
            Socket s;
            try {
                s = fileReadWriteRequests.accept();
                DataInputStream dis = new DataInputStream(s.getInputStream());
                int priority = dis.readInt();
                dis.close();
                ReadWriteRequest t = new ReadWriteRequest(s, file, priority);
                PQ.add(new Thread());
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        System.out.println("PriorityBlockingQueue size: " + Integer.toString(PQ.size()));
    }

    public static void main(String args[]) {

        int clientPort;
        int contentServerPort;
        // supplying port numbers are optional
        if (args.length == 2) {
            clientPort = Integer.parseInt(args[0]);
            contentServerPort = Integer.parseInt(args[1]);
        } else {
            // default port numbers
            clientPort = 4567;
            contentServerPort = 4568;
        }
        AS = new AggregationServer(clientPort, contentServerPort);



    }
}
