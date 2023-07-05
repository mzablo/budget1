package mza.thy.reminder;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
class EmailReminderListener {
    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.username}")
    private String to;

    private final JavaMailSender javaMailSender;

    @EventListener
    public void sendEmail(EmailDepositReminder emailReminder) {
        log.debug("sending email " + emailReminder);
        sendEmail(emailReminder.getText());
    }

    private void sendEmail(String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject("Deposits reminder");
        message.setText(text);
        javaMailSender.send(message);
        log.debug("email sent");
    }
}
