package com.SAFE_Rescue.API_Registros.controller;

import com.SAFE_Rescue.API_Registros.modelo.Estado;
import com.SAFE_Rescue.API_Registros.modelo.Historial;
import com.SAFE_Rescue.API_Registros.service.HistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    /**
     * Busca historiales por el ID de su estado asociado.
     * @param estadoId El ID del estado por el cual se desea filtrar el historial.
     * @return ResponseEntity con la lista de historiales coincidentes o NO_CONTENT.
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar historiales por su estado", description = "Filtra la lista de historiales por el ID del estado proporcionado.")
    public ResponseEntity<List<Historial>> buscarHistorialPorEstado(
            @Parameter(description = "ID del estado a buscar", required = true)
            @RequestParam Integer estadoId) {

        try {
            List<Historial> historiales = historialService.findByEstadoId(estadoId);

            if (historiales.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Caso de éxito: Datos encontrados (HTTP 200 OK)
            return ResponseEntity.ok(historiales);

        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo registro de historial")
    public ResponseEntity<Historial> createHistorial(@RequestBody Historial historial) {
        Historial nuevoHistorial = historialService.save(historial);
        return new ResponseEntity<>(nuevoHistorial, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro de historial por su ID")
    public ResponseEntity<Void> deleteHistorial(@PathVariable Integer id) {
        try {
            historialService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}