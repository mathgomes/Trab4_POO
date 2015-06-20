package OnlineStore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server implements Runnable{

    //create an object of Singleton Server
    private static Server server = new Server();
    // Vetor de runnables para cuidar de cada cliente
    private ArrayList<ClientThread> Clients = new ArrayList<>();

    private ArrayList<Client> Customers = new ArrayList<>();

    private ArrayList<Pair<Product,Integer>> Products = new ArrayList<>();

    private ArrayList<Supplier> Suppliers = new ArrayList<>();

    private ServerSocket serverSocket;

    private Server() {}

    public static Server getInstance() {
        return server;
    }

    public ArrayList<Client> getCustomers() {
        return this.Customers;
    }

    public ArrayList<Pair<Product,Integer>> getProducts() {
        return Products;
    }

    public ArrayList<Supplier> getSuppliers() {
        return Suppliers;
    }

    @Override
    public void run() {

        int portNumber = 12349;
        try {
            this.serverSocket = new ServerSocket(portNumber);
            System.out.println("Server started");
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
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

