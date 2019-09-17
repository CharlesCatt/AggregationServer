// A Java program for a Server
import java.net.*;
import java.io.*;


public class AggregationServer {
    // initialise the server threads
    public AggregationServer(int clientPort, int contentServerPort) {

        Thread connectCServer = new Thread(new ConnectContentServer(contentServerPort));
        connectCServer.start();
        Thread connectC = new Thread(new ConnectClient(clientPort));
        connectC.start();
        String fileName = "temp.dat";
        try {
            // create the file to use
            File file = new File("temp.dat");
            if (!(file.exists() || file.isFile())){
                file.createNewFile();
            }
            System.out.println("file is ready");
        } catch (IOException e) {
            System.out.println(e);
            System.exit(-1);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException i) {
            System.out.println(i);
            System.exit(-1);
        }


    }

    public int addToQueue(Socket handler, String packet) {
        System.out.println("from: " + socket);
        System.out.println("message: ");
        System.out.println(packet);
    }

    // static AggregationServer object so handlers can add things to the queue
    static AggregationServer AS;

    public static void main(String args[]) {

        int clientPort;
        int contentServerPort;
        // supplying port numbers are optional
        if (args.length == 2) {
            clientPort = Integer.parseInt(args[0]);
            contentServerPort = Integer.parseInt(args[1]);
        } else {
            // default port numbers
            clientPort = 2345;
            contentServerPort = 2346;
        }

        AS = new AggregationServer(clientPort, contentServerPort);
    }
}
