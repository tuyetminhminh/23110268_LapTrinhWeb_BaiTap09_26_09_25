package vn.org.com.mail;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class SendMail {

    public static void send(String to, String subject, String msg) {
        // TODO: thay bằng App Password thật của Gmail (bắt buộc bật 2FA)
        final String from = "phamthituyetminh9b1920@gmail.com";
        final String pass = "dgogheyrwxhoxsat";

        Properties props = new Properties();
        // Gmail SMTP
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");     // STARTTLS
        props.put("mail.smtp.starttls.required", "true");   // yêu cầu STARTTLS

        // (không bắt buộc) timeout an toàn
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        // Tạo session có Authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        // (tuỳ chọn) bật log để debug khi gửi mail
        // session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            message.setSubject(subject, "UTF-8");
            // Dùng UTF-8 để hiển thị tiếng Việt
            message.setText(msg, "UTF-8"); // hoặc: message.setContent(msg, "text/plain; charset=UTF-8");

            Transport.send(message);
            System.out.println("Gửi email thành công tới: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
