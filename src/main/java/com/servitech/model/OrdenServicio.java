package com.servitech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes_servicio")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrdenServicio implements Facturable, Asignable {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @OneToMany(mappedBy = "ordenServicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleOrdenRepuesto> repuestosUtilizados = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "ordenServicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HistorialRevision> historialRevisiones = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "ordenServicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Factura factura;

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

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    @PrePersist
    protected void onCreate() {
        fechaIngreso = LocalDateTime.now();
        if (estado == null) estado = EstadoOrden.PENDIENTE;
        if (costoManoObra == null) costoManoObra = 0.0;
    }

    @Override
    public Double calcularTotal() {
        double totalRepuestos = repuestosUtilizados.stream()
                .mapToDouble(dr -> (dr.getRepuesto() != null ? dr.getRepuesto().getPrecio() : 0.0) * (dr.getCantidad() != null ? dr.getCantidad() : 0))
                .sum();
        return totalRepuestos + (costoManoObra != null ? costoManoObra : 0.0);
    }

    @Override
    public void generarFactura() {
        if (this.factura == null) {
            Factura f = new Factura();
            f.setOrdenServicio(this);
            f.setFechaEmision(LocalDateTime.now());
            f.setTotal(this.calcularTotal());
            this.factura = f;
        }
    }

    @Override
    public void asignarTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    @Override
    public boolean estaDisponible() {
        return this.tecnico == null;
    }
}
