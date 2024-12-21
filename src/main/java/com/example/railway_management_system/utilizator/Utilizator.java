package com.example.railway_management_system.utilizator;

import com.example.railway_management_system.bilet.Bilet;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Utilizator {
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private Long utilizatorId;
    private String nume;
    private String prenume;
    private String email;
    private String telefon;
    private String parola;

    public Utilizator() {

    }

    public Utilizator(Long utilizatorId, String nume, String prenume,
                      String email, String telefon, String parola) {
        this.utilizatorId = utilizatorId;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.telefon = telefon;
        this.parola = parola;
    }

    public Utilizator(String nume, String prenume, String email,
                      String telefon, String parola) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.telefon = telefon;
        this.parola = parola;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Long getUtilizatorId() {
        return utilizatorId;
    }

    public void setUtilizatorId(Long utilizatorId) {
        this.utilizatorId = utilizatorId;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "parola='" + parola + '\'' +
                ", prenume='" + prenume + '\'' +
                ", utilizatorId=" + utilizatorId +
                ", nume='" + nume + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                '}';
    }
}
