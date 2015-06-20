package OnlineStore;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.*;

public class Client extends Application {


    // Socket desse cliente
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String ID;
    private String password;

    private static Object answer = null;
    private ArrayList<Product> shoppingCart = new ArrayList<>();

    public Client(){}
    public Client(String ID, String password, String name, String phoneNumber, String email, String address) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ID = ID;
        this.password = password;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getID() {
        return this.ID;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        System.out.println("Client started!!");
        socket = new Socket(Inet4Address.getLocalHost().getHostAddress(), 12349);
        System.out.println("Connection established!!");

        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        /* Thread para receber os inputs do servidor */
        new Thread(() -> {
            try {
                while (true) {
                    if (Thread.interrupted()) {
                        break;
                    }
                    // Le o vetor index recebido
                    answer = inputStream.readObject();
                    System.out.println("Cliente recebeu" + answer.getClass().getName());
                }
            } catch (Exception e) {
                System.out.println("Algo de errado aconteceu!! " + e.getMessage());
            }
        }
        ).start();

        GridPane loginPane = new GridPane();

        Label signIn = new Label("Ja é cadastrado ? Entre");
        Label ID = new Label("ID");
        Label password = new Label("Senha");
        TextField userField = new TextField();
        TextField passwordField = new TextField();
        Button log = new Button("Logar no sistema");

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

        Label notifications = new Label();

        loginPane.setHgap(5);
        loginPane.setVgap(5);

        GridPane.setHalignment(ID,HPos.CENTER);
        GridPane.setHalignment(password,HPos.CENTER);
        GridPane.setHalignment(newID,HPos.CENTER);
        GridPane.setHalignment(newpassword,HPos.CENTER);
        GridPane.setHalignment(name,HPos.CENTER);
        GridPane.setHalignment(phone,HPos.CENTER);
        GridPane.setHalignment(email,HPos.CENTER);
        GridPane.setHalignment(address,HPos.CENTER);
        GridPane.setHalignment(create,HPos.CENTER);

        log.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    try {
                        String data = userField.getText() + "," + passwordField.getText();
                        outputStream.writeObject(data);
                        Thread.sleep(200);
                        System.out.println("Enviado ao server" + data);
                    } catch (IOException | InterruptedException e1) {
                        e1.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    if (Objects.equals(answer, "Error")) {
                        notifications.setText("Usuario ou senha invalidos ");
                    } else if (Objects.equals(answer, "OK - Logado")) {
                        primaryStage.close();
                        new ShopStage();
                    }
                }
        );
        create.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    try {
                        String data = newuserField.getText() + "," + newpasswordField.getText() + "," +
                                nameField.getText() + "," + phoneField.getText() + "," + emailField.getText() +
                                "," + addressField.getText();
                        outputStream.writeObject(data);
                        Thread.sleep(200);
                        System.out.println("Enviado ao server " + data);
                    } catch (IOException | InterruptedException e1) {
                        e1.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    if (Objects.equals(answer,"Error")) {
                        notifications.setText("Usuario ja existe");
                    }
                    else if (Objects.equals(answer,"OK - Cadastrado")){
                        notifications.setText("Cadastro bem sucedido. Faca o login");
                    }
                }
        );

        loginPane.addColumn(0,signIn,ID,password);
        loginPane.add(userField,1,1);
        loginPane.add(passwordField,1,2);
        loginPane.add(log,0,3);

        loginPane.addColumn(2,createAccount,newID,newpassword,name,phone,email,address);
        loginPane.add(newuserField,3,1);
        loginPane.add(newpasswordField,3,2);
        loginPane.add(nameField,3,3);
        loginPane.add(phoneField,3,4);
        loginPane.add(emailField,3,5);
        loginPane.add(addressField,3,6);
        loginPane.add(create,3,7);

        loginPane.add(notifications,0,4,2,4);

        //loginPane.setGridLinesVisible(true);
        primaryStage.setTitle("Area de Login");
        primaryStage.setScene(new Scene(loginPane, 700, 400));
        primaryStage.show();

    }

    public class ShopStage extends Stage {

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


            listProducts.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                        area.setText("");
                        area.setText("Nome, Preco, Fornecedor, Validade\n");
                        try {
                            outputStream.writeObject("listItems");
                            Thread.sleep(400);
                        } catch (IOException | InterruptedException e1) {
                            e1.printStackTrace();
                        }

                        ArrayList<Product> list = (ArrayList<Product>) answer;
                        //System.out.println("Recebeu " + list.get(0).getName() + list.get(0).getAvailability()+"\n");

                        list.stream()
                                .forEach(p -> area.appendText(p.getName() + "," + p.getPrice() + "," +
                                        p.getSupplierName() + "," + p.getExpirationDate()+"\n"));


                    }
            );
            efetuarCompra.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                        notifications.setText("Digite o nome do produto que deseja comprar");
                        field.setOnAction(event -> {
                            try {
                                outputStream.writeObject(field.getText() + ",buyItem");
                                Thread.sleep(400);
                            } catch (IOException | InterruptedException e1) {
                                e1.printStackTrace();
                            }

                            if(answer instanceof String) {
                                notifications.setText("Item esgotado :(");
                            }
                            else {
                                Product p = (Product)answer;
                                System.out.println("Recebeu " + p.getName() + p.getAvailability()+"\n");
                                shoppingCart.add(p);
                                notifications.setText("Compra efetuada com sucesso");
                            }
                        });
                    }
            );
            logOut.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Platform.exit() );


            shopPane.add(listProducts,0,0);
            shopPane.add(efetuarCompra,0,1);
            shopPane.add(notifications,0,2);
            shopPane.add(field,0,3);
            shopPane.add(area,0,4);
            shopPane.add(logOut,0,5);

            this.setTitle("Area de Compras");
            this.setScene(new Scene(shopPane, 600, 500));
            this.show();

        }
    }

    public static void main(String []args) {
        launch(args);
    }
}
