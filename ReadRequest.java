// communicating with the Handler threads
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ReadRequest implements Callable, FileRequest {
    public String fileName;
    private BufferedReader reader;
    public int eventNo;


    public ReadRequest(String fn, int en){
        fileName = fn;
        eventNo = en;
    }

    public String call() throws Exception {
        String feed = "";
        String line = "";
        File file;
        try {
            file = new File(fileName);
            if (!file.exists()) {
                return "404 - file not found";
            }
            // implementation of GET
            // get the contents of the file
            reader = new BufferedReader(new FileReader(file.getName()));
            while (line != null) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                feed += line + "\n";
            }
            reader.close();
        } catch (FileNotFoundException i) {
            System.out.println("file not found while initialising reader");
            System.out.println(i);
            System.exit(-1);
        } catch (IOException e) {
            System.out.println(e);
        }
        // return the contents of the file, retrievable by using the future's get method.
        return feed;

    }


}
