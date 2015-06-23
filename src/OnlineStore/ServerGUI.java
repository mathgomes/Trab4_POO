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

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Classe que representa a parte grafica do servidor
 */
public class ServerGUI extends Application{

    // Para habilitar o uso fora da classe
    public static Label notifications;
    public ServerGUI() {}

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane serverPane = new GridPane();
        Button listAvailable = new Button("Listar Pordutos Disponiveis");
        Button listUnavailable = new Button("Listar Pordutos Nao Disponiveis");
        Button registerProduct = new Button("Registrar produto");
        Button generateSales = new Button ("Gerar PDF de vendas");
        Button logOut = new Button("LogOut");

        notifications = new Label();

        TextField field = new TextField();
        TextArea area = new TextArea();

        serverPane.setHgap(5);
        serverPane.setVgap(5);
        serverPane.setPadding(new Insets(10, 10, 10, 10));

        registerProduct.setMaxWidth(Double.MAX_VALUE);
        listAvailable.setMaxWidth(Double.MAX_VALUE);
        listUnavailable.setMaxWidth(Double.MAX_VALUE);

        // Evento acionado ao se clicar no botao de registrar um produto
        registerProduct.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    notifications.setText("Digite as informacoes do produto que deseja cadastrar(nome,preco,validade,fornecedor)");
                    field.setOnAction(event -> {
                        // Recebe o que foi digitado do textField
                        String input = field.getText();
                        // Divide em tokens
                        String[] token = input.split(",");
                        // Filtra o vetor de itens para o item com o nome digitado
                        List<Product> products = Server.getInstance().getProducts().stream()
                                .map(Pair::getL)
                                .filter(product -> Objects.equals(product.getName(), token[0]))
                                .collect(Collectors.toList());
                        // Se nao existir esse item no vetor
                        if(products.isEmpty()) {
                            // Cria um novo item
                            Product p = new Product(token[0],token[1],token[2],token[3]);
                            // Adiciona-o ao estoque
                            Server.getInstance().getProducts().add(new Pair<>(p,1) );

                            // Filtra o vetor de fornecedores para o fornecedor com o nome digitado
                            List<Supplier> list = Server.getInstance().getSuppliers().stream()
                                    .filter(supplier -> Objects.equals(supplier.getName(), token[3]))
                                    .collect(Collectors.toList());
                            // Se nao existir esse fornecedor
                            if(list.isEmpty()) {
                                // Cria um novo fornecedor e adiciona-o a lista de fornecedores
                                Supplier s = new Supplier(token[3]);
                                Server.getInstance().getSuppliers().add(s);
                            }
                            else {
                                notifications.setText("Produto ja existe");
                            }
                            notifications.setText("Produto cadastrado com sucesso");
                        }
                        field.setText("");

                    });
                }
        );
        // Evento acionado ao se clicar para listar os itens nao disponiveis no servidor
        listUnavailable.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    area.setText("");
                    area.setText("Produtos Indisponiveis\nNome, Preco, Fornecedor, Validade\n");
                    // Filtra o estoque para os itens nao disponiveis
                    Server.getInstance().getProducts().stream()
                            .filter(p -> p.getR() <= 0)
                            // Imprime esses itens na textArea
                            .forEach(p -> area.appendText(
                                    p.getL().getName() + "," + p.getL().getPrice() + "," +
                                            p.getL().getSupplierName() + "," + p.getL().getExpirationDate() + "\n"));

                }

        );
        // Evento acionado ao se clicar para listar os itens disponiveis no servidor
        listAvailable.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    area.setText("");
                    area.setText("Produtos Disponiveis\nNome, Preco, Fornecedor, Validade, Disponibilidade, Quantidade\n");
                    // Filtra o estoque para os itens disponiveis
                    Server.getInstance().getProducts().stream()
                            .filter(p -> p.getR() > 0)
                            // Imprime esses itens na textArea
                            .forEach(p -> area.appendText(
                                    p.getL().getName() + "," + p.getL().getPrice() + "," +
                                            p.getL().getSupplierName() + "," + p.getL().getExpirationDate() + ", "+
                                            p.getL().getAvailability()+","+p.getR()+"\n") );
                }
        );

        generateSales.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    notifications.setText("Digite o dia ou o mes na forma " + "dia,<dia> ou mes,<mes> em numeros");
                    field.setOnAction(event -> {

                        // Recebe o que foi digitado do textField

                        String input = field.getText();
                        System.out.println(input);
                        // Divide em tokens
                        String input2 = input.replaceAll(" ",",");
                        String[] token = input2.split(",");
                        area.setText("");

                        PDFGenerator generator = new PDFGenerator();
                        generator.create(Server.getInstance().getSales(),token[0],token[1]);
                        field.setText("");
                        notifications.setText("Arquivo de vendas PDF gerado con sucesso");
                    });
                }
        );
        // Evento acionado o se clicar para sair da aplicacao
        logOut.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    // Salva as informacoes do arquivo
                    CSVManager.writeCsvFile("information.csv");
                    Platform.exit();
                }
        );

        // Adiciona os componentes ao painel base
        serverPane.add(registerProduct,0,0);
        serverPane.add(listAvailable, 0, 1);
        serverPane.add(listUnavailable, 0, 2);
        serverPane.add(generateSales,0, 3);
        serverPane.add(notifications, 0, 4);
        serverPane.add(field, 0, 5);
        serverPane.add(area, 0, 6);
        serverPane.add(logOut, 0, 7);

        primaryStage.setTitle("Area do Servidor");
        primaryStage.setScene(new Scene(serverPane, 620, 500));
        primaryStage.show();

    }


    public static void main(String []args) {

        // Ao se iniciar o servidor, le as informacoes do arquivo
        CSVManager.readCsvFile("information.csv");
        new Thread(Server.getInstance()).start();
        launch(args);
    }

}
