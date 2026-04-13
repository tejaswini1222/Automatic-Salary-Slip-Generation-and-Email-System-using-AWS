package com.project;

import java.io.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;

public class PDFGenerator {

    public static File generatePDF(String html, String name) throws Exception {

        File file = new File(name + ".pdf");

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        doc.add(new Paragraph(html));
        doc.close();

        return file;
    }
}
