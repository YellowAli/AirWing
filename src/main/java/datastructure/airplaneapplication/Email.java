package datastructure.airplaneapplication;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Email
{

    final static String email = "aliElgalad@outlook.com";
    final static String password = "Mr.SpanishHost1991";
    public static void sendEmail(String toEmail, String confirmationCode)
    {
        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.outlook.com");
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");


        javax.mail.Authenticator authenticator1 = new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email,password);
            }
        };

        Session session = Session.getInstance(properties,authenticator1);

        try
        {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("aliElgalad@outlook.com", "NoReply-JD"));


            msg.setSubject("Confirmation code for AirPlane Application", "UTF-8");

            msg.setText("Your Confirmation Code is the following:"+confirmationCode, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
