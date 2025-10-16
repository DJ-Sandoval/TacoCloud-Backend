package com.api.Summit.API.reports.pdf.service;

import com.api.Summit.API.model.entities.Negocio;
import com.api.Summit.API.model.entities.Producto;
import com.api.Summit.API.model.repository.NegocioRepository;
import com.api.Summit.API.model.repository.ProductoRepository;
import com.api.Summit.API.view.dto.ProductoReportDTO;
import com.api.Summit.API.view.dto.ProductoReportDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoReportService {

    private final ProductoRepository productoRepository;
    private final NegocioRepository negocioRepository;
    private final PdfReportService pdfReportService;

    public byte[] generateProductosReportPdf(Long negocioId, String tipoReporte) {
        try {
            List<Producto> productos = productoRepository.findByNegocioId(negocioId);
            Negocio negocio = negocioRepository.findById(negocioId)
                    .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

            String titulo = "";
            switch (tipoReporte.toLowerCase()) {
                case "inventario":
                    titulo = "REPORTE DE INVENTARIO DE PRODUCTOS";
                    break;
                case "rentabilidad":
                    titulo = "REPORTE DE RENTABILIDAD DE PRODUCTOS";
                    break;
                case "resumen":
                    titulo = "REPORTE RESUMEN DE PRODUCTOS";
                    break;
                default:
                    titulo = "REPORTE DE PRODUCTOS";
                    break;
            }

            List<ProductoReportDataDTO> productosData = productos.stream()
                    .map(this::convertToReportDataDTO)
                    .collect(Collectors.toList());

            // Calcular estadísticas
            int totalProductos = productosData.size();
            double valorTotalInventario = productosData.stream()
                    .mapToDouble(producto -> producto.getCosto())
                    .sum();
            double precioPromedio = productosData.stream()
                    .mapToDouble(ProductoReportDataDTO::getPrecioUnitario)
                    .average()
                    .orElse(0.0);

            ProductoReportDTO reportDTO = ProductoReportDTO.builder()
                    .titulo(titulo)
                    .fechaGeneracion(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                    .productos(productosData)
                    .totalProductos(totalProductos)
                    .valorTotalInventario(valorTotalInventario)
                    .precioPromedio(precioPromedio)
                    .negocioNombre(negocio.getNombre())
                    .build();

            return pdfReportService.generateProductosReport(reportDTO, tipoReporte);

        } catch (Exception e) {
            log.error("Error generando reporte de productos", e);
            throw new RuntimeException("Error al generar reporte: " + e.getMessage());
        }
    }

    private ProductoReportDataDTO convertToReportDataDTO(Producto producto) {
        int cantidadCategorias = producto.getCategorias() != null ? producto.getCategorias().size() : 0;

        // Obtener nombres de las categorías
        String categoriasNombres = "Sin categorías";
        if (cantidadCategorias > 0) {
            categoriasNombres = producto.getCategorias().stream()
                    .map(categoria -> categoria.getNombre())
                    .collect(Collectors.joining(", "));
        }

        // Calcular márgenes
        double margenGanancia = producto.getPrecioUnitario() - producto.getCosto();
        double porcentajeMargen = producto.getCosto() > 0 ?
                (margenGanancia / producto.getCosto()) * 100 : 0;

        return ProductoReportDataDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .precioUnitario(producto.getPrecioUnitario())
                .costo(producto.getCosto())
                .margenGanancia(margenGanancia)
                .porcentajeMargen(porcentajeMargen)
                .categorias(categoriasNombres)
                .cantidadCategorias(cantidadCategorias)
                .build();
    }
}
