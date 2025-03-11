package nl.evosystems.springapi.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${application.email.sender}")
    private String sender;

    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName template,
            String confirmationURL,
            String verificationCode,
            String subject
    ) throws MessagingException {
        // set template name
        String templateName = "confirm-email";
        if(template != null) {
            templateName = template.name();
        }
        // create mime message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationURL", confirmationURL);
        properties.put("verificationCode", verificationCode);
        // create context
        Context ctx = new Context();
        ctx.setVariables(properties);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(to);
        // configure template
        mimeMessageHelper.setText(templateEngine.process(templateName, ctx), true);
        mailSender.send(mimeMessage);
    }
}
