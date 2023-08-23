import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket server;
    /* Create a synchronized set to ensure the threads non-colliding operations.
       Contains all connected users to the server, each active thread corresponds to connected user.*/
    private Set<Handler> users = Collections.synchronizedSet(new HashSet<>());

    Server(int port) {
        // make the server
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        ExecutorService es = Executors.newFixedThreadPool(1000);
        System.out.println("Server running (Ctrl+C) to quit!");

        // wait for incoming connections
        while (true) {
            try {
                // accept() the client connection
                Socket sockClient = server.accept();
                // Handler class create a thread
                Handler handler = new Handler(sockClient, users);
                users.add(handler);
                // assign the socket(handler object) to a thread
                es.execute(handler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


