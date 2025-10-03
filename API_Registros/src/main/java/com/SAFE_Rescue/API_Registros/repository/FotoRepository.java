package com.SAFE_Rescue.API_Registros.repository;

import com.SAFE_Rescue.API_Registros.modelo.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad {@link Foto}.
 * <p>
 * Facilita las operaciones de acceso a datos (CRUD) para la entidad {@code Foto},
 * utilizando un identificador de clave primaria de tipo {@code Integer}.
 * </p>
 *
 * @see Foto
 */
@Repository
public interface FotoRepository extends JpaRepository<Foto, Integer> {

}