package com.example.microservizi_prenotazione.Configuration;

import com.example.microservizi_prenotazione.Entities.emailDetails;

public interface emailService {
    String sendSimpleMail(emailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(emailDetails details);
}
