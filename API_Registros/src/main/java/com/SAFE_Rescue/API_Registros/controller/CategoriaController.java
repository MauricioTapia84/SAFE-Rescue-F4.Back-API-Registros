package com.SAFE_Rescue.API_Registros.controller;

import com.SAFE_Rescue.API_Registros.modelo.Categoria;
import com.SAFE_Rescue.API_Registros.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de categorías.
 * Proporciona endpoints para operaciones CRUD.
 */
@RestController
@RequestMapping("/api-registros/v1/categorias")
@Tag(name = "Categorias", description = "Operaciones de CRUD relacionadas con Categorías")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todas las categorías registradas en el sistema.
     * @return ResponseEntity con lista de categorías o estado NO_CONTENT si no hay registros.
     */
    @GetMapping
    @Operation(summary = "Obtener todas las categorías", description = "Obtiene una lista con todas las categorías.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "204", description = "No hay categorías registradas.")
    })
    public ResponseEntity<List<Categoria>> listar() {
        List<Categoria> categorias = categoriaService.findAll();
        if (categorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categorias);
    }

    /**
     * Busca una categoría por su ID.
     * @param id ID de la categoría a buscar.
     * @return ResponseEntity con la categoría encontrada o un mensaje de error.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría por su ID", description = "Obtiene una categoría al buscarla por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada.")
    })
    public ResponseEntity<?> buscarCategoria(@Parameter(description = "ID de la categoría a buscar", required = true)
                                             @PathVariable int id) {
        try {
            Categoria categoria = categoriaService.findById(id);
            return ResponseEntity.ok(categoria);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Categoría no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Busca categorías por su nombre (parcial o exacto).
     * * @param nombre El nombre o parte del nombre de la categoría a buscar.
     * @return ResponseEntity con la lista de categorías coincidentes o NO_CONTENT.
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar categorías por nombre", description = "Filtra la lista de categorías por el nombre proporcionado.")
    public ResponseEntity<List<Categoria>> buscarCategoriaPorNombre(
            @Parameter(description = "Nombre de la categoría a buscar", required = true)
            @RequestParam String nombre) {

        List<Categoria> categorias = categoriaService.findByNombre(nombre);

        if (categorias.isEmpty()) {
            // HTTP 204 No Content es apropiado cuando la búsqueda no encuentra resultados.
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // HTTP 200 OK con la lista de resultados.
        return ResponseEntity.ok(categorias);
    }

    /**
     * Crea una nueva categoría.
     * @param categoria Datos de la categoría a crear.
     * @return ResponseEntity con mensaje de confirmación o error.
     */
    @PostMapping
    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada con éxito."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud, el nombre ya existe o los datos son inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> agregarCategoria(@RequestBody @Parameter(description = "Datos de la categoría a crear", required = true)
                                                   Categoria categoria) {
        try {
            categoriaService.save(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body("Categoría creada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza una categoría existente.
     * @param id ID de la categoría a actualizar.
     * @param categoria Datos actualizados de la categoría.
     * @return ResponseEntity con mensaje de confirmación o error.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría existente", description = "Actualiza los datos de una categoría por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada con éxito."),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud, el nombre ya existe o los datos son inválidos."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> actualizarCategoria(@Parameter(description = "ID de la categoría a actualizar", required = true)
                                                      @PathVariable Integer id,
                                                      @RequestBody @Parameter(description = "Datos actualizados de la categoría", required = true)
                                                      Categoria categoria) {
        try {
            categoriaService.update(categoria, id);
            return ResponseEntity.ok("Categoría actualizada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría no encontrada.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Elimina una categoría del sistema.
     * @param id ID de la categoría a eliminar.
     * @return ResponseEntity con mensaje de confirmación.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada con éxito."),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada."),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar una categoría en uso."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> eliminarCategoria(@Parameter(description = "ID de la categoría a eliminar", required = true)
                                                    @PathVariable Integer id) {
        try {
            categoriaService.delete(id);
            return ResponseEntity.ok("Categoría eliminada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría no encontrada.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}