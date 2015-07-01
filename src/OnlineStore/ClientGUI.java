package OnlineStore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Classe que representa a parte grafica da interface do cliente
 */
public class ClientGUI extends Application{

    private Client client;
    // Notificacoes na tela
    private Label notifications;

    public ClientGUI() {
    }


    @Override
    public void start(Stage primaryStage) throws Exception {


        // Painel base para a tela de login
        GridPane loginPane = new GridPane();

        // Cria todos os botoes,labels, textAreas e fields
        Label signIn = new Label("Ja é cadastrado ? Entre");
        Label ID = new Label("ID");
        Label password = new Label("Senha");
        TextField userField = new TextField();
        TextField passwordField = new TextField();
        Button log = new Button("Logar no sistema");
        TextField IPfield = new TextField("Digite o IP aqui");
        Label conectLabel = new Label("Digite o ip em que deseja se conectar e aperte enter");

        Label createAccount = new Label("Nao é ? Crie sua conta");
        Label newID = new Label("ID");
        Label newpassword = new Label("Senha");
        Label name = new Label("Nome");
        Label phone = new Label("Telefone");
        Label email = new Label("Email");
        Label address = new Label("Endereco");
        TextField newuserField = new TextField();
        TextField newpasswordField = new TextField();
        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        TextField addressField = new TextField();
        Button create = new Button("Criar Conta");

        notifications = new Label();

        loginPane.setHgap(5);
        loginPane.setVgap(5);

        //Centraliza os botoes
        GridPane.setHalignment(ID, HPos.CENTER);
        GridPane.setHalignment(password,HPos.CENTER);
        GridPane.setHalignment(newID,HPos.CENTER);
        GridPane.setHalignment(newpassword,HPos.CENTER);
        GridPane.setHalignment(name,HPos.CENTER);
        GridPane.setHalignment(phone,HPos.CENTER);
        GridPane.setHalignment(email,HPos.CENTER);
        GridPane.setHalignment(address,HPos.CENTER);
        GridPane.setHalignment(create,HPos.CENTER);

        // Evento acionado ao se clicar para fazer login
        log.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    try {
                        // Recebe os inputs digitados nos campos
                        String data = userField.getText() + "," + passwordField.getText();
                        // Envia ao servidor
                        client.getOutputStream().writeObject(data);
                        // Tempo para o servidor enviar uma resposta
                        Thread.sleep(200);

                    } catch (IOException | InterruptedException e1) {
                        e1.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    // Se a resposta do servidor for erro
                    if (Objects.equals(client.getAnswer(), "Error")) {
                        notifications.setText("Usuario ou senha invalidos ");
                        // Se for OK, feche a tela de login e abra a tela de compras
                    } else if (Objects.equals(client.getAnswer(), "OK - Logado")) {
                        primaryStage.close();
                        new ShopStage();
                    }
                }
        );
        // Evento acionado ao se clicar no botao de criar uma nova conta
        create.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    try {
                        // Recebe os inputs digitados nos campos
                        String data = newuserField.getText() + "," + newpasswordField.getText() + "," +
                                nameField.getText() + "," + phoneField.getText() + "," + emailField.getText() +
                                "," + addressField.getText();
                        // Envia ao servidor
                        client.getOutputStream().writeObject(data);
                        // Tempo para o servidor enviar uma resposta
                        Thread.sleep(200);
                        System.out.println("Enviado ao server " + data);
                    } catch (IOException | InterruptedException e1) {
                        e1.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    // Se a resposta do servidor for erro
                    if (Objects.equals(client.getAnswer(),"Error")) {
                        notifications.setText("Usuario ja existe");
                    }
                    // Se for OK, reporte e dirija-se ao login
                    else if (Objects.equals(client.getAnswer(),"OK - Cadastrado")){
                        notifications.setText("Cadastro bem sucedido. Faca o login");
                    }
                }
        );

        // Evento acionado ao se apertar enter apos digitar o IP para conectar
        IPfield.setOnAction(event -> {
            String data = IPfield.getText();
            client = new Client();
            try {
                client.connect(data);
                conectLabel.setText("Conexao bem sucedida, faca login ou crie uma conta");
                IPfield.setText(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        // Adiciona todos os componentes no painel base
        loginPane.addColumn(0,signIn,ID,password);
        loginPane.add(userField,1,1);
        loginPane.add(passwordField,1,2);
        loginPane.add(log,0,3);
        loginPane.add(notifications,0,4,2,4);
        loginPane.add(IPfield,0,5);
        loginPane.add(conectLabel,0,6,2,6);

        loginPane.addColumn(2,createAccount,newID,newpassword,name,phone,email,address);
        loginPane.add(newuserField,3,1);
        loginPane.add(newpasswordField,3,2);
        loginPane.add(nameField,3,3);
        loginPane.add(phoneField,3,4);
        loginPane.add(emailField,3,5);
        loginPane.add(addressField,3,6);
        loginPane.add(create,3,7);


        primaryStage.setTitle("Area de Login");
        primaryStage.setScene(new Scene(loginPane, 700, 400));
        primaryStage.show();

    }

    /**
     *  Classe interna que representa a interface grafica da parte de compras de produtos
     */
    public class ShopStage extends Stage {


        // Cria todos os botoes,labels, textAreas e fields
        GridPane shopPane = new GridPane();
        Button listProducts = new Button("Listar Pordutos");
        Button efetuarCompra = new Button("Efetuar Compra");
        Button logOut = new Button("LogOut");

        Label notifications = new Label();

        TextField field = new TextField();
        TextArea area = new TextArea();



        public ShopStage(){

            shopPane.setHgap(5);
            shopPane.setVgap(5);
            shopPane.setPadding(new Insets(10, 10, 10, 10));

            listProducts.setMaxWidth(Double.MAX_VALUE);
            efetuarCompra.setMaxWidth(Double.MAX_VALUE);

            // Evento acionado ao se clicar no botão de listar produtos
            listProducts.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                        area.setText("");
                        area.setText("Nome, Preco, Fornecedor, Validade\n");
                        try {
                            // Envia a requisicao para listar os itens
                            client.getOutputStream().writeObject("listItems");
                            // Espera a resposta do servidor
                            Thread.sleep(400);
                        } catch (IOException | InterruptedException e1) {
                            e1.printStackTrace();
                        }

                        // Recebe a resposta do servidor numa lista de itens
                        ArrayList<Product> list = (ArrayList<Product>) client.getAnswer();

                        // Mostra os itens na TextArea
                        list.stream()
                                .forEach(p -> area.appendText(p.getName() + "," + p.getPrice() + "," +
                                        p.getSupplierName() + "," + p.getExpirationDate()+"\n"));


                    }
            );
            // Evento acionado ao se clicar no botão de efetuar uma compra
            efetuarCompra.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                        notifications.setText("Digite o nome do produto que deseja comprar");
                        field.setOnAction(event -> {
                            try {
                                // Envia a nome do item, mais a requisicao de comprar item
                                client.getOutputStream().writeObject(field.getText() + ",buyItem");
                                // Espera a resposta do servidor
                                Thread.sleep(400);
                            } catch (IOException | InterruptedException e1) {
                                e1.printStackTrace();
                            }

                            // Se a resposta for uma string, o item esta esgotado
                            if(client.getAnswer() instanceof String) {
                                notifications.setText("Item esgotado :(");
                            }
                            // Se nao, reporte o sucesso
                            else {
                                Product p = (Product)client.getAnswer();
                                notifications.setText("Compra efetuada com sucesso");
                            }
                        });
                    }
            );
            // Evento acionado ao se clicar no botao de logOut
            logOut.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Platform.exit());


            // Adiciona os componentes ao painel base
            shopPane.add(listProducts,0,0);
            shopPane.add(efetuarCompra,0,1);
            shopPane.add(notifications,0,2);
            shopPane.add(field,0,3);
            shopPane.add(area,0,4);
            shopPane.add(logOut,0,5);
            // mostra a cena
            this.setTitle("Area de Compras");
            this.setScene(new Scene(shopPane, 600, 500));
            this.show();

        }
    }

    public static void main(String []args) {
        launch(args);
    }
}
