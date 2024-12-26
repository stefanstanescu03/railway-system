package com.example.railway_management_system.auth;

public class RegisterRequest {

    private String nume;
    private String prenume;
    private String email;
    private String telefon;
    private String parola;
    private String rol;

    public RegisterRequest(String parola, String telefon, String email,
                           String prenume, String nume, String rol) {
        this.parola = parola;
        this.telefon = telefon;
        this.email = email;
        this.prenume = prenume;
        this.nume = nume;
        this.rol = rol;
    }

    public RegisterRequest() {
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
