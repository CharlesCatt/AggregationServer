// communicating with the Handler threads
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class WriteRequest implements Callable {
    private File file;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String packet;
    private String threadID;


    public WriteRequest(File f, String tID, String p){
        file = f;
        threadID = tID;
        packet = p;
    }


    public String call() throws Exception {
        String feed = "";
        String line = "";
        String status = "200";
        String newFile = null;
        try {
            // implementation of PUT
            try {
                // get the contents of the file, then find the id of the ContentServer in that String
                reader = new BufferedReader(new FileReader(file.getName()));
                while (!line.equals("</feed>")) {
                    line = reader.readLine();
                    feed += line + "\n";
                }

                if (feed.contains(threadID)) {
                    newFile = feed.substring(0,feed.indexOf(threadID) + 1) + packet + feed.substring(feed.substring(feed.indexOf(threadID)).indexOf("</feed>") + 1);
                } else {
                    newFile = threadID + feed + packet;
                }
                reader.close();
            } catch (FileNotFoundException i) {
                System.out.println("file not found while initialising reader");
                System.out.println(i);
                status = "404 file not found";
            }
        } catch (IOException e) {
            System.out.println(e);
            status = "500 internal server error";
        }
        try {
            writer = new BufferedWriter(new FileWriter(file.getName()));
            writer.write(newFile);
            writer.close();
        } catch (IOException e) {
            System.out.print(e);
            status = "500 internal server error, could not write to file";
        }
        // return the status of the operation
        return status;

    }


}
