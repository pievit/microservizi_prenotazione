package com.example.microservizi_prenotazione.Entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class emailDetails {

    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;


}
