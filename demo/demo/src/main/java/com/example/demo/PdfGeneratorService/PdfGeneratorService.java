package com.example.demo.PdfGeneratorService;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DAO.FinalOrder;
import com.example.demo.DAO.OrderDetails;
import com.example.demo.DAO.Orders;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

@Service
public class PdfGeneratorService {

    public ByteArrayOutputStream generateOrderInvoice(List<OrderDetails> orderDetails) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        Document document = new Document(pdfDocument);

        document.add(new Paragraph("Order Receipt"));

        document.add(new Paragraph("Order ID: " + orderDetails.get(0).getOrderId()));

        document.add(new Paragraph("Customer Email: " + orderDetails.get(0).getEmail()));

        document.add(new Paragraph("Total Amount: " + orderDetails.get(0).getTotalAmount()));

        document.add(new Paragraph("Amount after 10 percent discount: " + orderDetails.get(0).getNetAmount()));

        document.add(new Paragraph(" "));

        float[] columnWidths = { 1, 1, 1 };

        Table table = new Table(columnWidths);

        table.addCell("Product Name");

        table.addCell("Quantity Ordered");

        table.addCell("Price Per Item");

        for (OrderDetails order : orderDetails) {
            table.addCell(order.getProductName());
            table.addCell(String.valueOf(order.getCountProducts()));
            table.addCell(order.getPricePerItem());
        }

        document.add(table);

        document.close();

        return (byteArrayOutputStream);
    }
}
