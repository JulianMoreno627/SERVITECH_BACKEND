package com.servitech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordenes_servicio")
public class OrdenServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaEntrega;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    private Double costoManoObra;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @OneToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @OneToMany(mappedBy = "ordenServicio", cascade = CascadeType.ALL)
    private List<DetalleOrdenRepuesto> repuestosUtilizados;

    @OneToMany(mappedBy = "ordenServicio", cascade = CascadeType.ALL)
    private List<HistorialRevision> historialRevisiones;

    public OrdenServicio() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }

    public Double getCostoManoObra() { return costoManoObra; }
    public void setCostoManoObra(Double costoManoObra) { this.costoManoObra = costoManoObra; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Tecnico getTecnico() { return tecnico; }
    public void setTecnico(Tecnico tecnico) { this.tecnico = tecnico; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public List<DetalleOrdenRepuesto> getRepuestosUtilizados() { return repuestosUtilizados; }
    public void setRepuestosUtilizados(List<DetalleOrdenRepuesto> repuestosUtilizados) { this.repuestosUtilizados = repuestosUtilizados; }

    public List<HistorialRevision> getHistorialRevisiones() { return historialRevisiones; }
    public void setHistorialRevisiones(List<HistorialRevision> historialRevisiones) { this.historialRevisiones = historialRevisiones; }

    @PrePersist
    protected void onCreate() {
        fechaIngreso = LocalDateTime.now();
        if (estado == null) estado = EstadoOrden.PENDIENTE;
    }
}
