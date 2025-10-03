package com.SAFE_Rescue.API_Registros.service;

import com.SAFE_Rescue.API_Registros.modelo.Categoria;
import com.SAFE_Rescue.API_Registros.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service; // Agregando la anotación @Service

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Capa de servicio responsable de la lógica de negocio para la entidad {@link Categoria}.
 * <p>
 * Actúa como intermediario entre la capa de controlador (Controller) y la capa de acceso a datos (Repository),
 * asegurando la aplicación de validaciones y reglas de negocio.
 * </p>
 *
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Constructor para inyección de dependencias (Inyección por Constructor).
     *
     * @param categoriaRepository El repositorio de datos para la entidad Categoria.
     */
    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // -------------------------------------------------------------------------
    // OPERACIONES CRUD
    // -------------------------------------------------------------------------

    /**
     * Recupera una lista de todas las categorías registradas en el sistema.
     *
     * @return Una {@code List} de todos los objetos {@link Categoria}.
     */
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    /**
     * Busca y recupera un registro de {@code Categoria} por su identificador único.
     *
     * @param id El ID único de la {@code Categoria} a buscar.
     * @return El objeto {@code Categoria} si se encuentra.
     * @throws NoSuchElementException Si la categoría no es encontrada con el ID proporcionado.
     */
    public Categoria findById(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria no encontrada con ID: " + id));
    }

    /**
     * Busca y recupera categorías que coinciden con el nombre.
     *
     * @param nombre El nombre de la categoría a buscar.
     * @return Una lista de {@code Categoria}s.
     */
    public List<Categoria> findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    /**
     * Guarda un nuevo registro de {@code Categoria} en la base de datos.
     * <p>
     * Aplica validaciones de atributos antes de la persistencia.
     * </p>
     *
     * @param categoria El objeto {@code Categoria} a guardar.
     * @return El objeto {@code Categoria} guardado y persistido.
     * @throws IllegalArgumentException Si la categoría no cumple con las validaciones de atributos
     * o si ocurre un error de integridad de datos (ej. nombre duplicado).
     */
    public Categoria save(Categoria categoria) {
        validarAtributosCategoria(categoria);
        try {
            return categoriaRepository.save(categoria);
        } catch (DataIntegrityViolationException e) { // Se usa la excepción JPA específica para duplicados
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre. Error de integridad de datos.");
        }
    }

    /**
     * Actualiza un registro de {@code Categoria} existente.
     * <p>
     * Primero verifica la existencia, luego aplica validaciones y finalmente persiste
     * los cambios solo en los campos permitidos (nombre y descripción).
     * </p>
     *
     * @param categoria El objeto {@code Categoria} con los datos actualizados.
     * @param id El ID del registro de categoría a actualizar.
     * @throws IllegalArgumentException Si la categoría proporcionada es nula, o si la actualización falla.
     * @throws NoSuchElementException Si el registro de {@code Categoria} no se encuentra para el ID dado.
     */
    public void update(Categoria categoria, Integer id) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria a actualizar no puede ser nulo.");
        }

        Categoria existenteCategoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria no encontrada con ID: " + id));

        // 1. Aplica las validaciones al objeto entrante antes de actualizar.
        validarAtributosCategoria(categoria);

        // 2. Actualiza los campos de la entidad existente con los nuevos valores.
        existenteCategoria.setNombre(categoria.getNombre());
        existenteCategoria.setDescripcion(categoria.getDescripcion());

        try {
            categoriaRepository.save(existenteCategoria);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre. Error de integridad de datos.");
        }
    }

    /**
     * Elimina un registro de {@code Categoria} por su ID.
     *
     * @param id El ID del registro de categoría a eliminar.
     * @throws NoSuchElementException Si la categoría no es encontrada con el ID proporcionado.
     * @throws IllegalArgumentException Si no se puede eliminar la categoría debido a violaciones
     * de integridad referencial (ej. otras tablas dependen de esta categoría).
     */
    public void delete(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria no encontrada con ID: " + id));

        try {
            categoriaRepository.delete(categoria);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar Categoria porque está siendo referenciada por otros registros.");
        }
    }

    // -------------------------------------------------------------------------
    // MÉTODOS DE VALIDACIÓN
    // -------------------------------------------------------------------------

    /**
     * Valida los atributos obligatorios de la {@code Categoria} y verifica las restricciones de longitud.
     *
     * @param categoria El objeto {@code Categoria} a validar.
     * @throws IllegalArgumentException Si algún atributo obligatorio es nulo, vacío o excede la longitud máxima.
     */
    public void validarAtributosCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria no puede ser nula.");
        }

        // 1. Validación del nombre (Obligatorio)
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoria es un campo obligatorio.");
        }
        if (categoria.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre de la categoria no puede exceder los 50 caracteres.");
        }

        // 2. Validación de la descripción (Opcional - Solo se valida la longitud si no es nula)
        if (categoria.getDescripcion() != null && categoria.getDescripcion().length() > 100) {
            throw new IllegalArgumentException("La Descripción de la categoria no puede exceder los 100 caracteres.");
        }
    }
}