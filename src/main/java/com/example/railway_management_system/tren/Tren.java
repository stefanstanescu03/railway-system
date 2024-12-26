package com.example.railway_management_system.tren;

import jakarta.persistence.*;

@Entity
@Table
public class Tren {
    @Id
    @SequenceGenerator(
            name = "train_sequence",
            sequenceName = "train_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "train_sequence"
    )
    private Long trenId;

    private String denumire;
    private Integer capacitate;
    private Integer numarVagoane;

    public Tren() {

    }

    public Tren(Integer numarVagoane, Integer capacitate, String denumire) {
        this.numarVagoane = numarVagoane;
        this.capacitate = capacitate;
        this.denumire = denumire;
    }

    public Tren(Long trenId, Integer numarVagoane, Integer capacitate,
                String denumire) {
        this.trenId = trenId;
        this.numarVagoane = numarVagoane;
        this.capacitate = capacitate;
        this.denumire = denumire;
    }

    public Long getTrenId() {
        return trenId;
    }

    public void setTrenId(Long trenId) {
        this.trenId = trenId;
    }

    public Integer getNumarVagoane() {
        return numarVagoane;
    }

    public void setNumarVagoane(Integer numarVagoane) {
        this.numarVagoane = numarVagoane;
    }

    public Integer getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(Integer capacitate) {
        this.capacitate = capacitate;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    @Override
    public String toString() {
        return "Tren{" +
                "trenId=" + trenId +
                ", denumire='" + denumire + '\'' +
                ", capacitate=" + capacitate +
                ", numarVagoane=" + numarVagoane +
                '}';
    }
}
