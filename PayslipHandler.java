package com.project;

import java.util.List;
import java.util.Map;
import java.io.InputStream;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.regions.Regions;

public class PayslipHandler implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event event, Context context) {

        try {
            System.out.println("Lambda Triggered!");

            // 👉 Step 1: Get S3 file info
            String bucket = event.getRecords().get(0).getS3().getBucket().getName();
            String key = event.getRecords().get(0).getS3().getObject().getKey();

            System.out.println("Bucket: " + bucket);
            System.out.println("File: " + key);

            com.amazonaws.services.s3.AmazonS3 s3 =
                    com.amazonaws.services.s3.AmazonS3ClientBuilder.standard()
                    .withRegion(com.amazonaws.regions.Regions.AP_SOUTH_1)
                    .build();

            // ✅ Step 3: Download file from S3
            com.amazonaws.services.s3.model.S3Object object = s3.getObject(bucket, key);

            java.io.InputStream input = object.getObjectContent();
            // 👉 Step 2: Read Excel (you can reuse your method)
            List<Map<String, String>> employees = ExcelReader.readExcel(input);

            for (Map<String, String> emp : employees) {

            	double gross_total_stipend = parseDoubleSafe(emp.get("gross_total_stipend"));
            	double total_deduction = parseDoubleSafe(emp.get("total_deduction"));
            	double final_stipend = gross_total_stipend - total_deduction;

            	String html = "<html><body>Payslip</body></html>" 
                        .replace("{{name}}", safe(emp.get("name")))
                        .replace("{{employeecode}}", safe(emp.get("employeecode")))
                        .replace("{{client}}", safe(emp.get("client")))
                        .replace("{{dob}}", safe(emp.get("dob")))
                        .replace("{{totaldays}}", safe(emp.get("totaldays")))
                        .replace("{{dapartment}}", safe(emp.get("dapartment")))
                        .replace("{{paydays}}", safe(emp.get("hra")))
                        .replace("{{gmail}}", safe(emp.get("gmail")))
                        .replace("{{stipendrate}}", safe(emp.get("stipendrate")))
                        .replace("{{total_stipend}}", safe(emp.get("total_stipend")))
                        .replace("{{total_ot_amount_dec&_jan}}", safe(emp.get("total_ot_amount_dec&_jan")))
                        .replace("{{attendance_bonus}}", safe(emp.get("attendance_bonus")))
                        .replace("{{gross_total_stipend}}",safe( emp.get("gross_total_stipend")))
                        .replace("{{deduction}}", safe(emp.get("deduction")))
                        .replace("{{total_deduction}}", safe(emp.get("total_deduction")))
                        .replace("{{final_stipend}}", String.valueOf("final_stipend"));



                // 👉 Step 3: Generate PDF
                PDFGenerator.generatePDF(html, emp.get("name"));

                // 👉 Step 4: Send Email
                EmailService.sendEmail(emp.get("email"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Success";
    }

    private static double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}