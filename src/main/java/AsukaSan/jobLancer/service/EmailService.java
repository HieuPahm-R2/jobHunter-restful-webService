package AsukaSan.jobLancer.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import AsukaSan.jobLancer.domain.Job;
import AsukaSan.jobLancer.repository.JobRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final JobRepository jobRepository;
    public EmailService(SpringTemplateEngine templateEngine,JavaMailSender javaMailSender,
    JobRepository jobRepository){
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.jobRepository = jobRepository;
    }
    
    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
 // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
                message.setTo(to);
                message.setSubject(subject);
                message.setText(content, isHtml);
                this.javaMailSender.send(mimeMessage);
            } catch (MailException | MessagingException e) {
                System.out.println("ERROR SEND EMAIL: " + e);
            }
        }

    @Async
    public void sendEmailFromTemplateSync(String to, String subject, String templateName, String username, Object value) {
        Context context = new Context();
        context.setVariable("name", username);
        context.setVariable("jobs", value);
        String content = this.templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
}
    

}
