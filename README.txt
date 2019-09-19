###### DS assignment 2
##### Charles Catt
#### a1726075

How to use:
- Compile:
    javac -d classes AggregationServer.java ClientHandler.java GETClient.java ContentServer.java ReadRequest.java WriteRequest.java

- Start server:
    java -classpath classes AggregationServer

- Make requests: (in separate terminal)
    - PUT:
        java -classpath classes ContentServer
    - input:
        PUT
        name
        <feed>
        foo
        bar
        </feed>

    - GET:
        java -classpath classes GETClient
    - input:
        GET


Overview:
- AggregationServer hosts ServerSocket ready to take connections
- those connections are accepted by new ClientHandler threads
- the connected client (being a GETClient or ContentServer) send a request
    - in form of GET or PUT requests
- GET request:
    - ReadRequest (implements Callable) is submitted to the ExecutorService (residing in AggregationServer)
    - a Future is returned upon submission
    - upon initiation of the ReadRequest, it uses a BufferedReader to read each line of the file and append them in to a String
    - returns said String
    - this String is accessible with future.get() method, which halts the current thread while waiting for the result.
- PUT request:
    - WriteRequest (implements Callable) is submitted to the ExecutorService (residing in AggregationServer)
    - a Future is returned upon submission
    - upon initiation of the ReadRequest, it uses a BufferedReader to read each line of the file and append them in to a String
    - checks the String for other entries with the same name as the contentServer that made the request
    - appends to or overwrites sections of the String accordingly
    - writes the content of the String in to the file

Problems (and potential solutions):
- where to start....
- current implementation of ExecutorService does not support Lamport-Clock-based scheduling
    - can probably write a class that extends ExecutorService, what do you think? what do you recommend? please help !!
- writing to file does not produce expected result
    - the problem resides in lines 39-42 of WriteRequest.java
- currently using ad-hoc protocol for structuring requests
    - looking in to XML parsing
    - similarly, reading from file does not support ATOM structuring
- ATOM feed does not expire after 15 seconds
    - can probably have some sort of worker thread querying contents of the file and cross-checking times added and times modified with a table of contents
- cannot read from empty file in GET request
    - file creation system needs to be de-centralised from the AggregationServer and up to being created by the first WriteRequest, and refusing GET requests before this.
- Very little fault (crashing) tolerance
    - just needs more testing
