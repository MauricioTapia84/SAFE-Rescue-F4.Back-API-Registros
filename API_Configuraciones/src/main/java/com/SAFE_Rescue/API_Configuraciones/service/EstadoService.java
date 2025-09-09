package com.SAFE_Rescue.API_Configuraciones.service;

import com.SAFE_Rescue.API_Configuraciones.modelo.Estado;
import com.SAFE_Rescue.API_Configuraciones.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para la gestión de estados.
 * Maneja operaciones CRUD y validaciones de negocio.
 */
@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    /**
     * Obtiene todos los estados registrados en el sistema.
     *
     * @return Una lista de todos los estados.
     */
    public List<Estado> findAll() {
        return estadoRepository.findAll();
    }

    /**
     * Busca un estado por su ID único.
     *
     * @param id El ID del estado.
     * @return El estado encontrado.
     * @throws NoSuchElementException Si el estado no es encontrado.
     */
    public Estado findById(Integer id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estado no encontrado con ID: " + id));
    }

    /**
     * Guarda un nuevo estado en la base de datos.
     *
     * @param estado El objeto Estado a guardar.
     * @return El estado guardado.
     * @throws IllegalArgumentException Si el estado no cumple con las validaciones de negocio.
     */
    public Estado save(Estado estado) {
        validarAtributosEstado(estado);
        try {
            return estadoRepository.save(estado);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad de datos. El nombre del estado ya existe.");
        }
    }

    /**
     * Actualiza los datos de un estado existente.
     *
     * @param estado El objeto Estado con los datos actualizados.
     * @param id El ID del estado a actualizar.
     * @return El estado actualizado.
     * @throws IllegalArgumentException Si los datos del estado son inválidos.
     * @throws NoSuchElementException Si el estado a actualizar no es encontrado.
     */
    public Estado update(Estado estado, Integer id) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado a actualizar no puede ser nulo.");
        }

        Estado estadoExistente = estadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estado no encontrado con ID: " + id));

        // Actualizar solo los campos que tienen sentido actualizar
        validarAtributosEstado(estado);
        estadoExistente.setNombre(estado.getNombre());
        estadoExistente.setDescripcion(estado.getDescripcion());

        try {
            return estadoRepository.save(estadoExistente);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad de datos. El nombre del estado ya existe.");
        }
    }

    /**
     * Elimina un estado por su ID.
     *
     * @param id El ID del estado a eliminar.
     * @throws NoSuchElementException Si el estado no es encontrado.
     * @throws IllegalArgumentException Si el estado no puede ser eliminado debido a su uso por otras entidades.
     */
    public void delete(Integer id) {
        if (!estadoRepository.existsById(id)) {
            throw new NoSuchElementException("Estado no encontrado con ID: " + id);
        }
        try {
            estadoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar el estado, está siendo utilizado por otras entidades.");
        }
    }

    /**
     * Valida los atributos obligatorios del estado.
     *
     * @param estado El objeto Estado a validar.
     * @throws IllegalArgumentException Si algún atributo obligatorio es nulo o vacío.
     */
    public void validarAtributosEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        if (estado.getNombre() == null || estado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado es un campo obligatorio.");
        }
    }
}