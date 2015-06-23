package OnlineStore;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Sales {

    private String buyerName;
    private String productSold;
    private Calendar date;


    public Sales(String buyerName, String productSold, Date date) {
        this.buyerName = buyerName;
        this.productSold = productSold;
        this.date = Calendar.getInstance();
        this.date.setTime(date);
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getProductSold() {
        return productSold;
    }

    public Calendar getDate() {
        return date;
    }

    // Converte uma string lida em uma data
    public static Date convert(String c) {
        SimpleDateFormat format = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH);
        try {
            return format.parse(c);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
