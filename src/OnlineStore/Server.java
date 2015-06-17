package OnlineStore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    //create an object of Singleton Server
    private static Server server = new Server();
    // Endereço IP
    private int portNumber;
    // Vetor de runnables para cuidar de cada cliente
    private ArrayList<ClientThread> Clients = new ArrayList<>();
    private ArrayList<Client> Customers = new ArrayList<>();

    private ServerSocket serverSocket;

    private Server() {}

    public static Server getInstance() {
        return server;
    }

    public void communicate() throws IOException {
        this.portNumber = 12348;
        this.serverSocket = new ServerSocket(portNumber);
        System.out.println("Server started");
        try {

            while (true) {
                //wait for client to connect//
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                // Crie uma nova thread com esse objeto
                Clients.add(new ClientThread(socket));
                ClientThread clientThread = Clients.get(Clients.size()-1);
                Thread thread = new Thread(clientThread);
                thread.start();
                System.out.println("Thread Started");
            }
        } catch (IOException ioe) {
            //I/O error in ServerSocket//
            this.stopServer();
        }
    }

    public void stopServer() {
        try {
            serverSocket.close();
        }
        catch (IOException ioe) {
            //unable to close ServerSocket
        }
    }

    public ArrayList<Client> getCustomers() {
        return this.Customers;
    }

    public static void main(String []args) {

        try {
            Server s = Server.getInstance();
            s.communicate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

