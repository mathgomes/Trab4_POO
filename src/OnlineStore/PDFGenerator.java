package OnlineStore;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * Classe responsavel por guardar as informaçoes sobre a geracao de um pdf com dados fornecidos
 */
public class PDFGenerator {

    private Document document;
    private FileOutputStream output;

    public PDFGenerator() {
        this.document = new Document();
        try {
            this.output = new FileOutputStream("Sales.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recebe os dados e , com eles, cria um pdf.
     * @param sales Vetor de vendas
     * @param time Palavra chave representando "mes" ou "dia"
     * @param desiredTime Mes ou dia desejado
     */
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
                Paragraph para1 = new Paragraph();
                para1.add("Vendas do mes " + desiredTime);
                document.add(para1);
                sales.stream()
                        // Checa o mes do calendario
                        .filter(s -> s.getDate().get(Calendar.MONTH) == Integer.parseInt(desiredTime))
                        .forEach(s -> {
                            table.addCell(s.getBuyerName());
                            table.addCell(s.getProductSold());
                            table.addCell(String.valueOf(s.getDate().getTime()));
                        });
            }
            // A mesma coisa eh feita para o dia
            else if (Objects.equals(time, "dia")) {
                Paragraph para1 = new Paragraph();
                para1.add("Vendas do dia " + desiredTime);
                document.add(para1);
                document.addTitle("Vendas do dia "+ desiredTime);
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
