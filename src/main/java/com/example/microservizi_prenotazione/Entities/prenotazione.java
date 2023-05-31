package com.example.microservizi_prenotazione.Entities;

import jakarta.persistence.*;

@Entity
@Table( name = "prenotazione")
public class prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String partenza;

    private String arrivo;

    private String data;

    private String ora;

    private Integer n_posti;

    private Boolean seggiolino;

    private Boolean bagagliaio;

    private Integer pagamento;

    private float prezzo;

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    private String stato;

    private String cliente;

    private String tassista;

    private String codice;

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTassista() {
        return tassista;
    }

    public void setTassista(String tassista) {
        this.tassista = tassista;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartenza() {
        return partenza;
    }

    public void setPartenza(String partenza) {
        this.partenza = partenza;
    }

    public String getArrivo() {
        return arrivo;
    }

    public void setArrivo(String arrivo) {
        this.arrivo = arrivo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public Integer getN_posti() {
        return n_posti;
    }

    public void setN_posti(Integer n_posti) {
        this.n_posti = n_posti;
    }

    public Boolean getSeggiolino() {
        return seggiolino;
    }

    public void setSeggiolino(Boolean seggiolino) {
        this.seggiolino = seggiolino;
    }

    public Boolean getBagagliaio() {
        return bagagliaio;
    }

    public void setBagagliaio(Boolean bagagliaio) {
        this.bagagliaio = bagagliaio;
    }

    public Integer getPagamento() {
        return pagamento;
    }

    public void setPagamento(Integer pagamento) {
        this.pagamento = pagamento;
    }




}
