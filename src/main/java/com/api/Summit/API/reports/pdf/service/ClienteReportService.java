package com.api.Summit.API.reports.pdf.service;

import com.api.Summit.API.model.entities.Cliente;
import com.api.Summit.API.model.entities.Negocio;
import com.api.Summit.API.model.repository.ClienteRepository;
import com.api.Summit.API.model.repository.NegocioRepository;
import com.api.Summit.API.view.dto.ClienteDTO;
import com.api.Summit.API.view.dto.ClienteReportDTO;
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
public class ClienteReportService {

    private final ClienteRepository clienteRepository;
    private final PdfReportService pdfReportService;
    private final NegocioRepository negocioRepository;

    public byte[] generateClientesReportPdf(Long negocioId, String tipoReporte) {
        try {
            List<Cliente> clientes;
            String titulo = "";

            // Obtener información del negocio
            Negocio negocio = negocioRepository.findById(negocioId)
                    .orElseThrow(() -> new RuntimeException("Negocio no encontrado con ID: " + negocioId));

            switch (tipoReporte.toLowerCase()) {
                case "frecuentes":
                    clientes = clienteRepository.findByNegocioIdAndFrecuenteTrue(negocioId);
                    titulo = "CLIENTES FRECUENTES";
                    break;
                case "todos":
                default:
                    clientes = clienteRepository.findByNegocioId(negocioId);
                    titulo = "TODOS LOS CLIENTES";
                    break;
            }

            List<ClienteDTO> clientesDTO = clientes.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            int totalFrecuentes = (int) clientesDTO.stream()
                    .filter(ClienteDTO::isFrecuente)
                    .count();

            ClienteReportDTO reportDTO = ClienteReportDTO.builder()
                    .titulo(titulo)
                    .fechaGeneracion(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                    .clientes(clientesDTO)
                    .totalClientes(clientesDTO.size())
                    .totalFrecuentes(totalFrecuentes)
                    .negocioNombre(negocio.getNombre()) // Agregar esta línea
                    .build();

            return pdfReportService.generateClientesReport(reportDTO);

        } catch (Exception e) {
            log.error("Error generando reporte de clientes", e);
            throw new RuntimeException("Error al generar reporte: " + e.getMessage());
        }
    }


    private ClienteDTO convertToDTO(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .direccion(cliente.getDireccion())
                .frecuente(cliente.getFrecuente() != null ? cliente.getFrecuente() : false)
                .build();
    }
}