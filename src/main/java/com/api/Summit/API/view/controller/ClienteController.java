package com.api.Summit.API.view.controller;

import com.api.Summit.API.service.exception.ApiResponse;
import com.api.Summit.API.service.interfaces.ClienteService;
import com.api.Summit.API.view.dto.ClienteDTO;
import com.api.Summit.API.view.dto.ClienteRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // Listar clientes por negocio (con paginación)
    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<Page<ClienteDTO>> getClientesByNegocio(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ClienteDTO> clientes = clienteService.findAllByNegocioId(negocioId, pageable);
        return ResponseEntity.ok(clientes);
    }

    // Buscar cliente por ID y negocio
    @GetMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<ClienteDTO> getClienteById(
            @PathVariable Long id,
            @PathVariable Long negocioId) {

        ClienteDTO cliente = clienteService.findByIdAndNegocioId(id, negocioId);
        return ResponseEntity.ok(cliente);
    }

    // Crear nuevo cliente en un negocio
    @PostMapping("/negocio/{negocioId}")
    public ResponseEntity<ClienteDTO> createCliente(
            @RequestBody ClienteRequestDTO clienteRequestDTO,
            @PathVariable Long negocioId) {

        ClienteDTO nuevoCliente = clienteService.saveWithNegocio(clienteRequestDTO, negocioId);
        return ResponseEntity.ok(nuevoCliente);
    }

    // Actualizar cliente en un negocio
    @PutMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<ClienteDTO> updateCliente(
            @PathVariable Long id,
            @RequestBody ClienteRequestDTO clienteRequestDTO,
            @PathVariable Long negocioId) {

        ClienteDTO clienteActualizado = clienteService.updateWithNegocio(id, clienteRequestDTO, negocioId);
        return ResponseEntity.ok(clienteActualizado);
    }

    // Buscar clientes por nombre en un negocio
    @GetMapping("/negocio/{negocioId}/buscar")
    public ResponseEntity<Page<ClienteDTO>> searchClientes(
            @PathVariable Long negocioId,
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ClienteDTO> clientes = clienteService.searchByNombreAndNegocioId(nombre, negocioId, pageable);
        return ResponseEntity.ok(clientes);
    }

    // Obtener clientes frecuentes de un negocio
    @GetMapping("/negocio/{negocioId}/frecuentes")
    public ResponseEntity<Page<ClienteDTO>> getClientesFrecuentes(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ClienteDTO> clientes = clienteService.findFrecuentesByNegocioId(negocioId, pageable);
        return ResponseEntity.ok(clientes);
    }
}
