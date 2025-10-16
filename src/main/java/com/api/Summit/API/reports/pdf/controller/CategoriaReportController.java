package com.api.Summit.API.reports.pdf.controller;

import com.api.Summit.API.reports.pdf.service.CategoriaReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias/reportes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoriaReportController {

    private final CategoriaReportService categoriaReportService;

    @GetMapping("/negocio/{negocioId}/pdf")
    public ResponseEntity<byte[]> generateCategoriasReportPdf(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "detallado") String tipo) {

        try {
            byte[] pdfBytes = categoriaReportService.generateCategoriasReportPdf(negocioId, tipo);

            String filename = String.format("reporte_categorias_%s_%d.pdf", tipo, System.currentTimeMillis());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

