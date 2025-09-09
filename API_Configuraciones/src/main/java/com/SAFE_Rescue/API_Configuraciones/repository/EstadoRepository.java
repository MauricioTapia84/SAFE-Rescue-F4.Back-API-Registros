package com.SAFE_Rescue.API_Configuraciones.repository;

import com.SAFE_Rescue.API_Configuraciones.modelo.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la gestión de Estados
 * Maneja operaciones CRUD desde la base de datos usando Jakarta
 */
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    List<Estado> findByNombre(String nombre);

}
