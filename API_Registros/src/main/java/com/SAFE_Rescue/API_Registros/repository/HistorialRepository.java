package com.SAFE_Rescue.API_Registros.repository;

import com.SAFE_Rescue.API_Registros.modelo.Estado;
import com.SAFE_Rescue.API_Registros.modelo.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad {@link Historial}.
 * <p>
 * Proporciona automáticamente las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * básicas para la entidad {@code Historial}, donde la clave primaria es de tipo {@code Integer}.
 * </p>
 *
 * @see Historial
 */
@Repository
public interface HistorialRepository extends JpaRepository<Historial, Integer> {

    /**
     * Recupera una lista de todos los registros de historial que están asociados
     * con un {@link Estado} específico.
     * <p>
     * Este método se genera automáticamente por Spring Data JPA siguiendo
     * la convención de nombres {@code findBy[NombreCampo]}.
     * </p>
     *
     * @param estado El objeto {@code Estado} por el cual se desea filtrar el historial.
     * @return Una {@code List} de objetos {@code Historial} que coinciden con el estado proporcionado.
     * Retorna una lista vacía si no se encuentra ninguna coincidencia.
     */
    List<Historial> findByEstado(Estado estado);
}