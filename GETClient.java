// A Java program for a Client
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class GETClient {
    // initialize socket and input output streams
    private Socket              socket          = null;
    private Scanner             input           = null;
    private DataInputStream     serverResponse  = null;
    private DataOutputStream    dos             = null;
    private int                 eventNo;

    // constructor to put ip address and port
    public GETClient(String address, int port, String inputFileName) {

        eventNo = 0;
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected to server");

            // takes input from server
            serverResponse = new DataInputStream(socket.getInputStream());

            // takes input from file if provided, otherwise command line
            input = (inputFileName != null) ? new Scanner(new File(inputFileName)) : new Scanner(System.in);

            // sends output to the socket
            dos = new DataOutputStream(socket.getOutputStream());


        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        

    }

    public String readInput() {
        // string to read message from input
        String line = "";
        String data = "";

        // keep reading until "over" is input
        while (line != null & input.hasNextLine()) {
            line = input.nextLine();
            data += line + "\n";
        }
        return data;

        
    }

    public void sendData(String packet) {
        String response = null;
        try {
            eventNo += 1;
            dos.writeUTF(packet + "<eventNo>" + Integer.toString(eventNo) + "</eventNo>");
            response = serverResponse.readUTF();
            
        } catch (IOException e) {
            System.out.println(e);
            System.exit(-1);
        }
            
        // could run some sort of verification of format depending on xml parser

        // update eventNo:
        int givenTime = Integer.parseInt(response.substring(response.indexOf("<eventNo>") + 9, response.indexOf("</eventNo>")));
        eventNo = (givenTime > eventNo) ? (givenTime + 1) : (eventNo + 1);
        
        System.out.println("Response\n" + response);
        
    }

    public void closeAll() {
        // close the connection
        try {
            input.close();
            dos.close();
            serverResponse.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    public static void main(String args[]) {
        GETClient client;
        if (args.length > 1) {
            client = new GETClient("127.0.0.1", Integer.parseInt(args[1]), args[0]);
        } else if (args.length > 0) {
            client = new GETClient("127.0.0.1", 4567, args[0]);
        } else {
            client = new GETClient("127.0.0.1", 4567, "");
        }
        String data = client.readInput();
        client.sendData(data);
        client.closeAll();
    }

}
