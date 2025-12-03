package service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import model.Order;
import service.order.OrderService;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

public class ReportGeneratorService {
    private final OrderService orderService;
    private ReportDataService reportDataService;
    private Integer totalBooksSold;
    private Float totalMonthRevenue;

    public ReportGeneratorService(OrderService orderService){
        this.orderService = orderService;
        this.totalBooksSold = 0;
        this.totalMonthRevenue = 0.0f;
        this.reportDataService = new ReportDataService(orderService);
    }

    public void createPdf(){
        String dest = "activity_report.pdf";

        try{
            PdfWriter pdfWriter = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(pdfWriter);
            Document document = new Document(pdf);
            document.add(new Paragraph("Raport activitate"));
            createTable(document);
            generateStatistics(document);
            document.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void createTable(Document document){
        Table table = new Table(3);
        createHeader(table);
        createRows(table);
        document.add(table);
    }

    private void createHeader(Table table){
        String[] columnNames= {"Employee", "Book quantity", "Total price"};
        for(String name : columnNames){
            Cell cell = new Cell();
            cell.setBold();
            cell.add(new Paragraph(name));
            table.addHeaderCell(cell);
        }
    }

    private void createRows(Table table){
        Map<Long, Integer> booksEmployee = reportDataService.getBooksEmployee();
        Map<Long, Float> priceEmployee = reportDataService.getPriceEmployee();
        for(Map.Entry<Long, Integer> entry : booksEmployee.entrySet()){
            table.addCell(entry.getKey().toString());
            table.addCell(entry.getValue().toString());
            this.totalBooksSold+= entry.getValue();
            table.addCell(priceEmployee.get(entry.getKey()).toString());
            this.totalMonthRevenue+=priceEmployee.get(entry.getKey());
        }
    }

    private void generateStatistics(Document document){
        document.add(new Paragraph("Total books sold in 30 days:" + this.totalBooksSold));
        document.add(new Paragraph("Total revenue in 30 days:" + this.totalMonthRevenue));
    }
}
