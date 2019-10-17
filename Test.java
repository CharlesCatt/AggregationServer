import java.util.concurrent.*;

public class Test {
    public AggregationServer AS;

    public Test() {
        
        AS = new AggregationServer(4567);
        // ExecutorService  ex = Executors.newSingleThreadExecutor();
        Executors.newSingleThreadExecutor().execute(new Runnable(){
        
            @Override
            public void run() {
                AS.acceptConnections();
            }
        });
        System.out.println("accepting connections...");

    }

    public boolean testGET(GETClient client, String expectedOutput) {
        System.out.println("\nInitiating GET\n");
        try {
            String response = Executors.newSingleThreadExecutor().submit(new Callable<String>() {

                @Override
                public String call() {
                    String packet = client.readInput();
                    client.sendData(packet);
                    return client.getResponse();
                    
                }

            })
            .get();
            System.out.println("\ngot response from GET\n");
            if (expectedOutput == null) {
                System.out.println(response);
                return true;
            } else {
                return response.equals(expectedOutput);
            }

        } catch(InterruptedException | ExecutionException e) {
            System.out.println(e);
            return false;
        }
    }

    

    public boolean testPUT(ContentServer contentServer, String expectedOutput) {
        System.out.println("\nInitiating PUT\n");
        try {
            String response = Executors.newSingleThreadExecutor().submit(new Callable<String>() {
    
                @Override
                public String call() {
                    String packet = contentServer.readInput();
                    contentServer.sendData(packet);
                    return contentServer.getResponse();
                }
    
            })
            .get();
            System.out.println("\ngot response from PUT\n");
            if (expectedOutput == null) {
                System.out.println(response);
                return true;
            } else {
                return response.equals(expectedOutput);
            }

        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
            return false;
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        ContentServer contentServer = new ContentServer("127.0.0.1", 4567, "cs1_1");
        GETClient client = new GETClient("127.0.0.1", 4567, "GET");

        test.testPUT(contentServer, null);
        test.testGET(client, null);

    }

}