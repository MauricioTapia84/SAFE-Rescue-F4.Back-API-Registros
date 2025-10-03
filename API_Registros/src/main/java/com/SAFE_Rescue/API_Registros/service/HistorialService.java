package com.SAFE_Rescue.API_Registros.service;

import com.SAFE_Rescue.API_Registros.modelo.Estado;
import com.SAFE_Rescue.API_Registros.modelo.Historial;
import com.SAFE_Rescue.API_Registros.repository.EstadoRepository;
import com.SAFE_Rescue.API_Registros.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Capa de servicio responsable de la lógica de negocio para la entidad {@link Historial}.
 * <p>
 * Gestiona el registro y recuperación de eventos de auditoría. Dado que los registros de historial
 * deben ser inmutables (no modificables), el método de actualización ha sido marcado como obsoleto
 * y su uso no se recomienda.
 * </p>
 *
 */
@Service
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private EstadoService estadoService;


    // -------------------------------------------------------------------------
    // OPERACIONES CRUD BÁSICAS
    // -------------------------------------------------------------------------

    /**
     * Recupera una lista de todos los registros de historial del sistema.
     *
     * @return Una {@code List} de todos los objetos {@link Historial}.
     */
    public List<Historial> findAll() {
        return historialRepository.findAll();
    }

    /**
     * Busca y recupera un registro de {@code Historial} por su identificador único.
     *
     * @param id El ID único del {@code Historial} a buscar.
     * @return El objeto {@code Historial} si se encuentra.
     * @throws NoSuchElementException Si el historial no es encontrado con el ID proporcionado.
     */
    public Historial findById(Integer id) {
        return historialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Historial no encontrado con ID: " + id));
    }

    /**
     * Busca y recupera todos los registros de historial que están asociados
     * con un {@link Estado} específico, utilizando su identificador.
     * <p>
     * Internamente, este método usa el ID para recuperar el objeto Estado
     * y luego filtra los historiales por dicho objeto.
     * </p>
     * @param estadoId El identificador (ID) del estado por el cual se desea filtrar el historial.
     * @return Una {@code List} de objetos {@code Historial} que coinciden con el ID del estado proporcionado.
     * Retorna una lista vacía si no se encuentra ninguna coincidencia.
     */
    public List<Historial> findByEstadoId(Integer estadoId) {
        Estado estado = estadoService.findById(estadoId);
        return historialRepository.findByEstado(estado);
    }

    /**
     * Guarda un nuevo registro de {@code Historial} en la base de datos.
     * <p>
     * Aplica validaciones de atributos antes de la persistencia.
     * </p>
     *
     * @param historial El objeto {@code Historial} a guardar.
     * @return El objeto {@code Historial} guardado y persistido.
     * @throws IllegalArgumentException Si el historial no cumple con las validaciones de atributos
     * o si ocurre un error de integridad de datos (ej. claves foráneas inválidas).
     */
    public Historial save(Historial historial) {
        validarAtributosHistorial(historial);
        try {
            return historialRepository.save(historial);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad de datos. El historial contiene datos inválidos o referencias inexistentes.");
        }
    }

    /**
     * Elimina un registro de {@code Historial} por su ID.
     *
     * @param id El ID del registro de historial a eliminar.
     * @throws NoSuchElementException Si el historial no es encontrado con el ID proporcionado.
     * @throws IllegalArgumentException Si ocurre una violación de integridad de datos al intentar eliminar.
     */
    public void delete(Integer id) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Historial no encontrado con ID: " + id));

        try {
            historialRepository.delete(historial);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar Historial. Posible violación de integridad referencial.");
        }
    }

    // -------------------------------------------------------------------------
    // MÉTODOS DE VALIDACIÓN
    // -------------------------------------------------------------------------

    /**
     * Valida los atributos obligatorios del {@code Historial} y verifica las restricciones de longitud.
     *
     * @param historial El objeto {@code Historial} a validar.
     * @throws IllegalArgumentException Si algún atributo obligatorio es nulo, vacío o excede la longitud máxima.
     */
    public void validarAtributosHistorial(Historial historial) {
        if (historial == null) {
            throw new IllegalArgumentException("El historial no puede ser nulo.");
        }

        // Validación de Detalle
        if (historial.getDetalle() == null || historial.getDetalle().trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle del historial es un campo obligatorio.");
        }

        if (historial.getDetalle().length() > 250) {
            throw new IllegalArgumentException("El detalle del historial no puede exceder los 250 caracteres.");
        }

        // Validación de Fecha, Estado y Categoria (Relaciones)
        if (historial.getFechaHistorial() == null) {
            throw new IllegalArgumentException("La fecha del historial es un campo obligatorio.");
        }
        if (historial.getEstado() == null) {
            throw new IllegalArgumentException("El Estado del historial es un campo obligatorio.");
        }
        if (historial.getCategoria() == null) {
             throw new IllegalArgumentException("La Categoría del historial es un campo obligatorio.");
        }
    }
}