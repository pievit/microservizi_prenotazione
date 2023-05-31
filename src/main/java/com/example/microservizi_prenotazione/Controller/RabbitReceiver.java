package com.example.microservizi_prenotazione.Controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitReceiver {
    private final controllerricerca cc;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(String message) {
        //eliminare le prenotazioni dell'utente
        System.out.println("E' arrivato il messaggio "+ message);
        String email = message.split(" ")[1];
        String mes = cc.deletePrenotazioni(email);
        System.out.println(mes);
    }
}