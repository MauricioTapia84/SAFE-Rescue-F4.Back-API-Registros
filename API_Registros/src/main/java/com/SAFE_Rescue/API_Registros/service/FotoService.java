package com.SAFE_Rescue.API_Registros.service;

import com.SAFE_Rescue.API_Registros.modelo.Foto;
import com.SAFE_Rescue.API_Registros.repository.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Capa de servicio responsable de la lógica de negocio para la entidad {@link Foto}.
 * <p>
 * Gestiona las operaciones CRUD y las validaciones de negocio, actuando como
 * intermediario entre la capa de controlador y el repositorio de datos.
 * </p>
 *
 */
@Service
public class FotoService {

    private final FotoRepository fotoRepository;

    /**
     * Constructor para la inyección de dependencias (Inyección por Constructor).
     *
     * @param fotoRepository El repositorio de datos para la entidad Foto.
     */
    public FotoService(FotoRepository fotoRepository) {
        this.fotoRepository = fotoRepository;
    }

    // -------------------------------------------------------------------------
    // OPERACIONES CRUD
    // -------------------------------------------------------------------------

    /**
     * Recupera una lista de todas las fotos registradas en el sistema.
     *
     * @return Una {@code List} de todos los objetos {@link Foto}.
     */
    public List<Foto> findAll() {
        return fotoRepository.findAll();
    }

    /**
     * Busca y recupera un registro de {@code Foto} por su identificador único.
     *
     * @param id El ID único de la {@code Foto} a buscar.
     * @return El objeto {@code Foto} si se encuentra.
     * @throws NoSuchElementException Si la foto no es encontrada con el ID proporcionado.
     */
    public Foto findById(Integer id) {
        return fotoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Foto no encontrada con ID: " + id));
    }

    /**
     * Guarda un nuevo registro de {@code Foto} en la base de datos.
     * <p>
     * Aplica validaciones de atributos antes de la persistencia.
     * </p>
     *
     * @param foto El objeto {@code Foto} a guardar.
     * @return El objeto {@code Foto} guardado y persistido.
     * @throws IllegalArgumentException Si la foto no cumple con las validaciones de atributos
     * o si ocurre una violación de unicidad de la URL.
     */
    public Foto save(Foto foto) {
        validarAtributosFoto(foto);
        try {
            return fotoRepository.save(foto);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error de integridad de datos.");
        }
    }

    /**
     * Actualiza un registro de {@code Foto} existente.
     * <p>
     * Verifica la existencia, aplica validaciones del objeto entrante y persiste la actualización
     * de campos como la URL y la descripción.
     * </p>
     *
     * @param foto El objeto {@code Foto} con los datos actualizados.
     * @param id   El ID del registro de foto a actualizar.
     * @throws NoSuchElementException Si el registro de {@code Foto} no se encuentra para el ID dado.
     * @throws IllegalArgumentException Si la actualización falla debido a una violación de unicidad o si el objeto es nulo.
     */
    public void update(Foto foto, Integer id) {
        if (foto == null) {
            throw new IllegalArgumentException("La foto a actualizar no puede ser nula.");
        }

        Foto fotoExistente = fotoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Foto no encontrada con ID: " + id));

        // 1. Aplica validaciones al objeto entrante.
        validarAtributosFoto(foto);

        // 2. Actualiza los campos de la entidad existente con los nuevos valores.
        fotoExistente.setUrl(foto.getUrl());
        // Incluyendo el nuevo campo 'descripcion'
        fotoExistente.setDescripcion(foto.getDescripcion());
        try {
            fotoRepository.save(fotoExistente);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad de datos. La URL de la foto ya existe.");
        }
    }

    /**
     * Elimina un registro de {@code Foto} por su ID.
     *
     * @param id El ID del registro de foto a eliminar.
     * @throws NoSuchElementException Si la foto no es encontrada con el ID proporcionado.
     * @throws IllegalArgumentException Si no se puede eliminar la foto debido a violaciones
     * de integridad referencial (ej. la foto está siendo utilizada por la entidad Usuario).
     */
    public void delete(Integer id) {
        Foto foto = fotoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Foto no encontrada con ID: " + id));

        try {
            fotoRepository.delete(foto);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar la foto, está siendo utilizada por un usuario.");
        }
    }

    // -------------------------------------------------------------------------
    // MÉTODOS DE VALIDACIÓN
    // -------------------------------------------------------------------------

    /**
     * Valida los atributos obligatorios de la {@code Foto} y verifica las restricciones de longitud.
     * <p>
     * Asume que la URL y la fecha de subida son obligatorias, y que la descripción es opcional.
     * </p>
     *
     * @param foto El objeto {@code Foto} a validar.
     * @throws IllegalArgumentException Si la foto es nula, la URL o la fecha de subida son nulas o vacías,
     * o si algún campo excede su longitud máxima.
     */
    public void validarAtributosFoto(Foto foto) {
        if (foto == null) {
            throw new IllegalArgumentException("La foto no puede ser nula.");
        }

        // Validación de URL (Obligatoria, Máx 255)
        if (foto.getUrl() == null || foto.getUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("La URL de la foto es un campo obligatorio.");
        }
        if (foto.getUrl().length() > 255) {
            throw new IllegalArgumentException("La URL de la foto no puede exceder los 255 caracteres.");
        }

        // Validación de Fecha Subida (Obligatoria)
        if (foto.getFechaSubida() == null) {
            throw new IllegalArgumentException("La Fecha Subida de la foto es un campo obligatorio.");
        }

        // Validación de Descripción (Opcional, Máx 100)
        // Solo se valida la longitud si el valor no es nulo.
        if (foto.getDescripcion() != null && foto.getDescripcion().length() > 100) {
            throw new IllegalArgumentException("La Descripción de la foto no puede exceder los 100 caracteres.");
        }
    }
}