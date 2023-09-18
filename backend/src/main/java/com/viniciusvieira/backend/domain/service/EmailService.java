package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.exception.CreateTemplateException;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import freemarker.core.ParseException;
import freemarker.template.*;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

    private static final String ERROR_MESSAGE = "Erro ao tentar enviar um email ao cliente cadastrado";

    public void sendEmailSimples(String destinatario, String titulo, String mensagem) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(destinatario);
            message.setSubject(titulo);
            message.setText(mensagem);

            javaMailSender.send(message);
        } catch (Exception ex) {
            throw new NegocioException(ERROR_MESSAGE, ex);
        }
    }

    // COMMENT - Baeldung
    public void sendEmailTemplateBoasVindas(String destinatario, String titulo, Map<String, Object> propriedades) {
        try {
            Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                    .getTemplate("email-boas-vindas.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, propriedades);

            sendHtmlMessage(destinatario, titulo, htmlBody);

        } catch (TemplateException | TemplateNotFoundException | ParseException | MalformedTemplateNameException e) {
            throw new CreateTemplateException("Erro ao tentar criar o template para o envio de email.", e);
        } catch (IOException e) {
            throw new CreateTemplateException("Erro ao tentar encontrar o template para o envio de email.", e);
        }
    }

    public void sendEmailTemplateRecuperacaoCodigo(String destinatario, String titulo, Map<String, Object> propriedades) {
        try {
            Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                    .getTemplate("email-recuperacao-codigo.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, propriedades);

            sendHtmlMessage(destinatario, titulo, htmlBody);

        } catch (TemplateException | TemplateNotFoundException | ParseException | MalformedTemplateNameException e) {
            throw new CreateTemplateException("Erro ao tentar criar o template para o envio de email.", e);
        } catch (IOException e) {
            throw new CreateTemplateException("Erro ao tentar encontrar o template para o envio de email.", e);
        }
    }

    // COMMENT - Baeldung
    private void sendHtmlMessage(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(message);

        } catch (Exception ex) {
            throw new NegocioException(ERROR_MESSAGE, ex);
        }
    }

}
