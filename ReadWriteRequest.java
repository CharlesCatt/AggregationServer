// communicating with the Handler threads
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ReadWriteRequest implements Runnable {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private File file;
    private BufferedReader reader;
    public  int eventNo;


    public ReadWriteRequest(Socket s, File f, int en){
        socket = s;
        file = f;
        eventNo = en;
        try {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void run() {
        try {
            // inform handler that this thread is ready to read
            System.out.println("informing child process that its request is ready to be processed");
            dos.writeBoolean(true);
            String packet = dis.readUTF();
            String line = "";
            // implementation of GET
            if (packet.split(" ", 2).equals("GET")) {
                try {
                    reader = new BufferedReader(new FileReader(file.getName()));
                    while (!line.equals("</feed>")) {
                        line = reader.readLine();
                        dos.writeUTF(line);
                    }
                } catch (FileNotFoundException i) {
                    System.out.println("file not found while initialising reader");
                    System.out.println(i);
                    System.exit(-1);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }


}
