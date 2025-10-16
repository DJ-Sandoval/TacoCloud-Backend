package com.api.Summit.API.reports.excel.controller;

import com.api.Summit.API.reports.excel.service.ClienteExcelReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes/reportes/excel")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteExcelReportController {

    private final ClienteExcelReportService clienteExcelReportService;

    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<byte[]> generateClientesExcelReport(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "todos") String tipo) {

        try {
            byte[] excelBytes = clienteExcelReportService.generateClientesExcelReport(negocioId, tipo);

            String filename = String.format("reporte_clientes_%s_%d.xlsx", tipo, System.currentTimeMillis());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
