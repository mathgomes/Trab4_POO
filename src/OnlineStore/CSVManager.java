package OnlineStore;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class CSVManager {
    /**
     * Le o arquivo no formato CSV e o divide em strings de acordo com cada tipo
     * @param fileName, nome do arquivo a ser lido
     */
    public static void readCsvFile(String fileName) {

        BufferedReader fileReader = null;
        try {
            // String para armazenar uma linha lida
            String line;

            //Cria um fileReadear
            fileReader = new BufferedReader(new FileReader(fileName));
                // Le o header de quatidades
                fileReader.readLine();

                // Le o arquivo linha por linha a partir da segunda
                while ((line = fileReader.readLine()) != null) {
                    //Pega todos os pedaços da linha separados por virgula
                    String[] token = line.split(",");
                    // Se tiver algo pra ler
                    if (token.length > 0) {
                        switch(token[0]) {
                            // Se for um prduto
                            case "Product":
                                // Adiciona um novo produto
                                Server.getInstance().getProducts().add(
                                        new Pair<>(new Product(token[1], token[2], token[3], token[4]), Integer.parseInt(token[5]))
                                );
                                break;
                                // O mesmo para o cliente, fornecedor e vendas
                            case "Client":
                                Client c = new Client(token[1],token[2],token[3],token[4],token[5],token[6]);
                                Server.getInstance().getCustomers().add(c);
                                break;
                            case "Supplier":
                                Server.getInstance().getSuppliers().add(new Supplier(token[1]));
                                break;
                            case "Sale":
                                Server.getInstance().getSales().add(new Sales(token[1],token[2],Sales.convert(token[3])) );
                                break;
                        }
                    }
                }
        }
        catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            // Fecha o fileReader
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Escreve no arquivo no formato CSV, respeitando as restricoes para esse formato
     * @param fileName, nome do arquivo onde sera escrito
     */
    public static void writeCsvFile(String fileName) {

        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter(fileName);
            fileWriter.append("(client ou product ou supplier),(userID ou productName ou supplierName),(userPassword ou productPrice),(userName ou expirationDate)," +
                    "(phoneNumber ou productSupplier),(userEmail ou productQtd),(userAddress)\n");
            // Escreve todas as informacoes pertinentes dos produtos, clientes, fornecedores e vendas
            for(Pair<Product,Integer> product : Server.getInstance().getProducts()) {
                fileWriter.append("Product");
                fileWriter.append(",");
                fileWriter.append(product.getL().getName());
                fileWriter.append(",");
                fileWriter.append(product.getL().getPrice());
                fileWriter.append(",");
                fileWriter.append(product.getL().getExpirationDate());
                fileWriter.append(",");
                fileWriter.append(product.getL().getSupplierName());
                fileWriter.append(",");
                fileWriter.append(product.getR().toString());
                fileWriter.append("\n");
            }
            for(Client client : Server.getInstance().getCustomers()) {
                fileWriter.append("Client");
                fileWriter.append(",");
                fileWriter.append(client.getID());
                fileWriter.append(",");
                fileWriter.append(client.getPassword());
                fileWriter.append(",");
                fileWriter.append(client.getName());
                fileWriter.append(",");
                fileWriter.append(client.getPhoneNumber());
                fileWriter.append(",");
                fileWriter.append(client.getEmail());
                fileWriter.append(",");
                fileWriter.append(client.getAddress());
                fileWriter.append("\n");
            }
            for(Supplier supplier : Server.getInstance().getSuppliers()) {
                fileWriter.append("Supplier");
                fileWriter.append(",");
                fileWriter.append(supplier.getName());
                fileWriter.append("\n");
            }
            for(Sales sale : Server.getInstance().getSales()) {
                fileWriter.append("Sale");
                fileWriter.append(",");
                fileWriter.append(sale.getBuyerName());
                fileWriter.append(",");
                fileWriter.append(sale.getProductSold());
                fileWriter.append(",");
                fileWriter.append(String.valueOf(sale.getDate().getTime()));
                fileWriter.append("\n");
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }
}
