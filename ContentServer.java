// A Java program for a Client
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ContentServer
{
    // initialize socket and input output streams
    private Socket              socket         = null;
    private Scanner             input          = null;
    private DataInputStream     serverResponse = null;
    private DataOutputStream    out            = null;
    private int                 eventNo;

    // constructor to put ip address and port
    public ContentServer(String address, int port, String inputFileName) {
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
        
    }

    public String readInput() {
        // string to read message from input
        String line = "";
        String packet = "";
        String name = "";

        // keep reading until end over is input
        while (!line.equals("over") & input.hasNextLine())
        {
            // try {
                line = input.nextLine();
                name = input.nextLine();
                // System.out.print(line);
                // now, until end of feed, add the lines to the packet to be sent
                while(!line.equals("</feed>") & input.hasNextLine()) {
                    line = input.nextLine();
                    if (line == null) {
                        break;
                    }
                    packet += line + "\n";
                }
                return name + "\n" + packet;

            // } catch(IOException i) {
            //     System.out.println(i);
            // }
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
        return null;
    }

    void sendData(String packet) {
        try {
            // increment lamport time and start the message with that.
            eventNo += 1;
            packet = "PUT " + packet + "<eventNo>" + Integer.toString(eventNo) + "</eventNo>\n";
            out.writeUTF(packet);
            String response = serverResponse.readUTF();

            System.out.println("Response\n" + response);

            // update eventNo:
            int givenTime = Integer.parseInt(response.substring(response.indexOf("<eventNo>") + 9, response.indexOf("</eventNo>")));
            eventNo = (givenTime > eventNo) ? (givenTime + 1) : (eventNo + 1);

            // handle 400 bad request
            if (!response.contains("400 - Bad request")) {
                
            }
            // could run some sort of verification of format depending on xml parser

                
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) {
        ContentServer contentServer;
        if (args.length > 1) {
            contentServer = new ContentServer("127.0.0.1", Integer.parseInt(args[1]), args[0]);
        } else if (args.length > 0) {
            contentServer = new ContentServer("127.0.0.1", 4567, args[0]);
        } else {
            contentServer = new ContentServer("127.0.0.1", 4567, "");
        }
        String data = contentServer.readInput();
        contentServer.sendData(data);
    }
    
}
