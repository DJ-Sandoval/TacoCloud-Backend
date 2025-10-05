package com.api.Summit.API.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "producto_id", nullable = false)
        private Producto producto;

        @Column(name = "cantidad", nullable = false)
        private int cantidad;

        @Column(name = "cantidad_minima", nullable = false)
        private int cantidadMinima;

        @Column(name = "cantidad_maxima", nullable = false)
        private int cantidadMaxima;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "negocio_id", nullable = false)
        private Negocio negocio;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
    }
