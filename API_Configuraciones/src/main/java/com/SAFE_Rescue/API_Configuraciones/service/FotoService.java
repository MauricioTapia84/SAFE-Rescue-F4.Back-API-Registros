package com.SAFE_Rescue.API_Configuraciones.service;

import com.SAFE_Rescue.API_Configuraciones.modelo.Foto;
import com.SAFE_Rescue.API_Configuraciones.repository.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para la gestión de fotos de perfil.
 * Maneja operaciones CRUD y validaciones de negocio para la entidad Foto.
 */
@Service
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    /**
     * Obtiene todas las fotos registradas en el sistema.
     *
     * @return Una lista de todas las fotos.
     */
    public List<Foto> findAll() {
        return fotoRepository.findAll();
    }

    /**
     * Busca una foto por su ID único.
     *
     * @param id El ID de la foto.
     * @return La foto encontrada.
     * @throws NoSuchElementException Si la foto no es encontrada.
     */
    public Foto findById(Integer id) {
        return fotoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Foto no encontrada con ID: " + id));
    }

    /**
     * Guarda una nueva foto en la base de datos.
     *
     * @param foto El objeto Foto a guardar.
     * @return La foto guardada.
     * @throws IllegalArgumentException Si la foto no cumple con las validaciones de negocio.
     */
    public Foto save(Foto foto) {
        validarAtributosFoto(foto);
        try {
            return fotoRepository.save(foto);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad de datos. La URL de la foto ya existe.");
        }
    }

    /**
     * Actualiza los datos de una foto existente.
     *
     * @param foto El objeto Foto con los datos actualizados.
     * @param id   El ID de la foto a actualizar.
     * @return La foto actualizada.
     * @throws IllegalArgumentException Si los datos de la foto son inválidos.
     * @throws NoSuchElementException   Si la foto a actualizar no es encontrada.
     */
    public Foto update(Foto foto, Integer id) {
        if (foto == null) {
            throw new IllegalArgumentException("La foto a actualizar no puede ser nula.");
        }

        Foto fotoExistente = fotoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Foto no encontrada con ID: " + id));

        // Actualiza el campo URL, que es el único actualizable
        validarAtributosFoto(foto);
        fotoExistente.setUrl(foto.getUrl());

        try {
            return fotoRepository.save(fotoExistente);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad de datos. La URL de la foto ya existe.");
        }
    }

    /**
     * Elimina una foto por su ID.
     *
     * @param id El ID de la foto a eliminar.
     * @throws NoSuchElementException   Si la foto no es encontrada.
     * @throws IllegalArgumentException Si la foto no puede ser eliminada porque está asociada a un usuario.
     */
    public void delete(Integer id) {
        if (!fotoRepository.existsById(id)) {
            throw new NoSuchElementException("Foto no encontrada con ID: " + id);
        }
        try {
            fotoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar la foto, está siendo utilizada por un usuario.");
        }
    }

    /**
     * Valida los atributos obligatorios de la foto.
     *
     * @param foto El objeto Foto a validar.
     * @throws IllegalArgumentException Si la URL es nula o vacía.
     */
    public void validarAtributosFoto(Foto foto) {
        if (foto == null) {
            throw new IllegalArgumentException("La foto no puede ser nula.");
        }
        if (foto.getUrl() == null || foto.getUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("La URL de la foto es un campo obligatorio.");
        }
    }
}