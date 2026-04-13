package com.project;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;

public class EmailService {

    public static void sendEmail(String to) {

        BasicAWSCredentials creds = new BasicAWSCredentials(
                "YOUR_ACCESS_KEY",
                "YOUR_SECRET_KEY"
        );

        AmazonSimpleEmailService ses = AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withRegion(Regions.AP_SOUTH_1) // Mumbai
                .build();

        SendEmailRequest request = new SendEmailRequest()
        	    .withSource("tejaswinip672@gmail.com")  // ✅ verified sender
        	    .withDestination(new Destination()
        	        .withToAddresses("patiltejaswini194@gmail.com"))  // ✅ verified receiver
        	    .withMessage(new Message()
        	        .withSubject(new Content("Payslip Generated"))
        	        .withBody(new Body()
        	            .withText(new Content("Your payslip is generated successfully.")))
        	    );

        	ses.sendEmail(request);
    }
}