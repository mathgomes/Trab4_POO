package OnlineStore;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class PDFGenerator {

    Document document;
    FileOutputStream output;

    public PDFGenerator() {
        this.document = new Document();
        try {
            this.output = new FileOutputStream("Sales.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void create(ArrayList<Sales> sales, String time, String desiredTime) {
        try {

            PdfWriter.getInstance(document,output);
            document.open();
            PdfPTable table = new PdfPTable(3);

            table.addCell("Comprador");
            table.addCell("Produto");
            table.addCell("Data");

            // Se o desejado eh o mes
            if(Objects.equals(time, "mes")) {
                sales.stream()
                        // Checa o mes do calendario + 1
                        .filter(s -> s.getDate().get(Calendar.MONTH) == Integer.parseInt(desiredTime))
                        .forEach(s -> {
                            table.addCell(s.getBuyerName());
                            table.addCell(s.getProductSold());
                            table.addCell(String.valueOf(s.getDate().getTime()));
                        });
            }
            else if (Objects.equals(time, "dia")) {
                sales.stream()
                        .filter(s -> s.getDate().get(Calendar.DAY_OF_MONTH) == Integer.parseInt(desiredTime))
                        .forEach(s -> {
                            table.addCell(s.getBuyerName());
                            table.addCell(s.getProductSold());
                            table.addCell(String.valueOf(s.getDate().getTime()));
                        });
            }
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        document.close();

    }
}
