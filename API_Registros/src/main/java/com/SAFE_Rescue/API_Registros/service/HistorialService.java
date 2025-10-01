package com.SAFE_Rescue.API_Registros.service;

import com.SAFE_Rescue.API_Registros.modelo.Estado;
import com.SAFE_Rescue.API_Registros.modelo.Foto;
import com.SAFE_Rescue.API_Registros.modelo.Historial;
import com.SAFE_Rescue.API_Registros.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Servicio para la gestión de la entidad Historial.
 * Proporciona métodos para operaciones de negocio sobre los registros de historial.
 */
@Service
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    /**
     * Obtiene todos los registros de historial.
     * @return Una lista de todos los objetos Historial.
     */
    public List<Historial> findAll() {
        return historialRepository.findAll();
    }

    /**
     * Busca un registro de historial por su ID.
     * @param id El ID del registro de historial.
     * @return Un objeto Optional que contiene el registro de historial si se encuentra.
     */
    public Historial findById(Integer id) {
        return historialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estado no encontrado con ID: " + id));
    }

    /**
     * Guarda un nuevo registro de historial en la base de datos.
     * @param historial El objeto Historial a guardar.
     * @return El objeto Historial guardado.
     */
    public Historial save(Historial historial) {
        // Aquí puedes agregar lógica de negocio antes de guardar, si es necesario.
        validarAtributosHistorial(historial);
        try {
            return historialRepository.save(historial);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error de integridad de datos. No se pudo realizar la operación.");
        }
    }

    /**
     * Actualiza un registro de historial existente.
     * @param historial El objeto Historial con los datos actualizados.
     * @param id El ID del registro de historial a actualizar.
     * @return El objeto Historial actualizado.
     * @throws NoSuchElementException Si el registro de historial no se encuentra.
     */
    public Historial update(Historial historial, Integer id) {
        if (historial == null) {
            throw new IllegalArgumentException("El historial a actualizar no puede ser nulo.");
        }

        Historial existingHistorial = historialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Historial no encontrado con ID: " + id));

        // Actualiza los campos necesarios
        existingHistorial.setEstado(historial.getEstado());
        existingHistorial.setFechaHistorial(historial.getFechaHistorial());
        existingHistorial.setDetalle(historial.getDetalle());
        existingHistorial.setIdAsignacionIncidente(historial.getIdAsignacionIncidente());
        existingHistorial.setIdEnvioMensaje(historial.getIdEnvioMensaje());
        existingHistorial.setIdIncidente(historial.getIdIncidente());
        existingHistorial.setIdDireccion(historial.getIdDireccion());
        existingHistorial.setIdUsuarioReporte(historial.getIdUsuarioReporte());
        existingHistorial.setIdAsignacionCurso(historial.getIdAsignacionCurso());

        try {
            return historialRepository.save(existingHistorial);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El historial a actualizar no puede ser nulo.");
        }
    }

    /**
     * Elimina un registro de historial por su ID.
     * @param id El ID del registro de historial a eliminar.
     */
    public void deleteById(Integer id) {
        if (!historialRepository.existsById(id)) {
            throw new NoSuchElementException("Historial no encontrado con ID: " + id);
        }
        try {
            historialRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar Historial");
        }
        historialRepository.deleteById(id);
    }

    /**
     * Valida los atributos obligatorios del historial.
     *
     * @param historial El objeto Historial a validar.
     * @throws IllegalArgumentException Si algún atributo obligatorio es nulo o vacío.
     */
    public void validarAtributosHistorial(Historial historial) {
        if (historial == null) {
            throw new IllegalArgumentException("El historial no puede ser nulo.");
        }
        if (historial.getDetalle() == null || historial.getDetalle().trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle del historial es un campo obligatorio.");
        }
        if (historial.getFechaHistorial() == null) {
            throw new IllegalArgumentException("La fecha del historial es un campo obligatorio.");
        }
        if (historial.getEstado() == null) {
            throw new IllegalArgumentException("El Estado del historial es un campo obligatorio.");
        }
    }
}