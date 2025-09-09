package com.SAFE_Rescue.API_Configuraciones.controller;

import com.SAFE_Rescue.API_Configuraciones.modelo.Foto;
import com.SAFE_Rescue.API_Configuraciones.service.FotoService;
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
 * Controlador REST para la gestión de fotos de perfil.
 * Proporciona endpoints para operaciones CRUD.
 */
@RestController
@RequestMapping("/api-perfiles/v1/fotos")
@Tag(name = "Fotos", description = "Operaciones de CRUD relacionadas con Fotos")
public class FotoController {

    @Autowired
    private FotoService fotoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todas las fotos registradas en el sistema.
     * @return ResponseEntity con lista de fotos o estado NO_CONTENT si no hay registros.
     */
    @GetMapping
    @Operation(summary = "Obtener todas las fotos", description = "Obtiene una lista con todas las fotos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de fotos obtenida exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Foto.class))),
            @ApiResponse(responseCode = "204", description = "No hay fotos registradas.")
    })
    public ResponseEntity<List<Foto>> listar() {
        List<Foto> fotos = fotoService.findAll();
        if (fotos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(fotos);
    }

    /**
     * Busca una foto por su ID.
     * @param id ID de la foto a buscar.
     * @return ResponseEntity con la foto encontrada o un mensaje de error.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una foto por su ID", description = "Obtiene una foto al buscarla por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto encontrada.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Foto.class))),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada.")
    })
    public ResponseEntity<?> buscarFoto(@Parameter(description = "ID de la foto a buscar", required = true)
                                        @PathVariable int id) {
        try {
            Foto foto = fotoService.findById(id);
            return ResponseEntity.ok(foto);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Foto no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea una nueva foto.
     * @param foto Datos de la foto a crear.
     * @return ResponseEntity con mensaje de confirmación o error.
     */
    @PostMapping
    @Operation(summary = "Crear una nueva foto", description = "Crea una nueva foto en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Foto creada con éxito."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud, la URL ya existe o los datos son inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> agregarFoto(@RequestBody @Parameter(description = "Datos de la foto a crear", required = true)
                                              Foto foto) {
        try {
            fotoService.save(foto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Foto creada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza una foto existente.
     * @param id ID de la foto a actualizar.
     * @param foto Datos actualizados de la foto.
     * @return ResponseEntity con mensaje de confirmación o error.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una foto existente", description = "Actualiza los datos de una foto por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto actualizada con éxito."),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud, la URL ya existe o los datos son inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> actualizarFoto(@Parameter(description = "ID de la foto a actualizar", required = true)
                                                 @PathVariable Integer id,
                                                 @RequestBody @Parameter(description = "Datos actualizados de la foto", required = true)
                                                 Foto foto) {
        try {
            fotoService.update(foto, id);
            return ResponseEntity.ok("Foto actualizada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Foto no encontrada.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Elimina una foto del sistema.
     * @param id ID de la foto a eliminar.
     * @return ResponseEntity con mensaje de confirmación.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una foto", description = "Elimina una foto del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto eliminada con éxito."),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada."),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar una foto en uso."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> eliminarFoto(@Parameter(description = "ID de la foto a eliminar", required = true)
                                               @PathVariable Integer id) {
        try {
            fotoService.delete(id);
            return ResponseEntity.ok("Foto eliminada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Foto no encontrada.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}