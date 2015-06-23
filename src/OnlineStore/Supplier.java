package OnlineStore;

import javafx.application.Platform;

import java.util.Random;

/**
 * Classe que representa um fornecedor
 */
public class Supplier {

    // Nome do fornecedor
    private String name;


    public Supplier(String name) {
        this.name = name;
    }


    /**
     * Metodo acrescenta uma quantidade aleatoria de determinado item ao servidor
     * @param p Um par produto/quantidade
     */
    public synchronized void supply(Pair<Product,Integer> p) {

        new Thread(() -> {
            try {

                System.out.println("aguardando estoque de "+ p.getL().getName());
                // Espera o "tempo de transporte"
                Thread.sleep(new Random().nextInt(20000) + 10000);
                // Acrescenta a quantidade do item, aleatorio entre 1 e 5
                p.setR(p.getR() + (new Random().nextInt(5) + 1));
                // O item agora esta disponivel
                p.getL().setAvailability(true);
                Platform.runLater(() -> ServerGUI.notifications.setText("produto "+ p.getL().getName() +" restocado em "+ p.getR()+" unidades"));

            } catch (Exception e) {
                System.out.println("Algo de errado aconteceu!! " + e.getMessage());
            }
        }
        ).start();

    }

    public String getName() {
        return name;
    }
}
