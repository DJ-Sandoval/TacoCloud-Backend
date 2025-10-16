package com.api.Summit.API.reports.excel.service;

import com.api.Summit.API.model.entities.Categoria;
import com.api.Summit.API.model.entities.Negocio;
import com.api.Summit.API.model.repository.CategoriaRepository;
import com.api.Summit.API.model.repository.NegocioRepository;
import com.api.Summit.API.view.dto.CategoriaReportDTO;
import com.api.Summit.API.view.dto.CategoriaReportDataDTO;
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
public class CategoriaExcelReportService {
    private final CategoriaRepository categoriaRepository;
    private final NegocioRepository negocioRepository;
    private final ExcelReportService excelReportService;

    public byte[] generateCategoriasExcelReport(Long negocioId, String tipoReporte) {
        try {
            List<Categoria> categorias = categoriaRepository.findByNegocioId(negocioId);
            Negocio negocio = negocioRepository.findById(negocioId)
                    .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

            String titulo = "";
            switch (tipoReporte.toLowerCase()) {
                case "detallado":
                    titulo = "REPORTE DETALLADO DE CATEGORÍAS";
                    break;
                case "resumen":
                    titulo = "REPORTE RESUMEN DE CATEGORÍAS";
                    break;
                default:
                    titulo = "REPORTE DE CATEGORÍAS";
                    break;
            }

            List<CategoriaReportDataDTO> categoriasData = categorias.stream()
                    .map(this::convertToReportDataDTO)
                    .collect(Collectors.toList());

            int totalProductos = categoriasData.stream()
                    .mapToInt(CategoriaReportDataDTO::getCantidadProductos)
                    .sum();

            CategoriaReportDTO reportDTO = CategoriaReportDTO.builder()
                    .titulo(titulo)
                    .fechaGeneracion(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                    .categorias(categoriasData)
                    .totalCategorias(categoriasData.size())
                    .totalProductos(totalProductos)
                    .negocioNombre(negocio.getNombre())
                    .build();

            return excelReportService.generateCategoriasExcelReport(reportDTO, tipoReporte);

        } catch (Exception e) {
            log.error("Error generando reporte Excel de categorías", e);
            throw new RuntimeException("Error al generar reporte Excel: " + e.getMessage());
        }
    }

    private CategoriaReportDataDTO convertToReportDataDTO(Categoria categoria) {
        int cantidadProductos = categoria.getProductos() != null ? categoria.getProductos().size() : 0;

        // Obtener nombres de los primeros 3 productos como ejemplo
        String productosPrincipales = "N/A";
        if (cantidadProductos > 0) {
            productosPrincipales = categoria.getProductos().stream()
                    .limit(3)
                    .map(producto -> producto.getNombre())
                    .collect(Collectors.joining(", "));

            if (cantidadProductos > 3) {
                productosPrincipales += " y " + (cantidadProductos - 3) + " más";
            }
        }

        return CategoriaReportDataDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion() != null ? categoria.getDescripcion() : "Sin descripción")
                .cantidadProductos(cantidadProductos)
                .productosPrincipales(productosPrincipales)
                .build();
    }
}

