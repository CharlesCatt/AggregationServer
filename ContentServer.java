// A Java program for a Client
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ContentServer
{
    // initialize socket and input output streams
    private Socket socket            = null;
    private Scanner input = null;
    private DataInputStream  serverResponse = null;
    private DataOutputStream out     = null;

    // constructor to put ip address and port
    public ContentServer(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected to server");

            // takes input from server
            serverResponse = new DataInputStream(socket.getInputStream());

            // takes input from terminal
            // input  = new DataInputStream(System.in);
            // input = new BufferedReader(new InputStreamReader(System.in));
            input = new Scanner(System.in);

            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());


        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
            return;
        }
        catch(IOException i)
        {
            System.out.println(i);
            return;
        }
        // string to read message from input
        String line = "";
        String packet = "";
        String name = "";

        // keep reading until end over is input
        while (!line.equals("over"))
        {
            try {
                line = input.nextLine();
                name = input.nextLine();
                // System.out.print(line);
                // now, until end of feed, add the lines to the packet to be sent
                while(!line.equals("</feed>")) {
                    line = input.nextLine();
                    if (line == null) {
                        break;
                    }
                    packet += line + "\n";
                }
                out.writeUTF("PUT " + name + "\n" + packet);
                System.out.println("Response\n" + serverResponse.readUTF());

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
            ContentServer contentServer = new ContentServer("127.0.0.1", Integer.parseInt(args[0]));
        } else {
            ContentServer contentServer = new ContentServer("127.0.0.1", 4567);
        }    }
}
