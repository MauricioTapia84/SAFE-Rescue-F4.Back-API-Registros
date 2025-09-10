package com.SAFE_Rescue.API_Configuraciones.service;

import com.SAFE_Rescue.API_Configuraciones.modelo.Historial;
import com.SAFE_Rescue.API_Configuraciones.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<Historial> findById(Integer id) {
        return historialRepository.findById(id);
    }

    /**
     * Guarda un nuevo registro de historial en la base de datos.
     * @param historial El objeto Historial a guardar.
     * @return El objeto Historial guardado.
     */
    public Historial save(Historial historial) {
        // Aquí puedes agregar lógica de negocio antes de guardar, si es necesario.
        return historialRepository.save(historial);
    }

    /**
     * Actualiza un registro de historial existente.
     * @param historial El objeto Historial con los datos actualizados.
     * @param id El ID del registro de historial a actualizar.
     * @return El objeto Historial actualizado.
     * @throws NoSuchElementException Si el registro de historial no se encuentra.
     */
    public Historial update(Historial historial, Integer id) {
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

        return historialRepository.save(existingHistorial);
    }

    /**
     * Elimina un registro de historial por su ID.
     * @param id El ID del registro de historial a eliminar.
     */
    public void deleteById(Integer id) {
        historialRepository.deleteById(id);
    }
}