package com.SAFE_Rescue.API_Registros.controller;

import com.SAFE_Rescue.API_Registros.modelo.Historial;
import com.SAFE_Rescue.API_Registros.service.HistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api-registros/v1/historiales")
@Tag(name = "Historial", description = "Endpoints para la gestión de registros de historial")
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    @GetMapping
    @Operation(summary = "Obtener todos los registros de historial")
    public ResponseEntity<List<Historial>> getAllHistorial() {
        List<Historial> historiales = historialService.findAll();
        return ResponseEntity.ok(historiales);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un registro de historial por su ID")
    public ResponseEntity<Historial> getHistorialById(@PathVariable Integer id) {
        try {
            Historial historial = historialService.findById(id);
            return ResponseEntity.ok(historial);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo registro de historial")
    public ResponseEntity<Historial> createHistorial(@RequestBody Historial historial) {
        Historial nuevoHistorial = historialService.save(historial);
        return new ResponseEntity<>(nuevoHistorial, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un registro de historial existente")
    public ResponseEntity<Historial> updateHistorial(@PathVariable Integer id, @RequestBody Historial historial) {
        try {
            Historial updatedHistorial = historialService.update(historial, id);
            return ResponseEntity.ok(updatedHistorial);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro de historial por su ID")
    public ResponseEntity<Void> deleteHistorial(@PathVariable Integer id) {
        try {
            historialService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}