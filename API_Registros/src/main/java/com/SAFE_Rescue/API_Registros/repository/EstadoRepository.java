package com.SAFE_Rescue.API_Registros.repository;

import com.SAFE_Rescue.API_Registros.modelo.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad {@link Estado}.
 * <p>
 * Proporciona los métodos esenciales para la gestión de estados en la base de datos
 * (Crear, Leer, Actualizar, Eliminar - CRUD), utilizando un identificador de clave
 * primaria de tipo {@code Integer}.
 * </p>
 *
 * @see Estado
 */
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {

    /**
     * Busca y recupera una lista de objetos {@code Estado} que coinciden exactamente con el nombre proporcionado.
     * <p>
     * Dado que el campo 'nombre' en la entidad {@link Estado} es probablemente único,
     * este método podría retornar típicamente una lista con cero o un elemento.
     * </p>
     *
     * @param nombre El nombre exacto del estado que se desea buscar (ej. "Activo", "Inactivo").
     * @return Una {@code List} de objetos {@code Estado} que coinciden con el nombre.
     * Retorna una lista vacía si no se encuentra ninguna coincidencia.
     */
    List<Estado> findByNombre(String nombre);
}