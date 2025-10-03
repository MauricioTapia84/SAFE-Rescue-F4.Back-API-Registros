package com.SAFE_Rescue.API_Registros.controller;

import com.SAFE_Rescue.API_Registros.modelo.Estado;
import com.SAFE_Rescue.API_Registros.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de estados.
 * Proporciona endpoints para operaciones CRUD.
 */
@RestController
@RequestMapping("/api-registros/v1/estados")
@Tag(name = "Estados", description = "Operaciones de CRUD relacionadas con Estados")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los estados registrados en el sistema.
     * @return ResponseEntity con lista de estados o estado NO_CONTENT si no hay registros.
     */
    @GetMapping
    @Operation(summary = "Obtener todos los estados", description = "Obtiene una lista con todos los estados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estados obtenida exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "204", description = "No hay estados registrados.")
    })
    public ResponseEntity<List<Estado>> listar() {
        List<Estado> estados = estadoService.findAll();
        if (estados.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(estados);
    }

    /**
     * Busca un estado por su ID.
     * @param id ID del estado a buscar.
     * @return ResponseEntity con el estado encontrado o un mensaje de error.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un estado por su ID", description = "Obtiene un estado al buscarlo por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado encontrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado.")
    })
    public ResponseEntity<?> buscarEstado(@Parameter(description = "ID del estado a buscar", required = true)
                                          @PathVariable int id) {
        try {
            Estado estado = estadoService.findById(id);
            return ResponseEntity.ok(estado);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Estado no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Busca estados por su nombre (parcial o exacto).
     * * @param nombre El nombre o parte del nombre del estado a buscar.
     * @return ResponseEntity con la lista de estados coincidentes o NO_CONTENT.
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar estados por nombre", description = "Filtra la lista de estados por el nombre proporcionado.")
    public ResponseEntity<List<Estado>> buscarEstadoPorNombre(
            @Parameter(description = "Nombre del estado a buscar", required = true)
            @RequestParam String nombre) {

        List<Estado> estados = estadoService.findByNombre(nombre);

        if (estados.isEmpty()) {
            // Usa el builder noContent().build()
            return ResponseEntity.noContent().build();
        }

        // HTTP 200 OK con la lista de resultados.
        return ResponseEntity.ok(estados);
    }

    /**
     * Crea un nuevo estado.
     * @param estado Datos del estado a crear.
     * @return ResponseEntity con mensaje de confirmación o error.
     */
    @PostMapping
    @Operation(summary = "Crear un nuevo estado", description = "Crea un nuevo estado en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estado creado con éxito."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud, el nombre ya existe o los datos son inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> agregarEstado(@RequestBody @Parameter(description = "Datos del estado a crear", required = true)
                                                Estado estado) {
        try {
            estadoService.save(estado);
            return ResponseEntity.status(HttpStatus.CREATED).body("Estado creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza un estado existente.
     * @param id ID del estado a actualizar.
     * @param estado Datos actualizados del estado.
     * @return ResponseEntity con mensaje de confirmación o error.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un estado existente", description = "Actualiza los datos de un estado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado con éxito."),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud, el nombre ya existe o los datos son inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> actualizarEstado(@Parameter(description = "ID del estado a actualizar", required = true)
                                                   @PathVariable Integer id,
                                                   @RequestBody @Parameter(description = "Datos actualizados del estado", required = true)
                                                   Estado estado) {
        try {
            estadoService.update(estado, id);
            return ResponseEntity.ok("Estado actualizado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estado no encontrado.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un estado del sistema.
     * @param id ID del estado a eliminar.
     * @return ResponseEntity con mensaje de confirmación.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un estado", description = "Elimina un estado del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado eliminado con éxito."),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado."),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar un estado en uso."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> eliminarEstado(@Parameter(description = "ID del estado a eliminar", required = true)
                                                 @PathVariable Integer id) {
        try {
            estadoService.delete(id);
            return ResponseEntity.ok("Estado eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estado no encontrado.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}