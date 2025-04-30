import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    public static boolean sendEmail(String toEmail, String subject, String body) {
        System.out.println("Sending email to: " + toEmail);
        
        final String senderEmail = "";  // Replace with your Gmail
        final String senderPassword = "";  // Use App Password

//“First, I configure SMTP properties like host, port, and whether authentication and TLS are enabled. This helps JavaMail understand how to connect to the Gmail SMTP server.”

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

//Then I create a Session which represents the login environment. I pass the email ID and app password using Authenticator for authentication.

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

//I create a MimeMessage which contains the sender, recipient, subject, and message body. This forms the email content.
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println( "Email successfully sent to: " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.out.println(" Failed to send email: " + e.getMessage());
            e.printStackTrace();  // Print the full error message
            return false;
        }
    }
}

