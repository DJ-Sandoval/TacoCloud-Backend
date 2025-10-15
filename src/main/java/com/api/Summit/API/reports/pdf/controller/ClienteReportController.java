package com.api.Summit.API.reports.pdf.controller;

import com.api.Summit.API.reports.pdf.service.ClienteReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes/reportes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteReportController {

    private final ClienteReportService clienteReportService;

    @GetMapping("/negocio/{negocioId}/pdf")
    public ResponseEntity<byte[]> generateClientesReportPdf(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "todos") String tipo) {

        try {
            byte[] pdfBytes = clienteReportService.generateClientesReportPdf(negocioId, tipo);

            String filename = String.format("reporte_clientes_%s_%d.pdf", tipo, System.currentTimeMillis());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
