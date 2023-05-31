package com.example.microservizi_prenotazione.Controller;

import com.example.microservizi_prenotazione.Entities.emailDetails;
import com.example.microservizi_prenotazione.Entities.prenotazione;
import com.example.microservizi_prenotazione.Entities.prenotazioneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class controllerricerca {

    @Value("${API-GATEWAY-HOST}")
    private String gateway_host;
    @Autowired
    private prenotazioneRepository Prerepository;

     @GetMapping("/getRicPren")
     public List<prenotazione> getRicPren() {
         return Prerepository.findAll();
     }

     @GetMapping("/getTassistiDisp/{giorno}/{orario}/{posti}/{bag}/{seg}")
     public List<Object> getTassistiDisp(@PathVariable("giorno") String giorno, @PathVariable("orario") String orario, @PathVariable("posti") Integer posti, @PathVariable("bag") String bag, @PathVariable("seg") String seg){
         try {
             URI uri = new URI("http://"+gateway_host+"/api/utenti/getDisponibilita/"+giorno+"/"+orario);

             RestTemplate rt = new RestTemplate();
             List<ArrayList> tasDisp = rt.getForObject(uri,List.class);

             List<Object> tasDispSeri = new ArrayList<Object>();

             for(ArrayList td: tasDisp){
                 if((Integer) td.get(8) >= posti && (td.get(9).toString().equals(bag) || td.get(9).toString().equals("true")) && (td.get(10).toString().equals(seg) || td.get(10).toString().equals("true"))){
                     tasDispSeri.add(td);
                 }
             }

             return tasDispSeri;
         } catch (URISyntaxException e) {
             throw new RuntimeException(e);
         }
     }

     @PutMapping("/addPrenotazione")
    public String addPrenotazione(@RequestBody ObjectNode map){
         try{
             ObjectMapper mapper = new ObjectMapper();
             prenotazione pre = mapper.readValue(map.get("prenotazione").toString(), prenotazione.class);

             Prerepository.save(pre);
             emailDetails ed = new emailDetails(pre.getTassista(),"Hai una nuova prenotazione","Prenotazione aggiunta","");
             ControllerEmail ce = new ControllerEmail();
             ce.sendSimpleMessage(ed,"mailgratis60@gmail.com");
             return "Prenotazione inserita.";
         }catch(Exception e){
//             System.out.println(e);
             return e.getMessage();
         }
     }

    @DeleteMapping("/deletePrenotazione/{id}")
    public String deletePrenotazione(@PathVariable("id") Long id) {
        try {
            Prerepository.deleteById(id);
            return "Cancellazione prenotazione con id:" + id+" effettuata.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/deletePrenotazioni/{email}")
    public String deletePrenotazioni(@PathVariable("email") String email){
         try{
             Iterator<prenotazione> it = Prerepository.findAll().iterator();

             while(it.hasNext()){
                 prenotazione pre = it.next();
                 if(pre.getCliente().equals(email) || pre.getTassista().equals(email)){
                     Prerepository.delete(pre);
                 }
             }
             return "Prenotazioni cancellate.";
         }catch(Exception e){
             return e.getMessage();
         }
    }


    @GetMapping("/getPrenotazioniTassista/{email}")
    public ResponseEntity<List<prenotazione>> getPrenotazioniTassista(@PathVariable("email") String email){
        List<prenotazione> listaPre = new ArrayList<>();
        try{
            Iterator<prenotazione> it =  Prerepository.findAll().iterator();
            while(it.hasNext()){
                prenotazione pren = it.next();
                if(pren.getTassista().equals(email)){
                    listaPre.add(pren);
                }
            }

        }catch(Exception e){
            return new ResponseEntity<>(listaPre,HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(listaPre,HttpStatus.OK);
    }


    @GetMapping("/getPrenotazioniTassista/{email}/{stato}")
    public ResponseEntity<List<prenotazione>> getPrenotazioniTassistaTipo(@PathVariable("email") String email,@PathVariable("stato") String stato){
        List<prenotazione> listaPre = new ArrayList<>();
        try{

            Iterator<prenotazione> it =  Prerepository.findAll().iterator();
            while(it.hasNext()){
                prenotazione pren = it.next();
                if(pren.getTassista().equals(email) && pren.getStato().equals(stato)){
                    listaPre.add(pren);
                }
            }

        }catch(Exception e){
            System.out.println("ERRORE      "+e.getMessage());
            return new ResponseEntity<>(listaPre,HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(listaPre,HttpStatus.OK);
    }

    @GetMapping("/getPrenotazioniCliente/{email}")
    public ResponseEntity<List<prenotazione>> getPrenotazioniCliente(@PathVariable("email") String email){
        List<prenotazione> listaPre = new ArrayList<>();
        try{

            Iterator<prenotazione> it =  Prerepository.findAll().iterator();
            while(it.hasNext()){
                prenotazione pren = it.next();
                if(pren.getCliente().equals(email)){
                    listaPre.add(pren);
                }
            }

        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(listaPre,HttpStatus.OK);
    }


    @GetMapping("/getPrenotazioniCliente/{email}/{stato}")
    public ResponseEntity<List<prenotazione>> getPrenotazioniClienteTipo(@PathVariable("email") String email,@PathVariable("stato") String stato){
        List<prenotazione> listaPre = new ArrayList<>();
        try{

            Iterator<prenotazione> it =  Prerepository.findAll().iterator();
            while(it.hasNext()){
                prenotazione pren = it.next();
                if(pren.getCliente().equals(email) && pren.getStato().equals(stato)){
                    listaPre.add(pren);
                }
            }

        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(listaPre,HttpStatus.OK);
    }

    @PostMapping("/sceltaPrenotazione")
    public String sceltaPrenotazione(@RequestBody ObjectNode map){
        try{

            ObjectMapper mapper = new ObjectMapper();
            Long id = mapper.readValue(map.get("id").toString(), Long.class);
            Float prezzo = mapper.readValue(map.get("prezzo").toString(), Float.class);
            prenotazione pre = null;
            if(Prerepository.findById(id).isPresent())
                pre = Prerepository.findById(id).get();
            pre.setPrezzo(prezzo);
            pre.setStato("attesa_conferma");
            Prerepository.save(pre);
            emailDetails ed = new emailDetails(pre.getCliente(),"Hai una nuova prenotazione da confermare","Prenotazione in attesa di conferma","");
            ControllerEmail ce = new ControllerEmail();
            ce.sendSimpleMessage(ed,"mailgratis60@gmail.com");
//            ControllerEmail ce = new ControllerEmail();
//            ce.sendSimpleMessage(pre.getCliente(),"Prenotazione in attesa di conferma","Hai una nuova prenotazione da confermare.");
            return "Prenotazione in attesa di conferma.";
        }catch(Exception e){
            return e.getMessage();
        }
    }


    @PostMapping("/rifiutaPrenotazione")
    public String rifiutaPrenotazione(@RequestBody ObjectNode map){
        try{
            ObjectMapper mapper = new ObjectMapper();
            Long id = mapper.readValue(map.get("id").toString(), Long.class);
            String motivo = mapper.readValue(map.get("motivo").toString(), String.class);
            prenotazione pre = null;
            if(Prerepository.findById(id).isPresent())
                pre = Prerepository.findById(id).get();
            pre.setStato("annullata");
            Prerepository.save(pre);
            if(motivo.length()!=0){
                motivo = "\n\nMotivazioni:  " + motivo;
            }
            emailDetails ed = new emailDetails(pre.getTassista(),"La prenotazione "+pre.getId()+" del cliente: "+pre.getCliente()+" è stata annullata."+motivo,"Prenotazione annullata","");
            ControllerEmail ce = new ControllerEmail();
            ce.sendSimpleMessage(ed,"mailgratis60@gmail.com");
            emailDetails ed1 = new emailDetails(pre.getCliente(),"La prenotazione "+pre.getId()+" col tassista: "+pre.getTassista()+" è stata annullata."+motivo,"Prenotazione annullata","");
            ControllerEmail ce1 = new ControllerEmail();
            ce1.sendSimpleMessage(ed1,"mailgratis60@gmail.com");
//            ControllerEmail ce = new ControllerEmail();
//            ce.sendSimpleMessage(pre.getTassista(),"Prenotazione annullata","La prenotazione "+pre.getId()+" del cliente: "+pre.getCliente()+" è stata annullata.");
//            ce.sendSimpleMessage(pre.getCliente(),"Prenotazione annullata","La prenotazione "+pre.getId()+" col tassista: "+pre.getTassista()+" è stata annullata.");
            return "Prenotazione annullata con successo.";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @PostMapping("/confermaPrenotazione")
    public String confermaPrenotazione(@RequestBody ObjectNode map){

        try{

            int leftLimit = 48; //  '0'
            int rightLimit = 122; //  'z'
            int targetStringLength = 4;
            Random random = new Random();

            String codice = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            ObjectMapper mapper = new ObjectMapper();
            Long id = mapper.readValue(map.get("id").toString(), Long.class);
            prenotazione pre = null;
            if(Prerepository.findById(id).isPresent())
                pre = Prerepository.findById(id).get();
            pre.setCodice(codice);
            pre.setStato("confermata");
            Prerepository.save(pre);
            emailDetails ed = new emailDetails(pre.getTassista(),"La prenotazione "+pre.getId()+" del cliente: "+pre.getCliente()+" è stata confermata.","Prenotazione confermata","");
            ControllerEmail ce = new ControllerEmail();
            ce.sendSimpleMessage(ed,"mailgratis60@gmail.com");
            emailDetails ed1 = new emailDetails(pre.getCliente(),"La prenotazione "+pre.getId()+" col tassista: "+pre.getTassista()+" è stata confermata.","Prenotazione confermata","");
            ControllerEmail ce1 = new ControllerEmail();
            ce1.sendSimpleMessage(ed1,"mailgratis60@gmail.com");
//            ControllerEmail ce = new ControllerEmail();
//            ce.sendSimpleMessage(pre.getTassista(),"Prenotazione confermata","La prenotazione "+pre.getId()+" del cliente: "+pre.getCliente()+" è stata confermata.");
//            ce.sendSimpleMessage(pre.getCliente(),"Prenotazione confermata","La prenotazione "+pre.getId()+" col tassista: "+pre.getTassista()+" è stata confermata.");
            return "Prenotazione confermata.";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @PostMapping("/controlloCodice")
    public String controlloCodice(@RequestBody ObjectNode map){
         try{
             ObjectMapper mapper = new ObjectMapper();
             Long id = mapper.readValue(map.get("id").toString(), Long.class);
             prenotazione pre = null;
             if(Prerepository.findById(id).isPresent())
                 pre = Prerepository.findById(id).get();
             String codice = mapper.readValue(map.get("codice").toString(), String.class);
             if(pre.getCodice().equals(codice)){
                 pre.setStato("conclusa");
                 Prerepository.save(pre);
                 return "Codice confermato.";
             }else{
                 return "Codice errato, riprova.";
             }
         }catch(Exception e){
             return e.getMessage();
         }
    }

}
