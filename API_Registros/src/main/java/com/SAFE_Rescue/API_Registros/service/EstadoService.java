package com.SAFE_Rescue.API_Registros.service;

import com.SAFE_Rescue.API_Registros.modelo.Categoria;
import com.SAFE_Rescue.API_Registros.modelo.Estado;
import com.SAFE_Rescue.API_Registros.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Capa de servicio responsable de la lógica de negocio para la entidad {@link Estado}.
 * <p>
 * Gestiona las operaciones CRUD y las validaciones de negocio, actuando como
 * intermediario entre la capa de controlador y el repositorio de datos.
 * </p>
 *
 */
@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;

    /**
     * Constructor para la inyección de dependencias (Inyección por Constructor).
     *
     * @param estadoRepository El repositorio de datos para la entidad Estado.
     */
    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    // -------------------------------------------------------------------------
    // OPERACIONES CRUD
    // -------------------------------------------------------------------------

    /**
     * Recupera una lista de todos los estados registrados en el sistema.
     *
     * @return Una {@code List} de todos los objetos {@link Estado}.
     */
    public List<Estado> findAll() {
        return estadoRepository.findAll();
    }

    /**
     * Busca y recupera un registro de {@code Estado} por su identificador único.
     *
     * @param id El ID único del {@code Estado} a buscar.
     * @return El objeto {@code Estado} si se encuentra.
     * @throws NoSuchElementException Si el estado no es encontrado con el ID proporcionado.
     */
    public Estado findById(Integer id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estado no encontrado con ID: " + id));
    }

    /**
     * Busca y recupera estados que coinciden con el nombre.
     *
     * @param nombre El nombre del estado a buscar.
     * @return Una lista de {@code Estado}s.
     */
    public List<Estado> findByNombre(String nombre) {
        return estadoRepository.findByNombre(nombre);
    }

    /**
     * Guarda un nuevo registro de {@code Estado} en la base de datos.
     * <p>
     * Aplica validaciones de atributos antes de la persistencia.
     * </p>
     *
     * @param estado El objeto {@code Estado} a guardar.
     * @return El objeto {@code Estado} guardado y persistido.
     * @throws IllegalArgumentException Si el estado no cumple con las validaciones de atributos
     * o si ocurre una violación de unicidad del nombre.
     */
    public Estado save(Estado estado) {
        validarAtributosEstado(estado);
        try {
            return estadoRepository.save(estado);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad de datos. El nombre del estado ya existe o los datos son inválidos.");
        }
    }

    /**
     * Actualiza un registro de {@code Estado} existente.
     * <p>
     * Verifica la existencia, aplica validaciones del objeto entrante y persiste la actualización.
     * </p>
     *
     * @param estado El objeto {@code Estado} con los datos actualizados.
     * @param id El ID del registro de estado a actualizar.
     * @throws NoSuchElementException Si el registro de {@code Estado} no se encuentra para el ID dado.
     * @throws IllegalArgumentException Si la actualización falla debido a una violación de unicidad.
     */
    public void update(Estado estado, Integer id) {

        Estado estadoExistente = estadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estado no encontrado con ID: " + id));

        validarAtributosEstado(estado);

        estadoExistente.setNombre(estado.getNombre());
        estadoExistente.setDescripcion(estado.getDescripcion());

        try {
            estadoRepository.save(estadoExistente);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad de datos. El nombre del estado ya existe.");
        }
    }

    /**
     * Elimina un registro de {@code Estado} por su ID.
     *
     * @param id El ID del registro de estado a eliminar.
     * @throws NoSuchElementException Si el estado no es encontrado con el ID proporcionado.
     * @throws IllegalArgumentException Si no se puede eliminar el estado debido a violaciones
     * de integridad referencial (ej. otras tablas dependen de este estado).
     */
    public void delete(Integer id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estado no encontrado con ID: " + id));

        try {
            estadoRepository.delete(estado);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar el estado. Está siendo utilizado por otros registros.");
        }
    }

    // -------------------------------------------------------------------------
    // MÉTODOS DE VALIDACIÓN
    // -------------------------------------------------------------------------

    /**
     * Valida los atributos obligatorios de la {@code Estado} y verifica las restricciones de longitud.
     *
     * @param estado El objeto {@code Estado} a validar.
     * @throws IllegalArgumentException Si algún atributo obligatorio es nulo, vacío o excede la longitud máxima.
     */
    public void validarAtributosEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }

        // Validación del nombre (Obligatorio, Máx 50)
        if (estado.getNombre() == null || estado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado es un campo obligatorio.");
        }

        if (estado.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre del estado no puede exceder los 50 caracteres.");
        }

        // 2. Validación de la descripción (Opcional - Solo se valida la longitud si no es nula)
        if (estado.getDescripcion() != null && estado.getDescripcion().length() > 100) {
            throw new IllegalArgumentException("La Descripción del estado no puede exceder los 100 caracteres.");
        }
    }
}