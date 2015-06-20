package OnlineStore;


import java.io.Serializable;

public class Product implements Serializable{

    private String name;
    private String price;
    private String expirationDate;
    private String supplierName;
    //private transient Supplier observer;
    private boolean Availability;


    public Product(String name, String price, String expirationDate, String supplierName) {

        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
        this.supplierName = supplierName;
        this.Availability = true;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
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

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplier) {
        this.supplierName = supplier;
    }

}

