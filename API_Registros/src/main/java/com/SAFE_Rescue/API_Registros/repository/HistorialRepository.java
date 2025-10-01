package com.SAFE_Rescue.API_Registros.repository;

import com.SAFE_Rescue.API_Registros.modelo.Estado;
import com.SAFE_Rescue.API_Registros.modelo.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Historial.
 * Proporciona métodos de acceso a la base de datos para las operaciones CRUD.
 */
@Repository
public interface HistorialRepository extends JpaRepository<Historial, Integer> {
    List<Historial> findByEstado(Estado estado);

}