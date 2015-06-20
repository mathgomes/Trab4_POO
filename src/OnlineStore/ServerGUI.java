package OnlineStore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServerGUI extends Application{

    public ServerGUI() {}

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane serverPane = new GridPane();
        Button listAvailable = new Button("Listar Pordutos Disponiveis");
        Button listUnavailable = new Button("Listar Pordutos Nao Disponiveis");
        Button registerProduct = new Button("Registrar produto");
        Button logOut = new Button("LogOut");

        Label notifications = new Label();

        TextField field = new TextField();
        TextArea area = new TextArea();

        serverPane.setHgap(5);
        serverPane.setVgap(5);
        serverPane.setPadding(new Insets(10, 10, 10, 10));

        registerProduct.setMaxWidth(Double.MAX_VALUE);
        listAvailable.setMaxWidth(Double.MAX_VALUE);
        listUnavailable.setMaxWidth(Double.MAX_VALUE);

        registerProduct.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    notifications.setText("Digite as informacoes do produto que deseja cadastrar(nome,preco,validade,fornecedor)");
                    field.setOnAction(event -> {
                        String input = field.getText();
                        String[] token = input.split(",");
                        List<Product> products = Server.getInstance().getProducts().stream()
                                .map(Pair::getL)
                                .filter(product -> Objects.equals(product.getName(), token[0]))
                                .collect(Collectors.toList());

                        if(products.isEmpty()) {
                            Product p = new Product(token[0],token[1],token[2],token[3]);
                            Server.getInstance().getProducts().add(new Pair<>(p,1) );

                            List<Supplier> list = Server.getInstance().getSuppliers().stream()
                                    .filter(supplier -> Objects.equals(supplier.getName(), token[3]))
                                    .collect(Collectors.toList());

                            if(list.isEmpty()) {
                                Supplier s = new Supplier(token[3]);
                                Server.getInstance().getSuppliers().add(s);
                            }
                            notifications.setText("Produto cadastrado com sucesso");
                        }
                        field.setText("");

                    });
                }
        );
        listUnavailable.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    area.setText("");
                    area.setText("Produtos Indisponiveis\nNome, Preco, Fornecedor, Validade\n");
                    Server.getInstance().getProducts().stream()
                            .filter(p -> p.getR() <= 0)
                            .forEach(p -> area.appendText(
                                    p.getL().getName() + "," + p.getL().getPrice() + "," +
                                            p.getL().getSupplierName() + "," + p.getL().getExpirationDate() + "\n"));

                }

        );

        listAvailable.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    area.setText("");
                    area.setText("Produtos Disponiveis\nNome, Preco, Fornecedor, Validade\n");
                    Server.getInstance().getProducts().stream()
                            .filter(p -> p.getR() > 0)
                            .forEach(p -> area.appendText(
                                    p.getL().getName() + "," + p.getL().getPrice() + "," +
                                            p.getL().getSupplierName() + "," + p.getL().getExpirationDate() + p.getL().getAvailability()+","+p.getR()+"\n"));
                }
        );

        logOut.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    CSVManager.writeCsvFile("information.csv");
                    Platform.exit();
                }
        );


        serverPane.add(registerProduct,0,0);
        serverPane.add(listAvailable, 0, 1);
        serverPane.add(listUnavailable, 0, 2);
        serverPane.add(notifications, 0, 3);
        serverPane.add(field, 0, 4);
        serverPane.add(area, 0, 5);
        serverPane.add(logOut, 0, 6);

        primaryStage.setTitle("Area do Servidor");
        primaryStage.setScene(new Scene(serverPane, 620, 500));
        primaryStage.show();

    }


    public static void main(String []args) {

        CSVManager.readCsvFile("information.csv");
        new Thread(Server.getInstance()).start();
        launch(args);
    }

}
