package OnlineStore;

import java.io.*;
import java.net.Socket;

/**
 * Cliente que representa um consumidor que entra na loja
 */
public class Client {

    // Socket desse cliente
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    // Informacoes do cliente
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String ID;
    private String password;
    // Guarda as respostas vindas do servidor
    private static Object answer = null;

    public Client(){}
    public Client(String ID, String password, String name, String phoneNumber, String email, String address) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ID = ID;
        this.password = password;
    }

    // Getters e setter do cliente
    public static void setAnswer(Object answer) {Client.answer = answer;}
    public void setInputStream(ObjectInputStream inputStream) {this.inputStream = inputStream;}
    public ObjectInputStream getInputStream() {return inputStream;}
    public void setOutputStream(ObjectOutputStream outputStream) {this.outputStream = outputStream;}
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getID() {
        return this.ID;
    }
    public String getPassword() {
        return password;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public Socket getSocket() {
        return this.socket;
    }
    public ObjectOutputStream getOutputStream() {return outputStream;}
    public Object getAnswer() {return answer;}

    // Conecta no servidor
    public void connect(String IP) throws IOException {

        System.out.println("Client started!!");
        socket = new Socket(IP, 12344);

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
                    Client.setAnswer(inputStream.readObject());
                }
            } catch (Exception e) {
                System.out.println("Algo de errado aconteceu!! " + e.getMessage());
            }
        }
        ).start();
    }
}
