// A Java program for a Server
import java.net.*;
import java.io.*;
import java.util.PriorityQueue;







public class AggregationServer {
    public static AggregationServer AS = null;
    private static String fileName = null;
    private static File file = null;
    private static BufferedReader reader = null;
    private ServerSocket fileReadWriteRequests = null;


    // initialise the server threads
    public AggregationServer(int clientPort, int contentServerPort) {

        Thread connectCServer = new Thread(new ConnectContentServer(contentServerPort, this));
        connectCServer.start();
        Thread connectC = new Thread(new ConnectClient(clientPort, this));
        connectC.start();
        fileName = "temp.dat";
        try {
            // initialise the file to use
            file = new File("fileName");
            if (!(file.exists() || file.isFile())){
                file.createNewFile();
            }
            System.out.println("file is ready");
        } catch (IOException e) {
            System.out.println("file reading error");
            System.out.println(e);
            System.exit(-1);
        }
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException i) {
            System.out.println("file not found while initialising reader");
            System.out.println(i);
            System.exit(-1);
        }

        handleReadWriteRequests();

    }

    public void handleReadWriteRequests() {

        PriorityQueue<Thread> PQ = new PriorityQueue<Thread>();

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
            s = fileReadWriteRequests.accept();
            PQ.add(new Thread(new ReadWriteRequest(s)));
        }
        System.out.println("PriorityQueue size: " + Integer.toString(PQ.size()));
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
