package com.servitech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_revisiones")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HistorialRevision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private String observacion;
    
    @Enumerated(EnumType.STRING)
    private EstadoOrden estadoAnterior;
    
    @Enumerated(EnumType.STRING)
    private EstadoOrden estadoNuevo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "orden_id")
    private OrdenServicio ordenServicio;

    public HistorialRevision() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public EstadoOrden getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(EstadoOrden estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public EstadoOrden getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(EstadoOrden estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public OrdenServicio getOrdenServicio() { return ordenServicio; }
    public void setOrdenServicio(OrdenServicio ordenServicio) { this.ordenServicio = ordenServicio; }

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }
}
