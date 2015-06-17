package OnlineStore;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Classe runnable que cuida do cliente, recebendo dados enviados por ele e enviando para todos
 * os clientes de volta
 */
class ClientThread implements Runnable {

    // Streams de entrada e saida de objects
    private ObjectOutputStream OutObject;
    private ObjectInputStream InOnbject;

    ClientThread(Socket server) throws IOException {
        OutObject = new ObjectOutputStream(server.getOutputStream());
        InOnbject = new ObjectInputStream(server.getInputStream());
    }

    public void run () {

        while(true) {

            try {
                // Recebe o object que no caso é o vetor index
                Object input = InOnbject.readObject();
                System.out.println("Servidor recebeu " + input);
                String inputStr = (String) input;
                if(Objects.equals(inputStr, "listItems")) {

                }
                else if(Objects.equals(inputStr, "buyItem")) {

                }
                else {

                    String[] token = inputStr.split(",");
                    List ids = Server.getInstance().getCustomers().stream()
                            .filter(client -> Objects.equals(client.getID(), token[0]))
                            .filter(client -> Objects.equals(client.getPassword(), token[1]))
                            .collect(Collectors.toList());
                    if(ids.isEmpty()) {
                        if(token.length == 2) {
                            OutObject.writeObject("Error");
                        }
                        else {

                            Client c = new Client();
                            c.setAddress(token[5]);
                            c.setEmail(token[4]);
                            c.setID(token[0]);
                            c.setName(token[2]);
                            c.setPassword(token[1]);
                            c.setPhoneNumber(token[3]);
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
