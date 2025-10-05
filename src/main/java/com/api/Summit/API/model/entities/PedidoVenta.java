package com.api.Summit.API.model.entities;

import com.api.Summit.API.model.enums.EstadoPedido;
import com.api.Summit.API.model.enums.EstadoVenta;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido_venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_pedido", unique = true, nullable = false, length = 20)
    private String numeroPedido;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "total", nullable = false)
    private double total;

    @Column(name = "tipo_entrega", nullable = false, length = 20)
    private String tipoEntrega; // "MESA", "DOMICILIO", "MOSTRADOR"

    @Column(name = "numero_mesa", length = 10)
    private String numeroMesa;

    @Column(name = "direccion_entrega", length = 255)
    private String direccionEntrega;

    @Column(name = "telefono_cliente", length = 15)
    private String telefonoCliente;

    @Column(name = "nombre_cliente", length = 100)
    private String nombreCliente;

    // Estados unificados
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = false)
    @Builder.Default
    private EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_venta", nullable = false)
    @Builder.Default
    private EstadoVenta estadoVenta = EstadoVenta.PENDIENTE;

    @Column(name = "tiempo_estimado")
    private Integer tiempoEstimado;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "hora_entrega")
    private LocalDateTime horaEntrega;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "negocio_id", nullable = false)
    private Negocio negocio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caja_id")
    private Caja caja;

    // Detalles unificados - reemplazan tanto DetallePedido como Concepto
    @OneToMany(mappedBy = "pedidoVenta", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<DetallePedidoVenta> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "pedidoVenta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<HistorialEstadoPedido> historialEstados = new ArrayList<>();

    // Métodos de negocio
    public void marcarComoPagado() {
        this.estadoVenta = EstadoVenta.COMPLETA;
        this.fechaPago = LocalDateTime.now();
    }

    public void marcarComoEntregado() {
        this.estadoPedido = EstadoPedido.ENTREGADO;
        this.horaEntrega = LocalDateTime.now();

        // Si estaba pendiente de pago, se marca como completa automáticamente
        if (this.estadoVenta == EstadoVenta.PENDIENTE) {
            this.marcarComoPagado();
        }
    }

    public boolean esVentaCompleta() {
        return this.estadoVenta == EstadoVenta.COMPLETA;
    }

    public boolean esPedidoCompleto() {
        return this.estadoPedido == EstadoPedido.ENTREGADO;
    }

    // Métodos helper
    public void addDetalle(DetallePedidoVenta detalle) {
        this.detalles.add(detalle);
        detalle.setPedidoVenta(this);
    }

    public void removeDetalle(DetallePedidoVenta detalle) {
        this.detalles.remove(detalle);
        detalle.setPedidoVenta(null);
    }

    public void addHistorialEstado(HistorialEstadoPedido historial) {
        this.historialEstados.add(historial);
        historial.setPedidoVenta(this);
    }

    // Calcular total automáticamente
    @PrePersist
    @PreUpdate
    public void calcularTotal() {
        this.total = this.detalles.stream()
                .mapToDouble(DetallePedidoVenta::getSubtotal)
                .sum();
    }
}