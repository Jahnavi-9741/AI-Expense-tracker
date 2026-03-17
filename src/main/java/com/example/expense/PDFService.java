package com.example.expense;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PDFService {

    public byte[] generateReport(List<Expense> expenses) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Expense Tracker Report"));
        document.add(new Paragraph(" "));

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
            document.add(new Paragraph(
                    "Title: " + expense.getTitle()
                            + " | Category: " + expense.getCategory()
                            + " | Amount: ₹" + expense.getAmount()
                            + " | Date: " + expense.getDate()
            ));
        }

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total Expense: ₹" + total));

        document.close();
        return outputStream.toByteArray();
    }
}