package com.example.demo.PdfGeneratorService;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DAO.FinalOrder;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

@Service
public class PdfGeneratorService {

    public ByteArrayOutputStream generateOrderInvoice(FinalOrder finalOrder, List<?> orders) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        Document document = new Document(pdfDocument);

        document.add(new Paragraph("Order Receipt"));

        document.add(new Paragraph("Order ID: " + finalOrder.getOrderId()));

        document.add(new Paragraph("Customer Email: " + finalOrder.getEmail()));

        document.add(new Paragraph("Total Amount: " + finalOrder.getTotalAmount()));

        float[] columnWidths = { 1, 1, 1 };

        Table table = new Table(columnWidths);

        table.addCell("Product Name");

        table.addCell("Quantity");

        return (byteArrayOutputStream);
    }
}
