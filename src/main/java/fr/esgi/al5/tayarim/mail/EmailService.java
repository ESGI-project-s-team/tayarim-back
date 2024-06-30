package fr.esgi.al5.tayarim.mail;
import org.apache.commons.mail.EmailAttachment;
import org.springframework.stereotype.Service;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import java.io.File;

@Service
public class EmailService {
  //MailerClient mailerClient;

  /*
  public EmailService(MailerClient mailerClient) {
    this.mailerClient = mailerClient;
  }

  public void sendEmail() {
    //String cid = "1234";
    Email email = new Email()
        .setSubject("Testing email")
        .setFrom("senderName FROM <mailtrap@demomailtrap.com>")
        .addTo("receiverName TO <yaron1220@gmail.com>")
        // adds attachment
        //.addAttachment("attachment.pdf", new File("/some/path/attachment.pdf"))
        // adds inline attachment from byte array
        //.addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE)
        // adds cid attachment
        //.addAttachment("image.jpg", new File("/some/path/image.jpg"), cid)
        // sends text, HTML or both...
        .setBodyText("This is a testMessage");
        //.setBodyHtml("<html><body><p>An <b>html</b> message with cid <img src=\"cid:" + cid + "\"></p></body></html>");
    mailerClient.send(email);
  }

   */

  public void sendEmail() {
    OkHttpClient client = new OkHttpClient();
        .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{\"from\":{\"email\":\"mailtrap@demomailtrap.com\",\"name\":\"Mailtrap Test\"},\"to\":[{\"email\":\"yaron1220@gmail.com\"}],\"subject\":\"You are awesome!\",\"text\":\"Congrats for sending test email with Mailtrap!\",\"category\":\"Integration Test\"}");
    Request request = new Request.Builder()
        .url("https://send.api.mailtrap.io/api/send")
        .method("POST", body)
        .addHeader("Authorization", "Bearer ********c440")
        .addHeader("Content-Type", "application/json")
        .build();
    Response response = client.newCall(request).execute();
}