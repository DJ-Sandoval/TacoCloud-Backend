package com.api.Summit.API.reports.excel.controller;

import com.api.Summit.API.reports.excel.service.ProductoExcelReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos/reportes/excel")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductoExcelReportController {

    private final ProductoExcelReportService productoExcelReportService;

    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<byte[]> generateProductosExcelReport(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "inventario") String tipo) {

        try {
            byte[] excelBytes = productoExcelReportService.generateProductosExcelReport(negocioId, tipo);

            String filename = String.format("reporte_productos_%s_%d.xlsx", tipo, System.currentTimeMillis());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

