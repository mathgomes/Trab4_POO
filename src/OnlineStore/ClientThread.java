package OnlineStore;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Classe runnable que cuida do cliente, recebe requisicoes e envia respostas
 */
class ClientThread implements Runnable {

    // Streams de entrada e saida de objects
    private ObjectOutputStream OutObject;
    private ObjectInputStream InOnbject;

    ClientThread(Socket server) throws IOException {
        OutObject = new ObjectOutputStream(server.getOutputStream());
        InOnbject = new ObjectInputStream(server.getInputStream());

    }
    public void notifySupplier() {

        Server.getInstance().getProducts().stream()
                .filter(product -> product.getR() <= 0)
                .forEach(product -> Server.getInstance().getSuppliers().stream()
                        .filter(supplier -> Objects.equals(product.getL().getSupplierName(), supplier.getName()))
                        .forEach(supplier -> supplier.supply(product)));
    }

    public void run () {

        while(true) {

            try {
                // Recebe um object vindo do cliente
                Object input = InOnbject.readObject();
                System.out.println("Servidor recebeu " + input);
                String inputStr = (String) input;
                String[] token = inputStr.split(",");

                if(Objects.equals(inputStr, "listItems")) {

                    List<Product> products = Server.getInstance().getProducts().stream()
                            .map(Pair::getL)
                            .collect(Collectors.toList());
                    for( Pair<Product,Integer> p : Server.getInstance().getProducts()) {
                        System.out.println("Enviando "+ p.getL().getName() +","+ p.getL().getAvailability() +","+ p.getR()+"\n");
                    }
                    OutObject.writeObject(products);

                }
                else if(Objects.equals(token[1], "buyItem")) {
                    synchronized (this) {
                        for (Pair<Product, Integer> p : Server.getInstance().getProducts()) {

                            if (Objects.equals(p.getL().getName(), token[0])) {
                                if (p.getR() <= 0) {
                                    String esgotado = "Item esgotado";
                                    OutObject.writeObject(esgotado);
                                    notifySupplier();
                                    break;
                                } else {

                                    p.setR(p.getR() - 1);
                                    if (p.getR() <= 0) {
                                        p.getL().setAvailability(false);
                                    }
                                    System.out.println("Eviando " + p.getL().getName() + "," + p.getL().getAvailability());
                                    OutObject.writeObject(p.getL());
                                    notifySupplier();
                                    break;
                                }
                            }
                        }
                    }
                }
                else {

                    List ids = Server.getInstance().getCustomers().stream()
                            .filter(client -> Objects.equals(client.getID(), token[0]))
                            .filter(client -> Objects.equals(client.getPassword(), token[1]))
                            .collect(Collectors.toList());
                    if(ids.isEmpty()) {

                        if(token.length == 2) {
                            OutObject.writeObject("Error");
                        }
                        else {

                            Client c = new Client(token[0],token[1],token[2],token[3],token[4],token[5]);
                            Server.getInstance().getCustomers().add(c);
                            OutObject.writeObject("OK - Cadastrado");
                        }
                    }
                    else {

                        if(token.length == 6) {
                            OutObject.writeObject("Error");
                        }
                        else {
                            OutObject.writeObject("OK - Logado");
                        }
                    }
                    for( Client c : Server.getInstance().getCustomers()) {
                        System.out.println(c.getID());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
