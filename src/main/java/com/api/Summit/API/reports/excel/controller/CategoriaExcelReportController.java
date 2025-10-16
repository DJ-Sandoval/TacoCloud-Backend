package com.api.Summit.API.reports.excel.controller;

import com.api.Summit.API.reports.excel.service.CategoriaExcelReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias/reportes/excel")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoriaExcelReportController {

    private final CategoriaExcelReportService categoriaExcelReportService;

    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<byte[]> generateCategoriasExcelReport(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "detallado") String tipo) {

        try {
            byte[] excelBytes = categoriaExcelReportService.generateCategoriasExcelReport(negocioId, tipo);

            String filename = String.format("reporte_categorias_%s_%d.xlsx", tipo, System.currentTimeMillis());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
