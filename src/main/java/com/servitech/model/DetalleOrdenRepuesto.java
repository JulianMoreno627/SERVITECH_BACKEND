package com.servitech.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_orden_repuestos")
public class DetalleOrdenRepuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private OrdenServicio ordenServicio;

    @ManyToOne
    @JoinColumn(name = "repuesto_id")
    private Repuesto repuesto;

    private Integer cantidad;
    private Double precioUnitarioSnapshot;

    public DetalleOrdenRepuesto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrdenServicio getOrdenServicio() { return ordenServicio; }
    public void setOrdenServicio(OrdenServicio ordenServicio) { this.ordenServicio = ordenServicio; }

    public Repuesto getRepuesto() { return repuesto; }
    public void setRepuesto(Repuesto repuesto) { this.repuesto = repuesto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecioUnitarioSnapshot() { return precioUnitarioSnapshot; }
    public void setPrecioUnitarioSnapshot(Double precioUnitarioSnapshot) { this.precioUnitarioSnapshot = precioUnitarioSnapshot; }
}
