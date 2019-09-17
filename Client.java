// A Java program for a Client
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client
{
    // initialize socket and input output streams
    private Socket socket            = null;
    private DataInputStream  input   = null;
    private DataInputStream  serverResponse = null;
    private DataOutputStream out     = null;

    // constructor to put ip address and port
    public Client(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected to server");

            // takes input from server
            serverResponse = new DataInputStream(socket.getInputStream());

            // takes input from terminal
            input  = new DataInputStream(System.in);

            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());


        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        // Scanner input = new Scanner(System.in);
        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            try {
                // line = input.nextLine();
                line = input.readLine();
                out.writeUTF(line);
                // String outline =  "\nResponse: ";
                // outline += serverResponse.readLine();
                System.out.println("Response: " + serverResponse.readUTF());

            } catch(IOException i) {
                System.out.println(i);
            }
        }

        // close the connection
        try
        {
            input.close();
            out.close();
            serverResponse.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        if (args.length > 0) {
            Client client = new Client("127.0.0.1", Integer.parseInt(args[0]));
        } else {
            Client client = new Client("127.0.0.1", 2345);
        }    }
}