package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.service.IEmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarCorreoRecuperacionContrasena(String correoDestino, String enlaceRecuperacion) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(correoDestino);
        message.setSubject("Recuperación de contraseña - ReportaYA");
        message.setText(
                "Hola,\n\n" +
                "Has solicitado recuperar tu contraseña en ReportaYA.\n" +
                "Haz clic en el siguiente enlace para definir una nueva contraseña:\n\n" +
                enlaceRecuperacion + "\n\n" +
                "Si no realizaste esta solicitud, puedes ignorar este mensaje."
        );

        mailSender.send(message);
    }
}
