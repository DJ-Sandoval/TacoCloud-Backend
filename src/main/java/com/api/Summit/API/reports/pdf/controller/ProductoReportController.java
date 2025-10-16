package com.api.Summit.API.reports.pdf.controller;

import com.api.Summit.API.reports.pdf.service.ProductoReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos/reportes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductoReportController {

    private final ProductoReportService productoReportService;

    @GetMapping("/negocio/{negocioId}/pdf")
    public ResponseEntity<byte[]> generateProductosReportPdf(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "inventario") String tipo) {

        try {
            byte[] pdfBytes = productoReportService.generateProductosReportPdf(negocioId, tipo);

            String filename = String.format("reporte_productos_%s_%d.pdf", tipo, System.currentTimeMillis());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}