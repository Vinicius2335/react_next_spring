package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.exception.NegocioException;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    // configuration do freemarker
    @Autowired
    private Configuration fmConfiguration;

    public void sendEmailSimples(String destinatario, String titulo, String mensagem){
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(destinatario);
            message.setSubject(titulo);
            message.setText(mensagem);

            javaMailSender.send(message);
        } catch (Exception ex) {
            throw new NegocioException("Erro ao tentar enviar um email ao cliente cadastrado", ex);
        }
    }

    public void sendEmailTemplate(String destinatario, String titulo, Map<String, Object> propriedades){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);

            message.setSubject(titulo);
            message.setTo(destinatario);
            mimeMessage.setText(getConteudoTemplate(propriedades));

            javaMailSender.send(message.getMimeMessage());

        } catch (Exception ex) {
            throw new NegocioException("Erro ao tentar enviar um email ao cliente cadastrado", ex);
        }
    }

    private String getConteudoTemplate(Map<String, Object> model){
        StringBuffer content = new StringBuffer();

        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration
                    .getTemplate("email-recuperacao-codigo.flth"), model));

        } catch (Exception ex) {
            throw new NegocioException("Erro ao tentar Criar um email a partir de um template", ex);
        }
        return content.toString();
    }
}
