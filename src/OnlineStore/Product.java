package OnlineStore;


import java.io.Serializable;

/**
 * Classe que representa um produto compravel
 */
public class Product implements Serializable{

    // Informacoes sobre o produto
    private String name;
    private String price;
    private String expirationDate;
    private String supplierName;
    private boolean Availability;


    public Product(String name, String price, String expirationDate, String supplierName) {

        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
        this.supplierName = supplierName;
        this.Availability = true;
    }


    // Getters e setters para o produto
    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
    public String getExpirationDate() {
        return expirationDate;
    }
    public String getAvailability() {
        return Availability ? "Disponivel" : "Indisponivel";
    }
    public void setAvailability(boolean availability) {
        Availability = availability;
    }
    public String getSupplierName() {
        return supplierName;
    }

}

