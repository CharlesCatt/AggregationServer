// A Java program for a Server
import java.net.*;
import java.io.*;


public class AggregationServer {
    //
    public AggregationServer(int clientPort, int contentServerPort) {

        Thread connectCServer = new Thread(new ConnectContentServer(contentServerPort));
        connectCServer.start();
        Thread connectC = new Thread(new ConnectClient(clientPort));
        connectC.start();

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
            clientPort = 2345;
            contentServerPort = 2346;
        }

        AggregationServer AS = new AggregationServer(clientPort, contentServerPort);
    }
}
