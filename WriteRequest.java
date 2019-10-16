// communicating with the Handler threads
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class WriteRequest implements Callable, FileRequest {
    public  String fileName;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String packet;
    private String contentServerName;
    public  int eventNo;


    public WriteRequest(String fn, String csn, String p, int en){
        fileName = fn;
        contentServerName = csn;
        packet = p;
        eventNo = en;
    }


    public String call() throws Exception {
        String feed = "";
        String line = "";
        String status = "200"; // default status code
        String newFile = null;
        File file;
        // implementation of PUT
        try {
            file = new File(fileName);
            if (!(file.exists() || file.isFile())) {
                file.createNewFile();
                status = "201 - HTTP_CREATED";
            }
            // get the contents of the file, then find the id of the ContentServer in that String
            reader = new BufferedReader(new FileReader(fileName));
            while (line != null) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                feed += line + "\n";
            }
            // new way: go through all <author> tags to check if the contentServerName is already logged as an author
            String findingAuthor = feed;
            String searched = "";
            String author = "";
            while (findingAuthor.indexOf("<author>") != -1) {
                author = findingAuthor.substring(findingAuthor.indexOf("<author>") + 8, findingAuthor.indexOf("</author>"));
                if (author.equals(contentServerName)) {
                    break;
                } else {
                    System.out.println(findingAuthor);
                    searched += findingAuthor.substring(0,findingAuthor.indexOf("</feed>") + 8);
                    findingAuthor = findingAuthor.substring(findingAuthor.indexOf("</feed>") + 8);
                }
            }
            if (findingAuthor.indexOf("<author>") != -1) {
                // overwrite existing entry
                newFile = searched + findingAuthor.substring(0, findingAuthor.indexOf("</author>")+10) + packet + findingAuthor.substring(findingAuthor.indexOf("</feed>") + 8);
            } else {
                // create new entry
                newFile = feed + "\n" + "<author>" + contentServerName + "</author>" + "\n" + packet;

            }
            reader.close();


            try {
                File tempFile = new File("temp.feed");
                if (!(tempFile.exists() || tempFile.isFile())) {
                    tempFile.createNewFile();
                }

                writer = new BufferedWriter(new FileWriter("temp.feed"));
                writer.write(newFile);
                writer.close();
                tempFile.renameTo(file);
            } catch (IOException e) {
                System.out.print(e);
                status = "500 internal server error, could not write to file";
            }


        } catch (FileNotFoundException i) {
            System.out.println("file not found while initialising reader");
            System.out.println(i);
            status = "500 - file went missing";
        } catch (IOException e) {
            System.out.println(e);
            status = "500 - internal server error";
        }
        
        // return the status of the operation
        return status;

    }


}
