// communicating with the Handler threads
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ReadRequest implements Callable {
    private File file;
    private BufferedReader reader;


    public ReadRequest(File f){
        file = f;
    }

    public String call() throws Exception {
        String feed = "";
        String line = "";
        try {
            // implementation of GET
            try {
                // get the contents of the file
                reader = new BufferedReader(new FileReader(file.getName()));
                while (!line.equals("</feed>")) {
                    line = reader.readLine();
                    feed += line + "\n";
                }
            } catch (FileNotFoundException i) {
                System.out.println("file not found while initialising reader");
                System.out.println(i);
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        // return the contents of the file, retrievable by using the future's get method.
        return feed;

    }


}
