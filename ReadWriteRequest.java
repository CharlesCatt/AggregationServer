// communicating with the Handler threads
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ReadWriteRequest implements Callable {
    private File file;
    public  int eventNo;
    private BufferedReader reader;


    public ReadWriteRequest(File f, int en){
        file = f;
        eventNo = en;
    }

    public Object call() {
        String feed = "";
        try {
            // implementation of GET
            if (packet.split(" ", 2).equals("GET")) {
                try {
                    reader = new BufferedReader(new FileReader(file.getName()));
                    while (!line.equals("</feed>")) {
                        line = reader.readLine();
                        feed += line;
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
        return feed;

    }


}
