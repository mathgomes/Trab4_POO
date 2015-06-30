package OnlineStore;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Classe runnable que cuida do cliente, recebe requisicoes e envia respostas
 */
class ClientThread implements Runnable {

    // Streams de entrada e saida de objects
    private ObjectOutputStream OutObject;
    private ObjectInputStream InObject;
    private Socket server;

    ClientThread(Socket server) throws IOException {
        this.server = server;
        OutObject = new ObjectOutputStream(server.getOutputStream());
        InObject = new ObjectInputStream(server.getInputStream());

    }
    public void run () {

        while(true) {

            try {
                // Recebe um object vindo do cliente
                Object input = InObject.readObject();
                // Converte o object para uma string
                String inputStr = (String) input;
                // Divide a string em tokens
                String[] token = inputStr.split(",");

                // Se a requisicao for de listar os itens
                if(Objects.equals(inputStr, "listItems")) {

                    // Mapeia o vetor de itens em uma lista
                    List<Product> products = Server.getInstance().getProducts().stream()
                            .map(Pair::getL)
                            .collect(Collectors.toList());
                    // Envia a lista para o cliente
                    OutObject.writeObject(products);

                }
                // Se a requisicao for de comprar um item
                else if(Objects.equals(token[1], "buyItem")) {

                        // Percorre a lista de produtos
                        for (Pair<Product, Integer> p : Server.getInstance().getProducts()) {

                            // Se encontrou o nome do producto procurado
                            if (Objects.equals(p.getL().getName(), token[0])) {
                                // Se ele esta esgotado
                                if (p.getR() <= 0) {
                                    // Envia a mensagem de volta
                                    String esgotado = "Item esgotado";
                                    OutObject.writeObject(esgotado);
                                    break;
                                    // Se ele esta em estoque
                                } else {

                                    // Envia o item de volta
                                    OutObject.writeObject(p.getL());
                                    // Diminue o estoque
                                    p.setR(p.getR() - 1);
                                    //Adiciona a venda ao vetor

                                    Server.getInstance().getCustomers().stream()
                                            .filter(client -> client.getSocket() == this.server)
                                            .map(Client::getID)
                                            .forEach(client -> Server.getInstance().getSales().add(new Sales(client, p.getL().getName(), new Date())));
                                    // Se o estoque acabou
                                    if (p.getR() <= 0) {
                                        p.getL().setAvailability(false);
                                        // notifique o fornecedor
                                        notifySupplier(p);
                                    }
                                    break;
                                }
                            }
                        }
                }
                // Se a requisicao for qualquer outra coisa, eh uma requisicao de login ou cadastro
                else {
                    // Procura na lsita de clientes aquele com o nome e senha enviados ao server
                    List<Client> ids = Server.getInstance().getCustomers().stream()
                            .filter(client -> Objects.equals(client.getID(), token[0]))
                            .filter(client -> Objects.equals(client.getPassword(), token[1]))
                            .collect(Collectors.toList());
                    // Se nao existir, cria um novo
                    if(ids.isEmpty()) {

                        // Se tentou fazer lpgin sem ter conta, retorne erro
                        if(token.length == 2) {
                            OutObject.writeObject("Error");
                        }
                        else {

                            // Cria um novo cliente, adiciona no vetor de clientes e envia uma resposta
                            Client c = new Client(token[0],token[1],token[2],token[3],token[4],token[5]);
                            c.setSocket(server);
                            Server.getInstance().getCustomers().add(c);
                            OutObject.writeObject("OK - Cadastrado");
                        }
                    }
                    else {

                        // Se tentou se cadastrar com um usuario que ja existia, retorne erro
                        if(token.length == 6) {
                            OutObject.writeObject("Error");
                        }
                        else {
                            // Se nao, retorne OK
                            ids.get(0).setSocket(server);
                            OutObject.writeObject("OK - Logado");
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    // Metodo que procura no vetor de fornecedores pelo fornecedor e chama o metodo supply para esse produto
    public void notifySupplier(Pair<Product,Integer> product) {

        Server.getInstance().getSuppliers().stream()
                .filter(supplier -> Objects.equals(product.getL().getSupplierName(), supplier.getName()))
                .forEach(supplier -> supplier.supply(product));
    }
}
