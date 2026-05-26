package com.servitech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "facturas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private Double total;
    private boolean pagada = false;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "orden_id")
    private OrdenServicio ordenServicio;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetalleFactura> detalles;

    public Factura() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public boolean isPagada() { return pagada; }
    public void setPagada(boolean pagada) { this.pagada = pagada; }

    public OrdenServicio getOrdenServicio() { return ordenServicio; }
    public void setOrdenServicio(OrdenServicio ordenServicio) { this.ordenServicio = ordenServicio; }

    public List<DetalleFactura> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFactura> detalles) { this.detalles = detalles; }

    @PrePersist
    protected void onCreate() {
        fechaEmision = LocalDateTime.now();
        if (numeroFactura == null) {
            numeroFactura = "FAC-" + System.currentTimeMillis();
        }
    }
}
