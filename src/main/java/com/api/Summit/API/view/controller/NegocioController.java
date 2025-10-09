package com.api.Summit.API.view.controller;


import com.api.Summit.API.service.impl.NegocioService;
import com.api.Summit.API.view.dto.NegocioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/negocios")
@RequiredArgsConstructor
public class NegocioController {

    private final NegocioService negocioService;

    // Obtener todos los negocios del usuario autenticado
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NegocioDTO>> getNegociosByUsuario(@PathVariable Long usuarioId) {
        List<NegocioDTO> negocios = negocioService.getNegociosByUsuario(usuarioId);
        return ResponseEntity.ok(negocios);
    }

    // Obtener un negocio específico por ID
    @GetMapping("/{negocioId}/usuario/{usuarioId}")
    public ResponseEntity<NegocioDTO> getNegocioById(
            @PathVariable Long negocioId,
            @PathVariable Long usuarioId) {
        NegocioDTO negocio = negocioService.getNegocioByIdAndUsuario(negocioId, usuarioId);
        return ResponseEntity.ok(negocio);
    }

    // Crear nuevo negocio
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<NegocioDTO> createNegocio(
            @RequestBody NegocioDTO negocioDTO,
            @PathVariable Long usuarioId) {
        NegocioDTO nuevoNegocio = negocioService.createNegocio(negocioDTO, usuarioId);
        return ResponseEntity.ok(nuevoNegocio);
    }

    // Actualizar negocio
    @PutMapping("/{negocioId}/usuario/{usuarioId}")
    public ResponseEntity<NegocioDTO> updateNegocio(
            @PathVariable Long negocioId,
            @RequestBody NegocioDTO negocioDTO,
            @PathVariable Long usuarioId) {
        NegocioDTO negocioActualizado = negocioService.updateNegocio(negocioId, negocioDTO, usuarioId);
        return ResponseEntity.ok(negocioActualizado);
    }

    // Eliminar negocio
    @DeleteMapping("/{negocioId}/usuario/{usuarioId}")
    public ResponseEntity<Void> deleteNegocio(
            @PathVariable Long negocioId,
            @PathVariable Long usuarioId) {
        negocioService.deleteNegocio(negocioId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    // Verificar acceso a negocio
    @GetMapping("/{negocioId}/acceso/usuario/{usuarioId}")
    public ResponseEntity<Boolean> verificarAcceso(
            @PathVariable Long negocioId,
            @PathVariable Long usuarioId) {
        boolean tieneAcceso = negocioService.usuarioTieneAccesoANegocio(negocioId, usuarioId);
        return ResponseEntity.ok(tieneAcceso);
    }

    @GetMapping("/list")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NegocioDTO>> getAllNegocios() {
        List<NegocioDTO> negocios = negocioService.getAllNegocios();
        return ResponseEntity.ok(negocios);
    }

    @PostMapping("/create")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NegocioDTO> createNegocioSinUsuario(
            @RequestBody NegocioDTO negocioDTO) {
        NegocioDTO nuevoNegocio = negocioService.createNegocioSinUsuario(negocioDTO);
        return ResponseEntity.ok(nuevoNegocio);
    }

    // ✅ NUEVO: Asignar usuario a negocio existente
    @PostMapping("/{negocioId}/asignar-usuario/{usuarioId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NegocioDTO> asignarUsuarioANegocio(
            @PathVariable Long negocioId,
            @PathVariable Long usuarioId) {
        NegocioDTO negocio = negocioService.asignarUsuarioANegocio(negocioId, usuarioId);
        return ResponseEntity.ok(negocio);
    }

    // ✅ NUEVO: Remover usuario de negocio
    @PostMapping("/{negocioId}/remover-usuario/{usuarioId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NegocioDTO> removerUsuarioDeNegocio(
            @PathVariable Long negocioId,
            @PathVariable Long usuarioId) {
        NegocioDTO negocio = negocioService.removerUsuarioDeNegocio(negocioId, usuarioId);
        return ResponseEntity.ok(negocio);
    }

    // ✅ NUEVO: Eliminar negocio (para administradores - sin verificación de usuario)
    @DeleteMapping("/{negocioId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNegocioAdmin(@PathVariable Long negocioId) {
        negocioService.deleteNegocioAdmin(negocioId);
        return ResponseEntity.noContent().build();
    }

}
