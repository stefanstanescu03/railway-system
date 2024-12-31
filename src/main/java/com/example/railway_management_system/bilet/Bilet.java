package com.example.railway_management_system.bilet;

import com.example.railway_management_system.program.Program;
import com.example.railway_management_system.utilizator.Utilizator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
public class Bilet {
    @Id
    @SequenceGenerator(
            name = "ticket_sequence",
            sequenceName = "ticket_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_sequence"
    )
    private Long biletId;

    private Integer loc;
    private Integer vagon;
    private Double pret;
    private Integer clasa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilizatorId", nullable = false)
    @JsonIgnore
    private Utilizator utilizator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "programId", nullable = false)
    private Program program;

    public Bilet() {

    }

    public Bilet(Long biletId, Integer loc, Integer vagon, Double pret,
                 Integer clasa) {
        this.biletId = biletId;
        this.loc = loc;
        this.vagon = vagon;
        this.pret = pret;
        this.clasa = clasa;
    }

    public Bilet(Integer loc, Integer vagon, Double pret,
                 Integer clasa) {
        this.loc = loc;
        this.vagon = vagon;
        this.pret = pret;
        this.clasa = clasa;
    }

    public Long getBiletId() {
        return biletId;
    }

    public void setBiletId(Long biletId) {
        this.biletId = biletId;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public Double getPret() {
        return pret;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

    public Integer getVagon() {
        return vagon;
    }

    public void setVagon(Integer vagon) {
        this.vagon = vagon;
    }

    public Integer getLoc() {
        return loc;
    }

    public void setLoc(Integer loc) {
        this.loc = loc;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Integer getClasa() {
        return clasa;
    }

    public void setClasa(Integer clasa) {
        this.clasa = clasa;
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "pret=" + pret +
                ", vagon=" + vagon +
                ", loc=" + loc +
                ", biletId=" + biletId +
                '}';
    }
}
