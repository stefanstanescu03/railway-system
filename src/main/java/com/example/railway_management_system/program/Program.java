/** Clasa pentru Program
 * @author Stanescu Stefan
 * @version 10 Decembrie 2024
 */

package com.example.railway_management_system.program;

import com.example.railway_management_system.ruta.Ruta;
import com.example.railway_management_system.tren.Tren;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class Program {
    @Id
    @SequenceGenerator(
            name = "schedule_sequence",
            sequenceName = "schedule_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "schedule_sequence"
    )
    private Long programId;
    private Date dataPlecare;
    private Date dataAjungere;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rutaId", nullable = false)
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trenId", nullable = false)
    private Tren tren;

    public Program() {

    }

    public Program(Date dataPlecare, Long programId, Date dataAjungere) {
        this.dataPlecare = dataPlecare;
        this.programId = programId;
        this.dataAjungere = dataAjungere;
    }

    public Program(Date dataPlecare, Date dataAjungere) {
        this.dataPlecare = dataPlecare;
        this.dataAjungere = dataAjungere;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Date getDataAjungere() {
        return dataAjungere;
    }

    public void setDataAjungere(Date dataAjungere) {
        this.dataAjungere = dataAjungere;
    }

    public Date getDataPlecare() {
        return dataPlecare;
    }

    public void setDataPlecare(Date dataPlecare) {
        this.dataPlecare = dataPlecare;
    }

    public Tren getTren() {
        return tren;
    }

    public void setTren(Tren tren) {
        this.tren = tren;
    }

    @Override
    public String toString() {
        return "Program{" +
                "programId=" + programId +
                ", dataPlecare=" + dataPlecare +
                ", dataAjungere=" + dataAjungere +
                '}';
    }
}
