package oop.ryhmatoo;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;

public class MeiliSaatja {


    public static void saadaMeeldetuletus(String saajad) throws MessagingException {

        String saatja = "jutatime.jouluvana@gmail.com";
        String pass = "";

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(saatja, pass);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("jutatime.jouluvana@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(saajad));
        message.setSubject("Uuenda enda andmeid!");

        String msg = "On saabunud uus kuu! Uuenda enda kulutaste andmeid meie rakenduses, et enda tulemusi teistega võrrelda";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
