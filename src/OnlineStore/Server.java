package OnlineStore;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Representa o servidor da loja.
 * Onde estão localizados todos os dados guardados.
 */
public class Server implements Runnable{

    //Cria um objeto do servidor singleton
    private static Server server = new Server();
    // Vetor de runnables para cuidar de cada cliente
    private ArrayList<ClientThread> Clients = new ArrayList<>();
    // Vetor de Clientes cadastrados
    private ArrayList<Client> Customers = new ArrayList<>();
    // Vetor de Produtos e a quantidade de cada um
    private ArrayList<Pair<Product,Integer>> Products = new ArrayList<>();
    // Vetor de fornecedores
    private ArrayList<Supplier> Suppliers = new ArrayList<>();
    // Vetor de vendas
    private ArrayList<Sales> Sales = new ArrayList<>();

    private ServerSocket serverSocket;

    private Server() {}

    // Getters para os vetores
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
    public ArrayList<Sales> getSales() {
        return Sales;
    }
    public ServerSocket getSocket() {
        return this.serverSocket;
    }

    @Override
    public void run() {

        int portNumber = 12344;
        try {

            this.serverSocket = new ServerSocket(portNumber);
            System.out.println("Server started");
            while (true) {
                // Espera uma conexao
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                // Crie uma nova thread para cuidar desse cliente
                Clients.add(new ClientThread(socket));
                ClientThread clientThread = Clients.get(Clients.size()-1);
                Thread thread = new Thread(clientThread);
                thread.start();
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

