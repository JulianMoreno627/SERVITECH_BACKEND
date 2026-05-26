package com.servitech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_facturas")
public class DetalleFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String concepto;
    private Double subtotal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    public DetalleFactura() {}

    public DetalleFactura(Long id, String concepto, Double subtotal, Factura factura) {
        this.id = id;
        this.concepto = concepto;
        this.subtotal = subtotal;
        this.factura = factura;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
}
