package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.exception.NegocioException;
import freemarker.template.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService underTest;

    @Mock
    private JavaMailSender javaMailSenderMock;
    @Mock
    private Configuration fmConfigurationMock;

    @Test
    @DisplayName("sendEmailSimples() email should be sending")
    void whenSendEmailSimples_thenEmailMustBeSend() {
        //given
        String destinatario = "teste@gmail.com";
        String titulo = "teste";
        String mensagem = "teste";
        // Forma errada - doNothing().when(javaMailSenderMock).send(any(MimeMessage.class))
        doNothing().when(javaMailSenderMock).send(ArgumentMatchers.<SimpleMailMessage>any());
        // when
        underTest.sendEmailSimples(destinatario, titulo, mensagem);
        // then
        // Forma errada - verify(javaMailSenderMock, times(1)).send(any(MimeMessage.class))
        verify(javaMailSenderMock, times(1)).send(ArgumentMatchers.<SimpleMailMessage>any());
    }

    @Test
    @DisplayName("sendEmailSimples() throws NegocioException")
    void whenSendEmailSimples_thenThrowsNegocioException() {
        //given
        String destinatario = "teste@gmail.com";
        String titulo = "teste";
        String mensagem = "teste";
        // Forma errada - doNothing().when(javaMailSenderMock).send(any(MimeMessage.class))
        doThrow(new NegocioException("Erro ao tentar enviar um email ao cliente cadastrado"))
                .when(javaMailSenderMock).send(ArgumentMatchers.<SimpleMailMessage>any());
        // when
        assertThatThrownBy(() -> underTest.sendEmailSimples(destinatario, titulo, mensagem))
                .isInstanceOf(NegocioException.class)
                        .hasMessageContaining("Erro ao tentar enviar um email ao cliente cadastrado");
        // then
        // Forma errada - verify(javaMailSenderMock, times(1)).send(any(MimeMessage.class))
        verify(javaMailSenderMock, times(1)).send(ArgumentMatchers.<SimpleMailMessage>any());
    }
}