package com.SAFE_Rescue.API_Registros.repository;

import com.SAFE_Rescue.API_Registros.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad {@link Categoria}.
 * <p>
 * Este repositorio extiende {@code JpaRepository}, proporcionando automáticamente
 * la implementación de métodos CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad {@code Categoria}, la cual utiliza un identificador de clave
 * primaria de tipo {@code Integer}.
 * </p>
 *
 * @see Categoria
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    /**
     * Busca y recupera una lista de objetos {@code Categoria} que coinciden exactamente con el nombre proporcionado.
     * <p>
     * Este método es un "Query Method" de Spring Data JPA, generado automáticamente
     * para buscar por el campo 'nombre' de la entidad {@code Categoria}.
     * </p>
     *
     * @param nombre El nombre exacto de la categoría que se desea buscar (ej. "Incidente", "Sistema").
     * @return Una {@code List} de objetos {@code Categoria} que coinciden con el nombre.
     * Retorna una lista vacía si no se encuentra ninguna categoría con ese nombre.
     */
    List<Categoria> findByNombre(String nombre);
}