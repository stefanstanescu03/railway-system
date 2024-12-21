package com.example.railway_management_system.ruta;

import com.example.railway_management_system.program.Program;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Ruta {
    @Id
    @SequenceGenerator(
            name = "route_sequence",
            sequenceName = "route_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "route_sequence"
    )
    private Long rutaId;
    private String statiePlecare;
    private String statieDestinatie;
    private Integer distanta;
    private Integer durata;

    public Ruta() {

    }

    public Ruta(Long rutaId, String statiePlecare, Integer distanta,
                Integer durata, String statieDestinatie) {
        this.rutaId = rutaId;
        this.statiePlecare = statiePlecare;
        this.distanta = distanta;
        this.durata = durata;
        this.statieDestinatie = statieDestinatie;
    }

    public Ruta(String statiePlecare, Integer distanta,
                Integer durata, String statieDestinatie) {
        this.statiePlecare = statiePlecare;
        this.distanta = distanta;
        this.durata = durata;
        this.statieDestinatie = statieDestinatie;
    }

    public Long getRutaId() {
        return rutaId;
    }

    public void setRutaId(Long rutaId) {
        this.rutaId = rutaId;
    }

    public String getStatieDestinatie() {
        return statieDestinatie;
    }

    public void setStatieDestinatie(String statieDestinatie) {
        this.statieDestinatie = statieDestinatie;
    }

    public Integer getDistanta() {
        return distanta;
    }

    public void setDistanta(Integer distanta) {
        this.distanta = distanta;
    }

    public String getStatiePlecare() {
        return statiePlecare;
    }

    public void setStatiePlecare(String statiePlecare) {
        this.statiePlecare = statiePlecare;
    }

    public Integer getDurata() {
        return durata;
    }

    public void setDurata(Integer durata) {
        this.durata = durata;
    }

    @Override
    public String toString() {
        return "Ruta{" +
                "rutaId=" + rutaId +
                ", statiePlecare='" + statiePlecare + '\'' +
                ", statieDestinatie='" + statieDestinatie + '\'' +
                ", distanta=" + distanta +
                ", durata=" + durata +
                '}';
    }
}
